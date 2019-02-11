package com.example.administrator.laundry.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.laundry.R;
import com.example.administrator.laundry.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangPswActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_psw;
    }

    @Override
    protected void initView() {
        tvTitle.setText("设置密码");
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.img_back, R.id.btn_forgetPsw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_forgetPsw:
                finish();
                break;
        }
    }
}
