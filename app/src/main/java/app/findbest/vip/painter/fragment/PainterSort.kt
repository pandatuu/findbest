package app.findbest.vip.painter.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import app.findbest.vip.R
import app.findbest.vip.utils.FlowLayout
import app.findbest.vip.utils.flowLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

@Suppress("UNREACHABLE_CODE")
class PainterSort: Fragment() {

    interface SortClick{
        fun chooseSort(index: Int)
    }

    companion object{
        fun newInstance(context: Context, sortClick:SortClick): PainterSort{
            val fragment = PainterSort()
            fragment.mContext = context
            fragment.sortClick = sortClick
            return fragment
        }
    }

    private lateinit var sortClick:SortClick
    lateinit var mPerferences: SharedPreferences
    private var mSortList = arrayListOf(resources.getString(R.string.sort_first),
        resources.getString(R.string.sort_second),resources.getString(R.string.sort_third),resources.getString(R.string.sort_forth))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mPerferences =
            PreferenceManager.getDefaultSharedPreferences(mContext)

        if(mPerferences.getInt("painterSort",4)==4){
            val mEditor = mPerferences.edit()
            mEditor.putInt("painterSort", 0)
            mEditor.commit()
        }

        return createV()
    }

    private var sortSomeView: LinearLayout? = null
    private lateinit var flow: FlowLayout
    private lateinit var mContext: Context

    private fun createV(): View {
        val view = UI {
            frameLayout {
                verticalLayout {
                    backgroundColor = Color.parseColor("#FFFFFF")
                    verticalLayout {
                        linearLayout {
                            backgroundResource = R.drawable.ffe4e4e4_bottom_line
                            textView {
                                text = resources.getString(R.string.common_sort)
                                textSize = 17f
                                textColor = Color.parseColor("#FF222222")
                            }.lparams(wrapContent, wrapContent){
                                leftMargin = dip(5)
                                gravity = Gravity.CENTER_VERTICAL
                            }
                        }.lparams(matchParent, dip(56))
                        linearLayout {
                            flow = flowLayout {
                            }.lparams{
                                topMargin = dip(12)
                            }
                        }.lparams(matchParent,dip(62))
                    }.lparams(matchParent, matchParent){
                        leftMargin = dip(15)
                        rightMargin = dip(15)
                    }
                }.lparams(matchParent,dip(120))
            }
        }.view
        setSortFlow()
        return view
    }

    private fun setSortFlow(){
        for (index in mSortList.indices) {
            val view = UI {
                linearLayout {
                    val linea = linearLayout {
                        id = index + 1
                        backgroundColor = Color.parseColor("#FFFFFFFF")
                        textView {
                            text = mSortList[index]
                            textSize = 12f
                            textColor = Color.parseColor("#FF555555")
                        }.lparams {
                            setMargins(dip(10), dip(7), dip(10), dip(7))
                        }
                        setOnClickListener {
                            val mEditor = mPerferences.edit()
                            mEditor.putInt("painterSort", index)
                            mEditor.commit()
                            if (sortSomeView != null) {
                                if (sortSomeView!!.id != it.id) {
                                    sortSomeView!!.backgroundColor = Color.parseColor("#FFFFFFFF")
                                    val oldText = sortSomeView!!.getChildAt(0) as TextView
                                    oldText.textColor = Color.parseColor("#FF555555")
                                    sortSomeView = it as LinearLayout
                                    sortSomeView!!.backgroundColor = Color.parseColor("#FFFF7C00")
                                    val newText = sortSomeView!!.getChildAt(0) as TextView
                                    newText.textColor = Color.parseColor("#FFFFFFFF")
                                    sortClick.chooseSort(index)
                                }
                            }
                        }
                    }.lparams(dip(68), dip(30)) {
                        leftMargin = dip(10)
                        topMargin = dip(10)
                    }
                    val item = mPerferences.getInt("painterSort",4)
                    if(index == item){
                        sortSomeView = linea
                        linea.backgroundColor = Color.parseColor("#FFFF7C00")
                        val nowText = sortSomeView!!.getChildAt(0) as TextView
                        nowText.textColor = Color.parseColor("#FFFFFFFF")
                    }
                }
            }.view
            flow.addView(view)
        }
    }
}