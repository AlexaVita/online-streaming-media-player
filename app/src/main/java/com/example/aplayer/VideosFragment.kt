package com.example.aplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplayer.getMedia.MediaUtils
import com.example.aplayer.mediaSessionServ.PlayActivity
import com.example.aplayer.recview.VideoAdapter

class VideosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_videos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resolver = requireActivity().contentResolver
        val list = MediaUtils.getVideos(resolver)
        val videoAdapter: VideoAdapter = VideoAdapter(context,list) {
            video ->

            val intent = Intent(context, PlayActivity::class.java)
            intent.putExtra("uri", video?.uri.toString())
            startActivity(intent)
        }
        Log.i("ACTIVITY", "FRAGMENT")

        val recView:RecyclerView = view.findViewById(R.id.recView)
        recView.layoutManager=GridLayoutManager(context,2)
        recView.adapter=videoAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = VideosFragment()
    }
}