package ru.skillbranch.skillarticles.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.item_article.view.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.data.ArticleItemData
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.extensions.format

class ArticleItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        requestLayout()
//        View.inflate(context, R.layout.item_article, this)
    }

    fun bind(data: ArticleItemData) {
        val posterSize = context.dpToIntPx(64)
        val cornerRadius = context.dpToIntPx(8)
        val categorySize = context.dpToIntPx(40)

        Glide.with(context)
            .load(data.poster)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .override(posterSize)
            .into(iv_poster)

        Glide.with(context)
            .load(data.categoryIcon)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .override(categorySize)
            .into(iv_poster)

        tv_date.text = data.date.format()
        tv_author.text = data.author
        tv_title.text = data.title
        tv_description.text = data.description
        tv_likes_count.text = "${data.likeCount}"
        tv_comments_count.text = "${data.commentCount}"
        tv_read_duration.text = "${data.readDuration} min read"
    }
}