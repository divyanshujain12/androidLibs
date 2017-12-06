package com.androidlib.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidlib.Interfaces.RecyclerViewClick;
import com.androidlib.Models.ChatModel;
import com.locationlib.R;
import com.locationlib.databinding.AdapterChatMyTextviewBinding;
import com.locationlib.databinding.AdapterChatOtherTextviewBinding;


import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 12/6/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private ArrayList<ChatModel> chatModels;
    private Context context;
    private AdapterChatOtherTextviewBinding adapterChatOtherTextviewBinding;
    private AdapterChatMyTextviewBinding adapterChatMyTextviewBinding;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private MyViewHolder(View view) {
            super(view);
        }
    }

    public ChatAdapter(Context context, ArrayList<ChatModel> chatModels, RecyclerViewClick recyclerViewClick) {
        this.chatModels = chatModels;
        this.context = context;

    }

    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_chat_other_textview, parent, false);
                adapterChatOtherTextviewBinding = DataBindingUtil.bind(itemView);
                break;
            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_chat_my_textview, parent, false);
                adapterChatMyTextviewBinding = DataBindingUtil.bind(itemView);
                break;
        }

        return new ChatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatAdapter.MyViewHolder holder, int position) {
        ChatModel chatModel = chatModels.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                adapterChatOtherTextviewBinding.setData(chatModel);
                adapterChatOtherTextviewBinding.executePendingBindings();
                break;
            case 1:
                adapterChatMyTextviewBinding.setData(chatModel);
                adapterChatMyTextviewBinding.executePendingBindings();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel chatModel = chatModels.get(position);
        if (chatModel.getSameSide() == 1)
            return 1;
        else
            return 0;
    }
    public void addItem(ArrayList<ChatModel> chatModels){
        this.chatModels = chatModels;
        //notifyItemInserted(chatModels.size() - 1);
        notifyDataSetChanged();
    }
}
