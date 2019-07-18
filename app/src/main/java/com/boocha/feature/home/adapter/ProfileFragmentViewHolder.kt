package com.boocha.feature.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.boocha.model.Swap
import com.boocha.util.BOOK_STATUS_GOOD
import com.boocha.util.BOOK_STATUS_NOT_GOOD
import com.boocha.util.BOOK_STATUS_PERFECT
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_swap_list_profile.view.*

class ProfileFragmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun holder(swap: Swap) {
        Glide.with(itemView.context).load(swap.imageUri).into(itemView.ivBook)

        itemView.tvBookName.text = swap.book?.title
        itemView.tvAuthorName.text = swap.book?.author
        itemView.tvDescription.text = swap.ownerDescription
        itemView.tvBookStatus.text = bookStatus(swap.bookStatus)
    }

    private fun bookStatus(bookStatus: Int?): String {
        return when (bookStatus) {
            BOOK_STATUS_NOT_GOOD -> {
                "Book Status: Not Good"
            }
            BOOK_STATUS_GOOD -> {
                "Book Status: Good"

            }
            BOOK_STATUS_PERFECT -> {
                "Book Status: Perfect"
            }
            else -> {
                ""
            }
        }
    }
}