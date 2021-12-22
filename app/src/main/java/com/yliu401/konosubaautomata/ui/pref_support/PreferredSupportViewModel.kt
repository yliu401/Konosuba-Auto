package com.yliu401.konosubaautomata.ui.pref_support

import androidx.lifecycle.ViewModel
import com.yliu401.konosubaautomata.prefs.core.BattleConfigCore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreferredSupportViewModel @Inject constructor(
    battleConfigCore: BattleConfigCore
): ViewModel() {
    val supportPrefs = battleConfigCore.support
}