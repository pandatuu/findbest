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
import app.findbest.vip.utils.wheelView
import com.zyyoona7.wheel.WheelView
import org.jetbrains.anko.*

class ChooseCountry : Fragment() {


    interface DialogSelect {
        // 按下选项
        fun getSelectedItem(index: String)
    }

    private var mContext: Context? = null
    lateinit var dialogSelect: DialogSelect
    private lateinit var wheel: WheelView<Any>

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
        val fragmentView = createView()
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
                            setOnClickListener {
                                var string = wheel.selectedItemData.toString()
                                val length = string.indexOf(" ")
                                string = string.substring(0,length)
                                dialogSelect.getSelectedItem(string)
                            }
                        }.lparams(wrapContent, wrapContent){
                            alignParentRight()
                            centerVertically()
                            rightMargin = dip(25)
                        }
                    }.lparams(matchParent,dip(44))
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        val countryList = arrayListOf("${resources.getString(R.string.register_country_japan)}   81",
                            "${resources.getString(R.string.register_country_china)}   86",
                            "${resources.getString(R.string.register_country_korea)}   82")

                        wheel = wheelView {
                            data = countryList.toList()
                            setTextSize(16f, true)
                            normalItemTextColor = Color.parseColor("#FF333333")
                            selectedItemTextColor = Color.parseColor("#FF333333")
                            isShowDivider = true
                            setDividerColorRes(R.color.pickViewItemBorder)
                            isCyclic = false
                            visibleItems = 3
                            setLineSpacing(20f, true)
                            isCurved = false
                        }.lparams(matchParent, matchParent)
                    }.lparams(matchParent, matchParent)
                }.lparams(width = matchParent, height = dip(230))
            }
        }


        return view
    }
}