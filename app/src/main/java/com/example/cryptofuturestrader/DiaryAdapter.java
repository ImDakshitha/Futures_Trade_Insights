package com.example.cryptofuturestrader;

import android.app.DirectAction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class DiaryAdapter extends FirestoreRecyclerAdapter<Diary, DiaryAdapter.DiaryViewHolder> {
    Context context;

    public DiaryAdapter(@NonNull FirestoreRecyclerOptions<Diary> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DiaryViewHolder holder, int position, @NonNull Diary diary) {
        holder.pairTV.setText(diary.Pair);

        holder.entryTv.setText(diary.EntryPrice);
        holder.exitTV.setText(diary.ExitPrice);
        holder.qtyTV.setText(diary.Qty);
        holder.dateTV.setText(diary.Date);
        holder.timeTV.setText(diary.Time);

        float trade_qty = (Float.parseFloat(diary.Qty)/Float.parseFloat(diary.EntryPrice));

        if(diary.Direction==null){
            holder.directionTV.setText(diary.Direction);
            float pnl = trade_qty*(Float.parseFloat(diary.ExitPrice)-Float.parseFloat(diary.EntryPrice));
            holder.pnlTV.setText(String.valueOf(pnl)+" USDT");
        }else{
            if(diary.Direction.equals("LONG")&&diary.Direction!=null){
                holder.directionTV.setTextColor(Color.parseColor("#A4C639"));
                holder.directionTV.setText(diary.Direction);
                float pnl = trade_qty*(Float.parseFloat(diary.ExitPrice)-Float.parseFloat(diary.EntryPrice));
                if(pnl>0){
                    holder.pnlTV.setTextColor(Color.parseColor("#A4C639"));
                }else{
                    holder.pnlTV.setTextColor(Color.parseColor("#FF0000"));
                }
                holder.pnlTV.setText(String.valueOf(pnl)+" USDT");
            }
            if(diary.Direction.equals("SHORT")&&diary.Direction!=null){
                holder.directionTV.setTextColor(Color.parseColor("#FF0000"));
                holder.directionTV.setText(diary.Direction);
                float pnl = trade_qty*(Float.parseFloat(diary.EntryPrice)-Float.parseFloat(diary.ExitPrice));
                if(pnl>0){
                    holder.pnlTV.setTextColor(Color.parseColor("#A4C639"));
                }else{
                    holder.pnlTV.setTextColor(Color.parseColor("#FF0000"));
                }
                holder.pnlTV.setText(String.valueOf(pnl)+" USDT");
            }
        }
        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,DiaryDetails.class);
            intent.putExtra("pair",diary.Pair);
            intent.putExtra("entryPrice",diary.EntryPrice);
            intent.putExtra("exitPrice",diary.ExitPrice);
            intent.putExtra("qty",diary.Qty);
            intent.putExtra("direction",diary.Direction);
            intent.putExtra("date",diary.Date);
            intent.putExtra("time",diary.Time);
            String docId =this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_diary_item,parent,false);
        return  new DiaryViewHolder(view);
    }

    class DiaryViewHolder extends RecyclerView.ViewHolder{
        TextView pairTV,dateTV,timeTV,entryTv,exitTV,qtyTV,directionTV,pnlTV;

        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            pairTV = itemView.findViewById(R.id.tv_pair_diary);
            dateTV = itemView.findViewById(R.id.tv_trade_date_diary);
            entryTv = itemView.findViewById(R.id.tv_entry_price_diary);
            exitTV = itemView.findViewById(R.id.tv_exit_price_diary);
            qtyTV = itemView.findViewById(R.id.tv_trade_qty_diary);
            timeTV = itemView.findViewById(R.id.tv_trade_time_diary);
            directionTV=itemView.findViewById(R.id.tv_buy_sell_diary);
            pnlTV=itemView.findViewById(R.id.tv_pnl_diary);
        }


    }
}
