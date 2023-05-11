package com.example.talkie.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.talkie.R
import com.example.talkie.databinding.RowConversationBinding
import com.example.talkie.models.User

class UserAdapter (private val context: Context, private val userList: List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowConversationBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(private val binding: RowConversationBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: User) {
            binding.user = user
            binding.executePendingBindings()
            Glide.with(context).load(user.profileImage).placeholder(R.drawable.avatar).into(binding.profileImage)
        }
    }
}