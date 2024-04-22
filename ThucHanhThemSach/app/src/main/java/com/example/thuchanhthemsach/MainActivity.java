package com.example.thuchanhthemsach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NewsAdapter.NewsItemListener, SearchView.OnQueryTextListener
{

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private EditText newsName, newsAuthor, newsTimeRelease;
    private CheckBox cPhePhan, cSuThat, cChamBiem;
    private Button btAdd, btUpdate;
    private int pCurrent;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            initView();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        adapter = new NewsAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        searchView.setOnQueryTextListener(this);

        btAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    String title = newsName.getText().toString();
                    String author = newsAuthor.getText().toString();
                    String timeRelease = newsTimeRelease.getText().toString();
                    List<String> categories = new ArrayList<>();
                    try
                    {
                        if (title.isEmpty() || author.isEmpty() || timeRelease.isEmpty())
                        {
                            throw new Exception();
                        }
                        if (!timeRelease.matches("^\\d{2}:\\d{2}$"))
                        {
                            Toast.makeText(MainActivity.this, "Thời gian phải có định dạng: hh:mm", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(MainActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (cPhePhan.isChecked())
                    {
                        categories.add("phePhan");
                    }
                    if (cSuThat.isChecked())
                    {
                        categories.add("suThat");
                    }
                    if (cChamBiem.isChecked())
                    {
                        categories.add("chamBiem");
                    }
                    News news = new News(title, author, timeRelease, categories);
                    adapter.add(news);
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    String title = newsName.getText().toString();
                    String author = newsAuthor.getText().toString();
                    String timeRelease = newsTimeRelease.getText().toString();
                    List<String> categories = new ArrayList<>();
                    try
                    {
                        if (title.isEmpty() || author.isEmpty() || timeRelease.isEmpty())
                        {
                            throw new Exception();
                        }
                        // validate time
                        if (!timeRelease.matches("^\\d{2}:\\d{2}$"))
                        {
                            Toast.makeText(MainActivity.this, "Thời gian phải có định dạng: hh:mm", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(MainActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (cPhePhan.isChecked())
                    {
                        categories.add("phePhan");
                    }
                    if (cSuThat.isChecked())
                    {
                        categories.add("suThat");
                    }
                    if (cChamBiem.isChecked())
                    {
                        categories.add("chamBiem");
                    }
                    News news = new News(title, author, timeRelease, categories);
                    adapter.update(pCurrent, news);
                    btAdd.setEnabled(true);
                    btUpdate.setEnabled(false);
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initView()
    {
        this.recyclerView = findViewById(R.id.recyclerView);
        this.newsName = findViewById(R.id.newsName);
        this.newsAuthor = findViewById(R.id.newsAuthor);
        this.newsTimeRelease = findViewById(R.id.newsTime);
        this.cPhePhan = findViewById(R.id.phePhan);
        this.cSuThat = findViewById(R.id.suThat);
        this.cChamBiem = findViewById(R.id.chamBiem);
        this.btAdd = findViewById(R.id.buttonAdd);
        this.btUpdate = findViewById(R.id.buttonUpdate);
//        searchView = findViewById(R.id.searchView);
//        newsTimeRelease.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                Calendar c = Calendar.getInstance();
//                int hour = c.get(Calendar.HOUR_OF_DAY);
//                int minute = c.get(Calendar.MINUTE);
//
//                // Tạo đối tượng TimePickerDialog
//                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
//                        new TimePickerDialog.OnTimeSetListener()
//                        {
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
//                            {
//                                String time = hourOfDay + ":" + minute;
//                                newsTimeRelease.setText(time);
//                            }
//                        }, hour, minute, true);
//                timePickerDialog.show();
//            }
//        });
    }

    private void filter(String newText)
    {
        List<News> filterList = new ArrayList<>();
        for (News i : adapter.getBackUp())
        {
            if (i.getTitle().toLowerCase().contains(newText.toLowerCase()))
            {
                filterList.add(i);
            }
        }
        if (filterList.isEmpty())
        {
            Toast.makeText(this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
        }
        else
        {
            adapter.filterList(filterList);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        filter(newText);
        return false;
    }

    @Override
    public void onItemClick(View view, int p)
    {
        pCurrent = p;
        News news = adapter.getItem(p);
        newsName.setText(news.getTitle());
        newsAuthor.setText(news.getAuthor());
        newsTimeRelease.setText(news.getTimeRelease());
        cPhePhan.setChecked(false);
        cSuThat.setChecked(false);
        cChamBiem.setChecked(false);
        btUpdate.setEnabled(true);
        btAdd.setEnabled(false);
        for (String category : news.getCategories())
        {
            if (category.equals("phePhan"))
            {
                cPhePhan.setChecked(true);
            }
            if (category.equals("suThat"))
            {
                cSuThat.setChecked(true);
            }
            if (category.equals("chamBiem"))
            {
                cChamBiem.setChecked(true);
            }
        }
    }
}