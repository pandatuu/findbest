package app.findbest.vip.instance.activity

import android.os.Bundle
import app.findbest.vip.instance.fragment.InstanceSearch
import app.findbest.vip.utils.BaseActivity
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.verticalLayout

class InstanceSearchActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        verticalLayout {

            val topPartId = 10
            frameLayout {
                id = topPartId

                val instanceSearch = InstanceSearch.newInstance(this@InstanceSearchActivity)
                supportFragmentManager.beginTransaction().add(id, instanceSearch).commit()

            }.lparams {
                height = matchParent
                width = matchParent
            }




        }


    }







}