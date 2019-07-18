package com.boocha.feature.home.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boocha.R
import com.boocha.model.Swap

class ProfileFragmentAdapter : RecyclerView.Adapter<ProfileFragmentViewHolder>() {

    var swapList: MutableList<Swap> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileFragmentViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_swap_list_profile, parent, false)
        return ProfileFragmentViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return swapList.size
    }

    override fun onBindViewHolder(holder: ProfileFragmentViewHolder, position: Int) {
        holder.holder(swapList[position])
    }
}