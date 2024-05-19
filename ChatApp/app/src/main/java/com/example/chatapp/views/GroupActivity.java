package com.example.chatapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.adapter.GroupAdapter;
import com.example.chatapp.databinding.ActivityGroupBinding;
import com.example.chatapp.model.ChatGroup;
import com.example.chatapp.viewmodel.MyViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity
{
    private ArrayList<ChatGroup> groupList;
    private RecyclerView recyclerView;
    private GroupAdapter groupAdapter;
    private ActivityGroupBinding binding;
    private MyViewModel myViewModel;

    private Dialog chatGroupDialog;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_group);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myViewModel.getChatGroupList().observe(this, new Observer<List<ChatGroup>>()
        {
            @Override
            public void onChanged(List<ChatGroup> chatGroups)
            {
                groupList = new ArrayList<>();
                groupList.addAll(chatGroups);
                groupAdapter = new GroupAdapter(groupList);
                recyclerView.setAdapter(groupAdapter);
                groupAdapter.notifyDataSetChanged();
            }
        });

        fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showDialog();
            }
        });
    }

    public void showDialog()
    {
        chatGroupDialog = new Dialog(this);
        chatGroupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);

        chatGroupDialog.setContentView(view);
        chatGroupDialog.show();

        Button submit = view.findViewById(R.id.createGroupChatButton);
        EditText chatGroupName = view.findViewById(R.id.editTextTextPersonName);

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String groupName = chatGroupName.getText().toString();
                Toast.makeText(GroupActivity.this, "Your group name: " + groupName, Toast.LENGTH_SHORT).show();
                myViewModel.addChatGroup(groupName);
                chatGroupDialog.dismiss();
            }
        });

    }
}