package com.example.madstagrarn.network
import com.example.madstagrarn.Phone
import com.example.madstagrarn.Post
import com.example.madstagrarn.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


class DataService {
    val BASE_URL = "http://192.249.18.244:8080/"
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

    @POST("api/user/contact")
    fun getUsersByContact(@Body phoneList: ArrayList<Phone>) : Call<ArrayList<User>>

    @FormUrlEncoded
    @POST("api/user/login")
    fun loginRequest(@Field("isFacebookUser") isFacebookUser: Boolean, @Field("userId") userId: String, @Field("password") password: String) : Call<User>

    @FormUrlEncoded
    @POST("api/user")
    fun signupRequest(@Field("isFacebookUser") isFacebookUser: Boolean, @Field("userId") userId: String, @Field("password") password: String, @Field("name") name: String, @Field("phoneNumber") phoneNumber: String): Call<User>

    @Multipart
    @POST("api/post")
    fun uploadPost(@Part("userId") id: RequestBody, @Part image:MultipartBody.Part, @Part("description") description: RequestBody) : Call<Post>

    @GET("api/post/{postId}")
    fun getPost(@Path("postId") postId: String) : Call<Post>

    @GET("api/user/{id}/posts")
    fun getUserPosts(@Path("id") id: String) : Call<ArrayList<Post>>

    @DELETE("api/post/{id}")
    fun deletePost(@Path("id") id: String) : Call<ResponseBody>
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