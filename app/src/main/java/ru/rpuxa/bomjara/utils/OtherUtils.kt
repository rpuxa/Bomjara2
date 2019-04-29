package ru.rpuxa.bomjara.utils

import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import java.util.*
import kotlin.math.abs


fun Long.divider(): String {
    if (this == 0L)
        return "0"
    val builder = StringBuilder()
    var number = abs(this)
    val positive = (this ushr 63) == 0L

    var i = 0

    while (number != 0L) {
        builder.append(number % 10)
        number /= 10
        if (i == 2 && number != 0L) {
            i = 0
            builder.append(' ')
        } else {
            i++
        }
    }
    if (!positive)
        builder.append('-')
    return builder.reverse().toString()
}

val random = Random()

inline fun <T> MutableLiveData<T>.update(block: T.() -> Unit) {
    block(value!!)
    update()
}

fun <T> MutableLiveData<T>.update() {
    val v = value
    if (Looper.myLooper() == null)
        value = v
    else
        postValue(v)
}

inline fun <reified V : ViewModel> FragmentActivity.getViewModel() =
        ViewModelProviders.of(this).get(V::class.java)

inline fun <reified V : ViewModel> Fragment.getViewModel() = activity!!.getViewModel<V>()


inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline block: (T) -> Unit) {
    observe(owner, Observer { block(it) })
}


inline val <T> LiveData<T>.nnValue get() = value!!

inline val <T> LiveData<T>.v get() = nnValue

fun setVisibility(visibility: Int, vararg views: View) {
    for (v in views)
        v.visibility = visibility
}

var <T> MutableLiveData<T>.postValue: T
    @Deprecated("getter not supported")
    get() {
        throw UnsupportedOperationException()
    }
    set(value) {
        postValue(value)
    }


/*

fun <T> LiveData<T>.toObservable(owner: LifecycleOwner): Observable<T> {
    return Observable.create { subscriber ->
        observe(owner) { value ->
            subscriber.onNext(value)
        }
    }
}

fun <T> LiveData<T>.toObservableForever(): Observable<T> {
    return Observable.create { subscriber ->
        observeForever { value ->
            subscriber.onNext(value)
        }
    }
}


fun <T> MutableLiveData(value: T) =
        MutableLiveData<T>().apply { this.value = value }

*/
