package app.findbest.vip.individual.fragment

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
import org.jetbrains.anko.support.v4.UI

class ChooseRefuse : Fragment() {

    interface DialogSelect {
        // 按下选项
        fun getSelectedItem(str: String)
    }

    companion object {
        fun newInstance(context: Context,dialogSelect: DialogSelect): ChooseRefuse {
            val fragment = ChooseRefuse()
            fragment.mContext = context
            fragment.dialogSelect = dialogSelect
            return fragment
        }
    }

    private lateinit var mContext: Context
    private lateinit var dialogSelect: DialogSelect

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView()
    }

    @SuppressLint("ResourceType", "SetTextI18n")
    private fun createView(): View {
        return UI{
            linearLayout {
                gravity = Gravity.BOTTOM
                verticalLayout {
                    backgroundColor = Color.WHITE
                    relativeLayout {
                        backgroundColor = Color.parseColor("#FFF6F6F6")
                        textView {
                            text = resources.getString(R.string.my_project_invite_refuse)
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
                    }.lparams(matchParent, dip(44))
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        val countryList = arrayListOf(resources.getString(R.string.my_project_invite_refuse_one),
                            resources.getString(R.string.my_project_invite_refuse_two),resources.getString(R.string.my_project_invite_refuse_three),
                            resources.getString(R.string.my_project_invite_refuse_four))
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
                                }.lparams(wrapContent, wrapContent){
                                    gravity = Gravity.CENTER
                                }
                                setOnClickListener {
                                    dialogSelect.getSelectedItem(countryList[index])
                                }
                            }.lparams(matchParent, dip(45))
                        }
                    }.lparams(matchParent, matchParent)
                }.lparams(width = matchParent, height = dip(230))
            }
        }.view
    }
}