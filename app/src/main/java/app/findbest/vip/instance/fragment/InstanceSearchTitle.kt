package app.findbest.vip.instance.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.message.model.ChatRecordModel
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class InstanceSearchTitle: Fragment() {

    interface ChildrenClick{
        fun inputText(toString: String)
    }

    companion object{
        fun newInstance(context: Context, child: ChildrenClick): InstanceSearchTitle {
            val fragment = InstanceSearchTitle()
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
                        text = resources.getString(R.string.instance_title)
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
                                hint = resources.getString(R.string.common_search)
                                textSize = 15f
                                hintTextColor = Color.parseColor("#FF666666")
                                singleLine = true
                                imeOptions = EditorInfo.IME_ACTION_SEARCH
                                setOnEditorActionListener { v, actionId, _ ->
                                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                        //点击搜索的时候隐藏软键盘
                                        closeFocusjianpan()
                                        // 在这里写搜索的操作,一般都是网络请求数据
                                        child.inputText(v.text.toString())
                                        true
                                    }
                                    false
                                }
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
                            text = resources.getString(R.string.common_cancel)
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