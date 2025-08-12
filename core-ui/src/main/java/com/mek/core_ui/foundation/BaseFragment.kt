package com.mek.core_ui.foundation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.mek.core_ui.R

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) :
    HiltBaseFragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected abstract val viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    abstract fun observeViewModel()

    protected fun showError(errorMessage: String, positiveButtonClick: (() -> Unit)? = null) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.core_alert_title))
            .setMessage(errorMessage)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.core_alert_button_ok)) { _, _ -> positiveButtonClick?.invoke() }
            .create()
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}