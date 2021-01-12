package com.example.madstagrarn.dataclass

import java.io.Serializable
import java.text.SimpleDateFormat

data class User(
    val _id: String,
    var isFacebookUser: Boolean,
    var userId: String,
    var password: String?,
    var phoneNumber: String,
    var name: String,
    var profileImage: String,
    var followingIds: ArrayList<String>,
    var posts: ArrayList<String>,
    var signUpDate: String
): Serializable