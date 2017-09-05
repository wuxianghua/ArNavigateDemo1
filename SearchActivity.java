package com.example.administrator.arnavigatedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.arnavigatedemo.adapter.LoadBeaconAdapter;
import com.example.administrator.arnavigatedemo.adapter.LoadMapsAdapter;
import com.example.administrator.arnavigatedemo.http.GetVersionByMapIdService;
import com.example.administrator.arnavigatedemo.http.ServiceFactory;
import com.example.administrator.arnavigatedemo.manager.MapLoadManager;
import com.example.administrator.arnavigatedemo.model.MapInfo;
import com.example.administrator.arnavigatedemo.model.ServiceMapInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/10/010.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "SearchActivity";
    private MapLoadManager mapLoadManager;
    private ListView listView;
    private LoadMapsAdapter mapsAdapter;
    private LoadBeaconAdapter beaconAdapter;
    private List<MapInfo> mapInfo;
    private LinearLayout mSearchView;
    private ListView beaconMapListView;
    private boolean isShowBeaconList;
    private List<MapInfo> searchMapInfo;
    private ImageView mClearSearchContent;
    private EditText mSearchContent;
    private TextView mCancelSearchBtn;
    private List<ServiceMapInfo> serviceMapInfos;
    private GetVersionByMapIdService getVersionByMapIdService;
    private boolean isSearchState;
    private long mapId;
    private String mapName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initEvent();
        mapLoadManager.setOnLoadMapsListener(new MapLoadManager.OnLoadMapsListener() {
            @Override
            public void loadMapsFinished() {
                mapInfo = mapLoadManager.getMapInfo();
                mapsAdapter = new LoadMapsAdapter(SearchActivity.this,mapInfo);
                listView.setAdapter(mapsAdapter);
            }
        });
        mapLoadManager.requestMap();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isSearchState) {
                    mapId = searchMapInfo.get(i).mapId;
                    mapName = searchMapInfo.get(i).mapName;
                }else {
                    mapId = mapInfo.get(i).mapId;
                    mapName = mapInfo.get(i).mapName;
                }
                getVersionByMapId(mapId,mapName);
            }
        });

        beaconMapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this,MainActivity.class);
                intent.putExtra("mapId",mapId);
                intent.putExtra("mapName",mapName);
                intent.putExtra("versionId",serviceMapInfos.get(i).id);
                intent.putExtra("isNative",false);
                startActivityForResult(intent,0);
            }
        });

        mSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    mClearSearchContent.setVisibility(View.GONE);
                    isSearchState = false;
                    mapsAdapter.setMapData(mapInfo);
                }else {
                    isSearchState = true;
                    mClearSearchContent.setVisibility(View.VISIBLE);
                    String searchContent = mSearchContent.getText().toString();
                    if (searchMapInfo.size() != 0) {
                        searchMapInfo.clear();
                    }
                    if(searchContent!=null||searchContent!="") {

                        int length = mapInfo.size();
                        for (int i = 0; i < length; i++) {
                            long mapId = mapInfo.get(i).mapId;
                            String mapName = mapInfo.get(i).mapName;
                            String map = Long.toString(mapId);
                            if (map.contains(searchContent)||mapName.contains(searchContent)) {
                                searchMapInfo.add(mapInfo.get(i));
                            }
                        }
                        mapsAdapter.setMapData(searchMapInfo);
                    }
                }
                mapsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initEvent() {
        mClearSearchContent.setOnClickListener(this);
        mCancelSearchBtn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            long mapId = data.getLongExtra("mapId", 0);
            getVersionByMapId(mapId,null);
        }
    }

    private void getVersionByMapId(final long mapId, final String mapName) {
        if (getVersionByMapIdService == null) {
            getVersionByMapIdService = ServiceFactory.getInstance().createService(GetVersionByMapIdService.class);
        }
        Call<List<ServiceMapInfo>> versionByMapId = getVersionByMapIdService.getVersionByMapId(mapId);
        versionByMapId.enqueue(new Callback<List<ServiceMapInfo>>() {
            @Override
            public void onResponse(Call<List<ServiceMapInfo>> call, Response<List<ServiceMapInfo>> response) {
                if (response == null) return;
                serviceMapInfos = response.body();
                if (serviceMapInfos == null || serviceMapInfos.size() == 0) {
                    if (mapName != null) {
                        Intent intent = new Intent(SearchActivity.this,MainActivity.class);
                        intent.putExtra("mapId",mapId);
                        intent.putExtra("mapName",mapName);
                        intent.putExtra("isNative",false);
                        startActivityForResult(intent,0);
                    }else {

                    }
                }else {
                    isShowBeaconList = true;
                    beaconMapListView.setVisibility(View.VISIBLE);
                    mSearchView.setVisibility(View.GONE);
                    beaconAdapter = new LoadBeaconAdapter(SearchActivity.this,serviceMapInfos);
                    beaconMapListView.setAdapter(beaconAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ServiceMapInfo>> call, Throwable t) {

            }
        });
    }

    private void initView() {
        mapLoadManager = new MapLoadManager();
        listView = (ListView) findViewById(R.id.map_info_list);
        mClearSearchContent = (ImageView) findViewById(R.id.clear_search_content);
        mSearchContent = (EditText) findViewById(R.id.search_edt_content);
        mCancelSearchBtn = (TextView) findViewById(R.id.cancel_search_button);
        beaconMapListView = (ListView) findViewById(R.id.beacon_info_list_view);
        mSearchView = (LinearLayout) findViewById(R.id.search_view);
        searchMapInfo = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        if (isShowBeaconList) {
            beaconMapListView.setVisibility(View.GONE);
            mSearchView.setVisibility(View.VISIBLE);
            isShowBeaconList = false;
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_search_content:
                mSearchContent.setText(null);
                break;
            case R.id.cancel_search_button:
                finish();
                break;
        }
    }
}
