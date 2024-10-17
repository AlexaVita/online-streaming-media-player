package com.example.aplayer

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore


object RealPathUtil {
    fun getRealPath(context: Context, fileUri: Uri): String? {
        return getRealPathFromURI(context, fileUri)
    }

    @SuppressLint("NewApi")
    fun getRealPathFromURI(context: Context, uri: Uri): String? {
        val ikt = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        
        if (ikt && DocumentsContract.isDocumentUri(context, uri)) {

            if (externalB(uri)) {
                val document = DocumentsContract.getDocumentId(uri)
                val sp = document.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val type = sp[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + sp[1]
                }

            } else if (downloadsB(uri)) {
                val document = DocumentsContract.getDocumentId(uri)

                if (document.startsWith("raw:")) {
                    return document.replaceFirst("raw:", "");
                }
                if (document.contains("msf:")) {
                    if (context.contentResolver.getType(uri)!!.contains("audio"))
                        return getAudioPathFromURI(context, uri)
                    if (context.contentResolver.getType(uri)!!.contains("video"))
                        return getVideoPathFromURI(context, uri)
                }

                val uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(document)
                )
                return getData(context, uri, null, null)
            } else if (mediaB(uri)) {
                val document = DocumentsContract.getDocumentId(uri)
                val sp = document.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val type = sp[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                else {
                    contentUri = MediaStore.Files.getContentUri("external");
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    sp[1]
                )
                return getData(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            return if (photosB(uri)) uri.lastPathSegment else getData(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    @SuppressLint("Range")
    fun getAudioPathFromURI(context: Context, uri: Uri?): String? {
        var cur = context.contentResolver.query(uri!!, null, null, null, null)
        var path: String? = null
        if (cur != null) {
            cur.moveToFirst()
            var document_id = cur.getString(0)
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
            cur.close()
            cur = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Audio.Media._ID + " = ? ", arrayOf(document_id), null
            )
            if (cur != null) {
                cur.moveToFirst()
                path = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA))
                cur.close()
            }
        }

        return path
    }


    @SuppressLint("Range")
    fun getVideoPathFromURI(context: Context, uri: Uri?): String? {
        var cur = context.contentResolver.query(uri!!, null, null, null, null)
        var path: String? = null
        if (cur != null) {
            cur.moveToFirst()
            var document_id = cur.getString(0)
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
            cur.close()
            cur = context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Video.Media._ID + " = ? ", arrayOf(document_id), null
            )
            if (cur != null) {
                cur.moveToFirst()
                path = cur.getString(cur.getColumnIndex(MediaStore.Video.Media.DATA))
                cur.close()
            }
        }

        return path
    }

    fun getData(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    fun downloadsB(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun mediaB(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun externalB(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun photosB(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}