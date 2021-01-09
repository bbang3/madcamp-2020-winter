package com.example.madstagrarn.network

data class UserResponse (
    val id: String, var isFacebookUser: Boolean, var username: String, var password: String, var phoneNumber: String,
    var name: String, var followingIds: ArrayList<String>, var posts: ArrayList<String>, var signupDate: String
)