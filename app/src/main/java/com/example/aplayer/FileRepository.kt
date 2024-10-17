package com.example.aplayer

import android.util.Log
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class FileRepository {
    suspend fun uploadVideo(file: File) :String {
        return try {
            var res =FileApi.instance.uploadVideo(
                video = MultipartBody.Part
                    .createFormData(
                        "file",
                        file.name,
                        file.asRequestBody()
                    )
            )
            Log.i("SERVICE", res.body().toString())
            res.body().toString()
        } catch (e: IOException) {
            e.printStackTrace()
            "failure"
        }

    }
}