package cgland.job.sk_android.utils

import android.content.Context
import android.graphics.*
import android.widget.ImageView
import app.findbest.vip.R
import app.findbest.vip.utils.MimeType
import app.findbest.vip.utils.RetrofitUtils
import com.google.gson.JsonObject

import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.rx2.awaitSingle
import okhttp3.FormBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class UploadPic {

    companion object {
//
//        fun loadPicFromNet(str: String, i: ImageView) {
//            loadCircle(str, i,0,0, R.mipmap.default_avatar, R.mipmap.default_avatar)
//        }
//
//
//        fun loadPicNormal(str: String, i: ImageView) {
//            loadImage(str, i,null, R.mipmap.no_pic_show, R.mipmap.no_pic_show)
//        }

    }

    //　上传图片
    suspend fun upLoadPic(url: String, context: Context, bucketImg: String): JsonObject? {
        val imgFile = File(url)
        val byteArray: ByteArray
        val imgBody = when (imgFile.extension.toLowerCase()) {
            "png" -> {
                byteArray = getBitmapByte(url, Bitmap.CompressFormat.PNG) ?: return null
                FormBody.create(MimeType.IMAGE_PNG, byteArray)
            }
            "webp" -> {
                byteArray = getBitmapByte(url, Bitmap.CompressFormat.WEBP) ?: return null
                FormBody.create(MimeType.IMAGE_WEBP, byteArray)
            }
            "gif" -> {
                byteArray = getBitmapByte(url, Bitmap.CompressFormat.JPEG) ?: return null
                FormBody.create(MimeType.IMAGE_GIF, byteArray)
            }
            else -> {
                byteArray = getBitmapByte(url, Bitmap.CompressFormat.JPEG) ?: return null
                FormBody.create(MimeType.IMAGE_JPEG, byteArray)
            }
        }
        val multipart = MultipartBody.Builder()
            .setType(MimeType.MULTIPART_FORM_DATA)
            .addFormDataPart("bucket", bucketImg)
            .addFormDataPart("type", "IMAGE")
            .addFormDataPart("file", imgFile.name, imgBody)
            .build()
        println("---------------------" + imgFile.name + ":" + byteArray.size)

        var retrofitUils = RetrofitUtils(context, context.getString(R.string.storageUrl))
        return retrofitUils.create(UpLoadApi::class.java)
            .upLoadPic(multipart)
            .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
            .awaitSingle()

    }

    //　上传缩略图(统一为JPEG格式)
    suspend fun upLoadVedioThumb(bitmap: Bitmap, context: Context, bucketImg: String): JsonObject? {

        val byteArray: ByteArray = getByte(bitmap, Bitmap.CompressFormat.JPEG) ?: return null
        val imgBody = FormBody.create(MimeType.IMAGE_JPEG, byteArray)

        val multipart = MultipartBody.Builder()
            .setType(MimeType.MULTIPART_FORM_DATA)
            .addFormDataPart("bucket", bucketImg)
            .addFormDataPart("type", "THUMBNAIL")
            .addFormDataPart("file", "THUMBNAIL", imgBody)
            .build()
        println("---------------------" + "THUMBNAIL" + ":" + byteArray.size)

        var retrofitUils = RetrofitUtils(context, context.getString(R.string.storageUrl))
        return retrofitUils.create(UpLoadApi::class.java)
            .upLoadPic(multipart)
            .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
            .awaitSingle()

    }

    //　将图片转换成二进制流
    private fun getBitmapByte(url: String, format: Bitmap.CompressFormat): ByteArray? {
        try {
            val fis = FileInputStream(url)

            var bitmap = BitmapFactory.decodeStream(fis) ?: return null
            var geshi = url.substring(url.length - 3, url.length)
            //当有一边超过1280时，进行压缩
            if (!geshi.equals("gif") && (bitmap.getHeight() > 1280 || bitmap.getWidth() > 1280)) {
                bitmap = getResizedBitmap(bitmap)
            }
            val out = ByteArrayOutputStream()
            bitmap.compress(format, 80, out)

            out.flush()
            out.close()

            return out.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()

            return null
        }
    }

    //　将缩略图转换成二进制流
    private fun getByte(bitmap: Bitmap, format: Bitmap.CompressFormat): ByteArray? {
        try {
            var newbitmap: Bitmap = bitmap
            if (bitmap.height > 1280 || bitmap.width > 1280) {
                newbitmap = getResizedBitmap(bitmap)
            }
            val out = ByteArrayOutputStream()
            newbitmap.compress(format, 80, out)

            out.flush()
            out.close()

            return out.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()

            return null
        }
    }

    //　图片压缩　最大边为1280
    private fun getResizedBitmap(bitmap: Bitmap): Bitmap {
        var newHeight = 0f
        var newWidth = 0f
        if (bitmap.getHeight() > bitmap.getWidth() && bitmap.getHeight() > 1280) {
            newHeight = 1280f
            newWidth = bitmap.getWidth() * (newHeight / bitmap.getHeight().toFloat())
        } else if (bitmap.getHeight() < bitmap.getWidth() && bitmap.getWidth() > 1280) {
            newWidth = 1280f
            newHeight = bitmap.getHeight() * (newWidth / bitmap.getWidth().toFloat())
        }

        val resizedBitmap = Bitmap.createBitmap(newWidth.toInt(), newHeight.toInt(), Bitmap.Config.ARGB_8888);


        val scaleX: Float = newWidth / bitmap.getWidth()
        val scaleY: Float = newHeight / bitmap.getHeight()
        val pivotX: Float = 0f
        val pivotY: Float = 0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);
        val canvas = Canvas(resizedBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(
            bitmap, 0f, 0f, Paint(
                Paint.FILTER_BITMAP_FLAG +
                        Paint.DITHER_FLAG +
                        Paint.ANTI_ALIAS_FLAG
            )
        )

        return resizedBitmap;
    }


    fun getImageDate(str: String): RequestBody? {
        val imgFile = File(str)
        val byteArray: ByteArray

        val imgBody = when (imgFile.extension.toLowerCase()) {
            "png" -> {
                byteArray = getBitmapByte(str, Bitmap.CompressFormat.PNG) ?: return null
                FormBody.create(MimeType.IMAGE_PNG, byteArray)
            }
            "webp" -> {
                byteArray = getBitmapByte(str, Bitmap.CompressFormat.WEBP) ?: return null
                FormBody.create(MimeType.IMAGE_WEBP, byteArray)
            }
            "gif" -> {
                byteArray = getBitmapByte(str, Bitmap.CompressFormat.JPEG) ?: return null
                FormBody.create(MimeType.IMAGE_GIF, byteArray)
            }
            else -> {
                byteArray = getBitmapByte(str, Bitmap.CompressFormat.JPEG) ?: return null
                FormBody.create(MimeType.IMAGE_JPEG, byteArray)
            }
        }
        return imgBody;
    }


}