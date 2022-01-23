package com.example.myapplication.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.VISIBLE
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.myapplication.R
import com.example.myapplication.data.database.FirebaseDataSource
import com.example.myapplication.forceHideKeyboard
import com.example.myapplication.util.MyWorker
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var navView: BottomNavigationView
    private lateinit var mainProgressBar: ProgressBar
    private lateinit var notificationsBadge: BadgeDrawable


    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navView = findViewById(R.id.nav_btn)
        mainProgressBar = findViewById(R.id.progressBar)


        notificationsBadge =
            navView.getOrCreateBadge(R.id.notificationFragment).apply { isVisible = false }


        val navController = findNavController(R.id.fragmentContainerView)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.profileFragment -> navView.visibility = View.GONE
                R.id.chatMessageFragment -> navView.visibility = View.GONE
                R.id.startFragment2 -> navView.visibility = View.GONE
                R.id.loginFragment -> navView.visibility = View.GONE
                R.id.regesterFragment -> navView.visibility = View.GONE
                else -> navView.visibility=View.VISIBLE
            }
            showGlobalProgressBar(false)
            currentFocus?.rootView?.forceHideKeyboard()
        }



            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.chatFragment,
                    R.id.notificationFragment,
                    R.id.userFragment,
                    R.id.sittingsFragment,
                    R.id.startFragment2
                )
            )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        simpleWork()
        myWorkManager()
            }

            override fun onPause() {
                super.onPause()
                FirebaseDataSource.dbInstance.goOffline()
            }

            override fun onResume() {
                super.onResume()
                FirebaseDataSource.dbInstance.goOnline()
                setupViewModelObservers()

            }

private fun myWorkManager(){
    val constraint=Constraints.Builder()
        .setRequiresCharging(false)
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresCharging(false)
        .setRequiresBatteryNotLow(true)
        .build()

    val myRequest=PeriodicWorkRequest.Builder(
        MyWorker::class.java,
        15,
        TimeUnit.MINUTES
    ).setConstraints(constraint)
        .build()

    WorkManager.getInstance(this)
        .enqueueUniquePeriodicWork(
            "my_id",
            ExistingPeriodicWorkPolicy.KEEP,
            myRequest
        )
}
    private fun simpleWork(){
        val mRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .build()

        WorkManager.getInstance(this).enqueue(mRequest)
    }

private fun setupViewModelObservers() {
    viewModel.userNotificationsList.observe(this) {
        if (it.size > 0) {
            notificationsBadge.number = it.size
            notificationsBadge.isVisible = true
        } else {
            notificationsBadge.isVisible = false
        }
    }
}
    fun showGlobalProgressBar(show: Boolean) {
        if (show) mainProgressBar.visibility = View.VISIBLE
        else mainProgressBar.visibility = View.GONE
    }

}
