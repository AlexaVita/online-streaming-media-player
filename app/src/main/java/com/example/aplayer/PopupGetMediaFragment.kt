package com.example.aplayer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult

class PopupGetMediaFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        return inflater.inflate(R.layout.fragment_popup_get_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<Button>(R.id.popupMedia)
        val url = view.findViewById<EditText>(R.id.popupUrl)
        button.setOnClickListener() {
            val urlValue =
                "http://192.168.1.101:8080/media/"+url.text.toString()
            Log.i("FRAGMENT", urlValue)

            if (!url.text.toString().isNullOrEmpty()) {
                setFragmentResult("requestUrl", bundleOf("bundleUrl" to urlValue))
                Toast.makeText(context, "Подключение...", Toast.LENGTH_LONG).show()
                dismiss()
            } else {
                Toast.makeText(context, "Введите код видео", Toast.LENGTH_SHORT).show()
            }
        }

    }
}