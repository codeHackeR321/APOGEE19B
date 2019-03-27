package com.anenigmatic.apogee19.screens.orderHistory.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.menu.data.room.OrderItem
import kotlinx.android.synthetic.main.dia_past_order_detail.*
import kotlinx.android.synthetic.main.dia_past_order_detail.view.*

class OrderDetailDialog : DialogFragment() {

    private var currentContext : Context? = null
    private val totalPrice by lazy {
        arguments!!.get("Total Price") as Int
    }
    private val orderItems by lazy {
        arguments!!.getStringArrayList("Order List")!!.map { str ->
            val strs = str.split("<|>")
            OrderItem(0, strs[0].toInt(), 0, 0, strs[1], strs[2].toInt(), strs[3].toInt())
        }
    }
    private val otp by lazy {
        arguments!!.get("OTP") as Int
    }
    private val status by lazy {
        arguments!!.get("Status") as String
    }
    private val stallName by lazy {
        arguments!!.get("Stall Name") as String
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view  = inflater.inflate(R.layout.dia_past_order_detail, container, false)
        currentContext = view.context

        view.closeDialog.setOnClickListener {
            dismiss()
        }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onStart() {
        diaPastOrderLayout.minHeight = ((parentFragment!!.view!!.height)*0.85).toInt()
        diaPastOrderLayout.minWidth = ((parentFragment!!.view!!.width)*0.85).toInt()
        closeDialog.setColorFilter(view!!.resources.getColor(R.color.yel03), PorterDuff.Mode.SRC_IN)

        view!!.textViewTotal.text = "Total: \u20B9 $totalPrice"
        view!!.textViewOrderNo.text = "Order #${orderItems.first().orderId}"
        if (status >= "2")
            view!!.textViewOTP.text = "OTP : ${otp}"
        else
            view!!.textViewOTP.text = "OTP : ????"
        view!!.textViewStatus.text = when(status)
        {
            "0" -> "Pending"
            "1" -> "Accepted"
            "2" -> "Ready"
            "3" -> "OTP has been used"
            "4" -> "Declined"
            else -> "????"
        }
        view!!.textViewVendorName.text = stallName
        //items.sortedBy { it.stallId }
        recyViewCart.adapter = OrderDialogAdapter(orderItems)
        Log.d("Test","orderItems: $orderItems")
        recyViewCart.layoutManager = LinearLayoutManager(currentContext)
        recyViewCart.adapter!!.notifyDataSetChanged()

        super.onStart()
    }

}
