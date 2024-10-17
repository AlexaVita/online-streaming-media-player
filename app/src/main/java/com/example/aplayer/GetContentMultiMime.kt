package com.example.aplayer

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts

class GetContentMultiMime: ActivityResultContracts.GetContent() {
    override fun createIntent(context: Context, input:String): Intent {
        val inputArray = input.split(";").toTypedArray()
        val intent = super.createIntent(context, "*/*")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, inputArray)
        return intent
    }
}