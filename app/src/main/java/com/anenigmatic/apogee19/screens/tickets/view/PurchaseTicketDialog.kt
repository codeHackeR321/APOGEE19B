package com.anenigmatic.apogee19.screens.tickets.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.anenigmatic.apogee19.R
import kotlinx.android.synthetic.main.dia_purchase_ticket.view.*

class PurchaseTicketDialog : DialogFragment() {

    interface OnPurchaseAttemptListener {

        fun onPurchaseAttempt(ticketId: Long, isCombo: Boolean, quantity: Int)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.dia_purchase_ticket, container, false)

        rootPOV.nameLBL.text = arguments!!.getString("NAME")
        rootPOV.componentNamesLBL.text = "(${getComponentNames()})"
        rootPOV.priceLBL.text = "x ${arguments!!.getInt("PRICE")} INR"

        rootPOV.confirmBTN.setOnClickListener {
            val ticketId = arguments!!.getLong("TICKET_ID")
            val isCombo = arguments!!.getBoolean("IS_COMBO")
            val quantity = rootPOV.quantityTXT.text.toString().toInt()
            (parentFragment as? OnPurchaseAttemptListener)?.onPurchaseAttempt(ticketId, isCombo, quantity)
            dismiss()
        }

        return rootPOV
    }

    // This method has been overriden to make the rounded corners visible.
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }


    private fun getComponentNames(): String {
        val result = arguments!!.getStringArrayList("COMPONENT_NAMES")?.joinToString(", ")
        return result?: "Not a combo"
    }
}