package com.fanplus.fanplustalk.ui.channel.detail

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.fanplus.fanplustalk.R
import com.fanplus.fanplustalk.databinding.ActivityChannelDetailBinding
import com.fanplus.fanplustalk.databinding.ActivityFileDetailBinding
import java.io.File
import java.util.*

class FileDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFileDetailBinding
    private var downloadId: Long = -1L
    private lateinit var downloadManager: DownloadManager

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action) {
                if (downloadId == id) {
                    val query: DownloadManager.Query = DownloadManager.Query()
                    query.setFilterById(id)
                    val cursor = downloadManager.query(query)
                    if (!cursor.moveToFirst()) {
                        return
                    }

                    val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    val status = cursor.getInt(columnIndex)
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        Toast.makeText(context, "Download succeeded", Toast.LENGTH_SHORT).show()
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED == intent.action) {
                Toast.makeText(context, "Notification clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fileUri = intent.getStringExtra("uri")?.toUri()

        val imageView = binding.imageDetail
        val download = binding.download
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val intentFilter = IntentFilter()
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
        registerReceiver(onDownloadComplete, intentFilter)

        Glide.with(this).load(fileUri).into(imageView)

        download.setOnClickListener {
            fileUri?.let { it1 -> downloadFile(it1) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onDownloadComplete)
    }

    private fun downloadFile(uri: Uri){
        val file = File("/storage/emulated/0/DCIM/Camera/", "${Date().time}.${uri.path?.split(".")?.get(1)}")
        val request = DownloadManager.Request(uri).setTitle("파일 다운로드")
            .setDescription("파일 다운로드 중..")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(Uri.fromFile(file))
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        downloadId = downloadManager.enqueue(request)
    }
}