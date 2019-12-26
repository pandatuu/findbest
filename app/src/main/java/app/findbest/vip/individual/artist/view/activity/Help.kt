package app.findbest.vip.individual.artist.view.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.findbest.vip.R
import app.findbest.vip.individual.artist.view.fragment.HpActionbar
import app.findbest.vip.individual.artist.view.fragment.HpMain
import com.jaeger.library.StatusBarUtil
import com.umeng.message.PushAgent
import org.jetbrains.anko.*

class Help: AppCompatActivity() {
    private lateinit var hpActionbar: HpActionbar
    private lateinit var hpMain: HpMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PushAgent.getInstance(this).onAppStart()

        val mainScreenId=1
        frameLayout {
            backgroundColor = Color.WHITE
            id = mainScreenId
            verticalLayout {
                //ActionBar
                val actionBarId=2
                frameLayout{
                    id=actionBarId
                    hpActionbar = HpActionbar.newInstance()
                    supportFragmentManager.beginTransaction().replace(id,hpActionbar).commit()

                }.lparams {
                    height= wrapContent
                    width= matchParent
                }

                val recycleViewParentId=3
                frameLayout {

                    id=recycleViewParentId
                    val hpMain= HpMain.newInstance()
                    supportFragmentManager.beginTransaction().replace(id,hpMain).commit()
                }.lparams {
                    height= wrapContent
                    width= matchParent
                    leftMargin = dip(15)
                    rightMargin = dip(15)
//                    bottomMargin = dip(19)
                }
            }.lparams{
                width = matchParent
                height = matchParent
            }
        }
    }


    override fun onStart() {
        super.onStart()
        setActionBar(hpActionbar.TrpToolbar)
        StatusBarUtil.setTranslucentForImageView(this@Help, 0, hpActionbar.TrpToolbar)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        hpActionbar.TrpToolbar!!.setNavigationOnClickListener {
            finish()
            overridePendingTransition(R.anim.left_in,R.anim.right_out)
        }
    }
}