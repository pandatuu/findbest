package app.findbest.vip.utils

import android.content.Context
import android.view.ViewManager
import androidx.recyclerview.widget.RecyclerView
import cn.jiguang.imui.view.RoundImageView
import cn.jiguang.imui.view.ShapeImageView
import com.biao.pulltorefresh.PtrLayout
import com.google.android.material.tabs.TabLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import org.jetbrains.anko.AnkoViewDslMarker


import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.internals.AnkoInternals

inline fun ViewManager.flowLayout(
    ctx: Context = AnkoInternals.getContext(this),
    theme: Int = 0,
    init: FlowLayout.() -> Unit
): FlowLayout {
    return ankoView({ FlowLayout(ctx) }, theme, init)
}


inline fun ViewManager.shapeImageView(
    ctx: Context = AnkoInternals.getContext(this),
    theme: Int = 0,
    init: ShapeImageView.() -> Unit): ShapeImageView {
    return ankoView({ ShapeImageView(ctx) },theme,init)
}

inline fun ViewManager.roundImageView(
    ctx: Context = AnkoInternals.getContext(this),
    theme: Int = 0,
    init: RoundImageView.() -> Unit): RoundImageView {
    return ankoView({RoundImageView(ctx)},theme,init)
}


inline fun ViewManager.recyclerView(init: (@AnkoViewDslMarker RecyclerView).() -> Unit): RecyclerView {
    return ankoView({ ctx: Context -> RecyclerView(ctx) }, theme = 0) { init() }
}

inline fun ViewManager.tabLayout(init: (@AnkoViewDslMarker TabLayout).() -> Unit): TabLayout {
    return ankoView({ ctx: Context -> TabLayout(ctx) }, theme = 0) { init() }
}

inline fun ViewManager.smartRefreshLayout(init: (@AnkoViewDslMarker SmartRefreshLayout).() -> Unit): SmartRefreshLayout {
    return ankoView({ ctx: Context -> SmartRefreshLayout(ctx) }, theme = 0) { init() }
}
//inline fun ViewManager.ptrLayout(
//    ctx: Context = AnkoInternals.getContext(this),
//    theme: Int = 0,
//    init: PtrLayout.() -> Unit): PtrLayout {
//    return ankoView({ PtrLayout(ctx) },theme,init)
//}
