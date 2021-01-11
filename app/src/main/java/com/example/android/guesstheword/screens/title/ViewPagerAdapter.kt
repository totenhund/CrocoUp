package com.example.android.guesstheword.screens.title

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.guesstheword.R
import kotlinx.android.synthetic.main.item_page.view.*

class ViewPagerAdapter : RecyclerView.Adapter<PagerVH>() {

    private val colors = intArrayOf(
            R.drawable.animals,
            android.R.color.holo_blue_dark
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
            PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false))

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        tvTitle.text = "item $position"
        play_card_container.setBackgroundResource(colors[position])
    }
}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)