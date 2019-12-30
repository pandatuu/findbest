package app.findbest.vip.individual.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.FragmentParent
import click
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import withTrigger

class HMain:FragmentParent() {
    private var mContext: Context? = null
    lateinit var myEdit:EditText
    lateinit var myText:TextView

    companion object {
        fun newInstance(): HMain {
            return HMain()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView()
    }

    private fun createView():View {
        return UI {
            verticalLayout {
                verticalLayout {
                    gravity = Gravity.CENTER_HORIZONTAL
                    imageView {
                        imageResource = R.mipmap.default_avatar
                    }.lparams {
                        width = dip(120)
                        height = dip(120)
                        topMargin = dip(46)
                        gravity = Gravity.CENTER_HORIZONTAL
                    }

                    textView {
                        text = "Amy Gonzalez"
                        textColor = Color.parseColor("#333333")
                        textSize = 17f
                        gravity = Gravity.CENTER
                    }.lparams(width = matchParent,height = wrapContent){
                        topMargin = dip(20)
                    }
                }.lparams {
                    width = matchParent
                    height = matchParent
                    backgroundColor = Color.parseColor("#FFFFFFFF")
                }
            }
        }.view
    }


}