package app.findbest.vip.instance.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import app.findbest.vip.instance.fragment.InstanceDisplay
import com.jaeger.library.StatusBarUtil
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.frameLayout

class Instance: AppCompatActivity() {

   lateinit var topFragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var topPartId=10
        frameLayout {
            id=topPartId
            topFragment= InstanceDisplay.newInstance();
            supportFragmentManager.beginTransaction().replace(id,topFragment).commit()

        }
    }

    override fun onStart() {
        super.onStart()
        setActionBar((topFragment as InstanceDisplay).toolbar1)
        StatusBarUtil.setTranslucentForImageView(this@Instance, 0, (topFragment as InstanceDisplay).toolbar1)
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

    }


}