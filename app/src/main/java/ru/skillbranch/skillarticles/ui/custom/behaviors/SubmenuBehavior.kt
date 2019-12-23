package ru.skillbranch.skillarticles.ui.custom.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import ru.skillbranch.skillarticles.extensions.dpToPx
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min

class SubmenuBehavior<V : View>(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<V>(context, attrs) {
    private var centerX: Float = context.dpToPx(200)
    private var centerY: Float = context.dpToPx(96)

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        if (child.visibility == View.VISIBLE) {
            child.translationX = max(0f, min(hypot(centerX, centerY), child.translationX + dy))
            child.translationY = max(0f, min(hypot(centerX, centerY), child.translationY + dy))
        }
    }
}