package com.example.crudplayerapp;

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

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>
{
    private Context context;
    private List<Player> listBackup;
    private List<Player> mList;
    private PlayerItemListener mCatItem;

    public PlayerAdapter(Context context)
    {
        this.context = context;
        mList = new ArrayList<>();
        listBackup = new ArrayList<>();
    }

    public List<Player> getBackUp()
    {
        return listBackup;
    }

    public void filterList(List<Player> filterList)
    {
        mList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item_layout, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        Player player = mList.get(position);

        if (player == null)
        {
            return;
        }

        holder.playerImage.setImageResource(player.getSex().equals("male") ? R.drawable.male : R.drawable.female);
        holder.playerName.setText(player.getName());
        holder.hauVe.setChecked(false);
        holder.tienVe.setChecked(false);
        holder.tienDao.setChecked(false);
        holder.btRemove = holder.itemView.findViewById(R.id.deleteBtn);


        for (String p : player.getPosition())
        {
            if (p.equals("hauVe"))
            {
                holder.hauVe.setChecked(true);
            }
            if (p.equals("tienVe"))
            {
                holder.tienVe.setChecked(true);
            }
            if (p.equals("tienDao"))
            {
                holder.tienDao.setChecked(true);
            }
        }

        holder.hauVe.setEnabled(false);
        holder.tienVe.setEnabled(false);
        holder.tienDao.setEnabled(false);

        holder.btRemove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có chắc chắn muốn xóa " + player.getName() + " không?");
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

    @Override
    public int getItemCount()
    {
        if (mList != null)
        {
            return mList.size();
        }
        return 0;
    }

    public void setClickListener(PlayerItemListener mCatItem)
    {
        this.mCatItem = mCatItem;
    }

    public void add(Player player)
    {
        listBackup.add(player);
        mList.add(player);
        notifyDataSetChanged();
    }

    public void remove(int position)
    {
        listBackup.remove(position);
        mList.remove(position);
        notifyDataSetChanged();
    }

    public void update(int pCurrent, Player player)
    {
        listBackup.set(pCurrent, player);
        mList.set(pCurrent, player);
        notifyDataSetChanged();
    }

    public Player getItem(int p)
    {
        return mList.get(p);
    }


    public class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView playerImage;
        private TextView playerName;
        private CheckBox hauVe, tienVe, tienDao;
        private Button btRemove;

        public PlayerViewHolder(@NonNull View view)
        {
            super(view);
            playerImage = view.findViewById(R.id.playerImage);
            playerName = view.findViewById(R.id.playerNameShow);
            hauVe = view.findViewById(R.id.hauVe);
            tienVe = view.findViewById(R.id.tienVe);
            tienDao = view.findViewById(R.id.tienDao);
            view.setOnClickListener(this);
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

    public interface PlayerItemListener
    {
        void onItemClick(View view, int p);
    }
}
