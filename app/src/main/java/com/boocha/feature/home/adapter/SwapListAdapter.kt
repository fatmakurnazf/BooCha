package com.boocha.feature.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boocha.R
import com.boocha.model.Swap

class SwapListAdapter : RecyclerView.Adapter<SwapListViewHolder>() {

    var swapList: MutableList<Swap> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwapListViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_swap_list, parent, false)
        return SwapListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return swapList.size
    }

    override fun onBindViewHolder(holder: SwapListViewHolder, position: Int) {
        holder.holder(swapList[position])
    }
}