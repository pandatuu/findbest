package app.findbest.vip.project.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BackgroundFragment
import app.findbest.vip.project.api.ProjectApi
import app.findbest.vip.project.fragment.EnlistSuccessTipsDialog
import app.findbest.vip.utils.BaseActivity
import app.findbest.vip.utils.MimeType
import app.findbest.vip.utils.RetrofitUtils
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

class EnlistProject : BaseActivity(), EnlistSuccessTipsDialog.ButtomClick, BackgroundFragment.ClickBack {

    private val SYSTEM_PICTRUES = 13
    private var imageList = arrayListOf<String>()
    private lateinit var listText: TextView
    private lateinit var commitText: EditText
    private var projectId = ""
    private var mainId = 1

    private lateinit var linea: LinearLayout
    private var backgroundFragment: BackgroundFragment? = null
    private var tipsDialog: EnlistSuccessTipsDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(intent.getStringExtra("projectId") != null){
            projectId = intent.getStringExtra("projectId")!!.toString()
        }

        frameLayout {
            id = mainId
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

                commitText = editText {
                    padding = dip(15)
                    backgroundColor = Color.parseColor("#FFF6F6F6")
                    hint = "请输入项目说明与备注"
                    hintTextColor = Color.parseColor("#FFB5B5B5")
                    textSize = 15f
                    gravity = Gravity.START
                }.lparams(matchParent, dip(230)) {
                    setMargins(dip(15), dip(15), dip(15), dip(15))
                }

                verticalLayout {
                    listText = textView {
                        text = "（0/4）"
                        textSize = 15f
                        textColor = Color.parseColor("#FF333333")
                    }.lparams {
                        bottomMargin = dip(15)
                    }
                    linea = linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        for (index in 0..3) {
                            linearLayout {
                                backgroundResource = R.drawable.around_rectangle_2
                                gravity = Gravity.CENTER
                                imageView {
                                    imageResource = R.mipmap.ico_add_nor
                                }.lparams(dip(25),dip(25))
                                setOnClickListener {
                                    val intent = Intent(this@EnlistProject, RecentProject::class.java)
                                    startActivityForResult(intent, SYSTEM_PICTRUES)
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out)
                                }
                            }.lparams(dip(83), dip(83)){
                                leftMargin = dip(5)
                            }
                        }
                    }.lparams(matchParent, wrapContent)
                }.lparams(matchParent, dip(0)) {
                    weight = 1f
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }
                button {
                    text = "提交应征作品"
                    textColor = Color.parseColor("#FFFFFFFF")
                    textSize = 16f
                    backgroundResource = R.drawable.enable_rectangle_button
                    setOnClickListener {
                        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                            enlist(projectId)
                        }
                    }
                }.lparams(matchParent,dip(50)){
                    gravity = Gravity.BOTTOM
                }
            }.lparams(matchParent, matchParent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SYSTEM_PICTRUES) {
            if (data != null) {
                val array = data.getStringArrayListExtra("imageList")!!
                imageList.clear()
                array.forEach {
                    imageList.add(it)
                }
                changeImageList()
            }
        }
    }


    override fun click() {
        closeAlertDialog()
        finish()
        overridePendingTransition(R.anim.left_in, R.anim.right_out)
    }

    override fun clickAll() {

    }
    @SuppressLint("SetTextI18n")
    private fun changeImageList(){
        listText.text = "（${imageList.size}/4）"
        linea.removeAllViews()
        for (index in 0 until imageList.size) {
            val imageViews = UI {
                val linea = frameLayout {
                    backgroundResource = R.drawable.around_rectangle_2
                    val image = imageView {}.lparams(matchParent, matchParent){
                        setMargins(dip(1),dip(1),dip(1),dip(1))
                    }
                    Glide.with(this@EnlistProject)
                        .load(imageList[index])
                        .into(image)
                    imageView {
                        imageResource = R.mipmap.ico_close_nor
                        setOnClickListener {
                            imageList.removeAt(index)
                            changeImageList()
                        }
                    }.lparams(dip(20), dip(20)) {
                        gravity = Gravity.RIGHT
                        rightMargin = dip(5)
                        topMargin = dip(5)
                    }
                }
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )//定义一个LayoutParams
                layoutParams.width = dip(83)
                layoutParams.height = dip(83)
                layoutParams.setMargins(dip(5),0,0,0)
                linea.layoutParams = layoutParams
            }.view
            linea.addView(imageViews)
        }
        for (index in 0 until 4 - imageList.size) {
            val notImage = UI {
                val linea = linearLayout {
                    backgroundResource = R.drawable.around_rectangle_2
                    gravity = Gravity.CENTER
                    imageView {
                        imageResource = R.mipmap.ico_add_nor
                    }.lparams(dip(25),dip(25))
                    setOnClickListener {
                        val intent = Intent(this@EnlistProject, RecentProject::class.java)
                        intent.putExtra("imageList", imageList)
                        startActivityForResult(intent, SYSTEM_PICTRUES)
                        overridePendingTransition(R.anim.right_in, R.anim.left_out)
                    }
                }
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )//定义一个LayoutParams
                layoutParams.width = dip(83)
                layoutParams.height = dip(83)
                layoutParams.setMargins(dip(5),0,0,0)
                linea.layoutParams = layoutParams
            }.view
            linea.addView(notImage)
        }
    }

    private suspend fun enlist(id: String) {
        try {
            val params = mapOf(
                "projectId" to id,
                "comment" to commitText.text.toString(),
                "workses" to imageList
            )
            val userJson = JSON.toJSONString(params)
            val body = RequestBody.create(MimeType.APPLICATION_JSON, userJson)

            val retrofitUils =
                RetrofitUtils(this@EnlistProject, resources.getString(R.string.developmentUrl))
            val it = retrofitUils.create(ProjectApi::class.java)
                .enlistProject(body)
                .subscribeOn(Schedulers.io())
                .awaitSingle()
            if(it.code() in 200..299){
                val status = it.body()!!["status"].asString
                if(status == "OK"){
                    openDialog()
                }
            }
            if(it.code() == 400){
                toast("至少上传一个作品")
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                println(throwable.code())
            }
        }
    }


    private fun openDialog() {
        val mTransaction = supportFragmentManager.beginTransaction()

        if (backgroundFragment == null) {
            backgroundFragment = BackgroundFragment.newInstance(this@EnlistProject)

            mTransaction.add(mainId, backgroundFragment!!)
        }

        mTransaction.setCustomAnimations(R.anim.right_in, R.anim.right_in)

        tipsDialog = EnlistSuccessTipsDialog.newInstance(this@EnlistProject)
        mTransaction.add(mainId, tipsDialog!!)

        mTransaction.commit()
    }

    private fun closeAlertDialog() {

        val mTransaction = supportFragmentManager.beginTransaction()
        if (tipsDialog != null) {
            mTransaction.setCustomAnimations(R.anim.right_out, R.anim.right_out)

            mTransaction.remove(tipsDialog!!)
            tipsDialog = null
        }

        if (backgroundFragment != null) {
            mTransaction.setCustomAnimations(
                R.anim.fade_in_out_a, R.anim.fade_in_out_a
            )
            mTransaction.remove(backgroundFragment!!)
            backgroundFragment = null
        }
        mTransaction.commit()
    }
}