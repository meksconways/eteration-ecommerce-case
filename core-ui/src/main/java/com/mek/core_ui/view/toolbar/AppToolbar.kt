package com.mek.core_ui.view.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.mek.core_ui.R

class AppToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val imgBack: ImageView
    private val txtTitle: TextView

    var onBackClick: (() -> Unit)? = null

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.view_app_toolbar, this, true)

        imgBack = findViewById(R.id.imgBack)
        txtTitle = findViewById(R.id.txtTitle)

        context.theme.obtainStyledAttributes(attrs, R.styleable.AppToolbar, 0, 0).apply {
            try {
                val title = getString(R.styleable.AppToolbar_at_title)
                val showBack = getBoolean(R.styleable.AppToolbar_at_showBack, true)

                txtTitle.text = title.orEmpty()
                imgBack.isVisible = showBack
            } finally {
                recycle()
            }
        }

        imgBack.setOnClickListener { onBackClick?.invoke() }
    }

    fun setTitleText(text: String) {
        txtTitle.text = text
    }

    fun setBackVisible(visible: Boolean) {
        imgBack.isVisible = visible
    }
}