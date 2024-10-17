package com.example.aplayer.recview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.aplayer.R
import com.example.aplayer.databinding.VideoItemBinding
import com.example.aplayer.getMedia.MediaUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class VideoAdapter( val context:Context?, val videoList : MutableList<MediaUtils.Video>, private val onSelect: (MediaUtils.Video?) -> Unit) : RecyclerView.Adapter<VideoAdapter.VideoHolder>() {

    inner class VideoHolder(item:View) :RecyclerView.ViewHolder(item) {
        val binding = VideoItemBinding.bind(item)

        fun bind(video: MediaUtils.Video, onSelect: (MediaUtils.Video?) -> Unit) {

            val milliseconds =video.duration.toLong()
            val format = SimpleDateFormat("HH:mm:ss")
            format.timeZone= TimeZone.getTimeZone("GMT")

            Glide.with(context!!).asBitmap().load(video.uri).centerCrop().diskCacheStrategy(
                DiskCacheStrategy.AUTOMATIC).into(binding.imageView)

            binding.titleView.text =video.name
            binding.durationView.text=format.format(Date(milliseconds)).replaceFirst(Regex("0+:"), "")

            binding.root.setOnClickListener {
                onSelect(video)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        return VideoHolder(view)
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        holder.bind(videoList[position], onSelect)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }
}