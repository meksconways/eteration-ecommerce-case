package com.mek.feature.filter.model

import com.mek.domain.usecase.GetPagedProductsUseCase

data class FilterParams(
    val brands: Set<String> = emptySet(),
    val models: Set<String> = emptySet()
)

fun GetPagedProductsUseCase.Params.toFilterParams() =
    FilterParams(
        brands = this.brands.orEmpty(),
        models = this.models.orEmpty()
    )