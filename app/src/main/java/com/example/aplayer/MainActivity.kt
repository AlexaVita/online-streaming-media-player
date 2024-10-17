package com.example.aplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.aplayer.databinding.ActivityMainBinding
import com.example.aplayer.mediaSessionServ.PlayActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : AppCompatActivity() {

    private val fragmentList = listOf(VideosFragment.newInstance(), AudiosFragment.newInstance())
    private val fragmentText = listOf("ВИДЕО", "АУДИО")

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val getContent = registerForActivityResult(GetContentMultiMime()) { uri: Uri? ->

        if (uri!=null) {
            val path: String? = RealPathUtil.getRealPath(this, uri!!)
            Log.i("FILE", path.toString())
            lifecycleScope.launch { uploadVideo(path) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportFragmentManager.setFragmentResultListener("requestUrl", this) { requestKey, bundle ->
            val result = bundle.getString("bundleUrl")

            val intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("uri", result)
            startActivity(intent)
        }
        setContentView(binding.root)


        val viewPagerAdapter = ViewPagerAdapter(this, fragmentList)
        binding.viewPager.adapter = viewPagerAdapter


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_AUDIO
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_VIDEO
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.POST_NOTIFICATIONS
                ),
                1
            );
        }


        binding.uploadButton.setOnClickListener {
            getContent.launch("audio/*;video/*")
        }

        binding.videos.setOnClickListener {
            val popupGetMediaFragment = PopupGetMediaFragment()
            popupGetMediaFragment.show(this.supportFragmentManager, "showPopupMediaFragment")
        }

        TabLayoutMediator(binding.tabL, binding.viewPager) { tab, pos ->
            tab.text = fragmentText[pos]
        }.attach()
    }

    suspend fun uploadVideo(path: String?) {

        binding.uploadButton.isEnabled = false
        binding.uploadButton.isClickable = false
        val file = File(path)
        file.createNewFile()
        val mediaUrl = FileRepository().uploadVideo(file)
        val popupTakeUrlFragment = PopupTakeUrlFragment()

        val mBundle = Bundle()
        mBundle.putString("mediaUrl", mediaUrl)
        popupTakeUrlFragment.arguments = mBundle

        popupTakeUrlFragment.show(this.supportFragmentManager, "showPopupTakeUrlFragment")
        binding.uploadButton.isEnabled = true
        binding.uploadButton.isClickable = true
    }
}