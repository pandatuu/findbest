package app.findbest.vip.individual.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import anet.channel.util.Utils.context
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent
import app.findbest.vip.utils.RetrofitUtils
import click
import com.alibaba.fastjson.JSON
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import withTrigger
import android.annotation.SuppressLint
import android.view.inputmethod.InputMethodManager
import app.findbest.vip.individual.api.IndividualApi
import org.jetbrains.anko.support.v4.toast


class FbMain : FragmentParent() {
    private var mContext: Context? = null
    var activityInstance: Context? = this!!.context
    lateinit var myEdit: EditText
    lateinit var myText: TextView
    lateinit var sharedPreferences: SharedPreferences
    var json: MediaType? = MediaType.parse("application/json; charset=utf-8")

    companion object {
        fun newInstance(): FbMain {
            var fbMain = FbMain()
            fbMain.activityInstance = context
            return fbMain
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (parentFragment == null) {
            mContext = activity
        } else {
            mContext = parentFragment?.context
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView()
    }

    private fun createView(): View {
        val view = View.inflate(mContext, R.layout.edit_number, null)
        myEdit = view.findViewById(R.id.et_word)
        myText = view.findViewById(R.id.tv_word_count)
        return UI {
            verticalLayout {
                setOnClickListener {
                    closeFocusjianpan()
                }
                linearLayout {
                    addView(view)
                    myEdit.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            myText.text = s.length.toString()
                        }

                        override fun afterTextChanged(s: Editable) {

                        }
                    })
                }.lparams(width = matchParent, height = wrapContent) {
                    topMargin = dip(15)
                    bottomMargin = dip(30)
                }

                button {
                    backgroundResource = R.drawable.button_gradient
                    textResource = R.string.fb_submit
                    textColor = Color.WHITE
                    textSize = 15f

                    this.withTrigger().click {
                        submit()
                    }
                }.lparams(width = matchParent, height = dip(47)) {
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }

            }
        }.view
    }

    @SuppressLint("CheckResult")
    private fun submit() {
        val userId = sharedPreferences.getString("userId", "")?.trim()
        val condition = myEdit.text
        println(condition)
        val conditionParams = mapOf(
            "type" to 3,
            "content" to condition,
            "userId" to userId
        )
        val conditionJson = JSON.toJSONString(conditionParams)
        val conditionBody = RequestBody.create(json, conditionJson)

        val retrofitUils = RetrofitUtils(activity!!, this.getString(R.string.developmentUrl))

        retrofitUils.create(IndividualApi::class.java)
            .feedback(conditionBody)
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.code() in 200..299) {
                    activity!!.finish()
                    activity!!.overridePendingTransition(R.anim.left_in, R.anim.right_out)
                }
            }, {
                toast(R.string.liNetError)
            })
    }

    private fun closeFocusjianpan() {
        //关闭ｅｄｉｔ光标
        myEdit.clearFocus()
        myText.clearFocus()
        //关闭键盘事件
        val phone = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        phone.hideSoftInputFromWindow(myEdit.windowToken, 0)
        phone.hideSoftInputFromWindow(myText.windowToken, 0)
    }

}