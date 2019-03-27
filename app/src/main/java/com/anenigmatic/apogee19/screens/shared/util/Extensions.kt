package com.anenigmatic.apogee19.screens.shared.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anenigmatic.apogee19.screens.shared.core.User
import com.google.firebase.database.DatabaseException
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Reduces the code we need to type for casting LiveData to MutableLiveData.
 * */
fun <T> LiveData<T>.asMut(): MutableLiveData<T> {
    return (this as? MutableLiveData<T>) ?: throw IllegalArgumentException("Not a MutableLiveData")
}

/**
 * Makes CompositeDisposable hold only one disposable at a time. It is here
 * just  because I prefer  CompositeDisposable.set(disposable)  syntax over
 * Disposable = disposable syntax.
 * */
fun CompositeDisposable.set(disposable: Disposable) {
    clear()
    add(disposable)
}

fun JSONObject.toRequestBody(): RequestBody {
    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), this.toString())
}

fun<T: Any> Flowable<Optional<T>>.requireSome(): Flowable<T> {
    val operation = FlowableTransformer<Optional<T>, T> { source ->
        source.switchMap { optional ->
            when(optional) {
                is Optional.Some -> Flowable.just(optional.value)
                is Optional.None -> Flowable.error(Exception())
            }
        }
    }

    return this.compose(operation)
}

fun Throwable.extractMessage(message: String? = null): String {
    return when(this) {
        is HttpException          -> this.extractHttpMessage()
        is SocketTimeoutException -> "Couldn't connect to the server"
        is UnknownHostException   -> "Poor/No internet connection"
        is DatabaseException      -> "Remote database issue. Restart app"
        else                      -> message?: "Something went wrong"
    }
}

fun HttpException.extractHttpMessage(): String {
    val errorBody = this.response().errorBody()?.string()

    if(errorBody.isNullOrBlank()) {
        return "Something went wrong(${this.code()}) :/"
    }

    try {
        val json = JSONObject(errorBody)

        if(json.has("display_message")) {
            return json.getString("display_message")
        }

        if(json.has("detail")) {
            return json.getString("detail")
        }
    } catch(e: Exception) {
        return "An internal error occurred(${this.code()})"
    }

    return "Something went wrong(${this.code()})"
}