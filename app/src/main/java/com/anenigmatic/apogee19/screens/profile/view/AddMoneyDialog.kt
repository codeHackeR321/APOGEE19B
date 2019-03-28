package com.anenigmatic.apogee19.screens.profile.view

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
import kotlinx.android.synthetic.main.dia_add_money.view.*

class AddMoneyDialog : DialogFragment() {

    interface OnEnterAmountListener {

        fun onEnterAmount(amount: Int)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.dia_add_money, container, false)

        rootPOV.confirmBTN.setOnClickListener {
            (parentFragment as? OnEnterAmountListener)?.onEnterAmount(rootPOV.amountTXT.text.toString().toInt())
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
}