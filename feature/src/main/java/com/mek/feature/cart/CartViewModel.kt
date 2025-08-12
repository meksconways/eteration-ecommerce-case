package com.mek.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mek.domain.usecase.ClearCartUseCase
import com.mek.domain.usecase.DecrementCartUseCase
import com.mek.domain.usecase.IncrementCartUseCase
import com.mek.domain.usecase.ObserveCartItemsUseCase
import com.mek.feature.cart.model.CartItemUi
import com.mek.feature.cart.model.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    observeItems: ObserveCartItemsUseCase,
    private val incUseCase: IncrementCartUseCase,
    private val decUseCase: DecrementCartUseCase,
    private val clearCart: ClearCartUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CartState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        observeItems(Unit)
            .map { items ->
                val ui = items.map { it.toUi() }
                val total = items.sumOf { it.price * it.quantity }
                val totalText = "%,.0f ₺".format(total)
                CartState(
                    items = ui,
                    totalText = totalText,
                    isEmpty = ui.isEmpty(),
                    isLoading = false
                )
            }
            .catch { e ->
                _state.value = _state.value.copy(isLoading = false, errorMessage = e.message)
            }
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { newState ->
                _state.value = newState
            }
            .launchIn(viewModelScope)
    }

    fun inc(item: CartItemUi) = viewModelScope.launch {
        incUseCase(item.id)
    }

    fun dec(item: CartItemUi) = viewModelScope.launch {
        decUseCase(item.id)
    }

    fun clear() = viewModelScope.launch { clearCart(Unit) }
}