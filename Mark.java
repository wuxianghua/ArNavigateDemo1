package com.example.administrator.arnavigatedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import com.palmaplus.nagrand.position.ble.Beacon;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.palmaplus.nagrand.view.overlay.OverlayCell;

/**
 * Created by zhang on 2015/11/25.
 */
public class Mark extends LinearLayout implements OverlayCell {

  private TextView mTextView;
  private ImageView mIconView;
  private LinearLayout mLinearLayout;

  private int beaconId;
  private int minor;//beacon的minor
  private int major;//beacon的major
  private String uuid;//beacon的uuid
  private boolean mIsScaned;
  private long mFloorId;
  private BeaconInfo beaconInfo;
  private Beacon beacon;

  private double[] mGeoCoordinate;

  private OnClickListenerForMark onClickListenerForMark;

  public Mark(Context context, OnClickListenerForMark onClickListenerForMark) {

    super(context);
    this.onClickListenerForMark = onClickListenerForMark;
    initView();
    mIsScaned = false;
  }

  private void initView() {
    View root =  LayoutInflater.from(getContext()).inflate(R.layout.item_mark, this);
//    root.setOnClickListener(this);
    mIconView = (ImageView) findViewById(R.id.mark_icon);
    mTextView = (TextView) findViewById(R.id.mark_text);
    mLinearLayout = (LinearLayout) findViewById(R.id.mark);
    mLinearLayout.setEnabled(true);
    mLinearLayout.setClickable(true);
    mLinearLayout.setFocusable(true);

    //OnClickListener放在mark上不起作用，
    // 怀疑mapview吃掉了触摸事件，
    // 所以放到mLinearLayout上
    mLinearLayout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (onClickListenerForMark == null){
          return;
        }
        onClickListenerForMark.onMarkSelect(Mark.this);
      }
    });

  }

  public void setFloorId(long floorId) {
    mFloorId = floorId;
  }

    /*
      * 设置mark文本
      * */
  public void setText(){
    mTextView.setText(minor+"");
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    return false;
  }

  /*
        * 获取mark文本
        * */
  /*public String getText(){
    return mTextView.getText().toString();
  }*/

  /*
  * 设置mark图标
  * */
  public void setIcon(int resId){
    mIconView.setBackgroundResource(resId);
  }

  /**
   * @param i
   */
  public void setScanedColor(int i){
    if (i == 1){
      mIconView.setImageResource(R.mipmap.dot_green_small);

      mIsScaned = true;

    } else if (i == 2){
      mIconView.setImageResource(R.mipmap.dot_red_small);
      mIsScaned = false;
    }else {
      mIconView.setImageResource(R.mipmap.dot_yellow_small);
    }
  }

  public void setMinor(int minor) {
    this.minor = minor;
  }
  public void setMajor(int major) {
    this.major = major;
  }
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
  public int getMinor() {
    return minor;
  }

  public int getMajor() {
    return major;
  }

  public String getUuid() {
    return uuid;
  }

  @Override
  public void init(double[] doubles) {
    mGeoCoordinate = doubles;
  }

  @Override
  public double[] getGeoCoordinate() {
    return mGeoCoordinate;
  }

  @Override
  public void position(double[] doubles) {
    setX((float) doubles[0] - getWidth() / 2);
    setY((float) doubles[1] - getHeight() / 9 * 4);
  }

  @Override
  public long getFloorId() {
    return mFloorId;
  }


  public interface OnClickListenerForMark{
    void onMarkSelect(Mark mark);
  }

  public boolean isScaned(){
    return mIsScaned;
  }

  public void setBeacon(Beacon beacon) {
    this.beacon = beacon;
  }

  public void setBeaconInfo(BeaconInfo beaconInfo) {
    this.beaconInfo = beaconInfo;
  }

  public BeaconInfo getBeaconInfo() {
    return beaconInfo;
  }

  public Beacon getBeacon() {
    return beacon;
  }

  public int getBeaconId(){ return beaconId;}
  public void setBeaconIdId(int beaconId){ this.beaconId = beaconId;}

  /*public void setBeaconInfo(Beacon b){
    this.beaconId = b.getId();
  setScanedColor(b.isScaned());}*/

}
