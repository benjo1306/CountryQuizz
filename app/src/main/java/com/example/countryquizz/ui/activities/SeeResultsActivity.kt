package com.example.countryquizz.ui.activities

import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countryquizz.databinding.ActivitySeeResultsBinding
import com.example.countryquizz.ui.adapter.ResultsRecyclerAdapter
import com.example.countryquizz.ui.viewmodels.SeeResultsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.InputStream
import java.net.URL


class SeeResultsActivity : AppCompatActivity(), ResultsRecyclerAdapter.OnItemClickListener {

    private lateinit var binding: ActivitySeeResultsBinding
    private lateinit var seeResultsRecyclerAdapter: ResultsRecyclerAdapter
    private val seeResultsViewModel by viewModel<SeeResultsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.Default).launch {
            seeResultsViewModel.getDataFromDatabase()
        }

        initRecyclerView()

        seeResultsViewModel.didFetchData.observe(this) {
            seeResultsRecyclerAdapter.addResults(seeResultsViewModel.getResultDetails())
        }
    }

    private fun initRecyclerView() {
        binding.rvSeeResults.apply {
            layoutManager = LinearLayoutManager(this@SeeResultsActivity)
            seeResultsRecyclerAdapter = ResultsRecyclerAdapter(this@SeeResultsActivity)
            adapter = seeResultsRecyclerAdapter

            this.addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onItemClick(position: Int) {
        val item = seeResultsRecyclerAdapter.getItemAt(position)

        Log.d("onItemClickkkk", "clicked")

        AlertDialog.Builder(this)
            .setTitle("USER: " + item.username)
            .setMessage("SCORE:  " + item.score + "/5")
            .setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

}