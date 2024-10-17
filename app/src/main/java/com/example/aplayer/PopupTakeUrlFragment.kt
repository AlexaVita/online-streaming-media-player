package com.example.aplayer

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

class PopupTakeUrlFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.dialog_bg)
        return inflater.inflate(R.layout.fragment_popup_take_url, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.copyUrl)
        val url = view.findViewById<TextView>(R.id.urlView)
        val bundle = arguments
        val message = bundle!!.getString("mediaUrl")
        url.text = message

        button.setOnClickListener() {
            val clipboardManager =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("copiedUrl", url.text.toString())
            clipboardManager.setPrimaryClip(clip)

            Toast.makeText(context, "Скопировано", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}