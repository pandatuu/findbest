package app.findbest.vip.message.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import app.findbest.vip.R
import app.findbest.vip.commonfrgmant.BottomButton
import app.findbest.vip.instance.fragment.InstanceDetail
import app.findbest.vip.instance.fragment.InstanceDisplay
import app.findbest.vip.project.fragment.ProjectFragment
import app.findbest.vip.utils.BaseActivity
import app.findbest.vip.utils.roundImageView
import com.bumptech.glide.Glide

import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.toast

class videoRequestActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        verticalLayout {


            var topPartId = 10
            frameLayout {
                backgroundColor= Color.BLACK


                verticalLayout{
                    gravity=Gravity.CENTER
                    verticalLayout{
                        gravity=Gravity.CENTER_HORIZONTAL

                        roundImageView {

                            setBorderRadius(1250)

                            Glide.with(context)
                                .load("https://findbest-test-1258431445.cos.ap-chengdu.myqcloud.com/61967e90-b4b5-478b-94db-19b4ab338261.jpg")
                                .centerCrop()
                                .skipMemoryCache(false)
                                .dontAnimate()
                                .placeholder(R.mipmap.default_avatar)
                                .into(this)

                        }.lparams(){
                            width=dip(125)
                            height=dip(125)
                            topMargin=dip(150)
                        }


                        textView {
                            gravity=Gravity.CENTER
                            textSize=27f
                            textColor=Color.WHITE
                            text="Zack Alice"
                        }.lparams(){

                            topMargin=dip(20)
                        }

                        textView {
                            gravity=Gravity.CENTER
                            textSize=17f
                            textColor=Color.parseColor("#FFCECECE")
                            text="正在等待对方接听…"
                        }.lparams(){

                            topMargin=dip(100)
                        }



                        textView {
                            gravity=Gravity.CENTER
                            textSize=20f
                            text="O"
                            textColor=Color.WHITE
                            backgroundResource=R.drawable.red_circle
                        }.lparams(){
                            width=dip(90)
                            height=dip(90)
                            topMargin=dip(25)
                        }




                    }.lparams {
                        height = matchParent
                        width = wrapContent
                    }




                }.lparams {
                    height = matchParent
                    width = matchParent
                }


            }.lparams {
                height = matchParent
                width = matchParent
            }




        }


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (event != null) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                finish()
                overridePendingTransition(
                    R.anim.fade_in_out,
                    R.anim.fade_in_out
                )
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }


}