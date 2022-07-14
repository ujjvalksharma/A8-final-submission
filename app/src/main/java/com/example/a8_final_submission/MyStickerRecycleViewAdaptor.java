package com.example.a8_final_submission;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.List;

public class MyStickerRecycleViewAdaptor extends RecyclerView.Adapter<MyStickerRecycleViewAdaptor.ViewHolder>{

        private LayoutInflater layoutInflater;
        private NameClickListener nameClickListener;
        private List<String> stickerIds;
        Context context;
        UserInfo userInfo;
    MyStickerRecycleViewAdaptor(Context context, List<String> stickerIds,UserInfo userInfo) {
        this.layoutInflater = LayoutInflater.from(context);
        this.stickerIds=stickerIds;
        this.context=context;
        this.userInfo=userInfo;
    }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.my_sticker_row, parent, false));
    }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder,  int position) {
            Runnable loadImg=()->{
                URL url = null;
                try {
                    url = new URL(stickerIds.get(position));
                    String tempStickerId=stickerIds.get(position);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.myStickerImgView.setImageBitmap(bmp);
                            viewHolder.myStickerImgView.setContentDescription(tempStickerId);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            new Thread(loadImg).start();

    }

        @Override
        public int getItemCount() {
        return stickerIds.size();
    }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView myStickerImgView;
            ViewHolder(View itemView) {
                super(itemView);
                myStickerImgView = itemView.findViewById(R.id.myStickerImgView);
                itemView.setOnClickListener(this);


                myStickerImgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stickerId=myStickerImgView.getContentDescription().toString();
                        if(!TextUtils.isEmpty(stickerId)) {
                            Intent intent = new Intent(context, SendStickerActivity.class);
                            intent.putExtra("stickerId", stickerId);
                            intent.putExtra("UserInfo", userInfo);
                            context.startActivity(intent);
                        }else{
                            Toast.makeText(context,"Sticker id is empty!",Toast.LENGTH_SHORT);
                        }

                    }
                });

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
