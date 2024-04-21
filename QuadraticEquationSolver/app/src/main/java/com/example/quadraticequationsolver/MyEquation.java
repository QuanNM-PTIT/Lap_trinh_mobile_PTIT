package com.example.quadraticequationsolver;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.quadraticequationsolver.databinding.ActivityMainBinding;

public class MyEquation extends BaseObservable
{
    private String a;
    private String b;
    private String c;
    private ActivityMainBinding binding;

    public MyEquation(ActivityMainBinding binding)
    {
        this.binding = binding;
    }

    public MyEquation() {}

    @Bindable
    public String getA()
    {
        return a;
    }

    public void setA(String a)
    {
        this.a = a;
    }

    @Bindable
    public String getB()
    {
        return b;
    }

    public void setB(String b)
    {
        this.b = b;
    }

    @Bindable
    public String getC()
    {
        return c;
    }

    public void setC(String c)
    {
        this.c = c;
    }

    public void solve(View view)
    {
        double a = Integer.parseInt(getA());
        double b = Integer.parseInt(getB());
        double c = Integer.parseInt(getC());
        double d = b * b - 4 * a * c;

        double x1, x2;
        if (d > 0)
        {
            x1 = (-b + Math.sqrt(d)) / (2 * a);
            x2 = (-b - Math.sqrt(d)) / (2 * a);
            binding.textView.setText("x1 = " + x1 + ", x2 = " + x2);
        }
        else if (d == 0)
        {
            x1 = -b / (2 * a);
            binding.textView.setText("x = " + x1);
        }
        else
        {
            binding.textView.setText("No real roots!");
        }
    }
}
