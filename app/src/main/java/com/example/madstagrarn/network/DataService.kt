package com.example.madstagrarn.network
import com.example.madstagrarn.dataclass.Phone
import com.example.madstagrarn.dataclass.Post
import com.example.madstagrarn.dataclass.User
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
    @POST("api/user/signup")
    fun signupRequest(@Field("isFacebookUser") isFacebookUser: Boolean, @Field("userId") userId: String, @Field("password") password: String, @Field("name") name: String, @Field("phoneNumber") phoneNumber: String): Call<User>

    @PUT("api/user/follow/{id}/{followingId}")
    fun followRequest(@Path("id") id: String, @Path("followingId") followingId: String): Call<User>

    @DELETE("api/user/unfollow/{id}/{followingId}")
    fun unfollowRequest(@Path("id") id: String, @Path("followingId") followingId: String): Call<User>

    @Multipart
    @POST("api/post")
    fun uploadPost(@Part("userId") id: RequestBody, @Part image:MultipartBody.Part, @Part("description") description: RequestBody) : Call<Post>

    @GET("api/post/{postId}")
    fun getPost(@Path("postId") postId: String) : Call<Post>

    @GET("api/user/{id}/posts")
    fun getUserPosts(@Path("id") id: String) : Call<ArrayList<Post>>

    @DELETE("api/post/{id}")
    fun deletePost(@Path("id") id: String) : Call<ResponseBody>

    @Multipart
    @PUT("api/user/{userId}")
    fun updateUser(@Path("userId") id: String, @Part profileImage: MultipartBody.Part) : Call<User>
}