package com.example.talkie.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.talkie.databinding.ReceivedMessageBinding
import com.example.talkie.databinding.SentMessageBinding
import com.example.talkie.models.Message
import com.example.talkie.utils.FirebaseUtils

class MessagesAdapter(private val context: Context, private val messageList: List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val SENT = 1
        const val RECEIVED = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SENT) {
            SentMessagesViewHolder(SentMessageBinding.inflate(LayoutInflater.from(context), parent, false))
        } else {
            ReceivedMessagesViewHolder(ReceivedMessageBinding.inflate(LayoutInflater.from(context), parent, false))
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if (holder.javaClass == SentMessagesViewHolder::class.java) {
            (holder as SentMessagesViewHolder).bind(message)
        } else {
            (holder as ReceivedMessagesViewHolder).bind(message)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (FirebaseUtils.getFirebaseAuth().uid.equals(messageList[position].senderID)){
            SENT
        } else {
            RECEIVED
        }
    }
    inner class SentMessagesViewHolder(private val binding: SentMessageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.sentMessageTv.text = message.message

        }
    }

    inner class ReceivedMessagesViewHolder(private val binding: ReceivedMessageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.receivedMessageTv.text = message.message

        }
    }

}
