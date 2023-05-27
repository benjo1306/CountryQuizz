package com.example.countryquizz.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.countryquizz.databinding.LoadImageDialogBinding

class LoadImageDialog : DialogFragment()  {

    private lateinit var binding: LoadImageDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoadImageDialogBinding.inflate(layoutInflater)

        binding.btnGallery.setOnClickListener {
            val pickIntent = Intent()
            pickIntent.type = "image/*"
            pickIntent.action = Intent.ACTION_GET_CONTENT
            activity?.startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), GALLERY_CODE)
        }

        binding.btnCamera.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activity?.startActivityForResult(cameraIntent, CAMERA_CODE)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels)
        val height = (resources.displayMetrics.heightPixels * 0.2).toInt()

        dialog!!.window?.setLayout(width, height)
        dialog!!.window?.setGravity(Gravity.BOTTOM)
    }

    companion object {
        const val CAMERA_CODE = 42
        const val GALLERY_CODE = 202
    }
}