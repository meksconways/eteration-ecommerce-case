package com.mek.feature.home.effect

sealed interface HomeEffect {
    data class NavigateToDetail(val productId: String) : HomeEffect
    data object OpenFilterBottomSheet : HomeEffect
    data object OpenSortBottomSheet: HomeEffect
}