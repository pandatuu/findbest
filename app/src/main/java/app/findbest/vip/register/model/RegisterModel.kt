package app.findbest.vip.register.model

import java.io.Serializable


data class RegisterModel(
    var phone: String,
    var country: String,
    var vCode: String,
    var pwd: String,
    var email: String
): Serializable