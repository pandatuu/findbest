package app.findbest.vip.commonactivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent

import android.widget.FrameLayout
import android.widget.Toast

import androidx.fragment.app.Fragment

import app.findbest.vip.commonfrgmant.BottomButton
import app.findbest.vip.instance.fragment.InstanceDisplay
import app.findbest.vip.painter.fragment.PainterFragment
import app.findbest.vip.individual.fragment.Terminal
import app.findbest.vip.message.fragment.MessageChatRecordFragment
import app.findbest.vip.message.model.ChatRecordModel
import app.findbest.vip.project.fragment.ProjectFragment
import app.findbest.vip.utils.BaseActivity

import org.jetbrains.anko.*

class MainActivity : BaseActivity() {

    lateinit var topFragment: Fragment
    lateinit var bottomButton: BottomButton
    private var exitTime: Long = 0
    lateinit var mainFrameLayout: FrameLayout
    var mainId = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        frameLayout {
            id = mainId
            verticalLayout {
                var topPartId = 10
                mainFrameLayout = frameLayout {
                    id = topPartId

                    val projectList = ProjectFragment.newInstance(this@MainActivity)
                    supportFragmentManager.beginTransaction().add(id, projectList).commit()

                }.lparams {
                    height = dip(0)
                    weight = 1f
                    width = matchParent
                }

                val bottomPartId = 11
                frameLayout {
                    id = bottomPartId
                    bottomButton = BottomButton.newInstance(this@MainActivity);
                    supportFragmentManager.beginTransaction().replace(id, bottomButton)
                        .commit()
                }.lparams {
                    height = dip(50)
                    width = matchParent
                }
            }
        }
    }
//            override fun onStart() {
//                super.onStart()


//        setActionBar((topFragment as InstanceDisplay).toolbar1)
//        StatusBarUtil.setTranslucentForImageView(
//            this@MainActivity,
//            0,
//            (topFragment as InstanceDisplay).toolbar1
//        )
//        getWindow().getDecorView()
//            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

//            }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(applicationContext, "再按一次退出程序", Toast.LENGTH_SHORT)
                        .show();
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


    //跳转页面
    fun jumpPage(type: Int) {

        when (type) {
            1 -> {
                topFragment = ProjectFragment.newInstance(this@MainActivity)
                supportFragmentManager.beginTransaction()
                    .add(mainFrameLayout.id, topFragment)
                    .commit()

            }
            2 -> {
                topFragment = InstanceDisplay.newInstance(this@MainActivity)
                supportFragmentManager.beginTransaction()
                    .replace(mainFrameLayout.id, topFragment)
                    .commit()
            }
            3 -> {
                topFragment = PainterFragment.newInstance(this@MainActivity)
                supportFragmentManager.beginTransaction()
                    .replace(mainFrameLayout.id, topFragment)
                    .commit()
            }
            4 -> {
                topFragment = MessageChatRecordFragment.newInstance(this@MainActivity)
                supportFragmentManager.beginTransaction()
                    .replace(mainFrameLayout.id, topFragment)
                    .commit()
            }
            5 -> {
                topFragment = Terminal.newInstance(this@MainActivity)
                supportFragmentManager.beginTransaction()
                    .replace(mainFrameLayout.id, topFragment)
                    .commit()
            }
        }
    }


    /**
     *  得到返回的值
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (resultCode == 222) {
                println(intent.getStringExtra("content"))
                println(data.getStringExtra("content"))
                println("xxxxxxxxxxxxxxxxxxxxxxxxxxx")
                //  toast(data.getStringExtra("content"))
                //  (topFragment as InstanceDisplay).searchByContent("")
            }
        }
    }


}
