package com.example.finalahorasi.retrofit;

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.Part

object Client {
    private const val BASE_URL = "http://192.168.1.70:3000/app/students/"
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

data class RegisterRequest(
    val license: String,
    val name: String,
    val maternal_surname: String,
    val paternal_surname: String,
    val email: String
)

// Definir la respuesta del servidor
data class RegisterResponse(val message: String, val success: Boolean)
data class ImageRequest(val image: String)


interface ApiService {
    @POST("log")  // Asegúrate de que la ruta es correcta
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("r") // Ajusta la URL según tu API
    fun registerStudent(@Body request: StudentRequest): Call<Void>

    @GET("{license}")  // Pasamos la matrícula como parte de la URL
    fun getStudent(@Path("license") license: String): Call<Student>

    @PUT("{license}")
    fun updateStudent(@Path("license") license: String, @Body student: Student): Call<Void>

    @DELETE("{license}")
    fun deleteStudent(@Path("license") license: String): Call<Void>

    //@POST("p") // Cambia la ruta según tu API
    //fun uploadImage(@Body request: ImageRequest): Call<Void>

    @Multipart
    @POST("photo") // Ruta de tu API
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>

    @POST("video")
    suspend fun uploadVideo(@Part video: MultipartBody.Part): Response<ResponseBody>
}




data class Student(
    val license: String,
    val name: String,
    val maternalSurname: String,
    val paternalSurname: String,
    val email: String
)

