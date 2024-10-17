package com.example.aplayer

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileApi {
    @Multipart
    @POST("/")
    suspend fun uploadVideo(
        @Part video: MultipartBody.Part
    ) : Response<String>

    companion object {
        val instance by lazy {
            Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://192.168.1.101:8081/")
                .build()
                .create(FileApi::class.java)
        }
    }
}