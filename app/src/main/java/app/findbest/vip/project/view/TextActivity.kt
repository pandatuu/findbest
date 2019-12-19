package app.findbest.vip.project.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import app.findbest.vip.project.fragment.ProjectFragment
import app.findbest.vip.utils.BaseActivity
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.frameLayout

class TextActivity : BaseActivity() {

    private var exitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainId = 1
        frameLayout {
            id = mainId
            val projectList = ProjectFragment.newInstance(this@TextActivity)
            supportFragmentManager.beginTransaction().add(mainId, projectList).commit()

        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            if(keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN){
                if((System.currentTimeMillis()-exitTime) > 2000){
                    Toast.makeText(applicationContext, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis()
                } else {
                    val startMain = Intent(Intent.ACTION_MAIN)
                    startMain.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startMain.addCategory(Intent.CATEGORY_HOME)
                    startActivity(startMain)
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}