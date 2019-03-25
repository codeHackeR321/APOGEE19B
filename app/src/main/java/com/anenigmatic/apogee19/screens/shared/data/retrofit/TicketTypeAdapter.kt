package com.anenigmatic.apogee19.screens.shared.data.retrofit

import com.anenigmatic.apogee19.screens.shared.core.Ticket
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader

class TicketTypeAdapter {

    @FromJson
    fun fromJsonToTicketList(reader: JsonReader): List<Ticket> {
        val tickets = mutableListOf<Ticket>()

        reader.beginObject()
        while(reader.hasNext()) {
            tickets.add(readTicket(reader))
        }
        reader.endObject()

        return tickets
    }


    private fun readTicket(reader: JsonReader): Ticket {
        reader.nextName()
        reader.beginObject()
        var name = ""
        var quantity = 0
        while(reader.hasNext()) {
            when(reader.nextName()) {
                "show_name"    -> {
                    name = reader.nextString()
                }
                "unused_count" -> {
                    quantity = reader.nextInt()
                }
                else           -> {
                    reader.skipValue()
                }
            }
        }
        reader.endObject()

        return Ticket(name, quantity)
    }
}