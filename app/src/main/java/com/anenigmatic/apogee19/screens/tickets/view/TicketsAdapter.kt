package com.anenigmatic.apogee19.screens.tickets.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.tickets.core.Ticket
import kotlinx.android.synthetic.main.row_ticket.view.*

class TicketsAdapter(private val listener: ClickListener) : RecyclerView.Adapter<TicketsAdapter.TicketVHolder>() {

    interface ClickListener {

        fun onTicketClicked(ticket: Ticket)
    }


    var tickets = listOf<Ticket>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun getItemCount() = tickets.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TicketVHolder(inflater.inflate(R.layout.row_ticket, parent, false))
    }

    override fun onBindViewHolder(holder: TicketVHolder, position: Int) {
        val ticket = tickets[position]

        holder.nameLBL.text = when(ticket) {
            is Ticket.PlainTicket -> ticket.name
            is Ticket.ComboTicket -> ticket.name
        }

        holder.rootPOV.setOnClickListener {
            listener.onTicketClicked(ticket)
        }
    }


    class TicketVHolder(val rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val nameLBL: TextView = rootPOV.nameLBL
    }
}