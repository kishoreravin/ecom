package com.example.ecom.utils

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.transition.Fade
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


fun TextView.strikeThrough() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()


fun View.setVisibility(visibility: Boolean) {
    this.visibility = if (visibility) View.VISIBLE else View.GONE
}

fun String.toCurrency(): String {
    return "₹ $this"
}

fun TextView.setColor(color: String?) {
    this.setTextColor(Color.parseColor(color ?: "#0b1219"))
}

fun View.startDelayedTransition(parentView: ViewGroup?, duration: Long = 1000, visibility: Boolean = true) {
    val transition = Fade()
    transition.duration = duration
    transition.addTarget(parentView ?: this)
    TransitionManager.beginDelayedTransition(parentView, transition)
    this.setVisibility(visibility)
}

fun String.toColor(): Int {
    return Color.parseColor(this)
}

fun String.toColorListState(): ColorStateList {
    return ColorStateList.valueOf(Color.parseColor(this))
}
fun Int.toColorListState(): ColorStateList {
    return ColorStateList.valueOf(this)
}

fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {

    val query = MutableStateFlow("")

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            query.value = newText
            return true
        }
    })

    return query

}