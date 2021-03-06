package com.example.administrator.laundry.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.laundry.NetService.control.NetControl;
import com.example.administrator.laundry.NetService.data.BaseReseponseInfo;
import com.example.administrator.laundry.NetService.util.LoadingUI;
import com.example.administrator.laundry.NetService.util.Log;
import com.example.administrator.laundry.R;
import com.example.administrator.laundry.UpLoadFile.upLoadFile;
import com.example.administrator.laundry.base.BaseActivity;
import com.example.administrator.laundry.base.BaseApplication;
import com.example.administrator.laundry.util.GlideImageLoader;
import com.example.administrator.laundry.util.RxDeviceTool;
import com.example.administrator.laundry.util.ToastUtil;
import com.example.administrator.laundry.view.CountDownTextView;
import com.example.administrator.laundry.view.LoadDataView;
import com.example.administrator.laundry.view.SelectDialog;
import com.example.administrator.laundry.view.progress.LSProgressDialog;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    HashMap<String, String> mHashMap = new HashMap<>();
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.send_yzm)
    CountDownTextView sendYzm;
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.et_user_sex)
    TextView etUserSex;
    @BindView(R.id.et_user_code)
    EditText etUserCode;
    @BindView(R.id.et_user_login_psw)
    EditText etUserLoginPsw;
    @BindView(R.id.et_user_login_psw_next)
    EditText etUserLoginPswNext;
    @BindView(R.id.et_user_time)
    EditText etUserTime;
    @BindView(R.id.et_user_store_name)
    EditText etUserStoreName;
    @BindView(R.id.et_user_store_location)
    EditText etUserStoreLocation;
    @BindView(R.id.et_user_sign)
    EditText etUserSign;
    @BindView(R.id.et_user_centent)
    EditText etUserCentent;
    @BindView(R.id.regitster_layout)
    LinearLayout regitsterLayout;
    public static final int REQUEST_CODE_SELECT = 100;
    private LSProgressDialog progressDialog;
    private ArrayList<ImageItem> images;
    private String nimageNumber = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        tvTitle.setText(R.string.title_register);
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


    private void showSexDialog() {
        List<String> names = new ArrayList<>();
        names.add("男");
        names.add("女");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        etUserSex.setText("男");
                        break;
                    case 1:
                        etUserSex.setText("女");
                        break;
                }

            }
        }, names);

    }

    /**
     * 选择性别的dialog
     *
     * @param listener
     * @param names
     * @return
     */
    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(RegisterActivity.this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        dialog.show();
        return dialog;
    }


    public void register(String username,String psw) {
        progressDialog = new LSProgressDialog(this);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(username, psw);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            ToastUtil.show(RegisterActivity.this, "注册成功，用户名是:" + etUserName.getText().toString() + "  快开始聊天吧");
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    /**
                     * 关于错误码可以参考官方api详细说明
                     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                     */
                    final int errorCode = e.getErrorCode();
                    final String message = e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (errorCode) {
                                case EMError.NETWORK_ERROR:
                                    ToastUtil.show(RegisterActivity.this, "网络异常，请检查网络！ code: " + errorCode + "，message: " + message);
                                    break;
                                case EMError.USER_ALREADY_EXIST:
                                    ToastUtil.show(RegisterActivity.this, "用户名已存在,请尝试登录！ code: " + errorCode + "，message: " + message);
                                    break;
                                case EMError.USER_ALREADY_LOGIN:
                                    ToastUtil.show(RegisterActivity.this, "用户已登录！ code: " + errorCode + "，message: " + message);
                                    break;
                                case EMError.USER_AUTHENTICATION_FAILED:
                                    ToastUtil.show(RegisterActivity.this, "用户id或密码错误！ code: " + errorCode + "，message: " + message);
                                    break;
                                case EMError.SERVER_UNKNOWN_ERROR:
                                    ToastUtil.show(RegisterActivity.this, "服务器位置错误！ code: " + errorCode + "，message: " + message);
                                    break;
                                case EMError.USER_REG_FAILED:
                                    ToastUtil.show(RegisterActivity.this, "注册失败！ code: " + errorCode + "，message: " + message);
                                    break;
                                default:
                                    ToastUtil.show(RegisterActivity.this, "ml_sign_up_failed  code: " + errorCode + "，message: " + message);
                                    break;
                            }
                        }
                    });

                }
            }
        }).start();
    }

    private void showPicDialog() {
        List<String> names = new ArrayList<>();
        names.add("拍照");
        names.add("相册");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // 直接调起相机
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setImageLoader(new GlideImageLoader());
                        ImagePicker.getInstance().setMultiMode(false);
                        ImagePicker.getInstance().setCrop(false);
                        ImagePicker.getInstance().setShowCamera(false);
                        Intent intent = new Intent(RegisterActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    case 1:
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setImageLoader(new GlideImageLoader());
                        ImagePicker.getInstance().setMultiMode(false);
                        ImagePicker.getInstance().setCrop(false);
                        ImagePicker.getInstance().setShowCamera(false);
                        Intent intent1 = new Intent(RegisterActivity.this, ImageGridActivity.class);
                        startActivityForResult(intent1, REQUEST_CODE_SELECT);
                        break;
                    default:
                        break;
                }

            }
        }, names);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    ImagePicker.getInstance().getImageLoader().displayImage(this, images.get(0).path, userImage, 0, 0);
                    List<String> paths = new ArrayList<>();
                    paths.add(images.get(0).path);
                    upLoadFile.uploadFile(paths, new upLoadFile.ResultCallBack() {
                        @Override
                        public void succeed(List<String> str) {
                            nimageNumber = str.get(0);
                            ToastUtil.show(RegisterActivity.this, "文件上传成功");
                        }

                        @Override
                        public void faild() {
                            ToastUtil.show(RegisterActivity.this, "文件上传失败");
                        }
                    });
                }
            }
        }
    }

    @OnClick({R.id.img_back, R.id.user_image, R.id.et_user_sex, R.id.btn_register, R.id.send_yzm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.user_image:
                showPicDialog();
                break;
            case R.id.et_user_sex:
                showSexDialog();
                break;
            case R.id.btn_register:
//                register();
                meServerRister();
                break;
            case R.id.send_yzm:
                sendYzm();
                break;
        }
    }

    private void sendYzm() {
        String userPhone = etUserPhone.getText().toString();
        if (TextUtils.isEmpty(userPhone)) {
            ToastUtil.show(this, "请输入手机号");
            sendYzm.reset();
        } else {
            if (!RxDeviceTool.isMobileNO(userPhone)) {
                ToastUtil.show(this, "输入手机号有误");
                sendYzm.reset();
                return;
            }
            sendYzm.start();
            mHashMap.put("userPhone", userPhone);
            NetControl.GetCode(callback, mHashMap);
        }
    }

    NetControl.GetResultListenerCallback callback = new NetControl.GetResultListenerCallback() {
        @Override
        public void onFinished(Object o) {

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
                Toast.makeText(RegisterActivity.this,
                        "网络连接失败，请稍后重试！", Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void meServerRister() {
        String userName = etUserName.getText().toString().trim();
        String userPhone = etUserPhone.getText().toString().trim();
        String userSex = etUserSex.getText().toString().trim();
        String userCode = etUserCode.getText().toString().trim();
        String userLoginPsw = etUserLoginPsw.getText().toString().trim();
        String userTime = etUserTime.getText().toString().trim();
        String userStoreName = etUserStoreName.getText().toString().trim();
        String userStoreLocation = etUserStoreLocation.getText().toString().trim();
        String userSign = etUserSign.getText().toString().trim();
        String userCentent = etUserCentent.getText().toString().trim();
        String userLoginPswNext = etUserLoginPswNext.getText().toString().trim();
//        userTime.isEmpty() ||
//                userStoreName.isEmpty() ||
//                userStoreLocation.isEmpty() ||
//                userSign.isEmpty() ||
//                userCentent.isEmpty()

        if (userName.isEmpty() ||
                userPhone.isEmpty() ||
                userSex.isEmpty() ||
                userCode.isEmpty() ||
                userLoginPsw.isEmpty() ||
                userLoginPswNext.isEmpty() ||
                nimageNumber.isEmpty()
                ) {
            ToastUtil.show(RegisterActivity.this, "请输入所有必填项");
        } else {

            if (userLoginPswNext.equals(userLoginPsw)) {
                register(EaseConstant.EXTRA_HUANXIN+userPhone,userLoginPsw);
                mHashMap.put("userPhone", userPhone);
                mHashMap.put("proof", userCode);
                mHashMap.put("userPassword", userLoginPsw);
                mHashMap.put("userNickname", userName);
                mHashMap.put("userImgNumber", nimageNumber);
                mHashMap.put("userSign", userSign);
                mHashMap.put("userIntroduce", userCentent);
                mHashMap.put("userSex", (userSex.equals("男") ? 0 : 1) + "");
                mHashMap.put("userWorkingTime", userTime);
                mHashMap.put("userShop", userStoreName);
                mHashMap.put("userShopAddress", userStoreLocation);
                LoadingUI.showDialogForLoading(RegisterActivity.this, "正在加载", true);
                NetControl.Register(regiserCallBack, mHashMap);
            } else {
                ToastUtil.show(RegisterActivity.this, "两次密码不一致");
            }


        }

    }

    NetControl.GetResultListenerCallback regiserCallBack = new NetControl.GetResultListenerCallback() {
        @Override
        public void onFinished(Object o) {
            LoadingUI.hideDialogForLoading();
            BaseReseponseInfo info = (BaseReseponseInfo) o;
            if (null != info && null != info.getInfo()) {
                Toast.makeText(
                        BaseApplication.ApplicationContext,
                        info.getInfo(),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
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
                Toast.makeText(RegisterActivity.this,
                        "网络连接失败，请稍后重试！", Toast.LENGTH_SHORT).show();
            }
        }
    };


}
