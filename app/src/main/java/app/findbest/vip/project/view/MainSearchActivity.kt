package app.findbest.vip.project.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import app.findbest.vip.project.fragment.MainSearchList
import app.findbest.vip.project.fragment.MainSearchTitle
import app.findbest.vip.utils.BaseActivity
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.verticalLayout

@SuppressLint("Registered")
class MainSearchActivity : BaseActivity(), MainSearchTitle.ChildrenClick, MainSearchList.ClickBack {

    lateinit var mContext: Context
    private val top = 2
    val list = 3

    private var mainList: MainSearchList? = null
    private var title: MainSearchTitle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        frameLayout {
            //上部分
            verticalLayout{
                frameLayout {
                    id = this@MainSearchActivity.top
                    title = MainSearchTitle.newInstance(this@MainSearchActivity, this@MainSearchActivity)
                    supportFragmentManager.beginTransaction().add(this@MainSearchActivity.top, title!!).commit()
                }
                //中间adapter
                frameLayout {
                    backgroundColor = Color.GREEN
                    id = list
                    mainList = MainSearchList.newInstance(this@MainSearchActivity, this@MainSearchActivity)
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