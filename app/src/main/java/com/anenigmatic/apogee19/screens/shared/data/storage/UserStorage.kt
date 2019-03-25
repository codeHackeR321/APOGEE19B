package com.anenigmatic.apogee19.screens.shared.data.storage

import com.anenigmatic.apogee19.screens.shared.core.Signing
import com.anenigmatic.apogee19.screens.shared.util.Optional
import io.reactivex.Completable
import io.reactivex.Flowable


/**
 * Represents an on-device data source.
 * */
interface UserStorage {

    /**
     * Gives the user data stored in the storage.
     * If no user is logged-in,  Optional.None is
     * given.
     * */
    fun getUserData(): Flowable<Optional<UserStorageData>>

    /**
     * Updates the data in the storage to passed-
     * in value. If null is passed, the user data
     * is cleared.
     * */
    fun setUserData(userData: UserStorageData?): Completable

    /**
     * Updates the stored avatar id  value to the
     * passed-in value.
     * */
    fun setAvatarId(avatarId: Long): Completable

    /**
     * Updates the stored signings to the passed-
     * in value.
     * */
    fun setSignings(signings: List<Signing>): Completable

    /**
     * Updates the stored qr code value to passed
     * -in value.
     * */
    fun setQrCode(qrCode: String): Completable
}