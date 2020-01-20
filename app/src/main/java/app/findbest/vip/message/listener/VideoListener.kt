package app.findbest.vip.message.listener

import com.alibaba.fastjson.JSONObject

interface VideoListener {
    fun awaitVideo(json: JSONObject)
}