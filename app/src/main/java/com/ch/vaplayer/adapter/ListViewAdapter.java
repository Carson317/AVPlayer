package com.ch.vaplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ch.vaplayer.R;
import com.ch.vaplayer.media.LoadedImage;
import com.ch.vaplayer.media.Video;
import com.ch.vaplayer.utils.VALogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heng.cao on 9/5/16.
 */
public class ListViewAdapter extends BaseAdapter{

    public static final String TAG = "ListViewAdapter";
    public Context mContext;
    private List<Video> mListVideos;
    private LayoutInflater mInflate;
    public ArrayList<LoadedImage> photos = new ArrayList<LoadedImage>();



    public ListViewAdapter(Context context, List<Video> listVideos){
        mContext = context;
        mInflate = LayoutInflater.from(context);
        mListVideos = listVideos;
    }
    @Override
    public int getCount() {
        VALogger.Debug("photosSize", photos.size());
        VALogger.Debug("mListVideosSize", mListVideos.size());
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public void addPhoto(LoadedImage image){
        photos.add(image);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null ){

            convertView = mInflate.inflate(R.layout.item_media , null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.video_img);
            holder.mMediaTitle = (TextView) convertView.findViewById(R.id.video_name);
            holder.mMediaTime = (TextView) convertView.findViewById(R.id.video_time);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Bind the data efficiently with the holder.
        holder.mMediaTitle.setText(mListVideos.get(position).getTitle());

        long min = mListVideos.get(position).getDuration() /1000 / 60;
        long sec = mListVideos.get(position).getDuration() /1000 % 60;
        holder.mMediaTime.setText(sec+"s/"+min+"m");
        holder.imageView.setImageBitmap(photos.get(position).getBitmap());

        return convertView;
    }

    static class ViewHolder{
        ImageView imageView;
        TextView mMediaTitle;
        TextView mMediaTime;
    }
}
