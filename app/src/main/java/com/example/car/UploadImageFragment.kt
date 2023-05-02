package com.example.car

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment

class UploadImageFragment : Fragment() {
    private lateinit var selectedImageUri: Uri
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upload_image, container, false)
        imageView = view.findViewById(R.id.image_view)
        val selectImageButton: Button = view.findViewById(R.id.select_image_button)
        selectImageButton.setOnClickListener {
            selectImage()
        }
        return view
    }

    private fun selectImage() {
      /*  val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)*/
       */
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 10)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            selectedImageUri = data?.data!!
            imageView.setImageURI(selectedImageUri)
        }
    }

    companion object {
        private const val REQUEST_CODE_SELECT_IMAGE = 1
    }
}
