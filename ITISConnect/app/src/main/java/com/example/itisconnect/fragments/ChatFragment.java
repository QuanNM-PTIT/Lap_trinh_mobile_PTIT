package com.example.itisconnect.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.itisconnect.R;
import com.example.itisconnect.adapters.ChatAdapter;
import com.example.itisconnect.models.ChatMessage;
import com.example.itisconnect.viewModels.ChatViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment
{
    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageView sendButton;
    private ChatAdapter adapter;
    private List<ChatMessage> chatMessageList;
    private ChatViewModel viewModel;

    public ChatFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        messageEditText = view.findViewById(R.id.editText_chat);
        sendButton = view.findViewById(R.id.button_send_message);

        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        viewModel.getChatMessageList().observe(getViewLifecycleOwner(), chatMessages ->
        {
            chatMessageList = new ArrayList<>();
            chatMessageList.addAll(chatMessages);

            adapter = new ChatAdapter(chatMessageList, getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            int lastPosition = chatMessages.size() - 1;
            if (lastPosition > 0)
            {
                recyclerView.smoothScrollToPosition(lastPosition);
            }
        });

        sendButton.setOnClickListener(view1 ->
        {
            String message = messageEditText.getText().toString().trim();
            viewModel.sendMessage(message);
            messageEditText.setText("");
        });

        return view;
    }
}