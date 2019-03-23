package com.anenigmatic.apogee19.screens.shared.data.storage

import io.reactivex.Completable
import io.reactivex.Flowable


/**
 * Represents an on-device data source.
 * */
interface UserStorage {

    /**
     * Gives the user data stored in the storage.
     * */
    fun getUserData(): Flowable<UserStorageData>

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
}