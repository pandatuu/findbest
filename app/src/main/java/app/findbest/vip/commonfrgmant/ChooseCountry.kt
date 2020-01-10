package app.findbest.vip.commonfrgmant

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import org.jetbrains.anko.*

class ChooseCountry : Fragment() {

    private var mContext: Context? = null
    lateinit var dialogSelect: DialogSelect


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity

    }

    companion object {
        fun newInstance( ): ChooseCountry {
            var f = ChooseCountry()
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var fragmentView = createView()
        mContext = activity
        dialogSelect = activity as DialogSelect
        return fragmentView
    }

    @SuppressLint("ResourceType", "SetTextI18n")
    private fun createView(): View {

        val view = with(mContext!!) {
            linearLayout {
                gravity = Gravity.BOTTOM
                verticalLayout {
                    backgroundColor= Color.WHITE
                    relativeLayout {
                        backgroundColor = Color.parseColor("#FFF6F6F6")
                        textView {
                            text = resources.getString(R.string.common_choose_country)
                            textSize = 16f
                            textColor = Color.parseColor("#FF333333")
                        }.lparams(wrapContent, wrapContent){
                            centerInParent()
                        }
                        textView {
                            text = resources.getString(R.string.common_determine)
                            textSize = 14f
                            textColor = Color.parseColor("#FFF87A1B")
                        }.lparams(wrapContent, wrapContent){
                            alignParentRight()
                            centerVertically()
                            rightMargin = dip(25)
                        }
                    }.lparams(matchParent,dip(44))
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        val countryList = arrayListOf(resources.getString(R.string.register_country_japan),
                            resources.getString(R.string.register_country_china),resources.getString(R.string.register_country_korea))
                        val codeList = arrayListOf("81","86","82")
                        for (index in countryList.indices){
                                linearLayout {
                                    backgroundResource = R.drawable.login_input_bottom
                                    gravity = Gravity.CENTER
                                    linearLayout {
                                        orientation = LinearLayout.HORIZONTAL
                                        textView {
                                            text = countryList[index]
                                            textSize = 16f
                                            textColor = Color.parseColor("#FF333333")
                                        }
                                        textView {
                                            text = "+${codeList[index]}"
                                            textSize = 16f
                                            textColor = Color.parseColor("#FF333333")
                                        }.lparams{
                                            leftMargin = dip(20)
                                        }
                                    }.lparams(wrapContent, wrapContent){
                                        gravity = Gravity.CENTER
                                    }
                                    setOnClickListener {
                                        dialogSelect.getSelectedItem(index)
                                    }
                                }.lparams(matchParent,dip(45))
                            }
                    }.lparams(matchParent, matchParent)
                }.lparams(width = matchParent, height = dip(230))
            }
        }


        return view
    }



    interface DialogSelect {
        // 按下选项
        fun getSelectedItem(index: Int)
    }

}