package com.example.car.Cars

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.MainActivity
import com.example.car.Models.CarResponse
import com.example.car.R
import com.example.car.UserActivities.UpdateUserFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.ArrayAdapter
import com.example.car.UploadImage.UploadRequestBody
import com.example.car.UploadImage.UploadResponse
import com.example.car.UploadImage.getFileName
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class AddCarFragment : Fragment(), UploadRequestBody.UploadCallback {

    private var selectedImageUri: Uri? = null
    private lateinit var modelInput: Spinner
    private lateinit var marqueInput: EditText
    private lateinit var puissanceInput: EditText
    private lateinit var carburantInput: Spinner
    private lateinit var descriptionInput: EditText
    private lateinit var circulationDateInput: EditText
    private lateinit var addCarButton: Button
    private lateinit var imageview : ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressbar : ProgressBar
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
        imageview = rootView.findViewById(R.id.image_view)
        progressbar = rootView.findViewById(R.id.progress_bar)


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

        imageview.setOnClickListener {
            openImageChooser()
        }

        addCarButton.setOnClickListener {
            uploadImage()
        }
        return rootView
    }


    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    imageview.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun uploadImage() {

        val model = modelInput.selectedItem.toString().trim()
        val marque = marqueInput.text.toString().trim()
        val puissance = puissanceInput.text.toString().trim()
        val puissanceValue = puissance.toInt()
        val carburant = carburantInput.selectedItem.toString().trim()
        val description = descriptionInput.text.toString().trim()
        val circulationDate = circulationDateInput.text.toString().trim()
        sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val token1 = sharedPreferences.getString("token", null)
        println(token1)

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
            println("in the else")
        val parcelFileDescriptor =
            requireContext().contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)
                ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(requireContext().cacheDir, requireContext().contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        progressbar.progress = 0

      //  val token = RequestBody.create(MediaType.parse("text/plain"), "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY0MmVhOTZmMTc4N2Y4YWI2MjA1ZjMzNyIsImlhdCI6MTY4NDQyODIxNywiZXhwIjoxNjg0NTE0NjE3fQ.oHI3huQB15ievUMLAO-W-C3vPKeabWXqLnpglTH5EUY")
        val modele = RequestBody.create(MediaType.parse("text/plain"), model)
        val marque = RequestBody.create(MediaType.parse("text/plain"), marque)
        val puissance = RequestBody.create(MediaType.parse("text/plain"), puissanceValue.toString())
        val carburant = RequestBody.create(MediaType.parse("text/plain"), carburant)
        val description = RequestBody.create(MediaType.parse("text/plain"), description)
        val date_circulation = RequestBody.create(MediaType.parse("text/plain"), circulationDate)
        val imageBody = UploadRequestBody(file, "image", this)

            println(token1)
           // println(token)


            val carService = RetrofitClient.carinstace
        val call = carService.AddCar(
         //   token,
            modele,
            marque,
            puissance,
            carburant,
            description,
            date_circulation,
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                imageBody
            )
        )

        call.enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                progressbar.progress = 0
                println("failure")

            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {println("success")
                response.body()?.let {
                    progressbar.progress = 100
                }
            }
        })
    }
}
    override fun onProgressUpdate(percentage: Int) {
        progressbar.progress = percentage
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
    }
}