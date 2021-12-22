package com.yliu401.libautomata

sealed class CompareBy {
    class Width(val width: Int) : CompareBy()
    class Height(val height: Int) : CompareBy()
    object None : CompareBy()
}