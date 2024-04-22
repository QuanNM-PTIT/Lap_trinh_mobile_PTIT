package com.example.crudplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PlayerAdapter.PlayerItemListener, SearchView.OnQueryTextListener
{
    private RecyclerView recyclerView;
    private PlayerAdapter adapter;
    private EditText pName, birthDate;
    private RadioGroup radioGroup;
    private RadioButton malePlayer, femalePlayer;
    private CheckBox cTienVe, cHauVe, cTienDao;
    private Button btAdd, btUpdate;
    private int pCurrent;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        adapter = new PlayerAdapter(this);
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
//                Player player = new Player();
                try
                {
                    String name = pName.getText().toString();
                    Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (name.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Tên không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String time = birthDate.getText().toString();
                if (!time.matches("^\\d{2}-\\d{2}-\\d{4}$"))
                {
                    Toast.makeText(MainActivity.this, "Ngày tháng năm sinh phải có định dạng: dd-mm-yyyy", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (time.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Hãy nhập ngày tháng năm sinh!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String sex = malePlayer.isChecked() ? "male" : "female";
                List<String> positions = new ArrayList<>();
                if (cTienVe.isChecked()) positions.add("tienVe");
                if (cHauVe.isChecked()) positions.add("hauVe");
                if (cTienDao.isChecked()) positions.add("tienDao");
                if (positions.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Hãy chọn vị trí!", Toast.LENGTH_SHORT).show();
                    return;
                }
                player.setName(name);
                player.setDate(time);
                player.setSex(sex);
                player.setPosition(positions);
                adapter.add(player);
            }
                Toast.makeText(MainActivity.this, "Chức năng này đang được phát triển", Toast.LENGTH_SHORT).show();
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Player player = new Player();
                String name = pName.getText().toString();
                if (name.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Tên không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String time = birthDate.getText().toString();
                if (!time.matches("^\\d{2}-\\d{2}-\\d{4}$"))
                {
                    Toast.makeText(MainActivity.this, "Ngày tháng năm sinh phải có định dạng: dd-mm-yyyy", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (time.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Hãy nhập ngày tháng năm sinh!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String sex = malePlayer.isChecked() ? "male" : "female";
                List<String> positions = new ArrayList<>();
                if (cTienVe.isChecked()) positions.add("tienVe");
                if (cHauVe.isChecked()) positions.add("hauVe");
                if (cTienDao.isChecked()) positions.add("tienDao");
                if (positions.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Hãy chọn vị trí!", Toast.LENGTH_SHORT).show();
                    return;
                }
                player.setName(name);
                player.setDate(time);
                player.setSex(sex);
                player.setPosition(positions);
                adapter.update(pCurrent, player);
                btAdd.setEnabled(true);
                btUpdate.setEnabled(false);
            }
        });

    }


    private void initView()
    {
        recyclerView = findViewById(R.id.recyclerView);
        pName = findViewById(R.id.playerName);
        birthDate = findViewById(R.id.birthDate);
        birthDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Calendar c = Calendar.getInstance();
                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                int d = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        birthDate.setText(String.format("%02d-%02d-%d", dayOfMonth, month + 1, year));
                    }
                }, y, m, d);
                dialog.show();
            }
        });
        radioGroup = findViewById(R.id.sex);
        malePlayer = findViewById(R.id.male);
        femalePlayer = findViewById(R.id.female);
        cTienVe = findViewById(R.id.tienVe);
        cTienDao = findViewById(R.id.tienDao);
        cHauVe = findViewById(R.id.hauVe);
        btAdd = findViewById(R.id.buttonAdd);
        btUpdate = findViewById(R.id.buttonUpdate);
        btUpdate.setEnabled(false);
        searchView = findViewById(R.id.searchView);

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

    private void filter(String newText)
    {
        List<Player> filterList = new ArrayList<>();
        for (Player i : adapter.getBackUp())
        {
            if (i.getName().toLowerCase().contains(newText.toLowerCase()))
            {
                filterList.add(i);
            }
        }
        if (filterList.isEmpty())
        {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            adapter.filterList(filterList);
        }
    }

    @Override
    public void onItemClick(View view, int p)
    {
        btAdd.setEnabled(false);
        btUpdate.setEnabled(true);
        pCurrent = p;
        Player player = adapter.getItem(p);
        pName.setText(player.getName());
        birthDate.setText(player.getDate());
        malePlayer.setChecked(player.getSex().equals("nam") ? true : false);
        femalePlayer.setChecked(player.getSex().equals("nu") ? true : false);
        cHauVe.setChecked(player.getPosition().contains("hauve") ? true : false);
        cTienVe.setChecked(player.getPosition().contains("tienve") ? true : false);
        cTienDao.setChecked(player.getPosition().contains("tiendao") ? true : false);
    }
}