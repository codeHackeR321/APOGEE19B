package com.anenigmatic.apogee19.screens.shared.core

/**
 * Represents an avatar that a user can choose for customizing his/her
 * app experience.
 *
 * @property picUri  is the URI of the avatar's  picture. Use of a URI
 *      instead of a URL allows us to refer to both online and offline
 *      (R.drawable) pictures.
 * */
data class Avatar(val id: Long, val picUri: String)