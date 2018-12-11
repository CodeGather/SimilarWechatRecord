package com.raohong.SimilarWechatRecord;

import android.content.Intent;

import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

public class MainActivity extends UZModule {

    static final int ACTIVITY_REQUEST_CODE_A = 100;
    private UZModuleContext mJsCallback;

    public MainActivity(UZWebView webView) {
        super(webView);
    }

    public void jsmethod_startRecordVideo(final UZModuleContext moduleContext){
        mJsCallback = moduleContext;
        Intent intent = new Intent(context(), RecordedActivity.class);
        intent.putExtra("appParam", moduleContext.optString("maxTime"));
        intent.putExtra("needResult", true);
        startActivityForResult(intent, ACTIVITY_REQUEST_CODE_A);
    }
}
