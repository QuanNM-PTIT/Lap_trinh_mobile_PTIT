package com.example.itisconnect.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.itisconnect.R;
import com.example.itisconnect.views.EditUserActivity;
import com.example.itisconnect.views.EventActivity;
import com.example.itisconnect.views.LoginActivity;
import com.example.itisconnect.views.QrCodeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment
{
    private TextView userName, userEmail, userPosition, userDepartment;
    private CircleImageView userAvatar;
    private ImageView editProfileButton;
    private Button logoutButton, scanButton, eventsButton;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;

    public ProfileFragment()
    {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = view.findViewById(R.id.profile_fullname);
        userEmail = view.findViewById(R.id.profile_email);
        userPosition = view.findViewById(R.id.profile_position);
        userDepartment = view.findViewById(R.id.profile_department);

        userAvatar = view.findViewById(R.id.profile_avatar);
        editProfileButton = view.findViewById(R.id.edit_icon_profile);

        logoutButton = view.findViewById(R.id.logout_button);
        scanButton = view.findViewById(R.id.scan_qr_button);
        eventsButton = view.findViewById(R.id.events_button);

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.collection("Users").whereEqualTo("email", currentUser.getEmail()).get()
                .addOnSuccessListener(documentSnapshot ->
                {
                    if (!documentSnapshot.isEmpty())
                    {
                        userName.setText(documentSnapshot.getDocuments().get(0).getString("fullName"));
                        userEmail.setText(documentSnapshot.getDocuments().get(0).getString("email"));
                        userPosition.setText(documentSnapshot.getDocuments().get(0).getString("position"));
                        userDepartment.setText(documentSnapshot.getDocuments().get(0).getString("department"));

                        Glide.with(getContext()).load(documentSnapshot.getDocuments().get(0).getString("avatar")).into(userAvatar);
                    }
                });

        logoutButton.setOnClickListener(v ->
        {
            fAuth.signOut();
            ((ViewGroup) view).removeAllViews();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        scanButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(getActivity(), QrCodeActivity.class);
            startActivity(intent);
        });

        editProfileButton.setOnClickListener(v ->
        {
            db.collection("Users").whereEqualTo("email", currentUser.getEmail()).get()
                    .addOnSuccessListener(documentSnapshot ->
                    {
                        if (!documentSnapshot.isEmpty())
                        {
                            String imageUrl = documentSnapshot.getDocuments().get(0).getString("avatar");
                            Intent intent = new Intent(getActivity(), EditUserActivity.class);
                            intent.putExtra("email", currentUser.getEmail());
                            intent.putExtra("avatar", imageUrl);
                            startActivity(intent);
                        }
                    });

        });

        eventsButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(getActivity(), EventActivity.class);
            startActivity(intent);
        });

        return view;
    }
}