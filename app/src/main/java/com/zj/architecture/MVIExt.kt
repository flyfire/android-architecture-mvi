package com.zj.architecture

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.zj.architecture.utils.SingleLiveEvent
import kotlin.reflect.KProperty1

fun <T, A> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    action: (A) -> Unit
) {
    this.map {
        StateTuple1(prop1.get(it))
    }.distinctUntilChanged().observe(lifecycleOwner) { (a) ->
        action.invoke(a)
    }
}

fun <T, A, B> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    action: (A, B) -> Unit
) {
    this.map {
        StateTuple2(prop1.get(it), prop2.get(it))
    }.distinctUntilChanged().observe(lifecycleOwner) { (a, b) ->
        action.invoke(a, b)
    }
}

fun <T, A, B, C> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    prop3: KProperty1<T, C>,
    action: (A, B, C) -> Unit
) {
    this.map {
        StateTuple3(prop1.get(it), prop2.get(it), prop3.get(it))
    }.distinctUntilChanged().observe(lifecycleOwner) { (a, b, c) ->
        action.invoke(a, b, c)
    }
}

internal data class StateTuple1<A>(val a: A)
internal data class StateTuple2<A, B>(val a: A, val b: B)
internal data class StateTuple3<A, B, C>(val a: A, val b: B, val c: C)

fun <T> MutableLiveData<T>.setState(reducer: T.() -> T) {
    this.value = this.value?.reducer()
}

fun <T> SingleLiveEvent<T>.setEvent(value: T) {
    this.value = value
}

fun <T> withState(state: LiveData<T>, block: (T) -> Unit) {
    state.value?.let(block)
}
