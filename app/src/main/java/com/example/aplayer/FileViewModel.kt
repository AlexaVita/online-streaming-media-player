package com.example.aplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class FileViewModel(private val repository: FileRepository= FileRepository()) : ViewModel()
{
    fun uploadVideo(file: File) {
        viewModelScope.launch {
            repository.uploadVideo(file)
        }
    }
}