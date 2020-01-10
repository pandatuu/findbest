package app.findbest.vip.project.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.NullDataPageFragment
import app.findbest.vip.project.api.ProjectApi
import app.findbest.vip.project.fragment.RecentProjectImageList
import app.findbest.vip.utils.*
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import org.jetbrains.anko.*
import retrofit2.HttpException

class RecentProject : BaseActivity(), RecentProjectImageList.ClickImage {

    private lateinit var listText: TextView
    private var projectId = ""

    private var imageList = arrayListOf<String>()
    private lateinit var listFram: FrameLayout
    private lateinit var listFragment: RecentProjectImageList
    private var nullData: NullDataPageFragment? = null
    private val nullId = 2

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(intent.getStringExtra("projectId") != null){
            projectId = intent.getStringExtra("projectId")!!.toString()
        }
        if(intent.getStringArrayListExtra("imageList") != null) {
            imageList.addAll(intent.getStringArrayListExtra("imageList")!!)
        }
        frameLayout {
            backgroundColor = Color.WHITE
            verticalLayout {
                relativeLayout {
                    backgroundResource = R.drawable.ffe3e3e3_bottom_line
                    linearLayout {
                        toolbar {
                            navigationIconResource = R.mipmap.nav_ico_return
                            setOnClickListener {
                                finish()
                                overridePendingTransition(R.anim.left_in, R.anim.right_out)
                            }
                        }.lparams(dip(10), dip(18))
                    }.lparams(dip(20), dip(20)) {
                        leftMargin = dip(15)
                        bottomMargin = dip(8)
                        alignParentBottom()
                        alignParentLeft()
                    }
                    textView {
                        text = resources.getString(R.string.enlist_edit)
                        textSize = 17f
                        textColor = Color.parseColor("#FF222222")
                    }.lparams {
                        bottomMargin = dip(8)
                        centerHorizontally()
                        alignParentBottom()
                    }
                }.lparams(matchParent, dip(65))
                linearLayout {
                    backgroundResource = R.drawable.ffe3e3e3_bottom_line
                    listFram = frameLayout {
                        id = nullId
                        listFragment = RecentProjectImageList.newInstance(this@RecentProject,imageList, this@RecentProject)
                        supportFragmentManager.beginTransaction().add(nullId,listFragment).commit()
                    }
                    val listFramlp = listFram.layoutParams
                    listFramlp.width = LinearLayout.LayoutParams.MATCH_PARENT
                    listFramlp.height = LinearLayout.LayoutParams.MATCH_PARENT
                }.lparams(matchParent,dip(0)){
                    weight = 1f
                }
                relativeLayout {
                    linearLayout {
                        backgroundResource = R.drawable.enable_rectangle_button_3
                        gravity = Gravity.CENTER
                        listText = textView {
                            text = "${resources.getString(R.string.enlist_done)}（${imageList.size}）"
                            textSize = 16f
                            textColor = Color.parseColor("#FFFFFFFF")
                        }
                        setOnClickListener {
                            val intent = Intent()
                            intent.putExtra("imageList", imageList)
                            setResult(Activity.RESULT_OK ,intent)
                            finish()
                            overridePendingTransition(R.anim.left_in, R.anim.right_out)
                        }
                    }.lparams(dip(100),dip(35)) {
                        alignParentRight()
                        centerVertically()
                        rightMargin = dip(15)
                    }
                }.lparams(matchParent,dip(50))
            }.lparams(matchParent, matchParent)
        }

        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
            getMyPics()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun clickImage(size: Int) {
        listText.text = "${resources.getString(R.string.enlist_done)}（${imageList.size}）"
    }
    private suspend fun getMyPics() {
        try {
            val retrofitUils =
                RetrofitUtils(this@RecentProject, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(ProjectApi::class.java)
                .getMyPicsById()
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if(it.code() in 200..299){
                val model = it.body()!!.data
                if(model.size()>0){
                    if(nullData!=null){
                        supportFragmentManager.beginTransaction().remove(nullData!!).commit()
                        nullData = null
                    }
                    listFragment.addItems(model)
                }else{
                    nullData = NullDataPageFragment.newInstance()
                    supportFragmentManager.beginTransaction().add(nullId,nullData!!).commit()
                }
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }
}