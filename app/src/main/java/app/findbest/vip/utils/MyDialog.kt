package app.findbest.vip.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import app.findbest.vip.R


class MyDialog : Dialog {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    class Builder(private val context: Context) {
        private var message: String? = null
        private var isShowMessage = true
        private var isCancelable = false
        private var isCancelOutside = false

        /**
         * 设置提示信息
         * @param message
         * @return
         */

        fun setMessage(message: String): Builder {
            this.message = message
            return this@Builder
        }

        /**
         * 设置是否显示提示信息
         * @param isShowMessage
         * @return
         */
        fun setShowMessage(isShowMessage: Boolean): Builder {
            this.isShowMessage = isShowMessage
            return this@Builder
        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable
         * @return
         */

        fun setCancelable(isCancelable: Boolean): Builder {
            this.isCancelable = isCancelable
            return this@Builder
        }

        /**
         * 设置是否可以点击外部取消
         * @param isCancelOutside
         * @return
         */
        fun setCancelOutside(isCancelOutside: Boolean): Builder {
            this.isCancelOutside = isCancelOutside
            return this@Builder
        }

        fun create(): MyDialog {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.dialog_loading, null)
            val myDialog = MyDialog(context, R.style.MyDialogStyle)
//            val msgText = view.findViewById<View>(R.id.tipTextView) as TextView
//            if (isShowMessage) {
//                msgText.text = message
//            } else {
//                msgText.visibility = View.GONE
//            }
            myDialog.setContentView(view)
            myDialog.setCancelable(isCancelable)
            myDialog.setCanceledOnTouchOutside(isCancelOutside)
            //实现loading的透明度
            //            WindowManager.LayoutParams lp=mmLoading.getWindow().getAttributes();
            //            lp.alpha = 0.6f;
            //            mmLoading.getWindow().setAttributes(lp);
            return myDialog
        }
    }
}
