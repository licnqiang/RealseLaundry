
package com.example.administrator.laundry.NetService.parser;


import com.example.administrator.laundry.NetService.control.NetControl;
import com.example.administrator.laundry.NetService.data.BaseReseponseInfo;
import com.example.administrator.laundry.NetService.data.Detail;
import com.example.administrator.laundry.NetService.data.Login;
import com.example.administrator.laundry.NetService.http.HttpConnector;
import com.example.administrator.laundry.NetService.util.Log;
import com.google.gson.Gson;

import java.util.HashMap;


/**
 * @author lq
 * @fileName 忘记密码解析层
 * @data on  2019/2/14 11:03
 * @describe 发送请求，解析忘记密码返回数据
 */
public class listDetailParser extends BaseParser {

    private Detail mInfo;

    private String url = "getNoteDetails";


    private NetControl.GetResultListenerCallback listener;

    public listDetailParser(NetControl.GetResultListenerCallback listener, HashMap<String, String> mHashMap) {


        this.listener = listener;

        setTest(true);

        setTestFileName("listDetail.txt");

        setParameters(mHashMap);

        setUrlBody(url);

        setRequestMethod(HttpConnector.METHOD_POST);

        setReturnInfo(mInfo);
    }


    @Override
    protected void parser() {
        try {
            mInfo = new Gson().fromJson(mJson.toString(), Detail.class);
        } catch (Exception e) {
            Log.e(TAG, CLASS_NAME + "--e==" + e);
        }
    }

    @Override
    protected void Success() {
        listener.onFinished(mInfo);
    }

    @Override
    protected void Error() {
        listener.onErro(mInfo);
    }

}
