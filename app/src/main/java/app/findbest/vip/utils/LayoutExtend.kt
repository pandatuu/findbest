package app.findbest.vip.utils

import android.content.Context
import android.view.ViewManager
import cn.jiguang.imui.view.ShapeImageView
import com.biao.pulltorefresh.PtrLayout


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
//inline fun ViewManager.ptrLayout(
//    ctx: Context = AnkoInternals.getContext(this),
//    theme: Int = 0,
//    init: PtrLayout.() -> Unit): PtrLayout {
//    return ankoView({ PtrLayout(ctx) },theme,init)
//}

