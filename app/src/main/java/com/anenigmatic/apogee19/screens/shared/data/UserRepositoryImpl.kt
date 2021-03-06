package com.anenigmatic.apogee19.screens.shared.data

import com.anenigmatic.apogee19.R
import com.anenigmatic.apogee19.screens.shared.core.Avatar
import com.anenigmatic.apogee19.screens.shared.core.User
import com.anenigmatic.apogee19.screens.shared.data.firebase.UserWatcher
import com.anenigmatic.apogee19.screens.shared.data.firebase.UserWatcherData
import com.anenigmatic.apogee19.screens.shared.data.retrofit.UserApi
import com.anenigmatic.apogee19.screens.shared.data.storage.UserStorageData
import com.anenigmatic.apogee19.screens.shared.data.storage.UserStorage
import com.anenigmatic.apogee19.screens.shared.util.Optional
import com.anenigmatic.apogee19.screens.shared.util.requireSome
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
        Avatar(0, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_avatar1}"),
        Avatar(1, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_avatar2}"),
        Avatar(2, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_avatar3}"),
        Avatar(3, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_avatar4}"),
        Avatar(4, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_avatar5}"),
        Avatar(5, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_avatar6}"),
        Avatar(6, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_avatar7}"),
        Avatar(7, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_avatar8}"),
        Avatar(8, "android.resource://com.anenigmatic.apogee19/${R.drawable.ic_avatar9}")
    )


    override fun getUser(): Flowable<Optional<User>> {
        return uStorage.getUserData()
            .switchMap { storageData ->
                when(storageData) {
                    is Optional.Some -> {
                        uWatcher.watchUserId(storageData.value.id, storageData.value.isBitsian)
                        uWatcher.getUserData()
                            .map { watcherData ->
                                Optional.Some(combineStorageAndWatcherData(storageData.value, watcherData))
                            }
                    }
                    is Optional.None -> {
                        Flowable.just(Optional.None)
                    }
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
            .requireSome()
            .firstOrError()
            .flatMapCompletable { userData ->
                val body = JSONObject().apply {
                    put("avatar", avatarId)
                }.toRequestBody()
                uApi.chooseAvatar(userData.jwt, body)
            }
            .andThen(uStorage.setAvatarId(avatarId))
    }

    override fun fetchDetails(): Completable {
        return uStorage.getUserData()
            .requireSome()
            .firstOrError()
            .flatMap { userData ->
                uApi.getSigningsForUser(userData.jwt)
            }
            .flatMapCompletable { tickets ->
                uStorage.setSignings(tickets)
            }
    }

    override fun addMoney(amount: Int): Completable {
        return uStorage.getUserData()
            .requireSome()
            .firstOrError()
            .flatMapCompletable { userData ->
                val body = JSONObject().apply {
                    put("amount", amount)
                }.toRequestBody()
                uApi.addMoney(userData.jwt, body)
            }
    }

    override fun transferMoney(recipientId: Long, amount: Int): Completable {
        return uStorage.getUserData()
            .requireSome()
            .firstOrError()
            .flatMapCompletable { userData ->
                val body = JSONObject().apply {
                    put("id", recipientId)
                    put("amount", amount)
                }.toRequestBody()
                uApi.transferMoney(userData.jwt, body)
            }
    }

    override fun refreshQrCode(): Completable {
        return uStorage.getUserData()
            .requireSome()
            .firstOrError()
            .flatMap { userData ->
                val body = JSONObject().toRequestBody()
                uApi.refreshQrCode(userData.jwt, body)
            }
            .flatMapCompletable { qrResponse ->
                uStorage.setQrCode(qrResponse.qrCode)
            }
    }


    private fun login(body: RequestBody, isBitsian: Boolean): Completable {
        return uApi.login(body)
            .flatMapCompletable { user ->
                uStorage.setUserData(UserStorageData(user.id, user.name, user.jwt, user.qrCode, isBitsian, listOf(), 0))
            }
            .andThen(fetchDetails())
    }

    private fun combineStorageAndWatcherData(storageData: UserStorageData, watcherData: UserWatcherData): User {
        val avatar = avatars[storageData.avatarId.toInt()]
        return User(storageData.id, storageData.name, storageData.jwt, storageData.qrCode, storageData.isBitsian, watcherData.balance, storageData.signings, avatar, watcherData.coins)
    }
}