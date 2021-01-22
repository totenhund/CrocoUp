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

class ViewPagerAdapter(private var names: ArrayList<String>, private val itemClickListener: (Int) -> (Unit)) : RecyclerView.Adapter<PagerVH>() {

    private val colors = intArrayOf(
            R.color.temp1,
            R.color.temp2,
            R.color.temp3
    )

    private val icons = intArrayOf(
            0x1F423,
            0x1F4BB,
            0x1F4DA
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
            ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false))

    override fun getItemCount(): Int = names.size - 1

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        tvTitle.text = names[position+1]
        play_card_container.setBackgroundResource(colors[position])
        var unicode = Character.toChars(icons[position])
        iconTextView.text = String(unicode)
        Timber.i(R.string.animal.toString())
    }

    private inner class ItemViewHolder(itemView: View): PagerVH(itemView) {

        init {
            itemView.setOnClickListener {
                Timber.i("Working")
                itemClickListener(adapterPosition)
            }
        }
    }

}

open class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)