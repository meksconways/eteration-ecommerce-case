package com.mek.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mek.domain.usecase.ObserveCartBadgeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    observeCartBadgeCountUseCase: ObserveCartBadgeUseCase
) : ViewModel() {

    val badgeCount: StateFlow<Int> =
        observeCartBadgeCountUseCase(Unit)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )
}