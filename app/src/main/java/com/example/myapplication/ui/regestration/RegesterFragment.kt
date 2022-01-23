package com.example.myapplication.ui.regestration

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
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
    private val CHANNEL_ID="channel_id"
    private var resources= R.string.notifiction_channel_name.toString()




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


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(context:Context) {

        val mChannelName=resources
        val mChannelDescription="Channel Description"
        val mChannelImportance= NotificationManager.IMPORTANCE_DEFAULT
        val channel= NotificationChannel(CHANNEL_ID,mChannelName, mChannelImportance).toString()

        val notificationManager= getSystemService(context, NotificationManager::class.java)

        val builder =context.let {
            NotificationCompat.Builder(context.applicationContext ,channel)
                .setTicker("")
                .setSmallIcon(R.drawable.circle_online)
                .setContentTitle("textTitle")
                .setContentText("textContent")
                .setAutoCancel(true)
        }


        val notificationChannel= NotificationChannel(channel,"signup notification",
            NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager?.createNotificationChannel(notificationChannel)
        builder.setChannelId(channel)

        val notification = builder.build()
        notificationManager?.notify(100,notification)

    }



    @RequiresApi(Build.VERSION_CODES.O)
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







