package com.yliu401.konosubaautomata.scripts.models

sealed class ServantTarget(val autoSkillCode: Char) {
    object A : ServantTarget('1')
    object B : ServantTarget('2')
    object C : ServantTarget('3')

    // Emiya
    object Left : ServantTarget('7')
    object Right : ServantTarget('8')

    companion object {
        val list by lazy { listOf(A, B, C, Left, Right) }
    }
}