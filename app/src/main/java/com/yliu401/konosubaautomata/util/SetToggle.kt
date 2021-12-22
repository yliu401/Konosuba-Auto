package com.yliu401.konosubaautomata.util

fun <T> Set<T>.toggle(item: T) =
    if (item in this)
        this - item
    else this + item