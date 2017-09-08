package com.example.administrator.arnavigatedemo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.arnavigatedemo.BLEController;
import com.example.administrator.arnavigatedemo.MainActivity;
import com.example.administrator.arnavigatedemo.Mark;
import com.example.administrator.arnavigatedemo.R;
import com.example.administrator.arnavigatedemo.http.GetBeaconInfosService;
import com.example.administrator.arnavigatedemo.http.GetDelBeaconInfoService;
import com.example.administrator.arnavigatedemo.http.GetProjectEndService;
import com.example.administrator.arnavigatedemo.http.GetProjectStartService;
import com.example.administrator.arnavigatedemo.http.GetRefreshBeaconService;
import com.example.administrator.arnavigatedemo.http.UploadBeaconsService;
import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import com.example.administrator.arnavigatedemo.presenter.BleScanResultPresenter;
import com.example.administrator.arnavigatedemo.utils.CacheUtils;
import com.example.administrator.arnavigatedemo.utils.SPUtils;
import com.example.administrator.arnavigatedemo.utils.SelfDialog;
import com.google.gson.Gson;
import com.palmaplus.nagrand.core.Types;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.view.MapOptions;
import com.palmaplus.nagrand.view.MapView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/9/6/006.
 */

public class BleScanResultActivity extends AppCompatActivity implements BleScanResultView {
    private static final String TAG = "MainActivity";
    protected MapView mapView;
    protected ViewGroup container;
    private Button startScan;
    private Button enSure;
    private ImageView mAddIcon;
    private LinearLayout mShowScanResult;
    private TextView mBeaconUuid;
    private TextView mBeaconMinor;
    private TextView mBeaconMajor;
    private Button mBtnCancle;
    private Button mBtnSave;
    private BeaconInfo mBeacon;
    private BeaconInfo mMoveBeacon;
    private TextView mScanedBeaconNumber;
    private TextView mAddBeaconNumber;
    private LinearLayout mSaveBeaconInfo;
    private LinearLayout mModifyBeaconInfo;
    private Button mDeleteBeaconInfo;
    private Button mMoveBeaconInfo;
    private Mark mMoveLocationMark;
    private BeaconInfo moveBeaconInfo;
    private int widthPixels;
    private int heightPixels;
    private CacheUtils earthParking;
    private BeaconInfo beaconInfo;
    private Mark locationMark;
    private Intent mIntent;
    private Bundle mBundle;
    private DataSource mDataSource;
    private ArrayList<BeaconInfo> mDatas;
    private final int SHOW_BEACON_INFO = 2;
    private Button finishMove;
    private Button mBtnSaveNative;
    private JSONArray jsonArray;
    private long bDOrPgId;
    private Types.Point point;
    private BLEController bleController;
    private boolean isSaveBeaconInfo;
    private RequestBody body;
    private UploadBeaconsService upLoadBeaconsInfoservice;
    private GetBeaconInfosService getBeaconInfosService;
    private GetDelBeaconInfoService deleteBeaconsInfoService;
    private GetRefreshBeaconService getRefreshBeaconService;
    private GetProjectStartService getProjectStartService;
    private GetProjectEndService getProjectEndService;
    private long mapId;
    private int versionId;
    private String mapName;
    private boolean isShowSaveCard;
    private Button mMapRotate;
    private boolean isMapRotate;
    private Button mBtnShowMinor;
    private boolean isShowMinor = true;
    private boolean isUpload;
    private Button startAddBeacon;
    private SelfDialog dialog;
    private SelfDialog feildDialog;
    private Button mSetScanFeild;
    private boolean isStartModifyBeacon = true;
    private boolean isNative;
    private Button mSetMapTouchable;
    private boolean isIntercept;
    private RelativeLayout relativeLayout;


