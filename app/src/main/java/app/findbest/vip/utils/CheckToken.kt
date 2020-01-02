package app.findbest.vip.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.auth0.android.jwt.JWT


class CheckToken(val mContext: Context) {
    // JWT校验token 并解码
    fun jwtParse(token: String): String {
        try {
//            val keyBytes = Base64.decode(publicKey, Base64.DEFAULT)
////            val keySpec = X509EncodedKeySpec(keyBytes)
////            val keyFactory = KeyFactory.getInstance("RSA")
////            val publicKey = keyFactory.generatePublic(keySpec) as RSAPublicKey
////            val algorithm = Algorithm.RSA256(publicKey, null)
////            val verifier = JWT.require(algorithm)
////                .build()
////
////            val jwt = verifier.verify(token)
////            //公钥校验成功
////            println("----JWT subject---: ${jwt.subject}")
////            if(!jwt.subject.isNullOrBlank()){
////                println("----JWT payload---: ${jwt.getClaim("accountStatus").asString()}")
////                //获取到AccountStatus
////                return jwt.getClaim("accountStatus").asString()
////            }
            val jwt = JWT(token)


            if(!jwt.subject.isNullOrBlank()){
                val role = jwt.getClaim("roles").asList(String::class.java)[0]
                val userType = jwt.getClaim("userType").asInt()
                val id = jwt.getClaim("sub").asString()
                val mPerferences: SharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(mContext)
                val mEditor = mPerferences.edit()
                mEditor.putString("role", role)
                mEditor.putInt("userType", userType!!)
                mEditor.putString("userId", id)
                mEditor.commit()

                return jwt.getClaim("accountStatus").asString() ?: ""
            }
            return ""
        } catch (t: Throwable) {

            //报错
            println("-----JWT error---: $t")
            return ""
        }
    }
}