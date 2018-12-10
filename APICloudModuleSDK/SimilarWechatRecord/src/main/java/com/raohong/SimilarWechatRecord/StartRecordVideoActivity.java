package com.raohong.SimilarWechatRecord;

import android.content.Intent;

import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

public class StartRecordVideoActivity extends UZModule {

    static final int ACTIVITY_REQUEST_CODE_A = 100;

    private UZModuleContext mJsCallback;

    public StartRecordVideoActivity(UZWebView webView) {
        super(webView);
    }


    /**
     * <strong>函数</strong><br><br>
     * 该函数映射至Javascript中moduleDemo对象的startActivityForResult函数<br><br>
     * <strong>JS Example：</strong><br>
     * moduleDemo.startActivityForResult(argument);
     *
     * @param moduleContext  (Required)
     */
    public void jsmethod_startRecordVideo(UZModuleContext moduleContext){
        mJsCallback = moduleContext;
        Intent intent = new Intent(context(), RecordedActivity.class);
        intent.putExtra("appParam", moduleContext.optString("maxTime"));
        intent.putExtra("needResult", true);
        startActivityForResult(intent, ACTIVITY_REQUEST_CODE_A);
    }
}
