package com.example.thuchanhthemsach;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>
{

    private Context context;
    private List<News> listBackup;
    private List<News> mList;
    private static NewsItemListener mCatItem;

    public NewsAdapter(Context context)
    {
        this.context = context;
        mList = new ArrayList<>();
        listBackup = new ArrayList<>();
    }

    public List<News> getBackUp()
    {
        return listBackup;
    }

    public void filterList(List<News> filterList)
    {
        mList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        News news = mList.get(position);

        if (news == null)
        {
            return;
        }

        holder.author.setText(news.getAuthor());
        holder.title.setText(news.getTitle());
        holder.timeRelease.setText(news.getTimeRelease());


        for (String category : news.getCategories())
        {
            if (category.equals("phePhan"))
            {
                holder.phePhan.setChecked(true);
            }
            if (category.equals("suThat"))
            {
                holder.SuThat.setChecked(true);
            }
            if (category.equals("chamBiem"))
            {
                holder.chamBiem.setChecked(true);
            }
        }

        holder.chamBiem.setEnabled(false);
        holder.SuThat.setEnabled(false);
        holder.phePhan.setEnabled(false);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có chắc chắn muốn xóa không?");
                builder.setIcon(R.drawable.icon_remove);
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        remove(position);
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void remove(int position)
    {
        listBackup.remove(position);
        mList.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount()
    {
        if (mList != null)
        {
            return mList.size();
        }
        return 0;
    }

    public News getItem(int position)
    {
        return mList.get(position);
    }

    public void setClickListener(NewsItemListener mCatItem)
    {
        this.mCatItem = mCatItem;
    }

    public void add(News news)
    {
        listBackup.add(news);
        mList.add(news);
        notifyDataSetChanged();
    }

    public void update(int pCurrent, News news)
    {
        listBackup.set(pCurrent, news);
        mList.set(pCurrent, news);
        notifyDataSetChanged();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView imgNews;
        private TextView author, title, timeRelease;
        private CheckBox phePhan, SuThat, chamBiem;

        private Button deleteBtn;

        public NewsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imgNews = itemView.findViewById(R.id.newsImage);
            author = itemView.findViewById(R.id.authorNameCard);
            title = itemView.findViewById(R.id.newsNameCard);
            timeRelease = itemView.findViewById(R.id.newsTimeCard);
            phePhan = itemView.findViewById(R.id.phePhanCard);
            SuThat = itemView.findViewById(R.id.suThatCard);
            chamBiem = itemView.findViewById(R.id.chamBiemCard);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }

        @Override
        public void onClick(View v)
        {
            if (mCatItem != null)
            {
                mCatItem.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface NewsItemListener
    {
        void onItemClick(View view, int p);
    }
}
