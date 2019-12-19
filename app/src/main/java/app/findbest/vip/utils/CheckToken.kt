package app.findbest.vip.utils

import android.content.Context
import android.util.Base64
import android.widget.Toast
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec

class CheckToken(val mContext: Context) {


    private val publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzbv3ei4NZWLoPQjO2meH\n" +
            "Ga7xaP6uTAPNHOwA20/hZnxtuRzubxRpux5XmyCHIleqx6bOXw86U3a3emGjg+lO\n" +
            "Fo8D3samc5EpzQ9/74ZL/EhezacziwYCDXLCf55J8AnHe1g7OdvVlJLAk663uN+j\n" +
            "eDrTNVd9sRj8/CjpMGqMgkYoRNi4/oVzEFTR2BWW5SM4sZFh57lW3qOMgvXTfbV9\n" +
            "vcaOosIVO2GVVXwk32qn+DBytmTSSjQJLQRzFiCd8LXK4/NKdBFUiitdrYskAAwm\n" +
            "PD1KtxUr6VJDKywBVEXxhWq/bwqxGJi++xayFB24Iv2lcnbAfsXmnhOepz3IEi81\n" +
            "SwIDAQAB"


    // JWT校验token 并解码
    fun jwtParse(token: String): String {
        try {
            val keyBytes = Base64.decode(publicKey, Base64.DEFAULT)
            val keySpec = X509EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            val publicKey = keyFactory.generatePublic(keySpec) as RSAPublicKey
            val algorithm = Algorithm.RSA256(publicKey, null)
            val verifier = JWT.require(algorithm)
                .build()

            val jwt = verifier.verify(token)
            //公钥校验成功
            println("----JWT subject---: ${jwt.subject}")
            if(!jwt.subject.isNullOrBlank()){
                println("----JWT payload---: ${jwt.getClaim("accountStatus").asString()}")
                //获取到AccountStatus
                return jwt.getClaim("accountStatus").asString()
            }
            return ""
        } catch (t: Throwable) {
            //公钥校验失败
            println("-----JWT error---: $t")
            Toast.makeText(mContext,"token校验错误，请重新登录", Toast.LENGTH_SHORT).show()
            return ""
        }
    }
}