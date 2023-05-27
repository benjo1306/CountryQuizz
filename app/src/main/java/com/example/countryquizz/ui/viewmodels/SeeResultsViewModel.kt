package com.example.countryquizz.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countryquizz.data.firebase.CloudStorage
import com.example.countryquizz.model.ResultDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SeeResultsViewModel(private val firestore: FirebaseFirestore, private val storage: CloudStorage, private val auth: FirebaseAuth) : ViewModel() {

   var index: Int? = auth.currentUser?.email?.indexOf('@')
   var username: String? = index?.let { auth.currentUser?.email?.substring(0, it) }

   var didUploadResult: MutableLiveData<Boolean> = MutableLiveData()
   var didFetchData: MutableLiveData<Boolean> = MutableLiveData()

   private var resultDetails: ArrayList<ResultDetails> = ArrayList()

   @RequiresApi(Build.VERSION_CODES.O)
   suspend fun addToDatabase(score: String) {

      val results = hashMapOf(
         "username" to username,
         "score" to score,
         "date" to LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm")),
         "imageUrl" to storage.downloadPhoto().toString()
      )

      firestore.collection("results")
         .add(results)
         .addOnSuccessListener {
            didUploadResult.postValue(true)
         }
         .addOnFailureListener {
            Log.e("addToDatabase Failure: ", it.stackTraceToString())
         }.await()
   }

   suspend fun getDataFromDatabase() {
      firestore.collection("results")
         .get()
         .addOnCompleteListener { it ->
            if(it.isSuccessful) {
               for(document in it.result!!) {
                  val date = document.data["date"].toString()
                  val imageUrl = document.data["imageUrl"].toString()
                  val score = document.data["score"].toString()
                  val username = document.data["username"].toString()

                  resultDetails.add(ResultDetails(date, imageUrl, score, username))
                  resultDetails.sortByDescending { resultDetails ->
                     resultDetails.date
                  }
               }
            }
            didFetchData.postValue(true)
         }
         .addOnFailureListener {
            Log.e("getDataFromDatabase: ", it.stackTraceToString())
         }.await()
   }

   fun getResultDetails(): ArrayList<ResultDetails> {
      return resultDetails
   }
}