package com.example.administrator.arnavigatedemo;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import com.palmaplus.nagrand.position.ble.Beacon;
import com.palmaplus.nagrand.position.ble.BeaconUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/8/1/001.
 */

public class BLEController {
    private static final String TAG = BLEController.class.getSimpleName();
    /**
     * Represents the local devices Bluetooth adapter
     */
    private BluetoothAdapter bluetoothAdapter;

    /**
     * beacon扫描回调
     */
    private BluetoothAdapter.LeScanCallback leScanCallback;

    /**
     * 扫描周期
     */
    private static final long SCAN_PERIOD = 5000;

    /**
     * 被扫描到的beacon
     */
    private Map<Integer,Beacon> scanBeaconMap;

    /**
     * 是否在扫描中
     */
    public volatile boolean isScanning;

    private ArrayList<Integer> list;
    private ArrayList<BeaconInfo> beacons;
    private int minor;
    private int major;
    private String uuid;
    private BeaconInfo beaconInfo;
    private Handler handler;
    private OnScanBeaconNumberListener mOnScanBeaconNumberListener;

    private BLEController() {
        handler = new Handler(Looper.getMainLooper());
        scanBeaconMap = new ConcurrentHashMap<Integer, Beacon>();
        list = new ArrayList<>();
        beacons = new ArrayList<>();
    }

    private static final BLEController bleController = new BLEController();

    public static BLEController getInstance() {
        return bleController;
    }

    public void stop() {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.stopLeScan(leScanCallback);
            isScanning = false;
        }
    }

    public void setOnScanBeaconNumberListener(OnScanBeaconNumberListener onScanBeaconNumberListener) {
        this.mOnScanBeaconNumberListener = onScanBeaconNumberListener;
    }

    /**
     * 开始扫描周围的蓝牙设备
     */
    public boolean start() {
        if (bluetoothAdapter != null && leScanCallback != null) {
            bluetoothAdapter.stopLeScan(leScanCallback);
            bluetoothAdapter = null;
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            return false;
        }
        isScanning = true;
        Log.e(TAG,"我被设置为true");
        leScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
                final Beacon beacon = BeaconUtils.beaconFromLeScan(bluetoothDevice,i,bytes);
                if (beacon == null || beacon.getProximityUUID() == null) {
                    return;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        minor = beacon.getMinor();
                        major = beacon.getMajor();
                        uuid = beacon.getProximityUUID().toUpperCase();
                        if (!list.contains(minor)&&beacon.getDistance()<0.1) {
                            Log.e(TAG,beacon.getProximityUUID());
                            list.add(minor);
                            beaconInfo = new BeaconInfo();
                            beaconInfo.minor = beacon.getMinor();
                            beaconInfo.major = beacon.getMajor();
                            beaconInfo.uuid = beacon.getProximityUUID();
                            beaconInfo.rssi = beacon.getRssi();
                            beacons.add(beaconInfo);
                            mOnScanBeaconNumberListener.scanResult(beacons);
                        }
                    }
                });
            }
        };
        boolean b = bluetoothAdapter.startLeScan(leScanCallback);
        return b;
    }

    public ArrayList<BeaconInfo> getBeacons() {
        return beacons;
    }

    public void clearBeacons() {
        beacons.clear();
        list.clear();
    }

    interface OnScanBeaconNumberListener {
        void scanResult(List<BeaconInfo> beacons);
    }
}
