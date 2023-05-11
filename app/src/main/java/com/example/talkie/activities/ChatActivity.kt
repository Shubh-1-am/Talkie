package com.example.talkie.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.talkie.R
import com.example.talkie.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}