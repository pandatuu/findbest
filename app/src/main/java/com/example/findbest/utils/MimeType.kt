package com.example.findbest.utils

import okhttp3.MediaType

object MimeType {

    val MULTIPART_FORM_DATA = MediaType.parse("multipart/form-data; charset=utf-8")!!
    val IMAGE_JPEG = MediaType.parse("image/jpeg")!!
    val IMAGE_PNG = MediaType.parse("image/png")!!
    val IMAGE_WEBP = MediaType.parse("image/webp")!!
    val IMAGE_BMP = MediaType.parse("image/bmp")!!
    val IMAGE_GIF = MediaType.parse("image/gif")!!
    val APPLICATION_JSON = MediaType.parse("application/json")!!
    val TEXT_PLAIN = MediaType.parse("text/plain")!!
    val TEXT_HTML = MediaType.parse("text/html")!!
    val VIDEO_AVI = MediaType.parse("video/x-msvideo")!!
    val VIDEO_MP4 = MediaType.parse("video/mp4")!!
    val VIDEO_FLV = MediaType.parse("video/x-flv")!!
    val VIDEO_WMV = MediaType.parse("video/x-ms-wmv")!!
    val IMAGE_JPG = MediaType.parse("image/jpeg")!!
    val FILE_PDF =MediaType.parse("application/pdf")!!
    val FILE_DOC = MediaType.parse("application/msword")!!
    val FILE_DOCX = MediaType.parse("application/vnd.openxmlformats-officedocument.wordprocessingml.document")!!
    val FILE_XLS = MediaType.parse("application/vnd.ms-excel")!!
    val FILE_XLSX = MediaType.parse("application/vnd.ms-excel")!!

}
