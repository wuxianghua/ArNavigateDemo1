package com.example.administrator.arnavigatedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.arnavigatedemo.R;
import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import com.example.administrator.arnavigatedemo.model.MapInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/10/010.
 */

public class LoadMapsAdapter extends BaseAdapter {
    private List<MapInfo> mData;
    private Context mContext;

    public LoadMapsAdapter(Context context,List<MapInfo> data) {
        mContext = context;
        mData = data;
    }

    public void setMapData(List<MapInfo> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public MapInfo getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LoadMapsAdapter.ViewHolder holder;
        if(view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_maps_info,viewGroup,false);
            holder = new LoadMapsAdapter.ViewHolder(view);
        }else {
            holder = (LoadMapsAdapter.ViewHolder) view.getTag();
        }
        holder.mapId.setText(getItem(i).mapId+"");
        holder.mapName.setText(getItem(i).mapName);
        return view;
    }

    private class ViewHolder {
        private TextView mapId,mapName;

        public ViewHolder(View convertView) {
            mapId = convertView.findViewById(R.id.map_id);
            mapName = convertView.findViewById(R.id.map_name);
            convertView.setTag(this);
        }
    }
}
