package app.findbest.vip.painter.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import app.findbest.vip.painter.fragment.PainterSearchList
import app.findbest.vip.painter.fragment.PainterSearchTitle
import app.findbest.vip.utils.BaseActivity
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.verticalLayout

@SuppressLint("Registered")
class PainterSearchActivity : BaseActivity(), PainterSearchTitle.ChildrenClick, PainterSearchList.ClickBack {

    lateinit var mContext: Context
    private val top = 2
    val list = 3

    private var mainList: PainterSearchList? = null
    private var title: PainterSearchTitle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        frameLayout {
            //上部分
            verticalLayout{
                frameLayout {
                    id = this@PainterSearchActivity.top
                    title = PainterSearchTitle.newInstance(this@PainterSearchActivity, this@PainterSearchActivity)
                    supportFragmentManager.beginTransaction().add(this@PainterSearchActivity.top, title!!).commit()
                }
                //中间adapter
                frameLayout {
                    backgroundColor = Color.GREEN
                    id = list
                    mainList = PainterSearchList.newInstance(this@PainterSearchActivity, this@PainterSearchActivity)
                    supportFragmentManager.beginTransaction().add(list, mainList!!).commit()
                }.lparams(matchParent, matchParent)
                setOnClickListener {
                    title?.closeFocusjianpan()
                }
            }
        }

    }

    override fun inputText(toString: String) {
        mainList?.setText(toString)
    }

    override fun clickback() {
        title?.closeFocusjianpan()
    }
}