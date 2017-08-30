package com.example.administrator.arnavigatedemo.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.arnavigatedemo.R;

/**
 * Created by stone on 2017/5/11.
 */

public class SelfDialog extends Dialog {

    private TextView inputEnsure;//确认
    private TextView inputCancel;//取消输入
    private EditText mapIdContent;//输入的mapId

    private OnInputCancelOnclickListener mOnInputCancelclickListener;//忘记密码按钮被点击了的监听器
    private OnInputEnsureOnclickListener mOnInputEnsureclickListener;//重新输入按钮被点击了的监听器

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param onInputCancelOnclickListener
     */
    public void setInputCancelOnclickListener(OnInputCancelOnclickListener onInputCancelOnclickListener) {
        mOnInputCancelclickListener = onInputCancelOnclickListener;
    }

    /**
     * 设置确认按钮的显示内容和监听
     *
     * @param onInputEnsureclickListener
     */
    public void setInputEnsureOnclickListener(OnInputEnsureOnclickListener onInputEnsureclickListener) {
        mOnInputEnsureclickListener = onInputEnsureclickListener;
    }

    public SelfDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的取消和确认监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        inputEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnInputEnsureclickListener != null) {
                    String inputMapId = mapIdContent.getText().toString().trim();
                    mOnInputEnsureclickListener.onInputEnsureClick(inputMapId);
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        inputCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnInputCancelclickListener != null) {
                    mOnInputCancelclickListener.onInputCancelClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        inputCancel =  findViewById(R.id.input_cancel);
        inputEnsure =  findViewById(R.id.input_ensure);
        mapIdContent = findViewById(R.id.map_edt_content);
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface OnInputEnsureOnclickListener {
        void onInputEnsureClick(String content);
    }

    public interface OnInputCancelOnclickListener {
        void onInputCancelClick();
    }

    public static class Builder{
        public SelfDialog build(Context context){
            return new SelfDialog(context);
        }
    }
}