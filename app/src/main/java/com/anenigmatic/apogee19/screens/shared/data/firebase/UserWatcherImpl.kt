package com.anenigmatic.apogee19.screens.shared.data.firebase

import com.google.firebase.database.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject

class UserWatcherImpl : UserWatcher {

    private val userDataSubject = BehaviorSubject.create<UserWatcherData>()

    private var oldListener: ValueEventListener? = null

    private var watchedUserId = 0L

    private var isWatching = false


    override fun watchUserId(userId: Long, isBitsian: Boolean) {
        if(watchedUserId != userId || !isWatching) {
            val ref = getDatabaseReference(userId, isBitsian)

            oldListener?.let { oldListener ->
                ref.removeEventListener(oldListener)
            }

            val listener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        val balance = dataSnapshot.child("total_balance").value as Long
                        val coins = dataSnapshot.child("tokens").value as Long
                        userDataSubject.onNext(UserWatcherData(balance.toInt(), coins.toInt()))
                    } catch(e: Exception) {
                        userDataSubject.onError(e)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    userDataSubject.onError(error.toException())
                    isWatching = false
                }
            }

            watchedUserId = userId
            isWatching = true

            ref.addValueEventListener(listener)

            oldListener = listener
        }
    }

    override fun getUserData(): Flowable<UserWatcherData> {
        return userDataSubject.toFlowable(BackpressureStrategy.LATEST)
    }


    private fun getDatabaseReference(userId: Long, isBitsian: Boolean): DatabaseReference {
        val key = if(isBitsian) { "bitsian" } else { "participant" } + " - $userId"
        return FirebaseDatabase.getInstance().reference.child("users").child(key)
    }
}