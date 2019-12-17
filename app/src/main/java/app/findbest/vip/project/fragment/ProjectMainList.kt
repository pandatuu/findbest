package app.findbest.vip.project.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.findbest.vip.R
import app.findbest.vip.project.adapter.ProjectMainListAdapter
import app.findbest.vip.project.model.ProjectListModel
import app.findbest.vip.utils.recyclerView
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class ProjectMainList : Fragment() {

    companion object {
        fun newInstance(context: Context): ProjectMainList {
            val fragment = ProjectMainList()
            fragment.mContext = context
            return fragment
        }
    }

    lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    private fun createV(): View {
        return UI {
            linearLayout {
                backgroundColor = Color.parseColor("#FFF6F6F6")
                recyclerView {
                    layoutManager = LinearLayoutManager(mContext)
                    val a = mutableListOf<ProjectListModel>()
                    a.add(
                        ProjectListModel(
                            "乙女向帅哥立绘制作（乙女向きイケ…",
                            true,
                            "1920*1080",
                            "PSD",
                            "2019-11-22",
                            "china",
                            arrayListOf("写实风", "中国风", "狂野风"),
                            800.00F
                        )
                    )
                    a.add(
                        ProjectListModel(
                            "乙女向帅哥立绘制作（乙女向きイケ…",
                            false,
                            "1920*1080",
                            "PSD",
                            "2019-11-21",
                            "japan",
                            arrayListOf("写实风"),
                            800.00F
                        )
                    )
                    a.add(
                        ProjectListModel(
                            "乙女向帅哥立绘制作（乙女向きイケ…",
                            true,
                            "1920*1080",
                            "PSD",
                            "2019-11-23",
                            "korea",
                            arrayListOf("中国风"),
                            800.00F
                        )
                    )
                    a.add(
                        ProjectListModel(
                            "乙女向帅哥立绘制作（乙女向きイケ…",
                            true,
                            "1920*1080",
                            "PSD",
                            "2019-11-23",
                            "korea",
                            arrayListOf("中国风"),
                            800.00F
                        )
                    )
                    a.add(
                        ProjectListModel(
                            "乙女向帅哥立绘制作（乙女向きイケ…",
                            true,
                            "1920*1080",
                            "PSD",
                            "2019-11-23",
                            "korea",
                            arrayListOf("中国风"),
                            800.00F
                        )
                    )
                    a.add(
                        ProjectListModel(
                            "乙女向帅哥立绘制作（乙女向きイケ…",
                            true,
                            "1920*1080",
                            "PSD",
                            "2019-11-23",
                            "korea",
                            arrayListOf("中国风"),
                            800.00F
                        )
                    )
                    a.add(
                        ProjectListModel(
                            "乙女向帅哥立绘制作（乙女向きイケ…",
                            true,
                            "1920*1080",
                            "PSD",
                            "2019-11-23",
                            "korea",
                            arrayListOf("中国风"),
                            800.00F
                        )
                    )
                    val list = ProjectMainListAdapter(mContext, a)
                    adapter = list
                }.lparams(matchParent, matchParent) {
                    setMargins(dip(10), 0, dip(10), 0)
                }
            }
        }.view
    }
}