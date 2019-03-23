package com.anenigmatic.apogee19.screens.shared.data

import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.shared.core.Avatar
import com.anenigmatic.apogee19.screens.shared.core.User
import com.anenigmatic.apogee19.screens.shared.data.firebase.UserWatcher
import com.anenigmatic.apogee19.screens.shared.data.retrofit.UserApi
import com.anenigmatic.apogee19.screens.shared.data.storage.UserStorageData
import com.anenigmatic.apogee19.screens.shared.data.storage.UserStorage
import com.anenigmatic.apogee19.screens.shared.util.toRequestBody
import io.reactivex.Completable
import io.reactivex.Flowable
import okhttp3.RequestBody
import org.json.JSONObject

class UserRepositoryImpl(
    private val uStorage: UserStorage,
    private val uApi: UserApi,
    private val uWatcher: UserWatcher
) : UserRepository {

    private val avatars = listOf(
        Avatar(0, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
        Avatar(1, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
        Avatar(2, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
        Avatar(3, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
        Avatar(4, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}"),
        Avatar(5, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_google_logo}")
    )


    override fun getUser(): Flowable<User> {
        return uStorage.getUserData()
            .switchMap { storageData ->
                val avatar = avatars[storageData.avatarId.toInt()]
                uWatcher.watchUserId(storageData.id, storageData.isBitsian)
                uWatcher.getUserData()
                    .map { watcherData ->
                        User(storageData.id, storageData.name, storageData.jwt, storageData.qrCode, storageData.isBitsian, watcherData.balance, storageData.tickets, avatar, watcherData.coins)
                    }
            }
    }

    override fun loginBitsian(idToken: String): Completable {
        val body = JSONObject().apply {
            put("id_token", idToken)
            put("avatar", 0)
        }.toRequestBody()

        return login(body, true)
    }

    override fun loginOutstee(username: String, password: String): Completable {
        val body = JSONObject().apply {
            put("username", username)
            put("password", password)
            put("avatar", 0)
        }.toRequestBody()

        return login(body, false)
    }

    override fun logout(): Completable {
        return uStorage.setUserData(null)
    }

    override fun getAllAvatars(): Flowable<List<Avatar>> {
        return Flowable.just(avatars)
    }

    override fun chooseAvatar(avatarId: Long): Completable {
        return uStorage.getUserData()
            .firstOrError()
            .flatMapCompletable { userData ->
                val body = JSONObject().apply {
                    put("avatar", avatarId)
                }.toRequestBody()
                uApi.chooseAvatar(userData.jwt, body)
            }
            .andThen(uStorage.setAvatarId(avatarId))
    }

    override fun refreshDetails(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addMoney(amount: Int): Completable {
        return uStorage.getUserData()
            .firstOrError()
            .flatMapCompletable { userData ->
                val body = JSONObject().apply {
                    put("amount", amount)
                }.toRequestBody()
                uApi.addMoney(userData.jwt, body)
            }
    }

    override fun transferMoney(amount: Int, receivingQrCode: String): Completable {
        return uStorage.getUserData()
            .firstOrError()
            .flatMapCompletable { userData ->
                val body = JSONObject().apply {
                    put("amount", amount)
                    put("qr_code", receivingQrCode)
                }.toRequestBody()
                uApi.transferMoney(userData.jwt, body)
            }
    }


    private fun login(body: RequestBody, isBitsian: Boolean): Completable {
        return uApi.login(body)
            .flatMapCompletable { user ->
                uStorage.setUserData(UserStorageData(user.id, user.name, user.jwt, user.qrCode, isBitsian, listOf(), 0))
            }
    }
}