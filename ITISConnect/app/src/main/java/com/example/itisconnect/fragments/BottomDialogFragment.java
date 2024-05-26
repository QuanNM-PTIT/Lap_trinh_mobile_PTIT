package com.example.itisconnect.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.itisconnect.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BottomDialogFragment extends BottomSheetDialogFragment
{
    private TextView title;
    private Button button;
    private String content;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.bottom_dialog, container, false);

        title = view.findViewById(R.id.notification_message_qr_code);
        button = view.findViewById(R.id.cancel_button_qr_code);

        title.setText(content);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });

        return view;
    }

    public void setContent(String data)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                content = data;
            }
        });
    }
}
