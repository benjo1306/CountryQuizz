package com.example.countryquizz.ui.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryquizz.data.firebase.CloudStorage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class PlayOrSeeResultsViewModel(private val storage: CloudStorage) : ViewModel() {

    var takenImage = MutableLiveData<Bitmap>()
    var imageUri = MutableLiveData<Uri>()

    fun sendImage(image: Bitmap) {
        takenImage.postValue(image)
    }

    suspend fun uploadImage(imageUri: Uri) {
        storage.uploadImage(imageUri)
    }

    fun getImageUri(context: Context, image: Bitmap): Uri {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        return Uri.parse(MediaStore.Images.Media.insertImage(context.contentResolver, image, "Title", null))
    }

    suspend fun downloadPhoto() {
        imageUri.postValue(storage.downloadPhoto())
    }
}