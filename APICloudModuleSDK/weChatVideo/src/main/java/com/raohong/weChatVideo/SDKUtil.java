package com.raohong.weChatVideo;

import android.content.Context;
import com.yixia.camera.VCamera;

import java.io.File;

/*
 *  Created by 21克的爱情 on 2018/12/17
 *  Email：raohong07@163.com
 */

public class SDKUtil {

    public static String VIDEO_PATH =  "/sdcard/WechatVideoResources/";

    public static void initSDK(Context context) {

        VIDEO_PATH += String.valueOf(System.currentTimeMillis());
        File file = new File(VIDEO_PATH);
        if (!file.exists()) file.mkdirs();

        //设置视频缓存路径
        VCamera.setVideoCachePath(VIDEO_PATH);

        // log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(true);

        // 初始化拍摄SDK，必须
        VCamera.initialize(context);
    }
}
