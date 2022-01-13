package com.example.myapplication.ui.regestration

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.*
import com.example.myapplication.databinding.FragmentRegesterBinding
import com.example.myapplication.ui.main.MainActivity
import com.example.myapplication.util.SharedPreferencesUtil

private const val TAG = "RegisterFragment"
class RegesterFragment : Fragment() {


    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var viewDataBinding: FragmentRegesterBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentRegesterBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
        Log.d(TAG, "onCreateView: ")
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setupObservers() {
        viewModel.dataLoading.observe(viewLifecycleOwner,
            EventObserver { (activity as MainActivity).showGlobalProgressBar(it) })

        viewModel.snackBarText.observe(viewLifecycleOwner,
            EventObserver { text ->
                view?.showSnackBar(text)
                view?.forceHideKeyboard()
            })


        viewModel.isCreatedResponseStateEvent.observe(viewLifecycleOwner, EventObserver {
            SharedPreferencesUtil.saveUserID(requireContext(), it.uid)
            navigateToChats()

        })
    }
    private fun navigateToChats() {
        findNavController().navigate(R.id.from_regester_to_chats)
    }

    }







