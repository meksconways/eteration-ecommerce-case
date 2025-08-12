package com.mek.eteration.di

import com.mek.core_ui.navigation.AppNavigator
import com.mek.core_ui.navigation.AppNavigatorBinder
import com.mek.eteration.navigation.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
abstract class NavigationModule {
    @Binds
    @ActivityScoped
    abstract fun bindAppNavigator(impl: AppNavigatorImpl): AppNavigator

    @Binds
    @ActivityScoped
    abstract fun bindNavigatorBinder(impl: AppNavigatorImpl): AppNavigatorBinder
}