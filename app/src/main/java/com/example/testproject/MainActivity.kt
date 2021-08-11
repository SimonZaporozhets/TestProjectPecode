package com.example.testproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.testproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: DynamicFragmentAdapter
    private lateinit var pageChangeCallback: ViewPager2.OnPageChangeCallback
    private lateinit var notificationManager: NotificationManager
    private val viewModel: ItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        createNotificationChannel()

        viewPager = binding.viewpager

        mAdapter = DynamicFragmentAdapter(
            this
        )

        viewPager.adapter = mAdapter

        initListeners()
        initPageChangeCallback()

        notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun initListeners() {
        binding.add.setOnClickListener {
            mAdapter.addFragment(DynamicFragment())
            if(mAdapter.itemCount != 1) {
                binding.remove.visibility = VISIBLE
            } else {
                binding.remove.visibility = GONE
            }
        }

        binding.remove.setOnClickListener {
            val ntfId = mAdapter.removeFragment()
            notificationManager.cancel(ntfId)
            if(mAdapter.itemCount != 1) {
                binding.remove.visibility = VISIBLE
            } else {
                binding.remove.visibility = GONE
            }
        }
    }

    private fun initPageChangeCallback() {
        pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if(position == 0 && mAdapter.itemCount == 1) {
                    binding.remove.visibility = GONE
                } else {
                    binding.remove.visibility = VISIBLE
                }
                binding.barCounter.text = (position + 1).toString()
                viewModel.setFragmentIndex(position)
            }
        }

        viewPager.registerOnPageChangeCallback(pageChangeCallback)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(DynamicFragment.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}