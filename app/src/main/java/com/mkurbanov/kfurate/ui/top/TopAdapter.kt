package com.mkurbanov.kfurate.ui.top

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mkurbanov.kfurate.R
import com.mkurbanov.kfurate.data.models.TopStudent
import com.mkurbanov.kfurate.databinding.ItemTopBinding

class TopAdapter(private val students: List<TopStudent>) :
    RecyclerView.Adapter<TopAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAdapter.ViewHolder {
        val binding = ItemTopBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopAdapter.ViewHolder, position: Int) {
        with(holder.binding){
            with(students.get(position)){
                textViewName.text = name
                textViewTop.text = (position +1).toString()
                textViewLikeCount.text = likeCount.toString()
                imageView.load("${image}200x200"){
                    crossfade(true)
                }
            }
        }
    }

    override fun getItemCount(): Int = students.size

    class ViewHolder(val binding: ItemTopBinding) : RecyclerView.ViewHolder(binding.root)
}