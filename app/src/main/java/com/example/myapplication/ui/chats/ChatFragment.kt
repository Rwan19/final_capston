package com.example.myapplication.ui.chats
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.App
import com.example.myapplication.EventObserver
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentChatsBinding
import com.example.myapplication.model.ChatWthUserInfo
import com.example.myapplication.ui.chat.ChatMessageFragment
import com.example.myapplication.util.convertTwoUserIDs


class ChatFragment : Fragment() {
    private val viewModel: ChatsViewModel by viewModels { ChatsViewModelFactory(App.myUserID) }
    private lateinit var viewDataBinding:FragmentChatsBinding
    private lateinit var listAdapter: ChatsListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            FragmentChatsBinding.inflate(inflater, container, false)
                .apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
        setupObservers()
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = ChatsListAdapter(viewModel)
            viewDataBinding.chatsRecyclerView.adapter = listAdapter
        } else {
            throw Exception("The viewmodel is not initialized")
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupObservers() {
        viewModel.selectedChat.observe(viewLifecycleOwner,
            EventObserver { navigateToChat(it) })
    }

    private fun navigateToChat(chatWithUserInfo: ChatWthUserInfo) {
        val bundle = bundleOf(
            ChatMessageFragment.ARGS_KEY_USER_ID to App.myUserID,
            ChatMessageFragment.ARGS_KEY_OTHER_USER_ID to chatWithUserInfo.mUserInfo.id,
            ChatMessageFragment.ARGS_KEY_CHAT_ID to convertTwoUserIDs(App.myUserID, chatWithUserInfo.mUserInfo.id)
        )
        findNavController().navigate(R.id.from_chats_to_chatMessage, bundle)
    }

    }

