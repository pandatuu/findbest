package app.findbest.vip.project.view

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import app.findbest.vip.R
import app.findbest.vip.project.adapter.ProjectInformationAdapter
import app.findbest.vip.project.fragment.ProjectApplicants
import app.findbest.vip.project.fragment.ProjectDemand
import app.findbest.vip.utils.BaseActivity
import app.findbest.vip.utils.tabLayout
import com.google.android.material.tabs.TabLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.viewPager

class ProjectInformation: BaseActivity() {

    lateinit var viewpager: ViewPager
    val viewPagerId = 2
    val mainId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listTitle = ArrayList<String>()
        listTitle.add("项目需求")
        listTitle.add("应征画师")
        val datas = ArrayList<Fragment>()
        datas.add(ProjectDemand())
        datas.add(ProjectApplicants.newInstance(this@ProjectInformation))
        frameLayout {
            id = mainId
            linearLayout {
                orientation = LinearLayout.VERTICAL
                relativeLayout {
                    backgroundResource = R.drawable.ffe3e3e3_bottom_line
                    linearLayout {
                        gravity = Gravity.CENTER_VERTICAL
                        toolbar {
                            navigationIconResource = R.mipmap.nav_ico_return
                        }.lparams(dip(10),dip(18)){
                            setMargins(dip(5),dip(5),0,dip(8))
                        }
                        textView {
                            text = "返回"
                            textSize = 17f
                            textColor = Color.parseColor("#FF222222")
                        }.lparams{
                            setMargins(dip(5),dip(5),dip(5),dip(8))
                        }
                        setOnClickListener {
                            finish()
                            overridePendingTransition(R.anim.left_in, R.anim.right_out)
                        }
                    }.lparams{
                        leftMargin = dip(10)
                        alignParentBottom()
                        alignParentLeft()
                    }
                }.lparams(matchParent,dip(65))
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    backgroundColor = Color.WHITE
                    val tab = tabLayout {
                        tabGravity = TabLayout.GRAVITY_FILL
                        tabMode = TabLayout.MODE_FIXED
                        //下划线的颜色
                        setSelectedTabIndicatorColor(Color.TRANSPARENT)
                        setTabTextColors(Color.parseColor("#FF999999"),Color.parseColor("#FFFF7C00"))
                    }.lparams(matchParent, wrapContent)
                    linearLayout {
                        backgroundColor = Color.parseColor("#2EC6C6C6")
                    }.lparams(matchParent,dip(3))
                    viewpager = viewPager {
                        id = viewPagerId
                        val myPageAdapter = ProjectInformationAdapter(supportFragmentManager, datas, listTitle)
                        adapter = myPageAdapter
                    }.lparams(matchParent, dip(0)) {
                        weight = 1f
                    }
                    tab.setupWithViewPager(viewpager)
                }
            }
        }
    }
}