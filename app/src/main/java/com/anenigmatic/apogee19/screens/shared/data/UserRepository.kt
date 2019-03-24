package com.anenigmatic.apogee19.screens.shared.data

import com.anenigmatic.apogee19.screens.shared.core.Avatar
import com.anenigmatic.apogee19.screens.shared.core.User
import com.anenigmatic.apogee19.screens.shared.util.Optional
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Deals with all the user related functions such as: authentication, user-
 * details etc.
 * */
interface UserRepository {

    /**
     * Gives the currently logged-in user.  If there is no  logged-in user,
     * Optional.None is given.
     *
     * Note that the user details might be out of date. To get the most up-
     * to-date details, use the [refreshDetails] method.
     * */
    fun getUser(): Flowable<Optional<User>>

    /**
     * Attempts to login the user as bitsian using their BITS mail + Google
     * Sign-in. A default avatar is associated with the user at the time of
     * login. This ensures that the user has an avatar even if he/she skips
     * selecting an avatar.
     *
     * idToken is provided by Google Sign-in.
     * */
    fun loginBitsian(idToken: String) : Completable

    /**
     * Attempts to login the user as outstee using their username and their
     * password.A default avatar is associated with the user at the time of
     * login. This ensures that the user has an avatar even if he/she skips
     * selecting an avatar.
     *
     * Username and password are decided by the user when they register for
     * the fest.
     * */
    fun loginOutstee(username: String, password: String) : Completable

    /**
     * Logs out the currently logged-in user. It doesn't matter whether the
     * user is a bitsian or an outstee.
     *
     * If the user isn't logged-in and this method is mistakenly used, then
     * also Completable.complete() is given.
     * */
    fun logout(): Completable

    /**
     * Gives all the avatars a user is allowed  to choose from. Avatars aim
     * to make the app experience feel more personalized.
     * */
    fun getAllAvatars(): Flowable<List<Avatar>>

    /**
     * Sets the user's avatar and notifies backend about the change. Its an
     * all or none operation. Either backend  gets notified and avatar gets
     * changed OR nothing happens. This condition  ensures that  the app is
     * in sync with the backend at all times.
     * */
    fun chooseAvatar(avatarId: Long): Completable

    /**
     * Makes the app get the most up-to-date user details from the backend.
     * */
    fun refreshDetails(): Completable

    /**
     * Adds money to the user's account from their SWD account. The passed-
     * in amount is in INR.
     * */
    fun addMoney(amount: Int): Completable

    /**
     * Transfers money to user  associated with the  passed-in QR Code. The
     * passed-in amount is in INR.
     * */
    fun transferMoney(amount: Int, receivingQrCode: String): Completable
}