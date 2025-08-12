package com.mek.feature.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mek.feature.R
import com.mek.feature.databinding.BsFilterBinding
import com.mek.feature.filter.adapter.FilterAdapter
import com.mek.feature.filter.model.FilterOption
import com.mek.feature.filter.model.FilterParams

class FilterBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_BRANDS = "arg_brands"
        private const val ARG_MODELS = "arg_models"
        private const val ARG_SELECTED_BRANDS = "arg_sel_brands"
        private const val ARG_SELECTED_MODELS = "arg_sel_models"

        const val REQ_KEY = "req_filter"
        const val RES_BRANDS = "res_brands"
        const val RES_MODELS = "res_models"

        fun new(
            allBrands: List<String>,
            allModels: List<String>,
            selected: FilterParams
        ) = FilterBottomSheet().apply {
            arguments = bundleOf(
                ARG_BRANDS to ArrayList(allBrands),
                ARG_MODELS to ArrayList(allModels),
                ARG_SELECTED_BRANDS to ArrayList(selected.brands),
                ARG_SELECTED_MODELS to ArrayList(selected.models)
            )
        }
    }

    override fun getTheme() = R.style.AppBottomSheetDialog

    private var _binding: BsFilterBinding? = null
    private val binding get() = _binding!!

    private lateinit var brandAdapter: FilterAdapter
    private lateinit var modelAdapter: FilterAdapter

    private var allBrandOptions = mutableListOf<FilterOption>()
    private var allModelOptions = mutableListOf<FilterOption>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = BsFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, s: Bundle?) = with(binding) {
        val brands = requireArguments().getStringArrayList(ARG_BRANDS) ?: arrayListOf()
        val models = requireArguments().getStringArrayList(ARG_MODELS) ?: arrayListOf()
        val selBrands =
            requireArguments().getStringArrayList(ARG_SELECTED_BRANDS)?.toSet() ?: emptySet()
        val selModels =
            requireArguments().getStringArrayList(ARG_SELECTED_MODELS)?.toSet() ?: emptySet()

        allBrandOptions =
            brands.map { FilterOption(key = it, label = it, checked = it in selBrands) }
                .toMutableList()
        allModelOptions =
            models.map { FilterOption(key = it, label = it, checked = it in selModels) }
                .toMutableList()

        brandAdapter = FilterAdapter { changed ->
            // no-op
        }

        modelAdapter = FilterAdapter { changed ->
            // no-op
        }

        rvBrands.layoutManager = LinearLayoutManager(requireContext())
        rvBrands.adapter = brandAdapter
        rvModels.layoutManager = LinearLayoutManager(requireContext())
        rvModels.adapter = modelAdapter

        brandAdapter.submitList(allBrandOptions.toList())
        modelAdapter.submitList(allModelOptions.toList())

        searchBrand.onTextChanged = { q ->
            val list = if (q.isBlank()) allBrandOptions else allBrandOptions.filter {
                it.label.contains(
                    q,
                    true
                )
            }
            brandAdapter.submitList(list.toList())
        }
        searchModel.onTextChanged = { q ->
            val list = if (q.isBlank()) allModelOptions else allModelOptions.filter {
                it.label.contains(
                    q,
                    true
                )
            }
            modelAdapter.submitList(list.toList())
        }

        btnApply.setOnClickListener {
            val selectedBrands = allBrandOptions.filter { it.checked }.map { it.key }
            val selectedModels = allModelOptions.filter { it.checked }.map { it.key }

            parentFragmentManager.setFragmentResult(
                REQ_KEY,
                bundleOf(
                    RES_BRANDS to ArrayList(selectedBrands),
                    RES_MODELS to ArrayList(selectedModels)
                )
            )
            dismiss()
        }

        imgClose.setOnClickListener { dismiss() }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}