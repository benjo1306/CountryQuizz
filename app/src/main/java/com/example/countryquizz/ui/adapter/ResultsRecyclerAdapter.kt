package com.example.countryquizz.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.countryquizz.R
import com.example.countryquizz.model.ResultDetails
import kotlinx.android.synthetic.main.see_results_rv_item.view.*

class ResultsRecyclerAdapter(private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: ArrayList<ResultDetails> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ResultsViewHolder (
            LayoutInflater.from(parent.context).inflate(R.layout.see_results_rv_item, parent, false)
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ResultsViewHolder -> {
                holder.bind(list[position])
                holder.itemView.setOnClickListener { onItemClickListener.onItemClick(position) }
            }
        }
    }

    fun addResults(results: ArrayList<ResultDetails>) {
        if(list.isNotEmpty())
            list.clear()

        list.addAll(results)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getItemAt(position: Int): ResultDetails {
        return list[position]
    }

    inner class ResultsViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val imgView = itemView.iv_user_image
        private val score = itemView.tv_score
        private val date = itemView.tv_date
        private val username = itemView.tv_username

       fun bind(resultDetails: ResultDetails) {
           Log.d("bindingStarted", "jea")
           Glide.with(imgView.context)
               .load(resultDetails.imageUrl)
               .into(imgView)

           score.text = "Score: " + resultDetails.score + "/5"
           date.text = resultDetails.date
           username.text = "Username: " + resultDetails.username
       }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION)
                onItemClickListener.onItemClick(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}