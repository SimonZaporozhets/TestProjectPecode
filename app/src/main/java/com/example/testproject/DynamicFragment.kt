package com.example.testproject

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.testproject.databinding.FragmentDynamicBinding


class DynamicFragment : Fragment() {

    private lateinit var binding: FragmentDynamicBinding
    private var notificationId: Int = 0
    private val viewModel: ItemViewModel by activityViewModels()
    private var fragmentIndex: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDynamicBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fragmentIndex.observe(viewLifecycleOwner, { item ->
            fragmentIndex = item
        })

        binding.newNotification.setOnClickListener {
            sendNotification()
        }
    }

    private fun sendNotification() {
        notificationId = fragmentIndex

        val notificationIntent = Intent(activity, MainActivity::class.java)
        notificationIntent.putExtra("fragId", notificationId)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("You create a notification")
            .setContentText("Notification ${fragmentIndex + 1}")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(fragmentIndex, builder.build())
        }
    }

    companion object {
        const val CHANNEL_ID = "1"
    }

}