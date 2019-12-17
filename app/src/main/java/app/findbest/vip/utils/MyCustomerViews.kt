package app.findbest.vip.utils

import android.content.Context
import android.view.ViewManager
import androidx.appcompat.widget.Toolbar
import org.jetbrains.anko.AnkoViewDslMarker
import org.jetbrains.anko.custom.ankoView

inline fun ViewManager.xtoolbar(init: (@AnkoViewDslMarker Toolbar).() -> Unit): Toolbar {
    return ankoView({ ctx: Context -> Toolbar(ctx) }, theme = 0) { init() }
}