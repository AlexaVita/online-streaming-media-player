package com.example.aplayer.getMedia

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.util.concurrent.TimeUnit

class MediaUtils() {


    data class Video(val uri: Uri,
                     val name: String,
                     val duration: Int,
                     val size: Int
    )

    data class Audio(val uri: Uri,
                     val name: String,
                     val duration: Int,
                     val size: Int,
                     val albumUri: Uri
    )

    companion object {
        fun getVideos(contentResolver: ContentResolver) : MutableList<Video> {
            val videoList = mutableListOf<Video>()

            val collection =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Video.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL
                    )
                } else {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }

            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE
            )

            val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"


            val query = contentResolver.query(
                collection,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

                while (cursor.moveToNext()) {

                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val duration = cursor.getInt(durationColumn)
                    val size = cursor.getInt(sizeColumn)

                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    videoList += Video(contentUri, name, duration, size)
                }
                Log.i("ACTIVITY", "GETVIDEOS")
            }
            return videoList
        }

        fun getAudios(contentResolver : ContentResolver) : MutableList<Audio> {

            val audioList = mutableListOf<Audio>()

            val collection =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Audio.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL
                    )
                } else {
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM_ID
            )

            val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

            val query = contentResolver.query(
                collection,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

                while (cursor.moveToNext()) {

                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val duration = cursor.getInt(durationColumn)
                    val size = cursor.getInt(sizeColumn)
                    val albumId = cursor.getLong(albumIdColumn)

                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    val albumUri = ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"), albumId
                    )

                    audioList += Audio(contentUri, name, duration, size, albumUri)
                }
                Log.i("ACTIVITY", "GETAUDIOS")
            }
            return audioList
        }
    }
}