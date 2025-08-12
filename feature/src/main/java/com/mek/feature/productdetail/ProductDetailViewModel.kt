package com.mek.feature.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mek.domain.model.CartItem
import com.mek.domain.usecase.AddToCartUseCase
import com.mek.domain.usecase.GetProductByIdUseCase
import com.mek.domain.usecase.ObserveFavoriteIdsUseCase
import com.mek.domain.usecase.ToggleFavoriteUseCase
import com.mek.domain.util.DataState
import com.mek.feature.productdetail.model.toUi
import com.mek.feature.productdetail.model.withFav
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductById: GetProductByIdUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val observeFavoriteIds: ObserveFavoriteIdsUseCase,
    savedState: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state = _state.asStateFlow()

    private val productId: String = checkNotNull(savedState["productId"])

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        when (val res = getProductById(productId)) {
            is DataState.Success -> _state.update {
                it.copy(isLoading = false, product = res.body.toUi())
            }

            is DataState.Failure -> _state.update {
                it.copy(isLoading = false, errorMessage = res.errorMessage)
            }
        }

        observeFavoriteIds(Unit)
            .map { ids -> productId in ids }
            .distinctUntilChanged()
            .onEach { fav -> _state.update { it.copy(product = it.product?.withFav(fav)) } }
            .launchIn(viewModelScope)
    }

    fun addToCart() {
        viewModelScope.launch {
            val product = state.value.product
            if (product != null) {
                val item = CartItem(
                    productId = product.id,
                    name = product.name,
                    price = product.price,
                    imageUrl = product.imageUrl,
                    quantity = 1
                )
                addToCartUseCase.invoke(item)
            }
        }
    }

    fun onToggleFavorite() = viewModelScope.launch { toggleFavorite(productId) }
}