package app.findbest.vip.instance.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

@SuppressLint("WrongConstant")
class InstanceDetailListAdapter(fm: FragmentManager, fragmentList: ArrayList<Fragment>): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var datas = fragmentList

    override fun getItem(position: Int): Fragment {
        return datas[position]
    }

    override fun getCount(): Int {
        return datas.size
    }
}