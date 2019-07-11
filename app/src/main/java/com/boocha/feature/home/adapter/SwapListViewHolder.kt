package com.boocha.feature.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.boocha.model.Swap
import com.boocha.util.dateDifference
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_swap_list.view.*

class SwapListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun holder(swap: Swap) {
        Glide.with(itemView.context).load(swap.imageUri).into(itemView.ivBook)
        Glide.with(itemView.context).load(swap.owner?.profilePhoto).into(itemView.ivUser)

        itemView.tvBookName.text = swap.book?.title
        itemView.tvBookDescription.text = swap.book?.description
        itemView.tvUsername.text = "${swap.owner?.name} ${swap.owner?.surname}"
        itemView.tvDate.text = dateDifference(swap.date ?: "")
    }
}