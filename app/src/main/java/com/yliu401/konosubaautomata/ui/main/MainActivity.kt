package com.yliu401.konosubaautomata.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.yliu401.konosubaautomata.ui.pref_support.SupportViewModel
import com.yliu401.konosubaautomata.util.CutoutManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var cutoutManager: CutoutManager

    val vm: MainScreenViewModel by viewModels()
    val supportVm: SupportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FgaApp(
                vm = vm,
                supportVm = supportVm
            )
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        cutoutManager.applyCutout(this)
    }

    override fun onStart() {
        super.onStart()
        vm.activityStarted()
    }
}