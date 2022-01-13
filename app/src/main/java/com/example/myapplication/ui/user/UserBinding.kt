package com.example.myapplication.ui.user

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.database.entity.User


@BindingAdapter("bind_users_list")
fun bindUsersList(listView: RecyclerView, items: List<User>?) {
    items?.let { (listView.adapter as UsersListAdapter).submitList(items) }
}

