package com.mek.feature.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.mek.core_ui.extension.launchAndRepeatWithViewLifecycle
import com.mek.core_ui.navigation.AppNavigator
import com.mek.core_ui.navigation.AppNavigatorBinder
import com.mek.feature.R
import com.mek.feature.cart.CartFragment
import com.mek.feature.databinding.ActivityMainBinding
import com.mek.feature.favorites.FavoritesFragment
import com.mek.feature.home.fragment.HomeFragment
import com.mek.feature.profile.ProfileFragment
import com.trendyol.medusalib.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator.NavigatorListener {

    private lateinit var binding: ActivityMainBinding
    private val viewmodel: MainViewModel by viewModels()

    private val rootFragmentProvider: List<() -> Fragment> =
        listOf(
            { HomeFragment() },
            { CartFragment() },
            { FavoritesFragment() },
            { ProfileFragment() }
        )

    @Inject
    lateinit var navigator: AppNavigator

    @Inject
    lateinit var navigatorBinder: AppNavigatorBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bottom_navigation)) { view, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            view.isVisible = !imeVisible
            insets
        }

        navigatorBinder.bind(
            activity = this,
            containerId = R.id.container,
            roots = rootFragmentProvider,
            savedState = savedInstanceState,
            listener = this
        )

        navigatorBinder.bindOnBackPressed(this, onBackPressedDispatcher)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val index = when (item.itemId) {
                R.id.tab_home -> 0
                R.id.tab_cart -> 1
                R.id.tab_favorites -> 2
                R.id.tab_profile -> 3
                else -> 0
            }
            navigator.switchTab(index)
            true
        }

        binding.bottomNavigation.setOnItemReselectedListener {
            navigator.resetToRoot()
        }

        launchAndRepeatWithViewLifecycle(viewmodel.badgeCount) { count ->
            val badge = binding.bottomNavigation.getOrCreateBadge(R.id.tab_cart)
            badge.isVisible = count > 0
            badge.number = count
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        navigatorBinder.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onTabChanged(tabIndex: Int) {
        binding.bottomNavigation.menu[tabIndex].isChecked = true
    }

    override fun onDestroy() {
        navigatorBinder.unbind()
        super.onDestroy()
    }

}