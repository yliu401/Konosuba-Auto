package com.yliu401.konosubaautomata.util

interface IItemTouchHelperAdapter {
    fun onItemMove(From: Int, To: Int)
}

interface IItemTouchHelperViewHolder {
    fun onItemSelected()

    fun onItemClear()
}

