package app.findbest.vip.project.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import app.findbest.vip.R
import app.findbest.vip.project.api.ProjectApi
import app.findbest.vip.utils.*
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import okhttp3.RequestBody
import org.jetbrains.anko.*
import retrofit2.HttpException

class RecentProject : BaseActivity() {

    private var imageList = arrayListOf<String>()
    private lateinit var listText: TextView
    private var projectId = ""

    private var flow: FlowLayout? = null

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
                        text = "编辑"
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
                    flow = flowLayout {}.lparams{
                        setMargins(dip(15),dip(15),dip(15),dip(15))
                    }
                }.lparams(matchParent,dip(0)){
                    weight = 1f
                }
                relativeLayout {
                    linearLayout {
                        backgroundResource = R.drawable.enable_rectangle_button_3
                        gravity = Gravity.CENTER
                        listText = textView {
                            text = "完成（${imageList.size}）"
                            textSize = 16f
                            textColor = Color.parseColor("#FFFFFFFF")
                        }
                        setOnClickListener {
                            val intent = Intent()
                            intent.putExtra("imageList", imageList)
                            setResult(Activity.RESULT_OK ,intent)
                            finish()
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
                for (index in 0..model.size()){
                    val imageUrl = model[index].asJsonObject["url"].asString
                    var isClick = false
                    val imageViews = UI {
                        frameLayout {
                            frameLayout {
                                backgroundResource = R.drawable.around_rectangle_2
                                val image = imageView {}.lparams(matchParent, matchParent){
                                    setMargins(dip(1),dip(1),dip(1),dip(1))
                                }
                                Glide.with(this.context)
                                    .load(imageUrl)
                                    .into(image)
                                val smallImage = imageView {
                                    setOnClickListener {
                                        if(isClick){
                                            imageResource = R.mipmap.login_ico_checkbox_nor
                                            imageList.remove(imageUrl)
                                            listText.text = "完成（${imageList.size}）"
                                            isClick = false
                                        }else{
                                            if(imageList.size<4){
                                                imageResource = R.mipmap.login_ico_checkbox_pre
                                                imageList.add(imageUrl)
                                                listText.text = "完成（${imageList.size}）"
                                                isClick = true
                                            }else{
                                                toast("最多选择4张图片")
                                            }
                                        }
                                    }
                                }.lparams(dip(20), dip(20)) {
                                    gravity = Gravity.RIGHT
                                    rightMargin = dip(5)
                                    topMargin = dip(5)
                                }
                                if(imageList.size>0){
                                    for (index in 0 until imageList.size) {
                                        if (imageList[index] == imageUrl) {
                                            smallImage.imageResource = R.mipmap.login_ico_checkbox_pre
                                            isClick = true
                                        }else{
                                            smallImage.imageResource = R.mipmap.login_ico_checkbox_nor
                                        }
                                    }
                                }else{
                                    smallImage.imageResource = R.mipmap.login_ico_checkbox_nor
                                }
                            }.lparams(dip(83),dip(83)){
                                leftMargin = dip(5)
                                topMargin = dip(5)
                            }
                        }
                    }.view
                    flow?.addView(imageViews)
                }
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }
}