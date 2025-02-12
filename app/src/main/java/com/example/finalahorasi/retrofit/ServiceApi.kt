package com.example.finalahorasi.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val license: String, val name:String);
data class LoginResponse(val message:String, val success: Boolean);

data class StudentRequest(val license: String, val name:String, val maternalSurname:String, val paternalSurname:String, val email:String);
data class StudentResponse(val message:String, val success: String);

interface ServiceApi{
    @POST("/app/students/log")
    fun login(@Body request:LoginRequest): Call<LoginResponse>

    @POST("register") // Ajusta la URL según tu API
    fun registerStudent(@Body request: StudentRequest): Call<Void>
}