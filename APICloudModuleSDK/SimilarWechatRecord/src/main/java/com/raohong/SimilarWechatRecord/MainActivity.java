package com.raohong.SimilarWechatRecord;

import android.content.Intent;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

/*
 * 声明一个主类用于assets\uzmap\module.json中使用
 * 以及在APICloudModuleSDK\app\build.gradle中加载模块
 * 至此引用结束
 * Created by raohong on 18/12/11.
 */
public class MainActivity extends UZModule {

    static final int ACTIVITY_REQUEST_CODE_A = 100;
    private UZModuleContext mJsCallback;

    public MainActivity(UZWebView webView) {
        super(webView);
    }

    /*
     * jsmethod_startRecordVideo
     * startRecordVideo为外部JS调用
     */
    public void jsmethod_startRecordVideo(final UZModuleContext moduleContext){
        mJsCallback = moduleContext;
        Intent intent = new Intent(context(), RecordedActivity.class);
        intent.putExtra("appParam", moduleContext.optString("maxTime"));
        intent.putExtra("needResult", true);
        startActivityForResult(intent, ACTIVITY_REQUEST_CODE_A);
    }
}
