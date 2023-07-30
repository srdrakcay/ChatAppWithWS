package com.srdrakcy.chatappwithws.ui.chatscreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappwithws.databinding.ItemReceiverBinding
import com.example.chatappwithws.databinding.ItemSendBinding
import com.srdrakcy.chatappwithws.util.Constant.TYPE_MESSAGE_RECEIVER
import com.srdrakcy.chatappwithws.util.Constant.TYPE_MESSAGE_SENT

class ChatAdapter(
    var chatData: ArrayList<ChatData>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class SendViewHolder(private val viewBinding: ItemSendBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {


        fun bindItems(chatData: ChatData) {
            viewBinding.txtSender.text=chatData.message
        }

    }

    class ReciverViewHolder(private val _viewBinding: ItemReceiverBinding) :
        RecyclerView.ViewHolder(_viewBinding.root) {
        fun bindItems(chatData: ChatData) {
            _viewBinding.txtReceiver.text=chatData.message


        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_MESSAGE_SENT -> {
                val binding =
                    ItemSendBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return SendViewHolder(
                    binding
                )
            }
            else -> {
                val binding =
                    ItemReceiverBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ReciverViewHolder(
                    binding
                )
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.clearAnimation()

        val currentMessage = chatData[position].messageType
        if (currentMessage == 0) {
            (holder as SendViewHolder).bindItems(chatData[position])
        } else {
            (holder as ReciverViewHolder).bindItems(chatData[position])
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (chatData[position].messageType == 1) {
            TYPE_MESSAGE_RECEIVER
        } else {
            TYPE_MESSAGE_SENT
        }
    }

    override fun getItemCount(): Int {
        return chatData.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun update(list: ArrayList<ChatData>) {
        this.chatData = list
        notifyDataSetChanged()
    }
}





