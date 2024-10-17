package com.example.aplayer.recview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.aplayer.R
import com.example.aplayer.databinding.AudioItemBinding
import com.example.aplayer.databinding.VideoItemBinding
import com.example.aplayer.getMedia.MediaUtils
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Date
import java.util.TimeZone

class AudioAdapter( val context:Context?, val audioList : MutableList<MediaUtils.Audio>, private val onSelect: (MediaUtils.Audio?) -> Unit) : RecyclerView.Adapter<AudioAdapter.AudioHolder>() {

    inner class AudioHolder(item:View) :RecyclerView.ViewHolder(item) {
        val binding = AudioItemBinding.bind(item)

        fun bind(audio: MediaUtils.Audio, onSelect: (MediaUtils.Audio?) -> Unit) {

            val milliseconds =audio.duration.toLong()
            val format = SimpleDateFormat("HH:mm:ss")
            format.timeZone= TimeZone.getTimeZone("GMT")


            Glide.with(context!!).asBitmap().load(audio.albumUri).placeholder(R.drawable.baseline_audio_file_24).centerCrop().diskCacheStrategy(
                DiskCacheStrategy.RESOURCE).into(binding.imageView)

            binding.titleView.text =audio.name
            binding.durationView.text=format.format(Date(milliseconds)).replaceFirst(Regex("0+:"), "")

            binding.root.setOnClickListener {
                onSelect(audio)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.audio_item, parent, false)
        return AudioHolder(view)
    }

    override fun onBindViewHolder(holder: AudioHolder, position: Int) {
        holder.bind(audioList[position], onSelect)

    }

    override fun getItemCount(): Int {
        return audioList.size
    }
}