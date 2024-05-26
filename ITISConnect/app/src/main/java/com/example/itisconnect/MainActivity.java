package com.example.itisconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.itisconnect.adapters.MyViewPagerAdapter;
import com.example.itisconnect.fragments.ChatFragment;
import com.example.itisconnect.fragments.UsersFragment;
import com.example.itisconnect.fragments.PostFragment;
import com.example.itisconnect.fragments.ProfileFragment;
import com.example.itisconnect.fragments.TaskFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity
{
    private ViewPager2 viewPager;
    private MyViewPagerAdapter adapter;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new MyViewPagerAdapter(
                getSupportFragmentManager(),
                getLifecycle()
        );

        viewPager = findViewById(R.id.main_view_pager);
        tabLayout = findViewById(R.id.main_tab_layout);

        adapter.addFragment(new PostFragment());
        adapter.addFragment(new TaskFragment());
        adapter.addFragment(new ChatFragment());
        adapter.addFragment(new UsersFragment());
        adapter.addFragment(new ProfileFragment());

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
        {
            switch (position)
            {
                case 0:
                    tab.setIcon(R.drawable.news_icon);
                    break;
                case 1:
                    tab.setIcon(R.drawable.task_icon);
                    break;
                case 2:
                    tab.setIcon(R.drawable.chat_icon);
                    break;
                case 3:
                    tab.setIcon(R.drawable.users_icon);
                    break;
                case 4:
                    tab.setIcon(R.drawable.profile_icon);
                    break;
            }
        }).attach();
    }
}