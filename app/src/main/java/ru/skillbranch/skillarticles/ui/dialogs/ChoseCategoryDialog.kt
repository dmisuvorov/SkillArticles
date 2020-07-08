package ru.skillbranch.skillarticles.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_category.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.viewmodels.articles.ArticlesViewModel

class ChoseCategoryDialog : DialogFragment() {
    private val viewModel: ArticlesViewModel by activityViewModels()
    private val selectedCategories = mutableListOf<String>()
    private val args: ChoseCategoryDialogArgs by navArgs()
    private val categoriesAdapter = CategoriesAdapter { categoryItem, checked ->
        toggleCategory(categoryItem, checked)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val rvCategoryItemsList =
            (layoutInflater.inflate(R.layout.fragment_chose_category_dialog, null) as RecyclerView)
                .apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = categoriesAdapter
                }
        selectedCategories.clear()
        selectedCategories.addAll(
            savedInstanceState?.getStringArray(::selectedCategories.name) ?: args.selectedCategories
        )
        categoriesAdapter.submitList(getCategoryItems())


        val adb = AlertDialog.Builder(requireContext())
            .setTitle("Chose category")
            .setPositiveButton("Apply") { _, _ ->
                viewModel.applyCategories(selectedCategories)
            }
            .setNegativeButton("Reset") { _, _ ->
                viewModel.applyCategories(emptyList())
            }
            .setView(rvCategoryItemsList)


        return adb.create()
    }

    private fun toggleCategory(category: CategoryItem, checked: Boolean) {
        if (checked) selectedCategories.add(category.categoryId)
        else selectedCategories.remove(category.categoryId)
        categoriesAdapter.submitList(getCategoryItems())
    }

    private fun getCategoryItems(): List<CategoryItem> {
        val categoriesData = args.categories.toList()
        return categoriesData.map {
            CategoryItem(
                categoryId = it.categoryId,
                icon = it.icon,
                title = it.title,
                articlesCount = it.articlesCount,
                checked = selectedCategories.contains(it.categoryId)
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArray(::selectedCategories.name, selectedCategories.toTypedArray())
    }

}

data class CategoryItem(
    val categoryId: String,
    val icon: String,
    val title: String,
    val articlesCount: Int = 0,
    val checked: Boolean = false
)


class CategoriesAdapter(
    private val listener: (CategoryItem, Boolean) -> Unit
) : ListAdapter<CategoryItem, CategoryVH>(CategoryDiffCallback()) {
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
    val listener: (CategoryItem, Boolean) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: CategoryItem) {
        ch_select.isChecked = item.checked
        tv_category.text = item.title
        tv_count.text = "${item.articlesCount}"
        Glide.with(containerView.context)
            .load(item.icon)
            .into(iv_icon)

        ch_select.setOnCheckedChangeListener { _, checked -> listener(item, checked) }
    }

}

class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryItem>() {
    override fun areItemsTheSame(
        oldItem: CategoryItem,
        newItem: CategoryItem
    ): Boolean =
        oldItem.categoryId == newItem.categoryId

    override fun areContentsTheSame(
        oldItem: CategoryItem,
        newItem: CategoryItem
    ): Boolean =
        oldItem == newItem

}