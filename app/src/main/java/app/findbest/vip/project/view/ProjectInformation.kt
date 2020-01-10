package app.findbest.vip.project.view

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
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
    private val viewPagerId = 2
    private val mainId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val projectId = if(intent.getStringExtra("projectId")!=null){
            intent.getStringExtra("projectId")
        }else{
            ""
        }


        val listTitle = ArrayList<String>()
        listTitle.add(resources.getString(R.string.project_fragment_first))
        listTitle.add(resources.getString(R.string.project_fragment_second))
        val applicants = ProjectApplicants.newInstance(this@ProjectInformation,projectId)
        val datas = ArrayList<Fragment>()
        datas.add(ProjectDemand.newInstance(this@ProjectInformation,projectId, applicants))
        datas.add(applicants)
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
                            text = resources.getString(R.string.common_toolbar_back)
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
                        //选中title的颜色 和 未选择的title颜色
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