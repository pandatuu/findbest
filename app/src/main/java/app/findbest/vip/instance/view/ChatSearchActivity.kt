package app.findbest.vip.instance.view

import android.os.Bundle
import app.findbest.vip.instance.fragment.ChatSearch
import app.findbest.vip.utils.BaseActivity
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.verticalLayout

class ChatSearchActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        verticalLayout {

            val topPartId = 10
            frameLayout {
                id = topPartId

                val instanceSearch = ChatSearch.newInstance(this@ChatSearchActivity)
                supportFragmentManager.beginTransaction().add(id, instanceSearch).commit()

            }.lparams {
                height = matchParent
                width = matchParent
            }




        }


    }







}