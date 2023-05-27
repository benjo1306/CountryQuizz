package com.example.countryquizz.data.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class CloudStorage(storageReference: FirebaseStorage, auth: FirebaseAuth) {

    private val storageRef = storageReference.reference

    private var index: Int? = auth.currentUser?.email?.indexOf('@')
    var username: String? = index?.let { auth.currentUser?.email?.substring(0, it) }
    var imagesRef: StorageReference? = storageRef.child("$username.jpg")

    var usernameImage = "$username.jpg"

    private var downloadUri: Uri = Uri.EMPTY
    private var list: MutableList<StorageReference> = ArrayList()

    suspend fun uploadImage(imageUri: Uri) {
        try {
            imagesRef?.putFile(imageUri)?.addOnSuccessListener {
            }?.await()
        } catch (e: Exception) {
        }
    }

    suspend fun downloadPhoto(): Uri {
        try {
            storageRef.listAll().addOnSuccessListener {
                list = it.items
            }.await()
            for(item in list) {
                if(item.path.contains(usernameImage)) {
                    storageRef.child("$username.jpg").downloadUrl.addOnSuccessListener {
                        downloadUri = it
                    }.await()
                }
            }
        } catch (e: Exception) {
        }

        return downloadUri
    }

    fun getStorageRef() : MutableList<StorageReference> {
        return list
    }
}