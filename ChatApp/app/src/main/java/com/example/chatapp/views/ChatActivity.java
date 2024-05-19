package com.example.chatapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.chatapp.R;
import com.example.chatapp.adapter.ChatAdapter;
import com.example.chatapp.databinding.ActivityChatBinding;
import com.example.chatapp.model.ChatMessage;
import com.example.chatapp.viewmodel.MyViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity
{
    private ActivityChatBinding binding;
    private MyViewModel viewModel;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    private List<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        viewModel = new ViewModelProvider(this).get(MyViewModel.class);

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        String groupName = getIntent().getStringExtra("GROUP_NAME");
        viewModel.getChatMessageList(groupName).observe(this, chatMessages ->
        {
            this.chatMessages = new ArrayList<>();
            this.chatMessages.addAll(chatMessages);

            adapter = new ChatAdapter(this.chatMessages, getApplicationContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            int lastPosition = chatMessages.size() - 1;
            if (lastPosition > 0)
            {
                recyclerView.smoothScrollToPosition(lastPosition);
            }
        });

        binding.setViewModel(viewModel);
        binding.buttonSendMessage.setOnClickListener(view ->
        {
            String msg = binding.editTextChat.getText().toString();
            viewModel.sendMessage(groupName, msg);
            binding.editTextChat.setText("");
        });
    }
}