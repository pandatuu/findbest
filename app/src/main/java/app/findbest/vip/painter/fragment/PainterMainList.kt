package app.findbest.vip.painter.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import app.findbest.vip.R
import app.findbest.vip.painter.adapter.PainterAdapter
import app.findbest.vip.utils.tabLayout
import com.google.android.material.tabs.TabLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.viewPager

class PainterMainList: Fragment() {

    companion object{
        fun newInstance(context: Context): PainterMainList{
            val fragment = PainterMainList()
            fragment.mContext = context
            return fragment
        }
    }

    private lateinit var mContext:Context
    lateinit var viewpager: ViewPager

    private val mainId = 1
    private val viewPagerId = 2
    private var personList: PainterPersonList? = null
    private var companyList: PainterCompanyList? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    // 筛选
    fun setCategoryList(category: Int, style: Int) {
        personList?.setCategoryList(category, style)
        companyList?.setCategoryList(category, style)
    }
    // 排序
    fun setSortList(index: Int){
        personList?.setSortList(index)
        companyList?.setSortList(index)
    }

    private fun createV(): View {
        val listTitle = ArrayList<String>()
        listTitle.add(resources.getString(R.string.painter_of_person))
        listTitle.add(resources.getString(R.string.painter_of_team))
        personList = PainterPersonList.newInstance(mContext)
        companyList = PainterCompanyList.newInstance(mContext)
        val datas = ArrayList<Fragment>()
        datas.add(personList!!)
        datas.add(companyList!!)
        return UI {
            frameLayout {
                id = mainId
                linearLayout {
                    orientation = LinearLayout.VERTICAL
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
                            val myPageAdapter = PainterAdapter(childFragmentManager, datas, listTitle)
                            adapter = myPageAdapter
                        }.lparams(matchParent, dip(0)) {
                            weight = 1f
                        }
                        tab.setupWithViewPager(viewpager)
                    }
                }
            }
        }.view
    }
}