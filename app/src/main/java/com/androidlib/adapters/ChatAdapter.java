package com.androidlib.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidlib.CustomFontViews.CustomTextviewRegular;
import com.androidlib.Interfaces.RecyclerViewClick;
import com.androidlib.Models.ChatModel;
import com.locationlib.R;

import java.util.ArrayList;

/**
 * Created by divyanshu.jain on 12/6/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChatModel> chatModels;
    private Context context;

    private class MyChatViewHolder extends RecyclerView.ViewHolder {
        private CustomTextviewRegular myChatTV;

        private MyChatViewHolder(View itemView) {
            super(itemView);
            myChatTV = (CustomTextviewRegular) itemView.findViewById(R.id.myChatTV);
        }
    }

    private class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private CustomTextviewRegular otherChatTV;

        private OtherChatViewHolder(View view) {
            super(view);
            otherChatTV = (CustomTextviewRegular) itemView.findViewById(R.id.otherChatTV);
        }
    }

    public ChatAdapter(Context context, ArrayList<ChatModel> chatModels, RecyclerViewClick recyclerViewClick) {
        this.chatModels = chatModels;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_chat_other_textview, parent, false);
                return new OtherChatViewHolder(itemView);

            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_chat_my_textview, parent, false);
                return new MyChatViewHolder(itemView);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ChatModel chatModel = chatModels.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                ((OtherChatViewHolder) holder).otherChatTV.setText(chatModel.getContent());

                break;
            case 1:
                ((MyChatViewHolder) holder).myChatTV.setText(chatModel.getContent());
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

    public void addItem(ChatModel chatModel) {
        this.chatModels.add(chatModel);
        notifyItemInserted(chatModels.size() - 1);
    }
}
