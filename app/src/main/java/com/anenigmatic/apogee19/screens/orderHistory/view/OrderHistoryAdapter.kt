package com.anenigmatic.apogee19.screens.orderHistory.view

import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.menu.data.MenuRepositoryImpl
import com.example.manish.apogeewallet.screens.menu.data.room.PastOrder
import kotlinx.android.synthetic.main.row_order_list.view.*


class OrderHistoryAdapter(private val fragment : OrderHistory) : RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder>() {

    var dataset = listOf<PastOrder>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textViewOrderNo: TextView = view.findViewById(com.anenigmatic.apogee19.R.id.textViewOrderNo)
        var textViewStatus: TextView = view.findViewById(com.anenigmatic.apogee19.R.id.textViewStatus)
        var textViewOTP: Button = view.findViewById(com.anenigmatic.apogee19.R.id.otpbutton)
        var textViewTotal: TextView = view.findViewById(com.anenigmatic.apogee19.R.id.textViewTotalAmount)
        var parents : ConstraintLayout = view.findViewById(R.id.view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): OrderHistoryAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            com.anenigmatic.apogee19.R.layout.row_order_list,
            parent,
            false
        ) as View
        // set the view's size, margins, paddings and layout parameters

        return OrderHistoryAdapter.MyViewHolder(view)
    } // create a new view

    override fun getItemCount(): Int = dataset.size


    override fun onBindViewHolder(holder: OrderHistoryAdapter.MyViewHolder, position: Int) {

        holder.textViewStatus.setTextColor(fragment.resources.getColor(R.color.grn01))

        holder.textViewOTP.setOnClickListener {
            Log.d("Test" , "OTP button clicked")
            fragment.model.onOTPClicked(dataset[position].orderId)
        }

        holder.parents.setOnClickListener {

            Log.d("Testing", "Parent clicked")
            fragment.onOrderClicked(dataset[position].orderId , position)
        }

        holder.textViewOrderNo.text = "Order #"+dataset[position].orderId.toString()
        holder.textViewStatus.text = when(dataset[position].status){

            "0" -> "Pending"
            "1" -> "Accepted"
            "2" -> "Ready"
            "3" -> "Completed"
            "4" -> "Declined"
            else -> "????"
        }

        when(holder.textViewStatus.text)
        {
            "Declined" -> holder.textViewStatus.setTextColor(fragment.resources.getColor(R.color.red4))
        }

        holder.textViewTotal.text = "\u20B9 "+dataset[position].price.toString()

        if (dataset[position].showOtp){

            holder.textViewOTP.background.setColorFilter(fragment.resources.getColor(R.color.wht01) , PorterDuff.Mode.SRC_ATOP)
            holder.textViewOTP.setTextColor(fragment.resources.getColor(R.color.red4))
            Log.d("Test" , "Entered if condition in adapter")
            holder.textViewOTP.text = dataset[position].otp.toString()

        }
        else
        {
            holder.textViewOTP.text = "OTP"
            holder.textViewOTP.background.setColorFilter(fragment.resources.getColor(R.color.red4) , PorterDuff.Mode.SRC_ATOP)
            holder.textViewOTP.setTextColor(fragment.resources.getColor(R.color.wht01))
        }
    }

}