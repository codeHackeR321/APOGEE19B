package com.anenigmatic.apogee19.screens.events.view

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.events.core.EventFilterViewModel
import kotlinx.android.synthetic.main.row_filter.view.*

class FiltersAdapter(private val listener: ClickListener) : RecyclerView.Adapter<FiltersAdapter.FilterVHolder>() {

    interface ClickListener {

        fun onFilterSet(name: String)
    }


    var filters = listOf<EventFilterViewModel.FilterOption>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun getItemCount() = filters.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FilterVHolder(inflater.inflate(R.layout.row_filter, parent, false))
    }

    override fun onBindViewHolder(holder: FilterVHolder, position: Int) {
        val filter = filters[position]

        holder.nameLBL.text = filter.name

        when(filter.isActive) {
            true  -> {
                TextViewCompat.setTextAppearance(holder.nameLBL, R.style.ActiveFilterButton)
                holder.nameLBL.setBackgroundResource(R.drawable.sh_rounded_rectangle_08dp)

                val color = ContextCompat.getColor(holder.nameLBL.context, R.color.vio07)
                holder.nameLBL.backgroundTintList = ColorStateList.valueOf(color)
            }
            false -> {
                TextViewCompat.setTextAppearance(holder.nameLBL, R.style.InactiveFilterButton)
                holder.nameLBL.setBackgroundResource(R.drawable.sh_rounded_rectangle_thin_outline_08dp)

                holder.nameLBL.backgroundTintList = null
            }
        }

        holder.nameLBL.setOnClickListener {
            listener.onFilterSet(filter.name)
        }
    }


    class FilterVHolder(rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val nameLBL: TextView = rootPOV.nameLBL
    }
}