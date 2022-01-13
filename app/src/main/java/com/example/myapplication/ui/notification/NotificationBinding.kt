package com.example.myapplication.ui.notification

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.database.entity.UserInfo


@BindingAdapter("bind_notifications_list")
fun bindNotificationsList(listView: RecyclerView, items: List<UserInfo>?) {
    items?.let { (listView.adapter as NotificationsListAdapter).submitList(items) }
}
