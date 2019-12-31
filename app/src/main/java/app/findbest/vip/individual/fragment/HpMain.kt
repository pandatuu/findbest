package app.findbest.vip.individual.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent
import app.findbest.vip.utils.DialogUtils
import app.findbest.vip.utils.MyDialog
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI


class HpMain:FragmentParent() {
    private var mContext: Context? = null

    private lateinit var agreementWeb: WebView
    var thisDialog: MyDialog?=null
    var mHandler = Handler()
    var r: Runnable = Runnable {
        //do something

        if (thisDialog!=null && thisDialog?.isShowing==true && activity!=null){
            val toast = Toast.makeText(activity, "ネットワークエラー", Toast.LENGTH_SHORT)//网路出现问题
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
        DialogUtils.hideLoading(thisDialog)
    }

    companion object {
        fun newInstance(): HpMain {
            return HpMain()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(parentFragment==null){
            mContext = activity
        }else{
            mContext = parentFragment?.context
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun createView(): View {
        return UI {
            linearLayout {
                scrollView {
                    agreementWeb = webView {
                        this.settings.javaScriptEnabled = true
                        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
                        this.webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                                view?.loadUrl(request?.url.toString())
                                return true
                            }

                            // 设置等待过程
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                thisDialog= DialogUtils.showLoading(mContext!!)
                                mHandler.postDelayed(r, 20000)
                            }

                            // 等待结束，取消提示
                            override fun onPageFinished(view: WebView?, url: String?) {
                                DialogUtils.hideLoading(thisDialog)
                            }
                        }
                        this.loadUrl(activity?.getString(R.string.helpUrl))
                    }.lparams(width = matchParent,height = matchParent){}
                }.lparams(width = matchParent,height = wrapContent){
                    weight = 1f
                }
            }
        }.view
    }

    override fun onDestroy() {
        super.onDestroy()
        agreementWeb.destroy()

    }

}