    @Inject
    BleScanResultPresenter bleScanResultPresenter;
    @Inject
    Gson gson;
    @Inject
    ArrayList<String> mKeys;
    @Inject
    ArrayList<BeaconInfo> list;
    @Inject
    MapOptions options;
    @Inject
    Intent intent;
    @Inject
    ArrayList<Mark> mOverlayContainer;
    @Inject
    ArrayList<String> minorList;
    private Object refreshBeaconInfos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DaggerActivityComponent.builder().activityModule(new ActivityModule()).build().inject(this);
        bleScanResultPresenter.attachView(this);
        initView();
        initEvent();
        initData();
        setTitle(mapName);
        earthParking = CacheUtils.getInstance(mapName + "-" + mapId);
        mapView.getMap().startWithMapID(mapId);
        options.setSkewEnabled(false);
        mapView.setMapOptions(options);
        isStartModifyBeacon = (boolean) SPUtils.get(BleScanResultActivity.this, "isStartModifyBeacon", true);
        mapView.getMap().setDefaultWidgetContrainer(relativeLayout);
        mapView.getMap().getCompass().setVisibility(View.GONE);
        mapView.getMap().getScale().setVisibility(View.INVISIBLE);
        mapView.getMap().getSwitch().setVisibility(View.GONE);
        if (isStartModifyBeacon) {
            startAddBeacon.setText("开始打点");
            startScan.setVisibility(View.GONE);
        } else {
            startAddBeacon.setText("停止打点");
            startScan.setVisibility(View.VISIBLE);
        }
        if (!isNative) {
            startAddBeacon.setVisibility(View.VISIBLE);
            if (isStartModifyBeacon) {
                getBeaconsInfo(versionId);
            } else {
                getRefreshBeaconInfos();
            }
        } else {
            //Todo....子线程
            startAddBeacon.setVisibility(View.GONE);
            startScan.setVisibility(View.GONE);
            List earthparking = gson.fromJson(earthParking.getString(mapName), List.class);
            {
                for (int j = 0; j < earthparking.size(); j++) {
                    BeaconInfo serializable = (BeaconInfo) earthParking.getSerializable(String.valueOf(earthparking.get(j)).substring(0, 5));
                    mKeys.add(String.valueOf(earthparking.get(j)).substring(0, 5));
                    if (serializable == null) return;
                    addBeaconInfoMark(serializable);
                    list.add(serializable);
                    minorList.add(String.valueOf(serializable.minor));
                }
                mAddBeaconNumber.setText("添加的蓝牙数：" + list.size());
            }
        }
    }

    private void addBeaconInfoMark(BeaconInfo serializable) {

    }

    private void getBeaconsInfo(int versionId) {

    }

    //初始化数据
    private void initData() {
        mapId = getIntent().getLongExtra("mapId", 0);
        mapName = getIntent().getStringExtra("mapName");
        versionId = getIntent().getIntExtra("versionId",0);
        isNative = getIntent().getBooleanExtra("isNative",false);
    }

    //注册监听事件
    private void initEvent() {

    }

    //初始化控件
    private void initView() {
        startScan = (Button) findViewById(R.id.startScan);
        enSure = (Button) findViewById(R.id.ensure);
        mAddIcon = (ImageView) findViewById(R.id.image_add);
        mapView = (MapView) findViewById(R.id.mapView);
        mShowScanResult = (LinearLayout) findViewById(R.id.show_beacon_result);
        mBeaconUuid = (TextView) findViewById(R.id.beacon_uuid_main);
        mBeaconMinor = (TextView) findViewById(R.id.beacon_minor_main);
        mBeaconMajor = (TextView) findViewById(R.id.beacon_major_main);
        mScanedBeaconNumber = (TextView) findViewById(R.id.scaned_number_beacon);
        mSetScanFeild = (Button) findViewById(R.id.set_scan_field);
        mMapRotate = (Button) findViewById(R.id.startRotate);
        mBtnCancle = (Button) findViewById(R.id.cancle_save);
        mBtnSave = (Button) findViewById(R.id.save_beacon_data);
        container = (ViewGroup)findViewById(R.id.overlay_container);
        widthPixels = getResources().getDisplayMetrics().widthPixels;
        mSaveBeaconInfo = (LinearLayout) findViewById(R.id.beacon_info_save);
        mModifyBeaconInfo = (LinearLayout) findViewById(R.id.beacon_info_modify);
        mMoveBeaconInfo = (Button) findViewById(R.id.move_beacon_data);
        relativeLayout = (RelativeLayout)findViewById(R.id.control_container);
        mDeleteBeaconInfo = (Button) findViewById(R.id.delete_beacon_info);
        finishMove = (Button) findViewById(R.id.move_finish);
        mSetMapTouchable = (Button) findViewById(R.id.set_map_touchable);
        mAddBeaconNumber = (TextView) findViewById(R.id.add_beacon_number);
        mBtnShowMinor = (Button) findViewById(R.id.showminor);
        startAddBeacon = (Button) findViewById(R.id.start_modify_beacon);
    }

    public Object getRefreshBeaconInfos() {
        return refreshBeaconInfos;
    }
}
