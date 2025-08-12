package com.mek.eteration.navigation

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.mek.core_ui.navigation.AppNavigator
import com.mek.core_ui.navigation.AppNavigatorBinder
import com.trendyol.medusalib.navigator.MultipleStackNavigator
import com.trendyol.medusalib.navigator.Navigator
import com.trendyol.medusalib.navigator.NavigatorConfiguration
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AppNavigatorImpl @Inject constructor() : AppNavigator, AppNavigatorBinder {

    private var medusa: MultipleStackNavigator? = null
    private var backCallback: OnBackPressedCallback? = null

    override fun start(fragment: Fragment) {
        medusa?.start(fragment)
    }

    override fun switchTab(index: Int) {
        medusa?.switchTab(index)
    }

    override fun back() {
        if (medusa?.canGoBack() == true) {
            medusa?.goBack()
        }
    }

    override fun resetToRoot() {
        medusa?.resetCurrentTab()
    }

    override fun bind(
        activity: FragmentActivity,
        containerId: Int,
        roots: List<() -> Fragment>,
        savedState: Bundle?,
        listener: Navigator.NavigatorListener?
    ) {
        medusa = MultipleStackNavigator(
            fragmentManager = activity.supportFragmentManager,
            containerId = containerId,
            rootFragmentProvider = roots,
            navigatorListener = listener,
            navigatorConfiguration = NavigatorConfiguration(
                alwaysExitFromInitial = true
            )
        ).also { it.initialize(savedState) }
    }

    override fun bindOnBackPressed(
        owner: LifecycleOwner,
        dispatcher: OnBackPressedDispatcher
    ) {
        backCallback?.remove()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val m = medusa
                if (m != null && m.canGoBack()) {
                    m.goBack()
                } else {
                    isEnabled = false
                    dispatcher.onBackPressed()
                }
            }
        }

        backCallback = callback
        dispatcher.addCallback(owner, callback)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        medusa?.onSaveInstanceState(outState)
    }

    override fun unbind() {
        backCallback?.remove()
        backCallback = null
        medusa = null
    }
}