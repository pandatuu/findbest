package app.findbest.vip.project.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class MainSearchTitle: Fragment() {

    interface ChildrenClick{
        fun inputText(toString: String)
    }

    companion object{
        fun newInstance(context: Context, child: ChildrenClick): MainSearchTitle {
            val fragment = MainSearchTitle()
            fragment.child = child
            fragment.mContext = context
            return fragment
        }
    }

    private lateinit var child: ChildrenClick
    private lateinit var mContext: Context
    private lateinit var input: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    private fun createV(): View {
        val view = UI {
            linearLayout {
                orientation = LinearLayout.VERTICAL
                relativeLayout {
                    backgroundResource = R.drawable.ffe3e3e3_bottom_line
                    textView {
                        text = "项目"
                        textSize = 17f
                        textColor = Color.parseColor("#FF222222")
                    }.lparams(wrapContent, wrapContent){
                        alignParentBottom()
                        centerHorizontally()
                        bottomMargin = dip(8)
                    }
                }.lparams(matchParent,dip(65))
                linearLayout {
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        backgroundResource = R.drawable.fff6f6f6_around_button
                        imageView {
                            padding = dip(5)
                            imageResource = R.mipmap.tab_icon_search_nor
                        }
                        relativeLayout {
                            input = editText {
                                padding = dip(0)
                                background = null
                                hint = "搜索"
                                textSize = 15f
                                hintTextColor = Color.parseColor("#FF666666")
                                addTextChangedListener(object: TextWatcher{
                                    override fun afterTextChanged(s: Editable?) {}

                                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                        child.inputText(s.toString())
                                    }
                                })
                            }.lparams(matchParent, matchParent)
                        }.lparams(matchParent, matchParent){
                            leftMargin = dip(10)
                        }
                    }.lparams(dip(0), dip(30)){
                        weight = 1f
                        gravity = Gravity.CENTER_VERTICAL
                    }
                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL
                        textView {
                            text = "取消"
                            textSize = 15f
                            textColor= Color.parseColor("#FF333333")
                        }
                        setOnClickListener {
                            val text = input.text.toString().trim()
                            if(text.isNotBlank()){
                                input.setText("")
                            }else{
                                closeFocusjianpan()
                                activity?.finish()
                                activity?.overridePendingTransition(R.anim.left_in, R.anim.right_out)
                            }
                        }
                    }.lparams(wrapContent, matchParent){
                        leftMargin = dip(20)
                    }
                }.lparams(matchParent,dip(40)){
                    setMargins(dip(10),0,dip(10),0)
                }
            }
        }.view
        addKey()
        return view
    }

    private fun addKey(){
        input.isFocusable = true
        input.requestFocus()

        val imm = input.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
    }


    fun closeFocusjianpan() {
        //关闭ｅｄｉｔ光标
        input.clearFocus()
        //关闭键盘事件
        val phone = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        phone.hideSoftInputFromWindow(input.windowToken, 0)
    }
}