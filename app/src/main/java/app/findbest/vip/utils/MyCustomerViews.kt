package app.findbest.vip.utils

import android.content.Context
import android.view.ViewManager
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.AnkoViewDslMarker
import org.jetbrains.anko.custom.ankoView

inline fun ViewManager.recyclerView(init: (@AnkoViewDslMarker RecyclerView).() -> Unit): RecyclerView {
    return ankoView({ ctx: Context -> RecyclerView(ctx) }, theme = 0) { init() }
}