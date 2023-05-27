package com.example.countryquizz.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import com.example.countryquizz.databinding.ActivityPlayOrSeeResultsBinding
import com.example.countryquizz.ui.dialog.LoadImageDialog
import com.example.countryquizz.ui.viewmodels.PlayOrSeeResultsViewModel
import com.example.countryquizz.ui.viewmodels.RegistrationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class PlayOrSeeResultsActivity : AppCompatActivity() {

    private val viewModel by viewModel<RegistrationViewModel>()
    private val playOrSeeResultsViewModel by viewModel<PlayOrSeeResultsViewModel>()

    private lateinit var binding: ActivityPlayOrSeeResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayOrSeeResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.tvUsername.text = "Hello " + (viewModel.getCurrentUser()?.email ?: "null")

        binding.tvUsername.text = """Hello ${viewModel.getUsername()  ?: "null"}"""

        CoroutineScope(Dispatchers.Default).launch {
            playOrSeeResultsViewModel.downloadPhoto()
        }

        playOrSeeResultsViewModel.imageUri.observe(this) {
            if (it != null) {
                Glide.with(this)
                    .load(it.toString())
                    .into(binding.ivUserPhoto)
            }
        }

        binding.btnSignOut.setOnClickListener {
            viewModel.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnPlay.setOnClickListener {
            val intent = Intent(this, PlayQuizActivity::class.java)
            startActivity(intent)
        }

        binding.btnSeeResults.setOnClickListener {
            val intent = Intent(this, SeeResultsActivity::class.java)
            startActivity(intent)
        }

        binding.ivUserPhoto.setOnClickListener {
            LoadImageDialog().show(supportFragmentManager, "Load image dialog")
        }

        playOrSeeResultsViewModel.takenImage.observe(this) {
            if (it != null) {
                Glide.with(this)
                    .load(it)
                    .into(binding.ivUserPhoto)
            } else {
                Log.e("Login not successful", "testtest")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == LoadImageDialog.CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = data?.extras?.get("data") as Bitmap
            val imageUri : Uri = playOrSeeResultsViewModel.getImageUri(context = this, takenImage)

            CoroutineScope(Dispatchers.Default).launch {
                playOrSeeResultsViewModel.uploadImage(imageUri)
            }
            playOrSeeResultsViewModel.sendImage(takenImage)
        }
        else if(requestCode == LoadImageDialog.GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null) return

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                val imageUri: Uri = playOrSeeResultsViewModel.getImageUri(context = this, bitmap)
                CoroutineScope(Dispatchers.Default).launch {
                    playOrSeeResultsViewModel.uploadImage(imageUri)
                }
                playOrSeeResultsViewModel.sendImage(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}