package com.mek.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.mek.domain.model.CartItem
import com.mek.domain.model.Product
import com.mek.domain.repository.Sort
import com.mek.domain.usecase.AddToCartUseCase
import com.mek.domain.usecase.GetFilterMetaUseCase
import com.mek.domain.usecase.GetPagedProductsUseCase
import com.mek.domain.usecase.ObserveFavoriteIdsUseCase
import com.mek.domain.usecase.RefreshCatalogUseCase
import com.mek.domain.usecase.ToggleFavoriteUseCase
import com.mek.feature.filter.model.FilterParams
import com.mek.feature.home.effect.HomeEffect
import com.mek.feature.home.model.ProductItem
import com.mek.feature.home.model.toUi
import com.mek.feature.home.model.withFav
import com.mek.feature.home.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val refreshCatalogUseCase: RefreshCatalogUseCase,
    private val getPagedProductsUseCase: GetPagedProductsUseCase,
    private val getFilterMeta: GetFilterMetaUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    observeFavoriteIds: ObserveFavoriteIdsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState(isLoading = true))
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect = _effect.asSharedFlow()

    private val queryFlow = MutableStateFlow("")
    private val filterFlow =
        MutableStateFlow(GetPagedProductsUseCase.Params(sort = Sort.OLD_TO_NEW))

    val currentSort get() = filterFlow.value.sort
    val currentFilters get() = filterFlow.value

    private val _allBrands = MutableStateFlow<List<String>>(emptyList())
    private val _allModels = MutableStateFlow<List<String>>(emptyList())

    val allBrands get() = _allBrands.value
    val allModels get() = _allModels.value

    private val favIdsFlow: StateFlow<Set<String>> =
        observeFavoriteIds(Unit)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())


    init {
        refresh()
        ensureFilterMetaLoaded()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, errorMessage = null) }
                refreshCatalogUseCase(Unit)
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = e.message) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private val _loadState = MutableStateFlow<CombinedLoadStates?>(null)

    private fun ensureFilterMetaLoaded() {
        if (_allBrands.value.isNotEmpty() || _allModels.value.isNotEmpty()) return
        viewModelScope.launch {
            val meta = getFilterMeta(Unit)
            _allBrands.value = meta.brands
            _allModels.value = meta.models
        }
    }


    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val basePaging: Flow<PagingData<Product>> =
        combine(
            queryFlow.debounce(DEBOUNCE_MILLIS),
            filterFlow
        ) { q, f -> f.copy(query = q.trim().ifBlank { null }) }
            .distinctUntilChanged()
            .flatMapLatest { params -> getPagedProductsUseCase(params) } // Flow<PagingData<Product>>
            .cachedIn(viewModelScope) // <-- ÖNCE cache

    val products: Flow<PagingData<com.mek.feature.home.model.ProductItem>> =
        basePaging
            .map { paging -> paging.map { it.toUi() } }  // home.model.toUi importlu olsun
            .combine(favIdsFlow) { paging, ids ->
                paging.map { it.withFav(it.id in ids) } // home.model.withFav
            }

    fun onQueryChanged(q: String) {
        queryFlow.value = q
        _state.update { it.copy(query = q) } // sadece UI yansıması
    }

    fun onFiltersChanged(params: FilterParams) {
        val newParams = filterFlow.value.copy(
            brands = params.brands,
            models = params.models
        )
        filterFlow.value = newParams
        _state.update { it.copy(selectedFilters = newParams) }
    }

    fun onSortSelected(sort: Sort) {
        val newParams = filterFlow.value.copy(sort = sort)
        filterFlow.value = newParams
        _state.update { it.copy(selectedFilters = newParams) }
    }

    fun onFavoriteClick(productId: String) = viewModelScope.launch {
        toggleFavorite(productId)
    }

    fun addToCart(productItem: ProductItem) {
        viewModelScope.launch {
            val product = productItem
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

    fun onOpenFilterClick() =
        viewModelScope.launch { _effect.emit(HomeEffect.OpenFilterBottomSheet) }

    fun onOpenSortClick() = viewModelScope.launch { _effect.emit(HomeEffect.OpenSortBottomSheet) }
    fun onProductClick(id: String) =
        viewModelScope.launch { _effect.emit(HomeEffect.NavigateToDetail(id)) }

    fun onLoadStates(loadStates: CombinedLoadStates) {
        _loadState.value = loadStates
        val refresh = loadStates.refresh
        val isLoading = refresh is LoadState.Loading
        val errorMsg = (refresh as? LoadState.Error)?.error?.localizedMessage
        _state.update { it.copy(isLoading = isLoading, errorMessage = errorMsg) }
    }

    fun markEmpty(isEmpty: Boolean) {
        _state.update { it.copy(isEmpty = isEmpty) }
    }

    companion object {
        private const val DEBOUNCE_MILLIS = 350L
    }
}