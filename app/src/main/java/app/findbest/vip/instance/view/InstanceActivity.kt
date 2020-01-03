package app.findbest.vip.instance.view

import android.os.Bundle
import app.findbest.vip.instance.fragment.InstanceDetail
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