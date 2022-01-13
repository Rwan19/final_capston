package com.example.myapplication.ui.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.databinding.ListItemUserBinding
import com.example.myapplication.model.ChatWthUserInfo



class ChatsListAdapter internal constructor(private val viewModel: ChatsViewModel) :
    ListAdapter<(ChatWthUserInfo), ChatsListAdapter.ViewHolder>(ChatDiffCallback()) {

    class ViewHolder(private val binding:ListItemUserBinding ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ChatsViewModel, item: ChatWthUserInfo) {
            binding.viewmodel = viewModel
            binding.chatwithuserinfo = item
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemUserBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
}

class ChatDiffCallback : DiffUtil.ItemCallback<ChatWthUserInfo>() {
    override fun areItemsTheSame(oldItem: ChatWthUserInfo, itemWithUserInfo: ChatWthUserInfo): Boolean {
        return oldItem == itemWithUserInfo
    }

    override fun areContentsTheSame(oldItem: ChatWthUserInfo, itemWithUserInfo: ChatWthUserInfo): Boolean {
        return oldItem.mChat.info.id == itemWithUserInfo.mChat.info.id
    }
}