package com.anenigmatic.apogee19.screens.shared.data.firebase

import io.reactivex.Flowable

/**
 * Represents a real-time data source through which user data
 * can be observed/watched.
 * */
interface UserWatcher {

    /**
     * Makes the watcher watch the user with the passed user
     * id.
     * */
    fun watchUserId(userId: Long, isBitsian: Boolean)

    /**
     * Gives the data of the user who's being watched by the
     * watcher. Use [watchUserId] to watch a user.
     * */
    fun getUserData(): Flowable<UserWatcherData>
}