package com.anenigmatic.apogee19.screens.profile.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.shared.core.Signing
import kotlinx.android.synthetic.main.row_signing.view.*

class SigningsAdapter : RecyclerView.Adapter<SigningsAdapter.SigningVHolder>() {

    var signings = listOf<Signing>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = signings.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SigningVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SigningVHolder(inflater.inflate(R.layout.row_signing, parent, false))
    }

    override fun onBindViewHolder(holder: SigningVHolder, position: Int) {
        val ticket = signings[position]

        holder.nameLBL.text = ticket.name
        holder.quantityLBL.text = "${ticket.quantity} Passes"
    }

    class SigningVHolder(rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val nameLBL: TextView = rootPOV.nameLBL
        val quantityLBL: TextView = rootPOV.quantityLBL
    }
}