package com.example.administrator.arnavigatedemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.arnavigatedemo.adapter.BeaconInfoAdapter;
import com.example.administrator.arnavigatedemo.model.BeaconInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/8/2/002.
 */

public class ShowBeaconInfoActivity extends AppCompatActivity{
    private ListView mListView;
    private ArrayList<BeaconInfo> beacons;
    private BeaconInfoAdapter mAdapter;
    private Intent mIntent;
    private Handler mHandler;
    private BLEController bleController;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_info);
        mHandler = new Handler();
        mListView = (ListView) findViewById(R.id.beacon_list_view);
        bleController = BLEController.getInstance();
        beacons = bleController.getBeacons();
        Collections.sort(beacons, new Comparator<BeaconInfo>() {
            @Override
            public int compare(BeaconInfo beaconInfo, BeaconInfo beaconInfo1) {
                return beaconInfo1.rssi - beaconInfo.rssi;
            }
        });
        mAdapter = new BeaconInfoAdapter(this,beacons);
        mIntent = new Intent();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setResult(RESULT_OK,mIntent.putExtra("selectedBeacon",beacons.get(i)));
                finish();
            }
        });
        BLEController.getInstance().setOnScanBeaconNumberListener(new BLEController.OnScanBeaconNumberListener() {
            @Override
            public void scanResult(List<BeaconInfo> beacons) {
                Collections.sort(beacons, new Comparator<BeaconInfo>() {
                    @Override
                    public int compare(BeaconInfo beaconInfo, BeaconInfo beaconInfo1) {
                        return beaconInfo1.rssi - beaconInfo.rssi;
                    }
                });
                mAdapter = new BeaconInfoAdapter(ShowBeaconInfoActivity.this,beacons);
                ShowBeaconInfoActivity.this.beacons = (ArrayList<BeaconInfo>) beacons;
                mListView.setAdapter(mAdapter);
            }
        });
    }
}
