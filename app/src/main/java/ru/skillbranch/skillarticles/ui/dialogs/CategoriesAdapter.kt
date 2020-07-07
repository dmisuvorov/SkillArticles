package ru.skillbranch.skillarticles.ui.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_category.*
import ru.skillbranch.skillarticles.R

class CategoriesAdapter(
    private val listener: (ChoseCategoryDialog.CategoryItem, Boolean) -> Unit
) : ListAdapter<ChoseCategoryDialog.CategoryItem, CategoryVH>(CategoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryVH(view, listener)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        holder.bind(getItem(position))
    }
}

class CategoryVH(
    override val containerView: View,
    val listener: (ChoseCategoryDialog.CategoryItem, Boolean) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: ChoseCategoryDialog.CategoryItem) {
        ch_select.isChecked = item.checked
        tv_category.text = item.title
        tv_count.text = "${item.articlesCount}"
        Glide.with(containerView.context)
            .load(item.icon)
            .into(iv_icon)

        ch_select.setOnCheckedChangeListener { _, checked -> listener(item, checked) }
    }

}

class CategoryDiffCallback() : DiffUtil.ItemCallback<ChoseCategoryDialog.CategoryItem>() {
    override fun areItemsTheSame(
        oldItem: ChoseCategoryDialog.CategoryItem,
        newItem: ChoseCategoryDialog.CategoryItem
    ): Boolean =
        oldItem.categoryId == newItem.categoryId

    override fun areContentsTheSame(
        oldItem: ChoseCategoryDialog.CategoryItem,
        newItem: ChoseCategoryDialog.CategoryItem
    ): Boolean =
        oldItem == newItem

}