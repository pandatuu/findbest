package app.findbest.vip.project.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class EnlistSuccessTipsDialog : Fragment() {

    companion object {
        fun newInstance(buttomClick: ButtomClick): EnlistSuccessTipsDialog {
            val fragment = EnlistSuccessTipsDialog()
            fragment.buttomClick = buttomClick
            return fragment
        }
    }

    interface ButtomClick {
        fun click()
    }

    private lateinit var buttomClick: ButtomClick
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    private fun createV(): View {
        return UI {
            frameLayout {
                verticalLayout {
                    gravity = Gravity.CENTER_HORIZONTAL
                    backgroundResource = R.mipmap.iamge_submitted_successfully
                    verticalLayout {
                        gravity = Gravity.CENTER_HORIZONTAL
                        textView {
                            text = resources.getString(R.string.determine_submit)
                            textSize = 17f
                            textColor = Color.parseColor("#ffffff")
                        }.lparams{
                            topMargin = dip(76)
                        }
                        textView {
                            text = resources.getString(R.string.enlist_success)
                            textSize = 17f
                            textColor = Color.parseColor("#FF666666")
                        }.lparams(matchParent, wrapContent){
                            leftMargin = dip(40)
                            rightMargin = dip(40)
                            bottomMargin = dip(12.5f)
                            topMargin = dip(33.5f)
                        }
                    }.lparams(matchParent, wrapContent)
                    linearLayout {
                        backgroundColor = Color.parseColor("#FFE3E3E3")
                    }.lparams(matchParent,dip(1))
                    linearLayout {
                        gravity = Gravity.CENTER
                        textView {
                            text = resources.getString(R.string.common_determine)
                            textSize = 14f
                            textColor = Color.parseColor("#FF333333")
                            typeface = Typeface.DEFAULT_BOLD
                        }
                        setOnClickListener {
                            buttomClick.click()
                        }
                    }.lparams(matchParent,dip(45))
                }.lparams(matchParent, wrapContent) {
                    gravity = Gravity.CENTER_VERTICAL
                    leftMargin = dip(50)
                    rightMargin = dip(50)
                }
            }
        }.view
    }
}