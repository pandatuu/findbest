package app.findbest.vip.instance.view

import android.os.Bundle
import app.findbest.vip.instance.fragment.InvitationList
import app.findbest.vip.utils.BaseActivity

import org.jetbrains.anko.*

class InvitationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            val topPartId = 10
            frameLayout {
                id = topPartId
                val invitationList = InvitationList.newInstance()
                supportFragmentManager.beginTransaction().add(id, invitationList).commit()
            }.lparams {
                height = matchParent
                width = matchParent
            }
        }
    }
}