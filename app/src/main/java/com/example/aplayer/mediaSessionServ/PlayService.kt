package com.example.aplayer.mediaSessionServ

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.metrics.PlaybackStateEvent
import android.media.session.PlaybackState
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class PlayService : MediaLibraryService() {
    private lateinit var uri :String
    private lateinit var sharedPref : SharedPreferences
    lateinit var player: Player

    lateinit var session: MediaLibrarySession

    @SuppressLint("UnsafeOptInUsageError")
    override fun  onCreate() {
        Log.i("SERVICE Player", "create")

        super.onCreate()

        sharedPref = this.getSharedPreferences("playbackPos", Context.MODE_PRIVATE);

        player = ExoPlayer.Builder(applicationContext).build()

        session = MediaLibrarySession.Builder(this, player,
            object: MediaLibrarySession.Callback {
                override fun onAddMediaItems(
                    mediaSession: MediaSession,
                    controller: MediaSession.ControllerInfo,
                    mediaItems: MutableList<MediaItem>
                ): ListenableFuture<MutableList<MediaItem>> {

                    val updatedMediaItems = mediaItems.map { it.buildUpon().setUri(it.mediaId).build() }.toMutableList()
                    return Futures.immediateFuture(updatedMediaItems)
                }
            }).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return session
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("SERVICE Player", "START")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopSelf()
        Log.i("SERVICE Player", "DSSS")
    }

    override fun onDestroy() {
        Log.i("SERVICE Player", "DESTROY")
        uri= player.currentMediaItem!!.mediaId
        Log.i("SERVICE Player", player.currentPosition.toString())
        Log.i("SERVICE Player", player.contentDuration.toString())

        if (player.currentPosition<player.contentDuration)
            sharedPref.edit().putLong(uri, player.currentPosition).apply()
        else
            sharedPref.edit().remove(uri).apply()
        session?.run {
            player.release()
            release()
            //session = null
        }
        super.onDestroy()
    }
}