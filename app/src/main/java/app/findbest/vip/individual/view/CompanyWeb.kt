package app.findbest.vip.individual.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import app.findbest.vip.R
import app.findbest.vip.utils.BaseActivity
import org.jetbrains.anko.*

class CompanyWeb : BaseActivity() {

    private lateinit var web: WebView
    private lateinit var titleText: TextView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            relativeLayout {
                backgroundResource = R.drawable.ffe3e3e3_bottom_line
                linearLayout {
                    gravity = Gravity.CENTER
                    toolbar {
                        navigationIconResource = R.mipmap.icon_back
                        setOnClickListener {
                            finish()
                            overridePendingTransition(R.anim.left_in, R.anim.right_out)
                        }
                    }.lparams(dip(10), dip(18))
                    setOnClickListener {
                        finish()
                        overridePendingTransition(R.anim.left_in, R.anim.right_out)
                    }
                }.lparams(dip(30), dip(25)) {
                    alignParentBottom()
                    alignParentLeft()
                    leftMargin = dip(15)
                    bottomMargin = dip(10)
                }
                titleText = textView {
                    textSize = 17f
                    textColor = Color.parseColor("#FF222222")
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams {
                    centerHorizontally()
                    alignParentBottom()
                    bottomMargin = dip(10)
                }
            }.lparams(matchParent, dip(65))
            web = webView {
                loadUrl("https://findbest.vip")
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.blockNetworkImage = false
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.javaScriptCanOpenWindowsAutomatically = true
                settings.setSupportMultipleWindows(true)
                webChromeClient = object:  WebChromeClient(){
                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        super.onReceivedTitle(view, title)
                        titleText.text = title
                    }
                }
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        view?.loadUrl(request?.url.toString())
                        return true
                    }

                    // 设置等待过程
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                        thisDialog= DialogUtils.showLoading(this@CompanyWebSiteActivity)
//                        mHandler.postDelayed(r, 20000)
                    }

                    // 等待结束，取消提示
                    override fun onPageFinished(view: WebView?, url: String?) {
//                        DialogUtils.hideLoading(thisDialog)
                    }
                }
            }
        }
    }

    //打开网页,回退时不会退出activity,而是退回上个网页
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//监听返回键，如果可以后退就后退
            if (web.canGoBack()) {
                web.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}