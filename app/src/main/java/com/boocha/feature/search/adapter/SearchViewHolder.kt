package com.boocha.feature.search.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.boocha.model.book.Item
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_book_list.view.*
import java.text.ParseException
import java.text.SimpleDateFormat

class SearchViewHolder(itemView: View, onClickLister: SearchAdapter.OnClickLister) : RecyclerView.ViewHolder(itemView) {
    init {
        itemView.setOnClickListener {
            onClickLister.itemOnClick(it, adapterPosition)
        }
    }

    fun bind(item: Item) {
        Glide.with(itemView.context).load(item.volumeInfo?.imageLinks?.thumbnail).into(itemView.ivBook)

        itemView.tvBookName.text = item.volumeInfo?.title ?: ""
        itemView.tvAuthorName.text = item.volumeInfo?.authors?.get(0) ?: ""

        item.volumeInfo?.publishedDate?.let { date ->
            try {
                val oldDate = SimpleDateFormat("yyyy-MM-dd").parse(date)
                itemView.tvDate.text = "Published Date: ${SimpleDateFormat("dd.MM.yyyy").format(oldDate)}"
            } catch (exception: ParseException) {
                itemView.tvDate.text = ""
            }
        }

        item.volumeInfo?.language?.let { language ->
            itemView.tvLanguage.text = "Language: ${language.toUpperCase()}"
        }
    }
}