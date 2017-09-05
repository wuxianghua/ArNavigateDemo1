package com.example.administrator.arnavigatedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.arnavigatedemo.R;
import com.example.administrator.arnavigatedemo.model.ServiceMapInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2/002.
 */

public class LoadBeaconAdapter extends BaseAdapter{

    private List<ServiceMapInfo> mData;
    private Context mContext;

    public LoadBeaconAdapter(Context context, List<ServiceMapInfo> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ServiceMapInfo getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_beaconinfo_service,viewGroup,false);
            holder = new ViewHolder(view);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.updateTime.setText(getItem(i).date_Time);
        holder.mapName.setText(getItem(i).mapName);
        holder.beaconNumber.setText(getItem(i).count+"");
        holder.isReviewed.setText(getItem(i).auditState);
        return view;
    }

    private class ViewHolder {
        private TextView updateTime,mapName,beaconNumber,isReviewed;

        public ViewHolder(View convertView) {
            updateTime = convertView.findViewById(R.id.update_date);
            mapName = convertView.findViewById(R.id.map_name);
            beaconNumber = convertView.findViewById(R.id.beacon_number);
            isReviewed = convertView.findViewById(R.id.is_reviewed);
            convertView.setTag(this);
        }
    }
}
