package com.example.car.UserActivities

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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.Models.UserResponse
import com.example.car.R
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class UpdateUserFragment : Fragment() {
    private val GALLERY_REQUEST_CODE = 123

    private lateinit var emailinput : EditText
    private lateinit var passwordinput : EditText
    private lateinit var btn_update: Button
    private lateinit var btn_update_image: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var file: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_user, container, false)

        emailinput = view.findViewById(R.id.email)
        passwordinput = view.findViewById(R.id.password)
        btn_update = view.findViewById(R.id.updatebutton)
        btn_update_image = view.findViewById(R.id.updateImagebutton)


        val openGalleryButton = view.findViewById<Button>(R.id.open_gallery_button_user)
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

        btn_update.setOnClickListener {
            checkcredentials();
        }

        btn_update_image.setOnClickListener {
            updateImage(file);
        }

        return view
    }

    private fun updateImage(file: File) {
        val intentProfile1 = Intent(activity, ProfileFragment::class.java)
        sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        RetrofitClient.instance.Userimage("Bearer $token",
            MultipartBody.Part.createFormData("image",file.name,file.asRequestBody()))
            .enqueue(object: Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if(response.code() == 200)
                    {
                        startActivity(intentProfile1)
                        //Toast.makeText(requireContext(), "new user", Toast.LENGTH_LONG).show()
                        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("User Updated Successfully")
                            .show();
                    }
                    else if(response.code()==500)
                    {
                        //Toast.makeText(requireContext(), "error", Toast.LENGTH_LONG).show()
                        SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("An Error Has Occured")
                            //.setContentText("You Submitted The Wrong Code!")
                            .show()

                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message , Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun checkcredentials() {
        val intentProfile = Intent(activity, ProfileFragment::class.java)
        val email = emailinput.text.toString().trim()
        val password = passwordinput.text.toString().trim()
        sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if(email.isEmpty()){
            emailinput.error = "Email Required"
        }

        if(password.isEmpty()) {
            passwordinput.error = "Password Required"
        }

        else {
            RetrofitClient.instance.updateuser("Bearer $token",email,password)
                .enqueue(object: Callback<UserResponse> {
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if(response.code() == 200)
                        {
                            startActivity(intentProfile)
                            //Toast.makeText(requireContext(), "new user", Toast.LENGTH_LONG).show()
                            SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("User Updated Successfully")
                                .show();
                        }
                        else if(response.code()==500)
                        {
                            //Toast.makeText(requireContext(), "error", Toast.LENGTH_LONG).show()
                            SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("An Error Has Occured")
                                //.setContentText("You Submitted The Wrong Code!")
                                .show()

                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), t.message , Toast.LENGTH_LONG).show()
                    }

                })
        }
    }
}