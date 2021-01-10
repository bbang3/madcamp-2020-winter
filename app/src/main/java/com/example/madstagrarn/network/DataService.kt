package com.example.madstagrarn.network
import com.example.madstagrarn.Phone
import com.example.madstagrarn.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


class DataService {
    private val BASE_URL = "http://192.249.18.244:8080/"
    var retrofitClient = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var service = retrofitClient.create(RetrofitService::class.java)
}

interface RetrofitService {
    @GET("api/user/{id}/following")
    fun getFollowingUsers(@Path("id") id: String) : Call<ArrayList<User>>

    @GET("api/user/{id}")
    fun getUser(@Path("id") id: String) : Call<User>

    @GET("api/user")
    fun getUsersByContact(@Body body: ArrayList<Phone>) : Call<ArrayList<User>>
//    @GET("api/phone")
//    fun getPhoneList(): Call<ArrayList<Phone>>
//
//    @POST("api/phone")
//    fun addPhone(@Body body: Phone): Call<Phone>
//
//    @DELETE("api/phone/{id}")
//    fun delPhone(@Path("id") id: String): Call<ResponseBody>
//
//    @PUT("api/phone/{id}")
//    fun updatePhone(@Path("id") _id: String, @Body body: Phone) : Call<Phone>
//
//    @GET("api/image")
//    fun getGalleryInfo(): Call<ArrayList<Image>>
//
//    @GET("api/image/{id}")
//    fun getImage(@Path("id") _id: String) : Call<ResponseBody>
}