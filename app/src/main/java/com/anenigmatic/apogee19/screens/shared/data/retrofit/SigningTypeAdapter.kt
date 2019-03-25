package com.anenigmatic.apogee19.screens.shared.data.retrofit

import com.anenigmatic.apogee19.screens.shared.core.Signing
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader

class SigningTypeAdapter {

    @FromJson
    fun fromJsonToSigningList(reader: JsonReader): List<Signing> {
        val signings = mutableListOf<Signing>()

        reader.beginObject()
        while(reader.hasNext()) {
            signings.add(readSigning(reader))
        }
        reader.endObject()

        return signings
    }


    private fun readSigning(reader: JsonReader): Signing {
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

        return Signing(name, quantity)
    }
}