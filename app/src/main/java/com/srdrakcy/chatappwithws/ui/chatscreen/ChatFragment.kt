package com.srdrakcy.chatappwithws.ui.chatscreen

import android.content.ContentValues.TAG
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.example.chatappwithws.databinding.FragmentChatBinding
import com.srdrakcy.chatappwithws.data.WebSocketResource
import com.srdrakcy.chatappwithws.data.WsClient
import com.srdrakcy.chatappwithws.util.Constant.TYPE_MESSAGE_RECEIVER
import com.srdrakcy.chatappwithws.util.Constant.TYPE_MESSAGE_SENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {
    @Inject
    lateinit var viewModel: ChatViewModel

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var request: Request

    private lateinit var webSocketService: WsClient
    private lateinit var binding: FragmentChatBinding
    private val adapter: ChatAdapter = ChatAdapter(arrayListOf())
    private var webSocket: WebSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webSocketService = WsClient(viewModel, okHttpClient, request)
        webSocketService.connectWebSocket()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)
        binding.recyclerView.adapter = adapter
        socketStatus()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMessage()
        sendMessage()
    }

    private fun socketStatus() {
        viewModel.viewModelScope.launch(Dispatchers.IO) {

            viewModel.eventFlow.collect {
                when (it) {
                    is WebSocketResource.Open -> {
                        Log.e(TAG, "socketStatus: Open-> ${it.response}")
                    }

                    is WebSocketResource.Closed -> {
                        Log.e(TAG, "socketStatus: Closed-> ${it.reason}")
                    }

                    is WebSocketResource.Closing -> {
                        Log.e(TAG, "socketStatus: Closing-> ${it.reason}")

                    }

                    is WebSocketResource.Failure -> {
                        Log.e(TAG, "socketStatus: Failure-> ${it.response}")

                    }

                }
            }
        }
    }

    private fun sendSocketMessage() {
        val message = binding.editSendText.text.toString()
        val deviceId: String =
            Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)
        webSocketService.sendMessage("$deviceId $message")
        goToDown()
    }

    private fun getMessage() {
        viewModel.messages.observe(viewLifecycleOwner) {
            val deviceId: String =
                Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)
            val updatedList = ArrayList(adapter.chatData)
            val firstPart = it.substring(0, 16)
            val secondPart = it.substring(16)
            if (deviceId == firstPart) {
                updatedList.add(
                    ChatData(
                        message = secondPart.toString(),
                        messageType = TYPE_MESSAGE_SENT,
                        messageId = "Sender"
                    )
                )
                adapter.update(updatedList)
                goToDown()
            } else {
                updatedList.add(
                    ChatData(
                        message = secondPart.toString(),
                        messageType = TYPE_MESSAGE_RECEIVER,
                        messageId = "Receiver"
                    )
                )
                adapter.update(updatedList)
                goToDown()
            }


        }
    }

    private fun goToDown() {
        try {
            binding.recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
        } catch (_: Exception) {

        }
    }

    private fun sendMessage() {
        binding.editSendText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendSocketMessage()
                binding.editSendText.text?.clear()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webSocket?.close(1000,"onDestroyView")
    }
}