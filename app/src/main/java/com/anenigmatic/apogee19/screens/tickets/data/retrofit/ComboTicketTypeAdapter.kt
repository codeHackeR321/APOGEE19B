package com.anenigmatic.apogee19.screens.tickets.data.retrofit

import com.anenigmatic.apogee19.screens.tickets.core.Ticket
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader

class ComboTicketTypeAdapter {

    @FromJson
    fun fromJsonToComboTicket(reader: JsonReader): Ticket.ComboTicket {
        var id = 0L
        var name = ""
        var componentNames = listOf<String>()
        var price = 0

        reader.beginObject()

        while(reader.hasNext()) {
            when(reader.nextName()) {
                "id"    -> {
                    id = reader.nextLong()
                }
                "name"  -> {
                    name = reader.nextString()
                }
                "price" -> {
                    price = reader.nextInt()
                }
                "shows" -> {
                    componentNames = readComponentNames(reader)
                }
                else    -> {
                    reader.skipValue()
                }
            }
        }

        reader.endObject()

        return Ticket.ComboTicket(id, name, componentNames, price)
    }


    private fun readComponentNames(reader: JsonReader): List<String> {
        val componentNames = mutableListOf<String>()

        reader.beginArray()

        while(reader.hasNext()) {
            reader.beginObject()

            reader.nextName()
            componentNames.add(reader.nextString())

            reader.endObject()
        }

        reader.endArray()

        return componentNames
    }
}