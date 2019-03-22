package com.anenigmatic.apogee19.screens.shared.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.shared.core.Avatar
import com.anenigmatic.apogee19.screens.shared.core.Ticket
import com.anenigmatic.apogee19.screens.shared.core.User
import com.anenigmatic.apogee19.screens.shared.data.retrofit.UserApi
import com.anenigmatic.apogee19.screens.shared.util.toRequestBody
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import okhttp3.RequestBody
import org.json.JSONObject

class UserRepositoryImpl(private val prefs: SharedPreferences, private val uApi: UserApi) : UserRepository {

    private object Keys {

        const val id = "USER_ID"
        const val name = "USER_NAME"
        const val jwt = "JWT"
        const val qrCode = "QR_CODE"
        const val isBitsian = "IS_BITSIAN"
        const val balance = "BALANCE"
        const val tickets = "TICKETS"
        const val avatarId = "AVATAR_ID"
        const val coins = "COINS"
    }


    private val avatars = listOf(
        Avatar(0, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
        Avatar(1, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
        Avatar(2, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
        Avatar(3, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
        Avatar(4, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
        Avatar(5, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}")
    )

    private val defaultAvatar = 0L


    override fun getUser(): Flowable<User> {
        return Flowable.create({ emitter ->
            try {
                val id = prefs.getLong(Keys.id, 0)
                val name = prefs.getString(Keys.name, null)
                val jwt = prefs.getString(Keys.jwt, null)
                val qrCode = prefs.getString(Keys.qrCode, null)
                val isBitsian = prefs.getBoolean(Keys.isBitsian, false)
                val balance = prefs.getInt(Keys.balance, 0)
                val tickets = prefs.getStringSet(Keys.tickets, mutableSetOf()).toTickets().sortedBy { ticket -> ticket.name }
                val avatarId = prefs.getLong(Keys.avatarId, defaultAvatar)
                val coins = prefs.getInt(Keys.coins, 0)

                if(id == 0L || name == null || jwt == null || qrCode == null) {
                    emitter.onError(Exception("User isn't logged-in"))
                } else {
                    val avatar = avatars.first { avatar -> avatar.id == avatarId }
                    emitter.onNext(User(id, name, jwt, qrCode, isBitsian, balance, tickets, avatar, coins))
                }
            } catch(e: Exception) {
                emitter.onError(e)
            }
        }, BackpressureStrategy.LATEST)
    }

    override fun loginBitsian(idToken: String): Completable {
        val body = JSONObject().apply {
            put("id_token", idToken)
            put("avatar", defaultAvatar)
        }.toRequestBody()

        return login(body, true)
    }

    override fun loginOutstee(username: String, password: String): Completable {
        val body = JSONObject().apply {
            put("username", username)
            put("password", password)
            put("avatar", defaultAvatar)
        }.toRequestBody()

        return login(body, false)
    }

    override fun logout(): Completable {
        return Completable.create { emitter ->
            try {
                prefs.edit(commit = true) {
                    putLong(Keys.id, 0)
                    putString(Keys.name, null)
                    putString(Keys.jwt, null)
                    putString(Keys.qrCode, null)
                    putBoolean(Keys.isBitsian, false)
                    putInt(Keys.balance, 0)
                    putStringSet(Keys.tickets, mutableSetOf())
                    putLong(Keys.avatarId, defaultAvatar)
                    putInt(Keys.coins, 0)
                }
                emitter.onComplete()
            } catch(e: Exception) {
                emitter.onError(e)
            }
        }
    }

    override fun getAllAvatars(): Flowable<List<Avatar>> {
        return Flowable.just(avatars)
    }

    override fun chooseAvatar(avatarId: Long): Completable {
        val jwt = prefs.getString(Keys.jwt, null)

        if(jwt == null) {
            return Completable.error(IllegalStateException("User not logged-in"))
        }

        val body = JSONObject().apply {
            put("avatar", avatarId)
        }.toRequestBody()

        return uApi.chooseAvatar(jwt, body)
            .doOnComplete {
                prefs.edit(true) {
                    putLong(Keys.avatarId, avatarId)
                }
            }
    }

    override fun refreshDetails(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun login(body: RequestBody, isBitsian: Boolean): Completable {
        return uApi.login(body)
            .doOnSuccess { responseUser ->
                prefs.edit(commit = true) {
                    putLong(Keys.id, responseUser.id)
                    putString(Keys.name, responseUser.name)
                    putString(Keys.jwt, "JWT ${responseUser.jwt}")
                    putString(Keys.qrCode, responseUser.qrCode)
                    putBoolean(Keys.isBitsian, isBitsian)
                    putInt(Keys.balance, 0)
                    putStringSet(Keys.tickets, mutableSetOf())
                    putLong(Keys.avatarId, 0)
                    putInt(Keys.coins, 0)
                }
            }
            .ignoreElement()
    }


    private fun MutableSet<String>.toTickets(): List<Ticket> {
        return this.map { str ->
            Ticket(str.substringBefore("<|>"), str.substringAfter("<|>").toInt())
        }
    }
}