package com.example.talkie.models

data class User(
    val userID: String = "",
    val name: String = "",
    val profileImage: String = ""
) {
    constructor() : this("", "", "")
}


