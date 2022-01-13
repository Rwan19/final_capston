package com.example.myapplication.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.App
import com.example.myapplication.EventObserver
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentUserBinding
import com.example.myapplication.ui.profil.ProfileFragment


class UserFragment : Fragment() {
    private val viewModel: UsersViewModel by viewModels { UsersViewModelFactory(App.myUserID) }
    private lateinit var viewDataBinding: FragmentUserBinding
    private lateinit var listAdapter: UsersListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            FragmentUserBinding.inflate(inflater, container, false).apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
        setupObservers()
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = UsersListAdapter(viewModel)
            viewDataBinding.usersRecyclerView.adapter = listAdapter
        } else {
            throw Exception("The viewmodel is not initialized")
        }
    }

    private fun setupObservers() {
        viewModel.selectedUser.observe(viewLifecycleOwner, EventObserver { navigateToProfile(it.info.id) })
    }

    private fun navigateToProfile(userID: String) {
        val bundle = bundleOf(ProfileFragment.ARGS_KEY_USER_ID to userID)
        findNavController().navigate(R.id.action_userFragment_to_profileFragment, bundle)
    }
}