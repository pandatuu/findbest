package app.findbest.vip.individual.view

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import app.findbest.vip.R
import app.findbest.vip.individual.fragment.*
import app.findbest.vip.project.adapter.ProjectInformationAdapter
import app.findbest.vip.utils.BaseActivity
import app.findbest.vip.utils.tabLayout
import com.google.android.material.tabs.TabLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.viewPager

class ProjectSideProjectInfo : BaseActivity() {

    private lateinit var viewpager: ViewPager
    private val viewPagerId = 2
    private val mainId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val projectId = intent.getStringExtra("projectId") ?: "43b982d7-6877-498a-a602-1bf7c8b998c8"
        val status = intent.getIntExtra("status", 0)

        val listTitle = ArrayList<String>()
        listTitle.add("项目信息")
        if(status !in 10..11){
            listTitle.add("应征画师")
            listTitle.add("邀请画师")
        }
        val datas = ArrayList<Fragment>()
        if(status !in 10..11){
            val applicants = ProjectSideApplicants.newInstance(this@ProjectSideProjectInfo,projectId)
            val invite = ProjectSideProjectInvite.newInstance(this@ProjectSideProjectInfo, projectId)
            datas.add(ProjectSideProjectDetails.newInstance(this@ProjectSideProjectInfo,projectId,applicants,invite))
            datas.add(applicants)
            datas.add(invite)
        }else{
            datas.add(ProjectSideProjectDetails.newInstance(this@ProjectSideProjectInfo,projectId,null,null))
        }

        frameLayout {
            id = mainId
            verticalLayout {
                relativeLayout {
                    backgroundResource = R.drawable.ffe3e3e3_bottom_line
                    linearLayout {
                        gravity = Gravity.CENTER
                        toolbar {
                            navigationIconResource = R.mipmap.icon_back
                            setOnClickListener {
                                finish()
                                overridePendingTransition(R.anim.left_in, R.anim.right_out)
                            }
                        }.lparams(dip(10), dip(18))
                        setOnClickListener {
                            finish()
                            overridePendingTransition(R.anim.left_in, R.anim.right_out)
                        }
                    }.lparams(dip(30), dip(25)) {
                        alignParentBottom()
                        alignParentLeft()
                        leftMargin = dip(15)
                        bottomMargin = dip(10)
                    }
                }.lparams(matchParent, dip(65))
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    backgroundColor = Color.WHITE
                    val tab = tabLayout {
                        tabGravity = TabLayout.GRAVITY_FILL
                        tabMode = TabLayout.MODE_FIXED
                        //下划线的颜色
                        setSelectedTabIndicatorColor(Color.parseColor("#FFFF7C00"))
                        //选中title的颜色 和 未选择的title颜色
                        setTabTextColors(Color.parseColor("#FF999999"),Color.parseColor("#FFFF7C00"))
                    }.lparams(matchParent, wrapContent)
                    linearLayout {
                        backgroundColor = Color.parseColor("#2EC6C6C6")
                    }.lparams(matchParent,dip(3))
                    viewpager = viewPager {
                        backgroundColor = Color.WHITE
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