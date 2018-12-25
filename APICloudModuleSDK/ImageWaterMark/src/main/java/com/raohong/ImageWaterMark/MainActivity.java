package com.raohong.ImageWaterMark;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Spinner;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class MainActivity extends UZModule {

    public static final String TAG = "Sample";
    private static final int ACTIVITY_REQUEST_CODE_A = 100;
    private UZModuleContext mJsCallback;
    private Boolean mRadioGroupValue=false;     // 默认使用文字做为水印
    private Boolean mShaderSwitch=false;         // 默认不使用彩色字体
    private String mRubberStampText = "", mBaseBitMapUrl;        // 默认水印为空
    private int mRotationSeekBarValue=0, mTextSizeSeekBarValue=16,mAlphaSeekBarValue=255;
    private int mTextFonts;
    private int mRubberStampPosition=0;
    private RubberStamp mRubberStamp;
    // 默认字体红色
    @ColorInt
    private int mTextColorValue = Color.RED;
    // 默认背景透明
    @ColorInt
    private int mTextBackgroundColorValue = Color.TRANSPARENT;

    public MainActivity(UZWebView webView) {
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
    public void jsmethod_open(UZModuleContext moduleContext){
        WaterMarkMainActivity waterMarkMainActivity = new WaterMarkMainActivity();
        mJsCallback = moduleContext;
//        Intent intent = moduleContext.optString("maxTime");
        mRubberStampPosition = Integer.valueOf(moduleContext.optString("position"));
        /* 设置字体颜色 */
        mTextColorValue = Color.parseColor(moduleContext.optString("fontColor"));
        /* 设置背景颜色 */
        mTextBackgroundColorValue = Color.parseColor(moduleContext.optString("backgroundColor"));
        /* 设置是使用文字或者图片来做水印 */
        mRadioGroupValue = Boolean.valueOf(moduleContext.optString("textOrImage"));
        /* 设置文字水印内容 */
        mRubberStampText = moduleContext.optString("content");
        /* 设置是否彩虹字体 */
        mShaderSwitch = Boolean.valueOf(moduleContext.optString("isShader"));
        /* 设置字体旋转角度 */
        mRotationSeekBarValue = Integer.valueOf(moduleContext.optString("rotateAngle"));
        /* 设置字体大小 */
        mTextSizeSeekBarValue = Integer.valueOf(moduleContext.optString("fontSize"));
        /* 设置水印透明度字体 */
        mAlphaSeekBarValue = Integer.valueOf(moduleContext.optString("alpha"));
        /* 设置需要添加水印图片的路径 */
        mBaseBitMapUrl = moduleContext.optString("waterUrl");

        mTextFonts = Integer.valueOf(moduleContext.optString("fontStyle"));
//        mRubberStamp = new RubberStamp(this);
        Log.e("接收参数：","-------------------"+mRubberStampPosition+"----------------");
        Log.e("接收参数：","-------------------"+mTextColorValue+"----------------");
        Log.e("接收参数：","-------------------"+mTextBackgroundColorValue+"----------------");
        Log.e("接收参数：","-------------------"+mRadioGroupValue+"----------------");
        Log.e("接收参数：","-------------------"+mRubberStampText+"----------------");
        Log.e("接收参数：","-------------------"+mShaderSwitch+"----------------");
        Log.e("接收参数：","-------------------"+mRotationSeekBarValue+"----------------");
        Log.e("接收参数：","-------------------"+mTextSizeSeekBarValue+"----------------");
        Log.e("接收参数：","-------------------"+mAlphaSeekBarValue+"----------------");
        Log.e("接收参数：","-------------------"+mBaseBitMapUrl+"----------------");
        Log.e("接收参数：","-------------------"+mTextFonts+"----------------");

        waterMarkMainActivity.init();
        waterMarkMainActivity.setListeners();
        waterMarkMainActivity.setDefaults();
    }

}

