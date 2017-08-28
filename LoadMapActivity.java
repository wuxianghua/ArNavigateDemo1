package com.example.administrator.arnavigatedemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.constraint.solver.Cache;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.administrator.arnavigatedemo.adapter.HasBeaconsMapAdapter;
import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import com.example.administrator.arnavigatedemo.model.HasBeaconsMapInfo;
import com.example.administrator.arnavigatedemo.utils.CacheUtils;
import com.google.gson.Gson;
import com.palmaplus.nagrand.data.DataList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8/008.
 */

public class LoadMapActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "LoadMapActivity";
    private HasBeaconsMapInfo mapInfo;
    private LinearLayout mSearchMaps;
    private ListView mHasBeaconsMapList;
    private List<HasBeaconsMapInfo> beaconMapsInfoList;
    private HasBeaconsMapAdapter mapAdapter;
    private ImageView mDeleteHasBeaconMaps;
    private LinearLayout mllShowButtonDelete;
    private Button mBtnAllCheck;
    private Button mBtnEnsureDelete;
    private boolean isStateDelete;
    private int checkNum;
    private File absoluteFile;
    private List<HasBeaconsMapInfo> mDeleteBeaconMaps;
    public final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_map);
        initView();
        initEvent();
        beaconMapsInfoList = new ArrayList<>();
        initHasBeaconsInfo();
        mHasBeaconsMapList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isStateDelete) {
                    HasBeaconsMapAdapter.ViewHolder holder = (HasBeaconsMapAdapter.ViewHolder) view.getTag();
                    // 改变CheckBox的状态
                    holder.mCheckBox.toggle();
                    // 将CheckBox的选中状况记录下来
                    mapAdapter.getIsSelected().put(i, holder.mCheckBox.isChecked());
                    // 调整选定条目
                    if (holder.mCheckBox.isChecked() == true) {
                        checkNum++;
                        mDeleteBeaconMaps.add(beaconMapsInfoList.get(i));
                    } else {
                        checkNum--;
                        mDeleteBeaconMaps.remove(beaconMapsInfoList.get(i));
                    }
                    // 用TextView显示
                    mBtnEnsureDelete.setText("确认删除("+checkNum+")");
                }else {
                    Intent intent = new Intent(LoadMapActivity.this,MainActivity.class);
                    intent.putExtra("mapId",beaconMapsInfoList.get(i).mapId);
                    intent.putExtra("mapName",beaconMapsInfoList.get(i).mapName);
                    startActivityForResult(intent,REQUEST_CODE);
                }
            }
        });
    }

    private void initEvent() {
        mSearchMaps.setOnClickListener(this);
        mDeleteHasBeaconMaps.setOnClickListener(this);
        mBtnEnsureDelete.setOnClickListener(this);
        mBtnAllCheck.setOnClickListener(this);
    }

    public void initHasBeaconsInfo() {
        beaconMapsInfoList.clear();
        absoluteFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"beacontool");
        if (absoluteFile.listFiles() == null) return;
        for (File file : absoluteFile.listFiles()) {
            if (file.listFiles() != null&&file.listFiles().length != 0) {
                mapInfo = new HasBeaconsMapInfo();
                mapInfo.beacons = file.listFiles().length;
                String[] split = file.getName().split("-");
                mapInfo.mapName = split[0];
                CacheUtils instance = CacheUtils.getInstance(file.getName());
                String string = instance.getString(split[0]);
                Gson gson = new Gson();
                List<Double> list = gson.fromJson(string, List.class);
                if (list == null) return;
                for (double i : list) {
                    BeaconInfo beaconInfo = (BeaconInfo) instance.getSerializable(String.valueOf(i).substring(0,5));
                    if (beaconInfo == null) return;
                    if (beaconInfo.uploadSuccess) {

                    }else {
                        mapInfo.isUploadSuccess = true;
                    }
                }
                mapInfo.mapId = Integer.valueOf(split[1]);
                beaconMapsInfoList.add(mapInfo);
            }
        }
        mapAdapter = new HasBeaconsMapAdapter(this,beaconMapsInfoList);
        mHasBeaconsMapList.setAdapter(mapAdapter);
    }

    private void initView() {
        mSearchMaps = (LinearLayout) findViewById(R.id.serch_maps);
        mHasBeaconsMapList = (ListView) findViewById(R.id.has_beacons_list);
        mDeleteHasBeaconMaps = (ImageView) findViewById(R.id.delete_beacon_maps);
        mllShowButtonDelete = (LinearLayout) findViewById(R.id.check_box_button);
        mBtnAllCheck = (Button) findViewById(R.id.btn_all_check);
        mBtnEnsureDelete = (Button) findViewById(R.id.btn_ensure_delete);
        mDeleteBeaconMaps = new ArrayList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initHasBeaconsInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        beaconMapsInfoList.clear();
    }

    public  void deleteFolder(File file) {
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFolder(files[i]);
            }
        }
        file.delete();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.serch_maps:
                startActivityForResult(new Intent(this,SearchActivity.class),REQUEST_CODE);
                break;
            case R.id.delete_beacon_maps:
                if (isStateDelete) {
                    isStateDelete = false;
                    mllShowButtonDelete.setVisibility(View.GONE);
                    mapAdapter.setStateDelete(isStateDelete);
                    mapAdapter.notifyDataSetChanged();
                    mDeleteHasBeaconMaps.setImageResource(R.mipmap.delete_beacons_map);
                }else {
                    isStateDelete = true;
                    mllShowButtonDelete.setVisibility(View.VISIBLE);
                    mapAdapter.setStateDelete(isStateDelete);
                    mapAdapter.notifyDataSetChanged();
                    mDeleteHasBeaconMaps.setImageResource(R.mipmap.delete_beacon_cancle);
                }
                break;
            case R.id.btn_all_check:
                for (int i = 0; i < beaconMapsInfoList.size(); i++) {
                    if (!mapAdapter.getIsSelected().get(i)) {
                        mapAdapter.getIsSelected().put(i,true);
                        Log.e(TAG,"测试哈哈哈");
                        mDeleteBeaconMaps.add(beaconMapsInfoList.get(i));
                    }
                }
                checkNum = beaconMapsInfoList.size();
                // 刷新listview和TextView的显示
                dataChanged();
                break;
            case R.id.btn_ensure_delete:
                absoluteFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"beacontool");
                for (HasBeaconsMapInfo mDeleteBeaconMap : mDeleteBeaconMaps) {
                    for (File file : absoluteFile.listFiles()) {
                            String[] split = file.getName().split("-");
                            if (mDeleteBeaconMap.mapName.equals(split[0])){
                                CacheUtils.removeCacheFile(file);
                                deleteFolder(file);
                            }
                        }
                }
                initHasBeaconsInfo();
                break;
        }
    }

    private void dataChanged() {
        mapAdapter.notifyDataSetChanged();
        mBtnEnsureDelete.setText("确认删除("+checkNum+")");
    }
}
