package com.example.administrator.arnavigatedemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.arnavigatedemo.http.DeleteBeaconsInfoService;
import com.example.administrator.arnavigatedemo.http.GetBeaconInfosService;
import com.example.administrator.arnavigatedemo.http.GetDelBeaconInfoService;
import com.example.administrator.arnavigatedemo.http.GetProjectEndService;
import com.example.administrator.arnavigatedemo.http.GetProjectStartService;
import com.example.administrator.arnavigatedemo.http.GetRefreshBeaconService;
import com.example.administrator.arnavigatedemo.http.HttpResult;
import com.example.administrator.arnavigatedemo.http.ServiceFactory;
import com.example.administrator.arnavigatedemo.http.UploadBeaconsService;
import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import com.example.administrator.arnavigatedemo.model.GetBeaconsInfo;
import com.example.administrator.arnavigatedemo.model.ServiceMapInfo;
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

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
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
    private List<BeaconInfo> list;
    private Mark locationMark;
    private Intent mIntent;
    private Bundle mBundle;
    private List<String> minorList;
    private DataSource mDataSource;
    private ArrayList<BeaconInfo> mDatas;
    private ArrayList<String> mKeys;
    private final int SHOW_BEACON_INFO = 2;
    private Button finishMove;
    private Gson gson;
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
    private MapOptions options;
    private boolean isShowSaveCard;
    private Button mMapRotate;
    private boolean isMapRotate;
    private Button mBtnShowMinor;
    private boolean isShowMinor;
    private boolean isUpload;
    private Button startAddBeacon;
    private SelfDialog dialog;
    private boolean isStartModifyBeacon = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        container.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        heightPixels = container.getHeight();
                        container.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });
        bleController = BLEController.getInstance();
        mDatas = bleController.getBeacons();
        mAddIcon.setVisibility(View.GONE);
        mapView.setOverlayContainer(container);
        bleController.setOnScanBeaconNumberListener(new BLEController.OnScanBeaconNumberListener() {
            @Override
            public void scanResult(List<BeaconInfo> beacons) {
                if (beacons == null) return;
                mScanedBeaconNumber.setText("扫描的蓝牙数："+beacons.size());
            }
        });
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (bleController.isScanning&&!isShowSaveCard) {
                    enSure.setVisibility(View.VISIBLE);
                }else {
                    enSure.setVisibility(View.GONE);
                }
                if (!isShowSaveCard) {
                    mShowScanResult.setVisibility(View.GONE);
                    if (mMoveLocationMark != null) {
                        mMoveLocationMark.setScanedColor(1);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_compose:
                isUpload = true;
                List earthparking = gson.fromJson(earthParking.getString(mapName), List.class);
                if (earthparking == null) {
                    return super.onOptionsItemSelected(item);
                } else {
                    for (int j = 0; j < earthparking.size(); j++) {
                        BeaconInfo serializable = (BeaconInfo) earthParking.getSerializable(String.valueOf(earthparking.get(j)).substring(0, 5));
                        if (serializable == null) return super.onOptionsItemSelected(item);
                        uploadBeaconsInfo(serializable);
                    }
                }
                break;
            case R.id.refresh_beacon_info:
                getRefreshBeaconInfos();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initEvent() {
        startScan.setOnClickListener(this);
        enSure.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        mBtnCancle.setOnClickListener(this);
        mDeleteBeaconInfo.setOnClickListener(this);
        mModifyBeaconInfo.setOnClickListener(this);
        mMoveBeaconInfo.setOnClickListener(this);
        finishMove.setOnClickListener(this);
        mMapRotate.setOnClickListener(this);
        mBtnShowMinor.setOnClickListener(this);
        startAddBeacon.setOnClickListener(this);
    }

    private void initView() {
        mKeys = new ArrayList<>();
        gson = new Gson();
        list = new ArrayList<BeaconInfo>();
        startScan = (Button) findViewById(R.id.startScan);
        enSure = (Button) findViewById(R.id.ensure);
        mAddIcon = (ImageView) findViewById(R.id.image_add);
        mapView = (MapView) findViewById(R.id.mapView);
        mShowScanResult = (LinearLayout) findViewById(R.id.show_beacon_result);
        mShowScanResult.setVisibility(View.GONE);
        mBeaconUuid = (TextView) findViewById(R.id.beacon_uuid_main);
        mBeaconMinor = (TextView) findViewById(R.id.beacon_minor_main);
        mBeaconMajor = (TextView) findViewById(R.id.beacon_major_main);
        mScanedBeaconNumber = (TextView) findViewById(R.id.scaned_number_beacon);
        mapId = getIntent().getLongExtra("mapId", 0);
        mapName = getIntent().getStringExtra("mapName");
        versionId = getIntent().getIntExtra("versionId",0);
        this.setTitle(mapName);
        earthParking = CacheUtils.getInstance(mapName+"-"+mapId);
        mapView.getMap().startWithMapID(mapId);
        mapView.setMaxScale(Long.MAX_VALUE);
        options = new MapOptions();
        options.setSkewEnabled(false);
        mapView.setMapOptions(options);
        mMapRotate = (Button) findViewById(R.id.startRotate);
        mBtnCancle = (Button) findViewById(R.id.cancle_save);
        mBtnSave = (Button) findViewById(R.id.save_beacon_data);
        container = (ViewGroup)findViewById(R.id.overlay_container);
        widthPixels = getResources().getDisplayMetrics().widthPixels;
        mSaveBeaconInfo = (LinearLayout) findViewById(R.id.beacon_info_save);
        mModifyBeaconInfo = (LinearLayout) findViewById(R.id.beacon_info_modify);
        mMoveBeaconInfo = (Button) findViewById(R.id.move_beacon_data);
        enSure.setVisibility(View.GONE);
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.control_container);
        mapView.getMap().setDefaultWidgetContrainer(relativeLayout);
        mapView.getMap().getCompass().setVisibility(View.GONE);
        mapView.getMap().getScale().setVisibility(View.INVISIBLE);
        mapView.getMap().getSwitch().setVisibility(View.GONE);
        mDeleteBeaconInfo = (Button) findViewById(R.id.delete_beacon_info);
        finishMove = (Button) findViewById(R.id.move_finish);
        finishMove.setVisibility(View.GONE);
        mAddBeaconNumber = (TextView) findViewById(R.id.add_beacon_number);
        mBtnShowMinor = (Button) findViewById(R.id.showminor);
        isStartModifyBeacon = (boolean) SPUtils.get(MainActivity.this,"isStartModifyBeacon",true);
        startAddBeacon = (Button) findViewById(R.id.start_modify_beacon);
        if (isStartModifyBeacon) {
            startAddBeacon.setText("开始打点");
        }else {
            startAddBeacon.setText("停止打点");
        }
        minorList = new ArrayList<>();
        SelfDialog.Builder builder = new SelfDialog.Builder();
        dialog = builder.build(this);
        getBeaconsInfo(versionId);
        List earthparking = gson.fromJson(earthParking.getString(mapName), List.class);
        /*if (earthparking == null) {

        }else {
            for(int j = 0; j < earthparking.size(); j++) {
                BeaconInfo serializable = (BeaconInfo) earthParking.getSerializable(String.valueOf(earthparking.get(j)).substring(0,5));
                mKeys.add(String.valueOf(earthparking.get(j)).substring(0,5));
                if (serializable == null) return;
                addBeaconInfoMark(serializable);
                list.add(serializable);
                minorList.add(String.valueOf(serializable.minor));
            }
            mAddBeaconNumber.setText("添加的蓝牙数："+ list.size());
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.drop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startScan:
                if (isShowSaveCard) return;
                if (!bleController.isScanning) {
                    mAddIcon.setVisibility(View.VISIBLE);
                    startScan.setText("停止部署");
                    bleController.clearBeacons();
                    enSure.setVisibility(View.VISIBLE);
                    if (mDatas != null) {
                        mDatas.clear();
                    }
                    mScanedBeaconNumber.setText("扫描的蓝牙数："+0);
                    mShowScanResult.setVisibility(View.GONE);
                    bleController.start();
                }else {
                    mAddIcon.setVisibility(View.GONE);
                    startScan.setText("开始部署");
                    bleController.stop();
                    enSure.setVisibility(View.GONE);
                }
                break;
            case R.id.ensure:
                mIntent = new Intent(this,ShowBeaconInfoActivity.class);
                startActivityForResult(mIntent,SHOW_BEACON_INFO);
                break;
            case R.id.cancle_save:
                mShowScanResult.setVisibility(View.GONE);
                mDatas.clear();
                mScanedBeaconNumber.setText("扫描的蓝牙数："+0);
                bleController.clearBeacons();
                bleController.start();
                isShowSaveCard = false;
                mAddIcon.setVisibility(View.VISIBLE);
                mapView.removeOverlay(locationMark);
                enSure.setVisibility(View.VISIBLE);
                break;
            case R.id.save_beacon_data:
                mShowScanResult.setVisibility(View.GONE);
                mDatas.clear();
                isShowSaveCard = false;
                mAddIcon.setVisibility(View.VISIBLE);
                mScanedBeaconNumber.setText("扫描的蓝牙数："+0);
                bleController.clearBeacons();
                locationMark.setScanedColor(1);
                list.add(beaconInfo);
                mAddBeaconNumber.setText("添加的蓝牙数：" + list.size());
                mKeys.add(String.valueOf(mBeacon.minor));
                uploadBeaconsInfo(beaconInfo);
                minorList.add(String.valueOf(mBeacon.minor));
                isSaveBeaconInfo = true;
                enSure.setVisibility(View.VISIBLE);
                break;
            case R.id.delete_beacon_info:
                mapView.removeOverlay(mMoveLocationMark);
                mShowScanResult.setVisibility(View.GONE);
                enSure.setVisibility(View.VISIBLE);
                list.remove(moveBeaconInfo);
                earthParking.remove(String.valueOf(moveBeaconInfo.minor));
                mKeys.remove(String.valueOf(moveBeaconInfo.minor));
                deleteBeaconsInfo(moveBeaconInfo.minor);
                minorList.remove(String.valueOf(moveBeaconInfo.minor));
                earthParking.put(mapName,mKeys.toString());
                mAddBeaconNumber.setText("添加的蓝牙数：" + list.size());
                break;
            case R.id.move_beacon_data:
                mShowScanResult.setVisibility(View.GONE);
                mAddIcon.setVisibility(View.VISIBLE);
                finishMove.setVisibility(View.VISIBLE);
                list.remove(moveBeaconInfo);
                break;
            case R.id.move_finish:
                mapView.removeOverlay(mMoveLocationMark);
                finishMove.setVisibility(View.GONE);
                mAddIcon.setVisibility(View.GONE);
                addLocationMark(mMoveBeacon);
                uploadBeaconsInfo(moveBeaconInfo);
                locationMark.setScanedColor(1);
                break;
            case R.id.startRotate:
                if (isMapRotate) {
                    options.setRotateEnabled(false);
                    mapView.setMapOptions(options);
                    mapView.setMaxAngle(0);
                    mMapRotate.setText("地图旋转");
                    isMapRotate = false;
                }else {
                    options.setRotateEnabled(true);
                    mapView.setMapOptions(options);
                    mMapRotate.setText("停止旋转");
                    isMapRotate = true;
                }
                break;
            case R.id.showminor:
                if (isShowSaveCard) return;
                if (isShowMinor) {
                    isShowMinor = false;
                    mapView.removeAllOverlay();
                    for (BeaconInfo info : list) {
                        addBeaconInfoMark(info);
                    }
                    mBtnShowMinor.setText("隐藏Minor");
                }else {
                    isShowMinor = true;
                    mapView.removeAllOverlay();
                    for (BeaconInfo info : list) {
                        addBeaconInfoMark(info);
                    }
                    mBtnShowMinor.setText("显示Minor");
                }
                break;
            case R.id.start_modify_beacon:
                if (isStartModifyBeacon) {
                    dialog.show();
                    dialog.setInputCancelOnclickListener(new SelfDialog.OnInputCancelOnclickListener() {
                        @Override
                        public void onInputCancelClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.setInputEnsureOnclickListener(new SelfDialog.OnInputEnsureOnclickListener() {
                        @Override
                        public void onInputEnsureClick(String inputMapId) {
                            if (String.valueOf(mapId).equals(inputMapId)) {
                                Log.e(TAG,"开始打点");
                                isStartModifyBeacon = false;
                                SPUtils.put(MainActivity.this,"isStartModifyBeacon",isStartModifyBeacon);
                                startAddBeacon.setText("停止打点");
                                getProjectStart();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "你输入的mapId有误，请重新输入", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
                }else {
                    dialog.show();
                    dialog.setInputCancelOnclickListener(new SelfDialog.OnInputCancelOnclickListener() {
                        @Override
                        public void onInputCancelClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.setInputEnsureOnclickListener(new SelfDialog.OnInputEnsureOnclickListener() {
                        @Override
                        public void onInputEnsureClick(String inputMapId) {
                            if (String.valueOf(mapId).equals(inputMapId)) {
                                isStartModifyBeacon = true;
                                SPUtils.put(MainActivity.this,"isStartModifyBeacon",isStartModifyBeacon);
                                startAddBeacon.setText("开始打点");
                                getProjectEnd();
                                dialog.dismiss();
                            }else {
                                Toast.makeText(MainActivity.this,"你输入的mapId有误，请重新输入",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
        }
    }

    private void uploadBeaconsInfo(final BeaconInfo beaconInfo) {
        final ProgressDialog dialog = ProgressDialog.show(this,"上传beacon","上传中");
        if (upLoadBeaconsInfoservice == null) {
            upLoadBeaconsInfoservice = ServiceFactory.getInstance().createService(UploadBeaconsService.class);
        }
        body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),gson.toJson(beaconInfo));
        Call<HttpResult> httpResultCall = upLoadBeaconsInfoservice.uploadBeaconsInfo(body);
        httpResultCall.enqueue(new Callback<HttpResult>() {
            @Override
            public void onResponse(Call<HttpResult> call, Response<HttpResult> response) {
                dialog.hide();
                Log.e(TAG,"上传成功"+response.body().State);
                if (isUpload) {
                    Toast.makeText(MainActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                    isUpload = false;
                }
                    beaconInfo.uploadSuccess = true;
                    earthParking.put(String.valueOf(beaconInfo.minor),beaconInfo);
                    Log.e(TAG,earthParking.toString());
                    earthParking.put(mapName,mKeys.toString());
            }

            @Override
            public void onFailure(Call<HttpResult> call, Throwable t) {
                dialog.hide();
                Log.e(TAG,"上传失败"+t);
                if (isUpload) {
                    Toast.makeText(MainActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                    isUpload = false;
                }else{
                    beaconInfo.uploadSuccess = false;
                    earthParking.put(String.valueOf(beaconInfo.minor),beaconInfo);
                    earthParking.put(mapName,mKeys.toString());
                }
            }
        });
    }

    private void deleteBeaconsInfo(int minor) {
        if (deleteBeaconsInfoService == null) {
            deleteBeaconsInfoService = ServiceFactory.getInstance().createService(GetDelBeaconInfoService.class);
        }
        Call<HttpResult> deleteBeacons = deleteBeaconsInfoService.getDelBeaconInfo(mapId,minor);
        deleteBeacons.enqueue(new Callback<HttpResult>() {
            @Override
            public void onResponse(Call<HttpResult> call, Response<HttpResult> response) {
                Log.e(TAG,"删除成功");
            }
            @Override
            public void onFailure(Call<HttpResult> call, Throwable t) {
                Log.e(TAG,"删除失败");
            }
        });
    }

    private void getBeaconsInfo(int versionId) {
        if (getBeaconInfosService == null) {
            getBeaconInfosService = ServiceFactory.getInstance().createService(GetBeaconInfosService.class);
        }
        Call<List<BeaconInfo>> beaconsInfo = getBeaconInfosService.getBeaconById(versionId);
        beaconsInfo.enqueue(new Callback<List<BeaconInfo>>() {
            @Override
            public void onResponse(Call<List<BeaconInfo>> call, Response<List<BeaconInfo>> response) {
                if (response == null || response.body() == null) return;
                for (BeaconInfo info : response.body()) {
                    addBeaconInfoMark(info);
                }
            }

            @Override
            public void onFailure(Call<List<BeaconInfo>> call, Throwable t) {
                Log.e(TAG,"获取数据失败");
            }
        });
    }

    public void addLocationMark(BeaconInfo beacon) {
        beaconInfo = new BeaconInfo();
        beaconInfo = beacon;
        beaconInfo.floorId = mapView.getMap().getFloorId();
        beaconInfo.mapId = mapId;
        locationMark = new Mark(this, new Mark.OnClickListenerForMark() {
            @Override
            public void onMarkSelect(Mark mark) {
                if (mMoveLocationMark != null) {
                    mMoveLocationMark.setScanedColor(1);
                }
                mShowScanResult.setVisibility(View.VISIBLE);
                mBeaconMinor.setText(mark.getMinor() + "");
                mBeaconMajor.setText(mark.getMajor() + "");
                mBeaconUuid.setText(mark.getUuid());
                mSaveBeaconInfo.setVisibility(View.GONE);
                enSure.setVisibility(View.GONE);
                isSaveBeaconInfo = false;
                mModifyBeaconInfo.setVisibility(View.VISIBLE);
                mDeleteBeaconInfo.setVisibility(bleController.isScanning ? View.GONE : View.VISIBLE);
                mMoveBeaconInfo.setVisibility(bleController.isScanning ? View.GONE : View.VISIBLE);
                moveBeaconInfo = mark.getBeaconInfo();
                mMoveLocationMark = mark;
                mMoveBeacon = mark.getBeaconInfo();
                mMoveLocationMark.setScanedColor(3);
            }
        });
        point = mapView.converToWorldCoordinate(widthPixels / 2, heightPixels / 2);
        beaconInfo.locationX = point.x;
        beaconInfo.locationY = point.y;
        locationMark.setFloorId(mapView.getMap().getFloorId());
        locationMark.init(new double[]{point.x, point.y});
        locationMark.setUuid(beacon.uuid);
        locationMark.setMajor(beacon.major);
        locationMark.setMinor(beacon.minor);
        if (!isShowMinor) {
            locationMark.setText();
        }
        //将这个覆盖物添加到MapView中
        mapView.addOverlay(locationMark);
        locationMark.setBeaconInfo(beaconInfo);
    }

    public void addBeaconInfoMark(BeaconInfo beacon) {

        locationMark = new Mark(this, new Mark.OnClickListenerForMark() {
            @Override
            public void onMarkSelect(Mark mark) {
                if (mMoveLocationMark != null) {
                    mMoveLocationMark.setScanedColor(1);
                }
                mShowScanResult.setVisibility(View.VISIBLE);
                mBeaconMinor.setText(mark.getMinor() + "");
                mBeaconMajor.setText(mark.getMajor() + "");
                mBeaconUuid.setText(mark.getUuid());
                mSaveBeaconInfo.setVisibility(View.GONE);
                enSure.setVisibility(View.GONE);
                isSaveBeaconInfo = false;
                mModifyBeaconInfo.setVisibility(View.VISIBLE);
                mDeleteBeaconInfo.setVisibility(bleController.isScanning ? View.GONE : View.VISIBLE);
                mMoveBeaconInfo.setVisibility(bleController.isScanning ? View.GONE : View.VISIBLE);
                moveBeaconInfo = mark.getBeaconInfo();
                mMoveLocationMark = mark;
                mMoveBeacon = mark.getBeaconInfo();
                mMoveLocationMark.setScanedColor(3);
            }
        });
        locationMark.setFloorId(beacon.floorId);
        locationMark.init(new double[]{beacon.locationX, beacon.locationY});
        locationMark.setUuid(beacon.uuid);
        locationMark.setMajor(beacon.major);
        locationMark.setMinor(beacon.minor);
        if (!isShowMinor) {
            locationMark.setText();
        }
        locationMark.setScanedColor(1);
        //将这个覆盖物添加到MapView中
        mapView.addOverlay(locationMark);
        locationMark.setBeaconInfo(beacon);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bleController.setOnScanBeaconNumberListener(new BLEController.OnScanBeaconNumberListener() {
            @Override
            public void scanResult(List<BeaconInfo> beacons) {
                if (beacons == null) return;
                mScanedBeaconNumber.setText("扫描的蓝牙数："+beacons.size());
            }
        });
        if (resultCode == RESULT_OK && requestCode == SHOW_BEACON_INFO) {
            mBeacon = (BeaconInfo) data.getSerializableExtra("selectedBeacon");
            if (minorList.contains(String.valueOf(mBeacon.minor))){
                Toast.makeText(this,"你已添加该beacon",Toast.LENGTH_LONG).show();
                return;
            }
            isShowSaveCard = true;
            enSure.setVisibility(View.GONE);
            mAddIcon.setVisibility(View.GONE);
            mShowScanResult.setVisibility(View.VISIBLE);
            mBeaconMinor.setText(mBeacon.minor+"");
            mBeaconMajor.setText(mBeacon.major+"");
            mBeaconUuid.setText(mBeacon.uuid);
            mSaveBeaconInfo.setVisibility(View.VISIBLE);
            mModifyBeaconInfo.setVisibility(View.GONE);
            addLocationMark(mBeacon);
        }
    }

    public void getRefreshBeaconInfos() {
        if (getRefreshBeaconService == null) {
            getRefreshBeaconService = ServiceFactory.getInstance().createService(GetRefreshBeaconService.class);
        }
        Call<List<BeaconInfo>> refreshBeaconByMapId = getRefreshBeaconService.getRefreshBeaconByMapId(mapId);
        refreshBeaconByMapId.enqueue(new Callback<List<BeaconInfo>>() {
            @Override
            public void onResponse(Call<List<BeaconInfo>> call, Response<List<BeaconInfo>> response) {
                if (response == null||response.body() == null) return;
                mapView.removeAllOverlay();
                for (BeaconInfo info : response.body()) {
                    addBeaconInfoMark(info);
                }
            }

            @Override
            public void onFailure(Call<List<BeaconInfo>> call, Throwable t) {

            }
        });
    }

    public void getProjectStart() {
        if (getProjectStartService == null) {
            getProjectStartService = ServiceFactory.getInstance().createService(GetProjectStartService.class);
        }
        Call<HttpResult> projectStart = getProjectStartService.getProjectStart(mapId, versionId);
        projectStart.enqueue(new Callback<HttpResult>() {
            @Override
            public void onResponse(Call<HttpResult> call, Response<HttpResult> response) {
                Log.e(TAG,response.message());
            }

            @Override
            public void onFailure(Call<HttpResult> call, Throwable t) {
                Log.e(TAG,"开始失败");
            }
        });
    }

    public void getProjectEnd() {
        if (getProjectEndService == null) {
            getProjectEndService = ServiceFactory.getInstance().createService(GetProjectEndService.class);
        }
        Call<HttpResult> projectEnd = getProjectEndService.getProjectEnd(mapId);
        projectEnd.enqueue(new Callback<HttpResult>() {
            @Override
            public void onResponse(Call<HttpResult> call, Response<HttpResult> response) {

            }

            @Override
            public void onFailure(Call<HttpResult> call, Throwable t) {

            }
        });
    }
}
