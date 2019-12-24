package app.findbest.vip.project.fragment

import android.graphics.Color
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
                    textView {
                        text = "提交成功"
                        textSize = 17f
                        textColor = Color.parseColor("#ffffff")
                    }.lparams{
                        topMargin = dip(76)
                    }
                    textView {
                        text = "请等候项目方筛选！如果应征\n" +
                                "成功平台将会给您发送系统消息\n" +
                                "请注意查看！"
                        textSize = 17f
                        textColor = Color.parseColor("#FFFFFF")
                    }.lparams(matchParent, wrapContent){
                        leftMargin = dip(40)
                        rightMargin = dip(40)
                        topMargin = dip(34)
                    }
                    linearLayout {
                        backgroundColor = Color.parseColor("#FFE3E3E3")
                    }.lparams(matchParent,dip(1)){
                        topMargin = dip(13)
                    }
                    linearLayout {
                        gravity = Gravity.CENTER
                        textView {
                            text = "确定"
                            textSize = 14f
                            textColor = Color.parseColor("#FF333333")
                        }
                        setOnClickListener {
                            buttomClick.click()
                        }
                    }.lparams(matchParent,dip(45))
                }.lparams(wrapContent, wrapContent) {
                    gravity = Gravity.CENTER_VERTICAL
                    leftMargin = dip(50)
                    rightMargin = dip(50)
                }
            }
        }.view
    }
}