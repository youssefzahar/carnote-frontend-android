package com.example.car.Cars
import android.widget.ImageView
import okhttp3.RequestBody.Companion.asRequestBody
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.Models.CarResponse
import com.example.car.R
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import com.github.dhaval2404.imagepicker.ImagePicker
class AddCarFragment : Fragment() {
    private val GALLERY_REQUEST_CODE = 123

    private lateinit var modelInput: Spinner
    private lateinit var marqueInput: EditText
    private lateinit var puissanceInput: EditText
    private lateinit var carburantInput: Spinner
    private lateinit var descriptionInput: EditText
    private lateinit var circulationDateInput: EditText
    private lateinit var addCarButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var file: File

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_car, container, false)

        modelInput = rootView.findViewById(R.id.model)
        marqueInput = rootView.findViewById(R.id.marque)
        puissanceInput = rootView.findViewById(R.id.puissance)
        carburantInput = rootView.findViewById(R.id.carburant)
        descriptionInput = rootView.findViewById(R.id.description)
        circulationDateInput = rootView.findViewById(R.id.circulation_date)
        addCarButton = rootView.findViewById(R.id.addcarbutton)
        val openGalleryButton = rootView.findViewById<Button>(R.id.open_gallery_button)
        val startForImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == Activity.RESULT_OK) {
                    val uri: Uri = data?.data!!
                    file = uri.path?.let { File(it) }!!
                    val imageView = requireView().findViewById<ImageView>(R.id.create_post_imageview)
                    imageView.visibility = View.VISIBLE
                    imageView.setImageURI(uri)
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        openGalleryButton.setOnClickListener {
            ImagePicker.with(requireActivity()).compress(1024).crop().createIntent {
                startForImageResult.launch(it)
            }
        }

        val models  = arrayOf("BMW","Ferrari","Huandai")
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, models)
        modelInput.adapter = adapter

        modelInput.setOnItemSelectedListener(object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem  = models[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        })

        val carburants  = arrayOf("Essence","Gazoil")
        val adaptercarburants = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, carburants)
        carburantInput.adapter = adaptercarburants

        carburantInput.setOnItemSelectedListener(object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem  = models[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        })



        addCarButton.setOnClickListener {
            checkCredentials(file)
        }

        return rootView
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri : Uri? = data.data
            val image = imageUri.toString() // Convert URI to string
            file = imageUri?.path?.let { File(it) }!!
            // Perform further processing with the selected image URI
            Log.d("testttt", "checkCredentials: ${file.name}")

            // Example: Display the selected image in an ImageView
            val imageView = requireView().findViewById<ImageView>(R.id.create_post_imageview)
            imageView.visibility = View.VISIBLE
            imageView.setImageURI(imageUri)

            // Assign the image URI to the image property


            // Pass the post object to the API request or do further processing
        }
    }



    private fun checkCredentials(file:File) {
        val model = modelInput.selectedItem.toString().trim()
        val marque = marqueInput.text.toString().trim()
        val puissance = puissanceInput.text.toString().trim()
        val puissanceValue = puissance.toInt()
        val carburant = carburantInput.selectedItem.toString().trim()
        val description = descriptionInput.text.toString().trim()
        val circulationDate = circulationDateInput.text.toString().trim()
        sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if(marque.isEmpty()) {
            marqueInput.error = " Required"
        }
        if(puissance.isEmpty()) {
            puissanceInput.error = " Required"
        }
        if(description.isEmpty()) {
            descriptionInput.error = " Required"
        }
        if(circulationDate.isEmpty()) {
            circulationDateInput.error = " Required"
        } else {
            val imageUri = getImageUri() // Get the image URI from the ImageView

            if (imageUri != null) {
                val image = imageUri.toString() // Convert URI to string
                RetrofitClient.carinstace.AddCar("Bearer $token", model,marque, puissanceValue, carburant,description, circulationDate,
                    MultipartBody.Part.createFormData("image",file.name,file.asRequestBody()))
                    .enqueue(object : Callback<CarResponse> {
                        override fun onResponse(call: Call<CarResponse>, response: Response<CarResponse>) {
                            if(response.code() == 200) {
                                // Refresh the car list on the parent activity
                                val fragment = CarFragment()
                                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.frameLayout, fragment)
                                transaction.addToBackStack(null)
                                transaction.commit()

                                // Show success dialog
                                SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Car added successfully!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                    .show()
                            } else {
                                // Show error dialog
                                SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Failed to add car.")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<CarResponse>, t: Throwable) {
                            Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                        }
                    })
            } else {
                // Image not selected, handle this case accordingly
            }
        }
    }

    private fun getImageUri(): String? {
        // Retrieve the image URI from the ImageView
        val image = requireView().findViewById<ImageView>(R.id.create_post_imageview)
        return image?.let { image ->
            val drawable = image.drawable
            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap
                return saveImageToInternalStorage(bitmap) // Save the image to internal storage and get the file path
            }
            return null
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val wrapper = ContextWrapper(requireContext().applicationContext)

        // Create a file directory to store the image
        val directory = wrapper.getDir("images", Context.MODE_PRIVATE)

        // Generate a unique filename for the image
        val fileName = "image_${System.currentTimeMillis()}.jpg"

        // Create a file object with the directory and filename
        val file = File(directory, fileName)

        // Create an output stream to write the bitmap data to the file
        val outputStream = FileOutputStream(file)

        // Compress the bitmap and write it to the output stream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        // Close the output stream
        outputStream.close()

        // Return the absolute path of the saved image file
        return file.absolutePath
    }





}
