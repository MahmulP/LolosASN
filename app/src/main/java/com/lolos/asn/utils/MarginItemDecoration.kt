package com.lolos.asn.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(
    private val firstItemStartMargin: Int,
    private val restItemStartMargin: Int,
    private val lastItemEndMargin: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (position == 0) {
            outRect.left = firstItemStartMargin
        } else {
            outRect.left = restItemStartMargin
        }

        if (position == itemCount - 1) {
            outRect.right = lastItemEndMargin
        }
    }
}