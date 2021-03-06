package com.example.administrator.laundry.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.laundry.NetService.control.NetControl;
import com.example.administrator.laundry.NetService.data.BaseReseponseInfo;
import com.example.administrator.laundry.NetService.util.LoadingUI;
import com.example.administrator.laundry.NetService.util.Log;
import com.example.administrator.laundry.R;
import com.example.administrator.laundry.base.BaseActivity;
import com.example.administrator.laundry.base.BaseApplication;
import com.example.administrator.laundry.constant.SysContant;
import com.example.administrator.laundry.util.RxDeviceTool;
import com.example.administrator.laundry.util.SpHelper;
import com.example.administrator.laundry.util.ToastUtil;
import com.example.administrator.laundry.view.CountDownTextView;
import com.example.administrator.laundry.view.LoadDataView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangPswActivity extends BaseActivity {


    HashMap<String, String> mHashMap = new HashMap<>();
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_yzm)
    EditText etUserYzm;
    @BindView(R.id.et_user_next_psw)
    EditText etUserNextPsw;
    @BindView(R.id.send_yzm)
    CountDownTextView sendYzm;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_psw;
    }

    @Override
    protected void initView() {
        tvTitle.setText("修改密码");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected ViewGroup loadDataViewLayout() {
        return null;
    }

    @Override
    protected void getLoadView(LoadDataView loadView) {

    }

    @OnClick({R.id.img_back, R.id.btn_forgetPsw, R.id.send_yzm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_forgetPsw:
                judgeData();
                break;
            case R.id.send_yzm:
                String etUserPhone = etUserName.getText().toString();
                if (TextUtils.isEmpty(etUserPhone)) {
                    ToastUtil.show(this, "请输入手机号");
                    sendYzm.reset();
                } else {
                    if (!RxDeviceTool.isMobileNO(etUserPhone)) {
                        ToastUtil.show(this, "输入手机号有误");
                        sendYzm.reset();
                        return;
                    }
                    sendYzm.start();
                    mHashMap.put("userPhone", etUserPhone);
                    NetControl.GetCode(callback, mHashMap);
                }
                break;
        }
    }

    private void judgeData() {
        String userName = etUserName.getText().toString().trim();
        String userYzm = etUserYzm.getText().toString().trim();
        String userNextPsw = etUserNextPsw.getText().toString().trim();

        if (userName.isEmpty() || userYzm.isEmpty() || userNextPsw.isEmpty()) {
            ToastUtil.show(ChangPswActivity.this, "填写所有信息");
        } else {
            mHashMap.clear();
            mHashMap.put("userPhone", userName);
            mHashMap.put("proof", userYzm);
            mHashMap.put("userPassword", userNextPsw);
            LoadingUI.showDialogForLoading(this, "正在加载", true);
            NetControl.changpsw(registerCallback, mHashMap);
        }
    }


    NetControl.GetResultListenerCallback registerCallback = new NetControl.GetResultListenerCallback() {
        @Override
        public void onFinished(Object o) {
            BaseReseponseInfo info = (BaseReseponseInfo) o;
            if (null != info && null != info.getInfo()) {
                Toast.makeText(
                        BaseApplication.ApplicationContext,
                        info.getInfo(),
                        Toast.LENGTH_SHORT).show();
            }
            finish();
        }

        @Override
        public void onErro(Object o) {
            if (o != null) {
                BaseReseponseInfo mBaseReseponseInfo = (BaseReseponseInfo) o;
                int code = mBaseReseponseInfo.getFlag();
                String msg = mBaseReseponseInfo.getInfo();
                if (msg != null && msg.length() > 0) {
                    Log.e("TAG-code", code + "");
                    Log.e("TAG-msg", msg);
                    Toast.makeText(
                            BaseApplication.ApplicationContext,
                            msg + " code:" + code,
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ChangPswActivity.this,
                        "网络连接失败，请稍后重试！", Toast.LENGTH_SHORT).show();
            }
        }
    };


    NetControl.GetResultListenerCallback callback = new NetControl.GetResultListenerCallback() {
        @Override
        public void onFinished(Object o) {
            LoadingUI.hideDialogForLoading();
            ToastUtil.show(ChangPswActivity.this, "验证码获取成功");
        }

        @Override
        public void onErro(Object o) {
            if (o != null) {
                BaseReseponseInfo mBaseReseponseInfo = (BaseReseponseInfo) o;
                int code = mBaseReseponseInfo.getFlag();
                String msg = mBaseReseponseInfo.getInfo();
                if (msg != null && msg.length() > 0) {
                    Log.e("TAG-code", code + "");
                    Log.e("TAG-msg", msg);
                    Toast.makeText(
                            BaseApplication.ApplicationContext,
                            msg + " code:" + code,
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ChangPswActivity.this,
                        "网络连接失败，请稍后重试！", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
