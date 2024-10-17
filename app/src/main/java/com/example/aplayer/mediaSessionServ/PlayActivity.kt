package com.example.aplayer.mediaSessionServ

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.aplayer.databinding.ActivityPlayerBinding
import com.google.common.util.concurrent.MoreExecutors

class PlayActivity() : AppCompatActivity() {

    private lateinit var uri :String

    private lateinit var sharedPref : SharedPreferences

    lateinit var player : Player

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlayerBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("ACTIVITY Player", "CREATE")
        var inte = Intent(this, PlayService::class.java)
        stopService(inte)

        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

         sharedPref = this.getSharedPreferences("playbackPos", Context.MODE_PRIVATE);

        uri = intent.getStringExtra("uri").toString()
        val sessionToken =
            SessionToken(this, ComponentName(this, PlayService::class.java))

        val controllerFuture =
            MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({

            player =controllerFuture.get()

            val newItem = MediaItem.Builder()
                .setMediaId(uri)
                .build()

            viewBinding.videoView.player=player

            player.setMediaItem(newItem, sharedPref.getLong(uri, 0))
            player.prepare()
            player.play()
        }, MoreExecutors.directExecutor())
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, viewBinding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("ACTIVITY Player", "START")
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
    }

    override fun onStop() {
        super.onStop()
        Log.i("ACTIVITY Player", "STOP")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("ACTIVITY Player", "DESTROY")
    }
}