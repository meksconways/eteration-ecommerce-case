package com.mek.feature.sorting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mek.domain.repository.Sort
import com.mek.feature.R
import com.mek.feature.databinding.BsSortBinding

class SortBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_SELECTED = "arg_selected"
        const val REQ_KEY = "req_sort"
        const val RES_SORT = "res_sort"

        fun new(selected: Sort) = SortBottomSheet().apply {
            arguments = bundleOf(ARG_SELECTED to selected.name)
        }
    }

    override fun getTheme() = R.style.AppBottomSheetDialog

    private var _binding: BsSortBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BsSortBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val selected = Sort.valueOf(
            requireArguments().getString(ARG_SELECTED) ?: Sort.OLD_TO_NEW.name
        )

        when (selected) {
            Sort.OLD_TO_NEW -> binding.rbOldToNew.isChecked = true
            Sort.NEW_TO_OLD -> binding.rbNewToOld.isChecked = true
            Sort.PRICE_LOW_TO_HIGH -> binding.rbPriceLowHigh.isChecked = true
            Sort.PRICE_HIGH_TO_LOW -> binding.rbPriceHighLow.isChecked = true
        }

        binding.btnApply.setOnClickListener {
            val checked = when (binding.rgSort.checkedRadioButtonId) {
                R.id.rbOldToNew -> Sort.OLD_TO_NEW
                R.id.rbNewToOld -> Sort.NEW_TO_OLD
                R.id.rbPriceLowHigh -> Sort.PRICE_LOW_TO_HIGH
                R.id.rbPriceHighLow -> Sort.PRICE_HIGH_TO_LOW
                else -> Sort.OLD_TO_NEW
            }
            parentFragmentManager.setFragmentResult(
                REQ_KEY, bundleOf(RES_SORT to checked.name)
            )
            dismiss()
        }

        binding.imgClose.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}