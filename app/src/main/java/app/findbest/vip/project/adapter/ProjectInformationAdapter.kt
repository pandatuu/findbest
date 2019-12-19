package app.findbest.vip.project.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ProjectInformationAdapter(fm: FragmentManager, fragmentList: ArrayList<Fragment>, titleList: ArrayList<String>): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var datas = fragmentList
    var titles = titleList

    override fun getItem(position: Int): Fragment {
        return datas[position]
    }

    override fun getCount(): Int {
        return datas.size
    }

    //用来设置tab的标题
    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}