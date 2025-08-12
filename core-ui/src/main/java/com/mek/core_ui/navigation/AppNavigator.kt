package com.mek.core_ui.navigation

import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.trendyol.medusalib.navigator.Navigator

interface AppNavigator {
    fun start(fragment: Fragment)
    fun switchTab(index: Int)
    fun back()
    fun resetToRoot()
}

interface AppNavigatorBinder {
    fun bind(
        activity: FragmentActivity,
        containerId: Int,
        roots: List<() -> Fragment>,
        savedState: Bundle?,
        listener: Navigator.NavigatorListener?
    )

    fun bindOnBackPressed(owner: LifecycleOwner, dispatcher: OnBackPressedDispatcher)

    fun onSaveInstanceState(outState: Bundle)

    fun unbind()
}