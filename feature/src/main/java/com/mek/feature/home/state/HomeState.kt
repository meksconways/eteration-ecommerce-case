package com.mek.feature.home.state

import com.mek.domain.usecase.GetPagedProductsUseCase

data class HomeState(
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val errorMessage: String? = null,
    val query: String = "",
    val selectedFilters: GetPagedProductsUseCase.Params = GetPagedProductsUseCase.Params(),
)