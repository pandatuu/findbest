package app.findbest.vip.individual.artist.view.activity

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import app.findbest.vip.R
import app.findbest.vip.individual.artist.view.fragment.FbActionbar
import app.findbest.vip.individual.artist.view.fragment.FbMain
import com.jaeger.library.StatusBarUtil
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

class Feedback:AppCompatActivity() {
    lateinit var  fbActionbar:FbActionbar
    lateinit var  fbMain: FbMain

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        var mainScreenId=1
        frameLayout{
            id = mainScreenId

            linearLayout{
                orientation = LinearLayout.VERTICAL
                //ActionBar
                var actionBarId = 2
                frameLayout {
                    id = actionBarId
                    fbActionbar = FbActionbar.newInstance()
                    supportFragmentManager.beginTransaction().add(actionBarId, fbActionbar).commit()
                }.lparams {
                    width = matchParent
                    height = wrapContent
                }

                var newFragmentId = 3
                frameLayout {
                    id = newFragmentId
                    fbMain = FbMain.newInstance()
                    supportFragmentManager.beginTransaction().add(newFragmentId, fbMain).commit()
                }.lparams(width = matchParent, height = matchParent){}

            }.lparams(){
                width = matchParent
                height = matchParent
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setActionBar(fbActionbar.TrpToolbar)
        StatusBarUtil.setTranslucentForImageView(this@Feedback, 0, fbActionbar.TrpToolbar)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        fbActionbar.TrpToolbar!!.setNavigationOnClickListener {
            finish()
            overridePendingTransition(R.anim.left_in, R.anim.right_out)
        }

    }
}