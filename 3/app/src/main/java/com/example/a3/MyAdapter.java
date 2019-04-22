package com.example.a3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a3.model.trashbin;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private List<trashbin> mNews;
    private LayoutInflater mInflater;
    //private ImageLoader mImageLoader;
    public MyAdapter(List<trashbin> news, Context context){
        mContext=context;
        mNews=news;
        mInflater=LayoutInflater.from(mContext);
        //初始化ImageLoader，确保缓存只有一个
       // mImageLoader=new ImageLoader();
    }
    @Override
    public int getCount() {
        return mNews.size();
    }

    @Override
    public Object getItem(int position) {
        return mNews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if(convertView==null){
            mHolder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.item_layout,null);
            mHolder.mIconImageView= (ImageView) convertView.findViewById(R.id.item_iv_icon);
            mHolder.mNameTextView= (TextView) convertView.findViewById(R.id.item_tv_name);
            mHolder.mLocationTextView= (TextView) convertView.findViewById(R.id.item_tv_location);
            mHolder.mpertView= (TextView) convertView.findViewById(R.id.item_tv_per);
            mHolder.mIdTextView= (TextView) convertView.findViewById(R.id.trash_id);
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }
        String per=mNews.get(position).getPer();
        int x=Integer.parseInt(per);
        if (x>80) mHolder.mIconImageView.setImageResource(R.drawable.icons_levelfull_32);
        else if (x>=50 && x<80)  mHolder.mIconImageView.setImageResource(R.drawable.icons_levelcomm_32);
        else        mHolder.mIconImageView.setImageResource(R.drawable.icons_levellow_32);
       // mImageLoader.displayImageByAsyncTask(mHolder.mIconImageView,mNews.get(position).getmImageUrl());
        mHolder.mNameTextView.setText(mNews.get(position).getName());
        mHolder.mLocationTextView.setText(mNews.get(position).getLocation());
        mHolder.mIdTextView.setText(mNews.get(position).getId());
        mHolder.mpertView.setText(per+"%");
        //为ImageView设置TAG为对应的url,防止图片加载错位
     //   mHolder.mIconImageView.setTag(mNews.get(position).getmImageUrl());
        return convertView;
    }

    class ViewHolder{
        public TextView mNameTextView;
        public TextView mLocationTextView;
        public TextView mpertView;
        public ImageView mIconImageView;
        public TextView mIdTextView;
    }
}
