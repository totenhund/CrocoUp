package com.example.android.guesstheword.screens.title

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.android.guesstheword.R
import com.example.android.guesstheword.database.WordReaderDbHelper
import kotlinx.android.synthetic.main.item_page.view.*
import timber.log.Timber

class ViewPagerAdapter(private val itemClickListener: (String) -> (Unit)) : RecyclerView.Adapter<PagerVH>() {

    private var categoryList = emptyList<String>()

    private val colors = intArrayOf(
            R.color.temp1,
            R.color.temp2,
            R.color.temp3
    )

    private val icons = intArrayOf(
            0x1F98A,
            0x1F4BC,
            0x1F929
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
            ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false))

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        tvTitle.text = categoryList[position]
        play_card_container.setBackgroundResource(colors[position])
        var unicode = Character.toChars(icons[position])
        iconTextView.text = String(unicode)
    }

    fun setData(categories: List<String>){
        this.categoryList = categories
        notifyDataSetChanged()
    }

    private inner class ItemViewHolder(itemView: View): PagerVH(itemView) {

        init {
            itemView.setOnClickListener {
                Timber.i("Working")
                itemClickListener(categoryList[adapterPosition])
            }
        }
    }

}

open class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)