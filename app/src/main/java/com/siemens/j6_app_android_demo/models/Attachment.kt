package com.siemens.j6_app_android_demo.models

import java.io.Serializable

class Attachment(var attachment: Any, var type: Int, var fileName: String?, var extension: String?): Serializable {
    companion object {
        val IMAGE = 0
        val CAMERA = 1
        val FILE = 2
    }
}