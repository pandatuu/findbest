package app.findbest.vip.instance.activity

import android.content.Intent
import android.os.Bundle
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

import org.jetbrains.anko.*

class InstanceActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        verticalLayout {


            var topPartId = 10
            frameLayout {
                id = topPartId

                val instanceDetail = InstanceDetail.newInstance(this@InstanceActivity)
                supportFragmentManager.beginTransaction().add(id, instanceDetail).commit()

            }.lparams {
                height = matchParent
                width = matchParent
            }




        }


    }







}