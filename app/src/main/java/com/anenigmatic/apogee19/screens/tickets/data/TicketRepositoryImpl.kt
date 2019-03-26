package com.anenigmatic.apogee19.screens.tickets.data

import com.anenigmatic.apogee19.screens.shared.data.UserRepository
import com.anenigmatic.apogee19.screens.shared.util.requireSome
import com.anenigmatic.apogee19.screens.shared.util.toRequestBody
import com.anenigmatic.apogee19.screens.tickets.core.Ticket
import com.anenigmatic.apogee19.screens.tickets.data.retrofit.TicketsApi
import io.reactivex.Completable
import io.reactivex.Flowable
import org.json.JSONObject

class TicketRepositoryImpl(private val tApi: TicketsApi, private val uRepo: UserRepository) : TicketRepository {

    override fun getPurchaseableTickets(): Flowable<List<Ticket>> {
        return uRepo.getUser()
            .requireSome()
            .firstOrError()
            .flatMap { user ->
                tApi.getPurchaseableTickets(user.jwt)
                    .map { response ->
                        response.shows.plus(response.combos)
                    }
            }
            .toFlowable()
    }

    override fun purchaseTicket(ticketId: Long, isCombo: Boolean, quantity: Int): Completable {
        return uRepo.getUser()
            .requireSome()
            .firstOrError()
            .flatMapCompletable { user ->
                val body = JSONObject().apply {
                    val plainTicket = JSONObject().apply {
                        if(!isCombo) {
                            put(ticketId.toString(), quantity)
                        }
                    }
                    val comboTicket = JSONObject().apply {
                        if(isCombo) {
                            put(ticketId.toString(), quantity)
                        }
                    }

                    put("individual", plainTicket)
                    put("combos", comboTicket)
                }

                tApi.purchaseTicket(user.jwt, body.toRequestBody())
            }
            .andThen(uRepo.fetchDetails())
    }
}