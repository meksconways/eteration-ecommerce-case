package com.mek.core_ui.foundation

import androidx.fragment.app.Fragment
import com.mek.core_ui.navigation.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class HiltBaseFragment: Fragment() {

    @Inject
    lateinit var appNavigator: AppNavigator
}