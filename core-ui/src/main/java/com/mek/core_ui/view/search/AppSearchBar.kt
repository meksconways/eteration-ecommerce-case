package com.mek.core_ui.view.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import com.mek.core_ui.R

class AppSearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val etSearch: EditText
    private val imgSearch: ImageView
    private val imgClose: ImageView

    var onSearchAction: ((String) -> Unit)? = null
    var onTextChanged: ((String) -> Unit)? = null

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.view_app_search, this, true)

        etSearch = findViewById(R.id.etSearch)
        imgSearch = findViewById(R.id.imgSearch)
        imgClose = findViewById(R.id.imgClose)

        etSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                onSearchAction?.invoke(etSearch.text.toString())
                true
            } else false
        }

        etSearch.doAfterTextChanged { text ->
            val isNotEmpty = !text.isNullOrEmpty()
            imgClose.visibility = if (isNotEmpty) VISIBLE else GONE
            onTextChanged?.invoke(text?.toString().orEmpty())
        }

        imgSearch.setOnClickListener {
            onSearchAction?.invoke(etSearch.text.toString())
        }

        imgClose.setOnClickListener {
            etSearch.text.clear()
        }
    }

    fun setHint(hint: String) {
        etSearch.hint = hint
    }

    fun setText(text: String) {
        etSearch.setText(text)
    }

    fun getText(): String = etSearch.text.toString()
}