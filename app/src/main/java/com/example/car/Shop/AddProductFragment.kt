package com.example.car.Shop

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.Cars.CarFragment
import com.example.car.Models.CarResponse
import com.example.car.Models.ProductResponse
import com.example.car.R
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class AddProductFragment : Fragment() {
    private val GALLERY_REQUEST_CODE = 123

    private lateinit var titleInput: EditText
    private lateinit var stockInput: EditText
    private lateinit var prixInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var addProductButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_product, container, false)

        titleInput = rootView.findViewById(R.id.producttitle)
        stockInput = rootView.findViewById(R.id.productstock)
        prixInput = rootView.findViewById(R.id.productprix)
        descriptionInput = rootView.findViewById(R.id.productdescription)
        addProductButton = rootView.findViewById(R.id.addproductbutton)

        val openGalleryButton = rootView.findViewById<Button>(R.id.open_gallery_button_product)
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
        addProductButton.setOnClickListener{
            checkCredentials(file)
        }

        return rootView
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
        val title = titleInput.text.toString().trim()
        val stock = stockInput.text.toString().trim()
        val stockValue = stock.toInt()
        val prix = prixInput.text.toString().trim()
        val prixValue = prix.toInt()
        val description = descriptionInput.text.toString().trim()
        sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if(title.isEmpty()) {
            titleInput.error = " Required"
        }
        if(stock.isEmpty()) {
            stockInput.error = " Required"
        }
        if(prix.isEmpty()) {
            prixInput.error = " Required"
        }
        if(description.isEmpty()) {
            descriptionInput.error = " Required"
        }
        else {
            RetrofitClient.productinstace.AddCar("Bearer $token", title,stockValue, prixValue , description,
                MultipartBody.Part.createFormData("image",file.name,file.asRequestBody()))

                .enqueue(object: Callback<ProductResponse> {
                    override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                        if(response.code() == 200) {
                            // Refresh the car list on the parent activity
                            val fragment = ShopFragment()
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

                    override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), t.message , Toast.LENGTH_LONG).show()
                    }

                })
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
