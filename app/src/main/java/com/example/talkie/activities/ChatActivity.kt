package com.example.talkie.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.talkie.R
import com.example.talkie.adapters.MessagesAdapter
import com.example.talkie.databinding.ActivityChatBinding
import com.example.talkie.models.Message
import com.example.talkie.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: MessagesAdapter
    private lateinit var messages : ArrayList<Message>

    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String
    private lateinit var databaseRef: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val receiverUID = intent.getStringExtra("uid")
        val senderUID = FirebaseUtils.getFirebaseAuth().uid

        messages = ArrayList();
        adapter = MessagesAdapter(this,messages)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = adapter

        senderRoom = senderUID + receiverUID
        receiverRoom = receiverUID + senderUID
        binding.userName.text = name

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        databaseRef = FirebaseUtils.getDatabase()

        databaseRef.reference.child("chats")
            .child(senderRoom)
            .child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    if (snapshot.hasChildren()) {
                        for (dataSnapshot in snapshot.children) {
                            if (dataSnapshot.hasChild("message")) {
                                val message = dataSnapshot.getValue(Message::class.java)
                                messages.add(message!!)
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })


        binding.sendBtn.setOnClickListener {
            val messageText: String = binding.messageEditText.text.toString()
            val date : Date = Date()
            val message: Message = Message(messageText,senderUID!!, date.time)
            binding.messageEditText.setText("")
            databaseRef.reference.
            child("chats").
            child(senderRoom).
            child("messages").push().
            setValue(message).addOnSuccessListener {
                databaseRef.reference.
                child("chats").
                child(receiverRoom).
                child("messages").push().
                setValue(message).addOnSuccessListener {

                }
            }

        }



    }
}