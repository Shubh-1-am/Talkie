package com.example.talkie.models

data class Message(
    var messageID: String = "",
    var message: String = "",
    var senderID: String = "",
    var timestamp: Long = 0,
    var reaction: Int = 0
) {
    constructor() : this("", "", "", 0, 0)
    constructor(message: String, senderID: String, timestamp: Long) : this("", message, senderID, timestamp, 0)
}

