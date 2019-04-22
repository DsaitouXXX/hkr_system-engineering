package com.example.a3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a3.model.message;
import com.example.a3.model.trashbin;

import java.util.List;

public class messageAdapter extends BaseAdapter {
    private Context mContext;
    private List<message> mNews;
    private LayoutInflater mInflater;
    //private ImageLoader mImageLoader;
    public messageAdapter(List<message> news, Context context){
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if(convertView==null){
            mHolder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.message_list,null);
            mHolder.mdot= (TextView) convertView.findViewById(R.id.dot);
            mHolder.mnote= (TextView) convertView.findViewById(R.id.note);
            mHolder.mtrashid_m= (TextView) convertView.findViewById(R.id.trashid_m);
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }

        if (mNews.get(position).isFlag()) mHolder.mdot.setText( " ");
            else mHolder.mdot.setText(Html.fromHtml("&#8226;"));

        mHolder.mtrashid_m.setText("Trash Bin "+mNews.get(position).getId());
        String per=mNews.get(position).getPer();
        String note=" ";
        int x=Integer.parseInt(per);
        if (x>80) note=" should ";
        else if (x>=50 && x<80)   note=" can ";
        mHolder.mnote.setText(note+"be cleaned");
        return convertView;
    }

    class ViewHolder{
        private TextView mdot;
        private TextView mtrashid_m;
        private TextView mnote;
    }
}
