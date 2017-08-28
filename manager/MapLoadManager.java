package com.example.administrator.arnavigatedemo.manager;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.administrator.arnavigatedemo.model.MapInfo;
import com.palmaplus.nagrand.data.DataList;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.MapModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/10/010.
 */

public class MapLoadManager {

    private OnLoadMapsListener mOnLoadMapsListener;
    private DataSource dataSource;
    private MapInfo mapInfo;
    private List<MapInfo> mapInfoList;
    public MapLoadManager() {
        dataSource = new DataSource("http://api.ipalmap.com/");
        mapInfoList = new ArrayList<>();
    }

    public void setOnLoadMapsListener(OnLoadMapsListener onLoadMapsListener) {
        mOnLoadMapsListener = onLoadMapsListener;

    }

    public void requestMap() {
        dataSource.requestMaps(new DataSource.OnRequestDataEventListener<DataList<MapModel>>() {

            @Override
            public void onRequestDataEvent(DataSource.ResourceState resourceState, DataList<MapModel> mapModelDataList) {
                if (resourceState == DataSource.ResourceState.OK) {
                    if (mapModelDataList.getSize()>0) {
                        for (int i = 0;i < mapModelDataList.getSize();i++) {
                            MapModel poi = mapModelDataList.getPOI(i);
                            mapInfo = new MapInfo();
                            mapInfo.mapName = MapModel.name.get(poi);
                            mapInfo.mapId = MapModel.id.get(poi);
                            mapInfoList.add(mapInfo);
                            mOnLoadMapsListener.loadMapsFinished();
                        }
                    }
                }
            }
        });
    }

    public List<MapInfo> getMapInfo() {
        return mapInfoList;
    }

    public interface OnLoadMapsListener{
        void loadMapsFinished();
    }
}
