package com.boris.expert.adminmagic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boris.expert.adminmagic.R
import com.boris.expert.adminmagic.model.Message
import com.boris.expert.adminmagic.utils.Constants
import com.google.android.material.textview.MaterialTextView
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(val context: Context, val chatList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageBody: MaterialTextView

        init {
            messageBody = itemView.findViewById(R.id.message_body)
        }
    }

    class ItemViewHolder1(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageBody: MaterialTextView

        init {
            messageBody = itemView.findViewById(R.id.message_body)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.sender_message_item_row,
                parent,
                false
            )
            return ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.receiver_message_item_row,
                parent,
                false
            )
            return ItemViewHolder1(view)
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = chatList[position]
        when (holder.itemViewType) {
            0 -> {
                val senderHolder = holder as ItemViewHolder
                senderHolder.messageBody.text = item.message
            }
            1 -> {
                val receiverHolder = holder as ItemViewHolder1
                receiverHolder.messageBody.text = item.message
            }
            else -> {

            }
        }


    }

    override fun getItemCount(): Int {
        return chatList.size
    }


    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].userId == Constants.firebaseUserId) {
            1
        } else {
            0
        }
    }

}