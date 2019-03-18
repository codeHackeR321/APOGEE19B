package com.anenigmatic.apogee19.screens.shared.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.shared.core.Avatar
import com.anenigmatic.apogee19.screens.shared.core.User
import com.anenigmatic.apogee19.screens.shared.data.retrofit.UserApi
import com.anenigmatic.apogee19.screens.shared.util.toRequestBody
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
        const val avatar = "AVATAR"
        const val coins = "COINS"
    }


    private val defaultAvatar = 0L


    override fun getUser(): Maybe<User> {
        return Maybe.create { emitter ->
            try {
                val id = prefs.getLong(Keys.id, 0)
                val name = prefs.getString(Keys.name, null)
                val jwt = prefs.getString(Keys.jwt, null)
                val qrCode = prefs.getString(Keys.qrCode, null)
                val avatar = prefs.getLong(Keys.avatar, defaultAvatar)
                val coins = prefs.getInt(Keys.coins, 0)

                if(id == 0L || name == null || jwt == null || qrCode == null) {
                    emitter.onComplete()
                } else {
                    emitter.onSuccess(User(id, name, jwt, qrCode, avatar, coins))
                }
            } catch(e: Exception) {
                emitter.onError(e)
            }
        }
    }

    override fun loginBitsian(idToken: String): Completable {
        val body = JSONObject().apply {
            put("id_token", idToken)
            put("avatar", defaultAvatar)
        }.toRequestBody()

        return login(body)
    }

    override fun loginOutstee(username: String, password: String): Completable {
        val body = JSONObject().apply {
            put("username", username)
            put("password", password)
            put("avatar", defaultAvatar)
        }.toRequestBody()

        return login(body)
    }

    override fun logout(): Completable {
        return Completable.create { emitter ->
            try {
                prefs.edit(commit = true) {
                    putLong(Keys.id, 0)
                    putString(Keys.name, null)
                    putString(Keys.jwt, null)
                    putString(Keys.qrCode, null)
                    putLong(Keys.avatar, defaultAvatar)
                    putInt(Keys.coins, 0)
                }
                emitter.onComplete()
            } catch(e: Exception) {
                emitter.onError(e)
            }
        }
    }

    override fun getAllAvatars(): Flowable<List<Avatar>> {
        return Flowable.just(listOf(
            Avatar(0, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
            Avatar(1, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
            Avatar(2, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
            Avatar(3, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
            Avatar(4, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
            Avatar(5, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}")
        ))
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
                    putLong(Keys.avatar, avatarId)
                }
            }
    }

    override fun refreshDetails(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun login(body: RequestBody): Completable {
        return uApi.login(body)
            .doOnSuccess { responseUser ->
                prefs.edit(commit = true) {
                    putLong(Keys.id, responseUser.id)
                    putString(Keys.name, responseUser.name)
                    putString(Keys.jwt, "JWT ${responseUser.jwt}")
                    putString(Keys.qrCode, responseUser.qrCode)
                    putLong(Keys.avatar, 0)
                    putInt(Keys.coins, 0)
                }
            }
            .ignoreElement()
    }
}