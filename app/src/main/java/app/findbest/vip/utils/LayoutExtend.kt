package app.findbest.vip.utils

import android.content.Context
import android.view.ViewManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import cn.jiguang.imui.view.RoundImageView
import cn.jiguang.imui.view.ShapeImageView
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.tabs.TabLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zyyoona7.wheel.WheelView
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

inline fun ViewManager.photoView(init: (@AnkoViewDslMarker PhotoView).() -> Unit): PhotoView {
    return ankoView({ ctx: Context -> PhotoView(ctx) }, theme = 0) { init() }
}
inline fun ViewManager.appCompatTextView(init: (@AnkoViewDslMarker AppCompatTextView).() -> Unit): AppCompatTextView {
    return ankoView({ ctx: Context -> AppCompatTextView(ctx) }, theme = 0) { init() }
}
inline fun ViewManager.wheelView(init: (@AnkoViewDslMarker WheelView<Any>).() -> Unit): WheelView<Any> {
    return ankoView({ ctx: Context -> WheelView(ctx) }, theme = 0) { init() }
}
//inline fun ViewManager.ptrLayout(
//    ctx: Context = AnkoInternals.getContext(this),
//    theme: Int = 0,
//    init: PtrLayout.() -> Unit): PtrLayout {
//    return ankoView({ PtrLayout(ctx) },theme,init)
//}

