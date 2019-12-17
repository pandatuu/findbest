package app.findbest.vip.project.view

import android.graphics.Color
import android.os.Bundle
import app.findbest.vip.project.fragment.ProjectFragment
import app.findbest.vip.utils.BaseActivity
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.frameLayout

class TextActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainId = 1
        frameLayout {
            id = mainId
            val projectList = ProjectFragment.newInstance(this@TextActivity)
            supportFragmentManager.beginTransaction().add(mainId, projectList).commit()

        }
    }
}