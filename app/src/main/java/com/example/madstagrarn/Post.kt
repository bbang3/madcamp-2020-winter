package com.example.madstagrarn

data class Post(
    val _id: String,
    var authorId: String,
    var author: String,
    var authorProfile: String?,
    var description: String,
    var images: ArrayList<String>,
    var date: String
)