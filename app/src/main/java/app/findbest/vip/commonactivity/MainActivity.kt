package app.findbest.vip.commonactivity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import app.findbest.vip.commonfrgmant.BottomButton
import app.findbest.vip.instance.fragment.InstanceDisplay
import com.bumptech.glide.Glide
import com.jaeger.library.StatusBarUtil
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    lateinit var topFragment: Fragment
    lateinit var bottomButton: BottomButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val glide=Glide.with(this@MainActivity)


        verticalLayout {
            var topPartId = 10
            frameLayout {
                id = topPartId
                topFragment = InstanceDisplay.newInstance(glide);
                supportFragmentManager.beginTransaction().replace(id, topFragment).commit()

            }.lparams {
                height = dip(0)
                weight = 1f
                width = matchParent
            }

            var bottomPartId = 11
            frameLayout {
                id = bottomPartId
                bottomButton = BottomButton.newInstance();
                supportFragmentManager.beginTransaction().replace(id, bottomButton).commit()
            }.lparams {
                height = dip(50)
                width = matchParent
            }


        }


    }

    override fun onStart() {
        super.onStart()
        setActionBar((topFragment as InstanceDisplay).toolbar1)
        StatusBarUtil.setTranslucentForImageView(
            this@MainActivity,
            0,
            (topFragment as InstanceDisplay).toolbar1
        )
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

    }


}