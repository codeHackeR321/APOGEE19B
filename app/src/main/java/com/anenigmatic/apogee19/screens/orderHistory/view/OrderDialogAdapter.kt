package com.anenigmatic.apogee19.screens.orderHistory.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anenigmatic.apogee19.screens.menu.data.room.OrderItem

class OrderDialogAdapter(private var dataset : List<OrderItem>) : RecyclerView.Adapter<OrderDialogAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        var nameLBL: TextView = view.findViewById(com.anenigmatic.apogee19.R.id.nameLBL)
        var infoLBL: TextView= view.findViewById(com.anenigmatic.apogee19.R.id.infoLBL)

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): OrderDialogAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            com.anenigmatic.apogee19.R.layout.row_order_history,
            parent,
            false
        ) as View
        // set the view's size, margins, paddings and layout parameters

        return OrderDialogAdapter.MyViewHolder(view)
    } // create a new view

    override fun getItemCount(): Int = dataset.size


    override fun onBindViewHolder(holder: OrderDialogAdapter.MyViewHolder, position: Int) {


        holder.nameLBL.text = dataset[position].name
        holder.infoLBL.text="\u20B9 ${dataset[position].price} X ${dataset[position].quantity}"

    }
}