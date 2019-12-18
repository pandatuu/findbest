package app.findbest.vip.utils

import android.content.Context
import android.view.ViewManager
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import org.jetbrains.anko.AnkoViewDslMarker
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.internals.AnkoInternals

inline fun ViewManager.recyclerView(init: (@AnkoViewDslMarker RecyclerView).() -> Unit): RecyclerView {
    return ankoView({ ctx: Context -> RecyclerView(ctx) }, theme = 0) { init() }
}

inline fun ViewManager.flowLayout(
    ctx: Context = AnkoInternals.getContext(this),
    theme: Int = 0,
    init: FlowLayout.() -> Unit
): FlowLayout {
    return ankoView({ FlowLayout(ctx) }, theme, init)
}

inline fun ViewManager.tabLayout(init: (@AnkoViewDslMarker TabLayout).() -> Unit): TabLayout {
    return ankoView({ ctx: Context -> TabLayout(ctx) }, theme = 0) { init() }
}

inline fun ViewManager.smartRefreshLayout(init: (@AnkoViewDslMarker SmartRefreshLayout).() -> Unit): SmartRefreshLayout {
    return ankoView({ ctx: Context -> SmartRefreshLayout(ctx) }, theme = 0) { init() }
}