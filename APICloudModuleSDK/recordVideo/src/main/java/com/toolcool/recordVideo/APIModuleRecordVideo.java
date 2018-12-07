package com.toolcool.recordVideo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONException;
import org.json.JSONObject;


/*
 * 需要在app  build.gradle中添加 compile project(':recordVideo')
 */
public class APIModuleRecordVideo extends UZModule {

    private AlertDialog.Builder mAlert;

    public APIModuleRecordVideo(UZWebView webView) {
        super(webView);
    }

    /**
     * <strong>函数</strong><br><br>
     * 该函数映射至Javascript中recordVideo对象的showAlertView函数<br><br>
     * <strong>JS Example：</strong><br>
     * moduleDemo.showAlertView(argument);
     *
     * @param moduleContext  (Required)
     */
    public void jsmethod_showAlertView(final UZModuleContext moduleContext){
        if(null != mAlert){
            return;
        }
        String showMsg = moduleContext.optString("maxTime");
        mAlert = new AlertDialog.Builder(context());
        mAlert.setTitle("测试事件");
        mAlert.setMessage(showMsg);
        mAlert.setCancelable(false);
        mAlert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mAlert = null;
                JSONObject ret = new JSONObject();
                try {
                    ret.put("buttonIndex", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                moduleContext.success(ret, true);
            }
        });
        mAlert.show();
    }
}
