package com.anenigmatic.apogee19.screens.tickets.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.tickets.core.Ticket
import com.anenigmatic.apogee19.screens.tickets.core.TicketsViewModel
import com.anenigmatic.apogee19.screens.tickets.core.TicketsViewModel.UiOrder
import com.anenigmatic.apogee19.screens.tickets.core.TicketsViewModelFactory
import kotlinx.android.synthetic.main.fra_tickets.view.*

class TicketsFragment : Fragment(), TicketsAdapter.ClickListener, PurchaseTicketDialog.OnPurchaseAttemptListener {

    private val viewModel by lazy {
        ViewModelProviders.of(this, TicketsViewModelFactory())[TicketsViewModel::class.java]
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.fra_tickets, container, false)

        rootPOV.ticketsRCY.adapter = TicketsAdapter(this)

        rootPOV.backBTN.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }

        viewModel.orderData.observe(this, Observer { order ->
            when(order) {
                is UiOrder.ShowLoadingState -> showLoadingState()
                is UiOrder.ShowWorkingState -> showWorkingState(order.tickets)
            }
        })

        viewModel.toastData.observe(this, Observer { toast ->
            if(toast != null) {
                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
            }
        })

        return rootPOV
    }

    override fun onTicketClicked(ticket: Ticket) {
        val id = when(ticket) {
            is Ticket.PlainTicket -> ticket.id
            is Ticket.ComboTicket -> ticket.id
        }

        val name = when(ticket) {
            is Ticket.PlainTicket -> ticket.name
            is Ticket.ComboTicket -> ticket.name
        }

        val price = when(ticket) {
            is Ticket.PlainTicket -> ticket.price
            is Ticket.ComboTicket -> ticket.price
        }

        val isCombo = when(ticket) {
            is Ticket.PlainTicket -> false
            is Ticket.ComboTicket -> true
        }

        val componentNames = when(ticket) {
            is Ticket.PlainTicket -> null
            is Ticket.ComboTicket -> ticket.componentNames
        }

        PurchaseTicketDialog().apply {
            arguments = bundleOf(
                "TICKET_ID" to id,
                "NAME" to name,
                "PRICE" to price,
                "IS_COMBO" to isCombo,
                "COMPONENT_NAMES" to componentNames
            )
        }.show(childFragmentManager, "PurchaseTicketDialog")
    }

    override fun onPurchaseAttempt(ticketId: Long, isCombo: Boolean, quantity: Int) {
        viewModel.onPurchaseTicketAction(ticketId, isCombo, quantity)
    }


    private fun showLoadingState() {
        view?.let { view ->
            view.loaderPBR.visibility = View.VISIBLE
            (view.ticketsRCY.adapter as TicketsAdapter).tickets = listOf()
        }
    }

    private fun showWorkingState(tickets: List<Ticket>) {
        view?.let { view ->
            view.loaderPBR.visibility = View.GONE

            (view.ticketsRCY.adapter as TicketsAdapter).tickets = tickets
        }
    }
}