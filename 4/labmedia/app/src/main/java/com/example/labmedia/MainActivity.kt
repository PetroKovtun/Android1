package com.example.labmedia

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var tvNowPlaying: TextView

    private val pickAudio =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                updateNowPlaying(it, isAudio = true)
                playMedia(it)
            }
        }

    private val pickVideo =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                updateNowPlaying(it, isAudio = false)
                playMedia(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById(R.id.playerView)
        tvNowPlaying = findViewById(R.id.tvNowPlaying)

        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val etUrl = findViewById<EditText>(R.id.etUrl)

        findViewById<Button>(R.id.btnPlayUrl).setOnClickListener {
            val url = etUrl.text.toString().trim()

            if (url.isEmpty()) {
                Toast.makeText(this, "Введіть URL відео", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            tvNowPlaying.text = "Відтворюється відео з Інтернету:\n$url"
            playMedia(Uri.parse(url))
        }

        findViewById<Button>(R.id.btnPickAudio).setOnClickListener {
            pickAudio.launch(arrayOf("audio/*"))
        }

        findViewById<Button>(R.id.btnPickVideo).setOnClickListener {
            pickVideo.launch(arrayOf("video/*"))
        }

        findViewById<Button>(R.id.btnStop).setOnClickListener {
            player.stop()
            tvNowPlaying.text = "Відтворення зупинено"
        }
    }

    private fun playMedia(uri: Uri) {
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }


    private fun updateNowPlaying(uri: Uri, isAudio: Boolean) {
        var name = "Невідомий файл"

        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index != -1 && cursor.moveToFirst()) {
                name = cursor.getString(index)
            }
        }

        tvNowPlaying.text =
            if (isAudio) "Відтворюється аудіо:\n$name"
            else "Відтворюється відео:\n$name"
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}
