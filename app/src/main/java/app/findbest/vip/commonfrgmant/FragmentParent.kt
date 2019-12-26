package app.findbest.vip.commonfrgmant

import android.app.Activity
import android.content.Context
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

import androidx.fragment.app.Fragment

open class FragmentParent: Fragment() {


    open fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId =
            context.getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId)
            var scale = context.getResources().getDisplayMetrics().density;
            result = ((result / scale + 0.5f).toInt());
        }
        return result
    }


    fun closeSoftKeyboard(view: View?) {
        if (view == null || view.windowToken == null) {
            return
        }
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

     fun getDisplay(context: Context): Display? {
        val wm: WindowManager?
        if (context is Activity) {
            val activity = context as Activity
            wm = activity.getWindowManager()
        } else {
            wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
        return if (wm != null) {
            wm!!.getDefaultDisplay()
        } else null
    }

    fun px2dp(px: Float): Int {
        val scale = resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    fun dp2px(value: Float): Int {
        val scale = resources.displayMetrics.density
        return (value * scale + 0.5f).toInt()
    }


}