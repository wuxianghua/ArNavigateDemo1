package com.example.administrator.arnavigatedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.arnavigatedemo.R;
import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import com.palmaplus.nagrand.position.ble.Beacon;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2/002.
 */

public class BeaconInfoAdapter extends BaseAdapter{

    private List<BeaconInfo> mData;
    private Context mContext;

    public BeaconInfoAdapter(Context context,List<BeaconInfo> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public BeaconInfo getItem(int i) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_beacon,viewGroup,false);
            holder = new ViewHolder(view);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.beaconUuid.setText(getItem(i).uuid);
        holder.beaconRssi.setText(getItem(i).rssi+"");
        holder.beaconMinor.setText(getItem(i).minor+"");
        holder.beaconMajor.setText(getItem(i).major+"");
        return view;
    }

    private class ViewHolder {
        private TextView beaconRssi,beaconUuid,beaconMinor,beaconMajor;

        public ViewHolder(View convertView) {
            beaconMajor = convertView.findViewById(R.id.beacon_major);
            beaconMinor = convertView.findViewById(R.id.beacon_minor);
            beaconRssi = convertView.findViewById(R.id.beacon_rssi);
            beaconUuid = convertView.findViewById(R.id.beacon_uuid);
            convertView.setTag(this);
        }
    }
}
