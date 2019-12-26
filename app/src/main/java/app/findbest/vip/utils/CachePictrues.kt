package app.findbest.vip.utils

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import org.jetbrains.anko.button
import org.jetbrains.anko.dip
import org.jetbrains.anko.frameLayout

class CachePictrues: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout {
            button {
                text = "1111111111111"
                setOnClickListener {
                    // 创建Intent，用于打开手机本地图库选择图片
                    val intent1 = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    // 启动intent打开本地图库
                    startActivityForResult(intent1, 13)
                }
            }.lparams(dip(200),dip(300))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("resultCode------$resultCode")
        if(requestCode ==13){
//            val intent1 = Intent("com.android.camera.action.CROP");
            // 获取图库所选图片的uri
            val uri = data?.data
            println("data------$uri")
        }
    }
}