package app.findbest.vip.painter.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import app.findbest.vip.R
import app.findbest.vip.project.api.ProjectApi
import app.findbest.vip.project.model.StyleModel
import app.findbest.vip.project.model.TypeModel
import app.findbest.vip.utils.RetrofitUtils
import click
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import retrofit2.HttpException
import withTrigger

class PainterMainScreen : Fragment(), PainterScreenType.ScreenAll {

    interface PainterScreen {
        fun backgroundClick()
        fun confirmClick(array: ArrayList<Int>)
    }

    companion object {
        fun newInstance(context: Context, painterScreen: PainterScreen): PainterMainScreen {
            val fragment = PainterMainScreen()
            fragment.painterScreen = painterScreen
            fragment.mContext = context
            return fragment
        }
    }

    private lateinit var painterScreen: PainterScreen
    private lateinit var mContext: Context
    private var screenType: PainterScreenType? = null
    private var screenStyle: PainterScreenStyle? = null

    private val screenTypeId = 3
    private val screenStyleId = 4
    private val typeModelList = arrayListOf<TypeModel>()
    private val typeList = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createV()
    }

    //点击type
    override fun clickType(s: String) {
        if (s != resources.getString(R.string.srceen_all) && s != resources.getString(R.string.srceen_more)) {
            typeModelList.forEach {
                if (it.lang == s) {
                    GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                        getStyleList(it.id)
                    }
                }
            }
        }else if(s == resources.getString(R.string.srceen_all)){
            screenStyle!!.setTextGone()
        }
    }


    private fun createV(): View {
        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            getTypeList()
        }
        screenType = PainterScreenType.newInstance(this@PainterMainScreen)
        screenStyle = PainterScreenStyle.newInstance(mutableListOf())
        return UI {
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                relativeLayout {
                    backgroundColor = Color.TRANSPARENT
                    this.withTrigger().click {
                        painterScreen.backgroundClick()
                    }
                }.lparams(dip(0), matchParent) {
                    weight = 1f
                }
                verticalLayout {
                    backgroundColor = Color.parseColor("#FFFFFF")
                    scrollView {
                        verticalLayout {
                            frameLayout {
                                id = screenTypeId
                                childFragmentManager.beginTransaction().add(screenTypeId, screenType!!).commit()
                            }.lparams(matchParent, wrapContent)
                            frameLayout {
                                id = screenStyleId
                                childFragmentManager.beginTransaction().add(screenStyleId, screenStyle!!)
                                    .commit()
                            }.lparams(matchParent, wrapContent)
                        }
                    }.lparams(matchParent, dip(0)){
                        weight = 1f
                    }
                    linearLayout {
                        button {
                            text = resources.getString(R.string.common_reset)
                            textSize = 14f
                            textColor = Color.parseColor("#FFFFFFFF")
                            backgroundColor = Color.parseColor("#FFFF7C00")
                            setOnClickListener {
                                screenType?.onResume()
                                screenStyle?.setTextGone()
                            }
                        }.lparams(dip(0), matchParent) {
                            weight = 1f
                        }
                        button {
                            text = resources.getString(R.string.common_determine)
                            textSize = 14f
                            textColor = Color.parseColor("#FFFFFFFF")
                            backgroundColor = Color.parseColor("#FF1D1D1D")
                            setOnClickListener {
                                val array = arrayListOf<Int>()
                                val type = screenType?.getTypeItem()
                                val style = screenStyle?.getStyleItem()
                                if(type == resources.getString(R.string.srceen_all)){
                                    array.add(0)
                                    array.add(0)
                                    painterScreen.confirmClick(array)
                                    return@setOnClickListener
                                }
                                typeModelList.forEach {
                                    if(it.lang == type){
                                        array.add(it.id)
                                    }
                                }
                                array.add(0)
                                typeModelList.forEach {
                                    it.styleList.forEach { child ->
                                        if(child.lang == style){
                                            array[1] = child.id
                                        }
                                    }
                                }
                                painterScreen.confirmClick(array)
                            }
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

    private suspend fun getTypeList() {
        try {
            val mPerferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
            val systemCountry = mPerferences.getString("systemCountry", "").toString()
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(ProjectApi::class.java)
                .getTypeList(systemCountry)
                .subscribeOn(Schedulers.io())
                .awaitSingle()

            if (it.code() in 200..299) {
                val data = it.body()!!
                data.forEach {
                    val item = it.asJsonObject
                    typeModelList.add(
                        TypeModel(
                            item["id"].asInt,
                            item["name"].asString,
                            item["lang"].asString,
                            arrayListOf()
                        )
                    )
                    typeList.add(item["lang"].asString)
                }
                screenType = PainterScreenType.newInstance(this@PainterMainScreen, typeList)
                childFragmentManager.beginTransaction().replace(screenTypeId, screenType!!).commit()
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }

    private suspend fun getStyleList(typeId: Int) {
        try {
            val mPerferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
            val systemCountry = mPerferences.getString("systemCountry", "").toString()
            val retrofitUils =
                RetrofitUtils(mContext, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(ProjectApi::class.java)
                .getStyleList(systemCountry, typeId)
                .subscribeOn(Schedulers.io())
                .awaitSingle()

            if (it.code() in 200..299) {
                val data = it.body()!!
                val list = arrayListOf<StyleModel>()
                val styleList = arrayListOf<String>()
                data.forEach {
                    val item = it.asJsonObject
                    list.add(
                        StyleModel(
                            item["id"].asInt,
                            item["name"].asString,
                            item["categoryId"].asInt,
                            item["lang"].asString
                        )
                    )
                    styleList.add(item["lang"].asString)
                }
                typeModelList.forEach {
                    if (typeId == it.id) {
                        it.styleList = list
                    }
                }

                screenStyle!!.setStyleList(styleList)
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }
}