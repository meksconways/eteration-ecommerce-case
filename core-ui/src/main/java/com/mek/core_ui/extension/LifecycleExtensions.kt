package com.mek.core_ui.extension

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <T> Fragment.launchAndRepeatWithViewLifecycle(
    flow: Flow<T>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(minActiveState) {
            flow.collect {
                action(it)
            }
        }
    }
}

inline fun <T> ComponentActivity.launchAndRepeatWithViewLifecycle(
    flow: Flow<T>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(minActiveState) {
            flow.collect {
                action(it)
            }
        }
    }
}