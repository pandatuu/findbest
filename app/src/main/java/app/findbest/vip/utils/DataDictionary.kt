package app.findbest.vip.utils

import android.content.Context
import com.auth0.android.jwt.JWT
import org.jetbrains.anko.db.DEFAULT


class DataDictionary() {

    companion object {

        fun getFormat(i: Int): String {

            when (i) {
                3 -> {
                    return "PSD"
                }

                6 -> {
                    return "JPEG"
                }

                9 -> {
                    return "PNG"
                }

                12 -> {
                    return "OTHER"
                }
            }

            return "OTHER"

        }

    }


}