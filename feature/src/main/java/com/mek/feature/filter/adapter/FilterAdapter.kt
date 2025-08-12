package com.mek.feature.filter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mek.feature.databinding.ItemFilterCheckBinding
import com.mek.feature.filter.model.FilterOption

class FilterAdapter(
    private val onToggle: (FilterOption) -> Unit
) : ListAdapter<FilterOption, FilterAdapter.VH>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<FilterOption>() {
        override fun areItemsTheSame(o: FilterOption, n: FilterOption) = o.key == n.key
        override fun areContentsTheSame(o: FilterOption, n: FilterOption) =
            o.label == n.label && o.checked == n.checked
    }

    inner class VH(
        private val b: ItemFilterCheckBinding
    ) : RecyclerView.ViewHolder(b.root) {

        fun bind(item: FilterOption) = with(b) {
            check.setOnCheckedChangeListener(null)
            check.text = item.label
            check.isChecked = item.checked

            check.setOnCheckedChangeListener { _, isChecked ->
                onToggle(item.copy(checked = isChecked))
            }

            root.setOnClickListener { onToggle(item.copy(checked = !item.checked)) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inf = LayoutInflater.from(parent.context)
        return VH(ItemFilterCheckBinding.inflate(inf, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        getItem(position)?.let(holder::bind)
    }
}