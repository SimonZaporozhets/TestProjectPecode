package com.example.testproject

import android.app.NotificationManager
import android.content.Context
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

        setContentView(binding.root)

        initViewPager()
        loadData()
        initListeners()
        initPageChangeCallback()

        notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val fragId = intent?.getIntExtra("fragId", 0)
        fragId?.let {
            viewPager.currentItem = it
        }

    }

    private fun initViewPager() {
        viewPager = binding.viewpager

        mAdapter = DynamicFragmentAdapter(
            this
        )

        viewPager.adapter = mAdapter
    }

    private fun initListeners() {
        binding.add.setOnClickListener {
            mAdapter.addFragment(DynamicFragment())
            if (mAdapter.itemCount != 1) {
                binding.remove.visibility = VISIBLE
            } else {
                binding.remove.visibility = GONE
            }
            saveData()
        }

        binding.remove.setOnClickListener {
            val ntfId = mAdapter.removeFragment()
            notificationManager.cancel(ntfId)
            if (mAdapter.itemCount != 1) {
                binding.remove.visibility = VISIBLE
            } else {
                binding.remove.visibility = GONE
            }
            saveData()
        }
    }

    private fun initPageChangeCallback() {
        pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0 && mAdapter.itemCount == 1) {
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


    private fun loadData() {

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val listSize = sharedPref.getInt(getString(R.string.list_size), 0)

        if (listSize == 1) {
            binding.remove.visibility = GONE
        } else {
            binding.remove.visibility = VISIBLE
        }

        if (listSize == 1) return
        mAdapter.updateList(listSize)
    }

    private fun saveData() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(getString(R.string.list_size), mAdapter.itemCount)
            apply()
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