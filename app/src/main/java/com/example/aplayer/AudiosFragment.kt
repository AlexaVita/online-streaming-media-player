package com.example.aplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplayer.getMedia.MediaUtils
import com.example.aplayer.mediaSessionServ.PlayActivity
import com.example.aplayer.recview.AudioAdapter


class AudiosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_audios, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("FRAGMENT", "ONVIEWCREATED")
        super.onViewCreated(view, savedInstanceState)

        val resolver = requireActivity().contentResolver
        val list = MediaUtils.getAudios(resolver)
/////////////
        val mediaitems = ArrayList<MediaItem>()
        for (audio in list) {
            mediaitems.add(MediaItem.Builder()
                .setMediaId(audio?.uri.toString())
               .build())
        }


        val audioAdapter: AudioAdapter = AudioAdapter(context,list) {
                audio ->
            list.indexOf(audio)

            val intent = Intent(context, PlayActivity::class.java)
            intent.putExtra("uri", audio?.uri.toString())
            startActivity(intent)
        }

        val recView: RecyclerView = view.findViewById(R.id.recView)
        recView.layoutManager= GridLayoutManager(context,2)
        recView.adapter=audioAdapter
    }

    override fun onStart() {
        Log.i("FRAGMENT", "START")
        super.onStart()
    }

    override fun onStop() {
        Log.i("FRAGMENT", "STOP")
        super.onStop()
    }

    companion object {
        @JvmStatic
        fun newInstance() = AudiosFragment()
    }
}