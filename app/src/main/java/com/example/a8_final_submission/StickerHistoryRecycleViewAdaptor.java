package com.example.a8_final_submission;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.List;

public class StickerHistoryRecycleViewAdaptor extends RecyclerView.Adapter<StickerHistoryRecycleViewAdaptor.ViewHolder>{
    private LayoutInflater layoutInflater;
    private NameClickListener nameClickListener;
    private List<StickerUserInfoRelation> StickerUserInfoRelations;
    Context context;

    StickerHistoryRecycleViewAdaptor(Context context, List<StickerUserInfoRelation> StickerUserInfoRelations) {
        this.layoutInflater = LayoutInflater.from(context);
        this.StickerUserInfoRelations=StickerUserInfoRelations;
        this.context=context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.sticker_history_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Runnable loadImg=()->{
            URL url = null;
            try {
                String stickerId=StickerUserInfoRelations.get(position).getStickerId();
                url = new URL(stickerId);
                String tempStickerId=stickerId;
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.stickerHistoryImageView.setImageBitmap(bmp);
                        viewHolder.stickerHistoryImageView.setContentDescription(tempStickerId);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(loadImg).start();

        viewHolder.senderAndReciverUsernameTextView.setText("Sender : "+StickerUserInfoRelations.get(position).getSenderUsername());
        viewHolder.RecivedDateTextView.setText("Received Date: "+StickerUserInfoRelations.get(position).getStickerRecivedDate());
    }

    @Override
    public int getItemCount() {
        return StickerUserInfoRelations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView stickerHistoryImageView;
        TextView senderAndReciverUsernameTextView;
        TextView RecivedDateTextView;

        ViewHolder(View itemView) {
            super(itemView);
          //  stickerIdTextView = itemView.findViewById(R.id.stickerIdTextView);
            stickerHistoryImageView = itemView.findViewById(R.id.stickerHistoryImageView);
            senderAndReciverUsernameTextView = itemView.findViewById(R.id.senderAndReciverUsernameTextView);
            RecivedDateTextView = itemView.findViewById(R.id.RecivedDateTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (nameClickListener != null){
                nameClickListener.onClickName(view, getAdapterPosition());
            }
        }
    }

    void setClickListener(NameClickListener itemClickListener) {
        this.nameClickListener = itemClickListener;
    }

    public interface NameClickListener {
        void onClickName(View view, int position);
    }



}
