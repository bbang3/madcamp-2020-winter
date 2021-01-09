package com.example.madstagram

import java.text.SimpleDateFormat

data class User(
    val _id: String,
    var isFacebookUser: Boolean,
    var username: String?,
    var password: String?,
    var phoneNumber: String,
    var name: String,
    var followingIds: ArrayList<String>,
    var posts: ArrayList<String>,
    var signUpDate: String
)