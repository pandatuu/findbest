package app.findbest.vip.instance.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import app.findbest.vip.instance.fragment.InstanceSearchList
import app.findbest.vip.instance.fragment.InstanceSearchTitle
import app.findbest.vip.project.fragment.MainSearchList
import app.findbest.vip.project.fragment.MainSearchTitle
import app.findbest.vip.utils.BaseActivity
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.verticalLayout

@SuppressLint("Registered")
class InstanceSearchActivity : BaseActivity(), InstanceSearchTitle.ChildrenClick, InstanceSearchList.ClickBack {

    lateinit var mContext: Context
    private val top = 2
    val list = 3

    private var title: InstanceSearchTitle? = null
    private var mainList: InstanceSearchList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        frameLayout {
            //上部分
            verticalLayout{
                frameLayout {
                    id = this@InstanceSearchActivity.top
                    title = InstanceSearchTitle.newInstance(this@InstanceSearchActivity, this@InstanceSearchActivity)
                    supportFragmentManager.beginTransaction().add(this@InstanceSearchActivity.top, title!!).commit()
                }
                //中间adapter
                frameLayout {
                    backgroundColor = Color.GREEN
                    id = list
                    mainList = InstanceSearchList.newInstance(this@InstanceSearchActivity, this@InstanceSearchActivity)
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