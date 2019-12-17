package app.findbest.vip.project.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import click
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import withTrigger

class ProjectMainScreen : Fragment() {

    interface ProjectScreen {
        fun backgroundClick()
    }

    companion object {
        fun newInstance(projectScreen: ProjectScreen): ProjectMainScreen {
            val fragment = ProjectMainScreen()
            fragment.projectScreen = projectScreen
            return fragment
        }
    }

    private lateinit var projectScreen: ProjectScreen

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
                orientation = LinearLayout.HORIZONTAL
                relativeLayout {
                    backgroundColor = Color.TRANSPARENT
                    this.withTrigger().click {
                        projectScreen.backgroundClick()
                    }
                }.lparams(dip(0), matchParent) {
                    weight = 1f
                }
                verticalLayout {
                    backgroundColor = Color.parseColor("#FFFFFF")
                    val screenId = 3
                    frameLayout {
                        id = screenId
                        val typeList = mutableListOf("角色设计","立绘","生物","美宣","场景设计","概念艺术","卡牌")
                        val styleList = mutableListOf("赛璐璐风","萌系","厚涂","美式漫画风","Q版","像素风","萝莉")
                        val screen = ProjectScreenAll.newInstance(typeList,styleList)
                        childFragmentManager.beginTransaction().add(screenId, screen).commit()
                    }.lparams(matchParent, dip(0)) {
                        weight = 1f
                    }
                    linearLayout {
                        button {
                            text = "重置"
                            textSize = 14f
                            textColor = Color.parseColor("#FFFFFFFF")
                            backgroundColor = Color.parseColor("#FFFF7C00")
                        }.lparams(dip(0), matchParent) {
                            weight = 1f
                        }
                        button {
                            text = "确定"
                            textSize = 14f
                            textColor = Color.parseColor("#FFFFFFFF")
                            backgroundColor = Color.parseColor("#FF1D1D1D")
                        }.lparams(dip(0), matchParent) {
                            weight = 1f
                        }
                    }.lparams(matchParent, dip(50))
                }.lparams(dip(0), matchParent) {
                    weight = 3f
                }
            }
        }.view
    }
}