package com.srdrakcy.chatappwithws.ui.chatscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srdrakcy.chatappwithws.data.WebSocketResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel  @Inject constructor() :ViewModel(){
         private val eventChannel = Channel<WebSocketResource>()
         val eventFlow = eventChannel.receiveAsFlow()

             private val _messages = MutableLiveData<String>()
             val messages: LiveData<String> = _messages




    fun triggerEvent(boardType: WebSocketResource) = viewModelScope.launch {
        eventChannel.send(boardType)
    }

            fun addMessage(message: String) {
                _messages.postValue(message)
            }




}