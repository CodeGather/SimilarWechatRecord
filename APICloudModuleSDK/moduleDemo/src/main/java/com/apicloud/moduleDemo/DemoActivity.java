package com.apicloud.moduleDemo;

import org.json.JSONException;
import org.json.JSONObject;
import com.apicloud.sdk.moduledemo.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 原生activity
 *
 */
public class DemoActivity extends Activity {
    public static final int RECORD_SYSTEM_VIDEO = 1;
    //    public static final int RECORD_CUSTOM_VIDEO = 2;
    private VideoView mVideoView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mo_demo_main_activity);
		Intent data = getIntent();
		String appParam = data.getStringExtra("maxTime");
		if(null != appParam){
			TextView text = (TextView) findViewById(R.id.text);
			text.setText("JS传入的参数为：" + appParam);
		}
		final boolean needResult = data.getBooleanExtra("needResult", false);
		Button btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(needResult){
					Uri fileUri = Uri.fromFile(getOutputMediaFile());
					Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
					intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);     //限制的录制时长 以秒为单位
//        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024);        //限制视频文件大小 以字节为单位
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);      //设置拍摄的质量0~1
//        intent.putExtra(MediaStore.EXTRA_FULL_SCREEN, false);        // 全屏设置
					startActivityForResult(intent, RECORD_SYSTEM_VIDEO);
				};
				finish();
			}
		});
	}

    /**
     * Create a File for saving an video
     */
    private File getOutputMediaFile() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "请检查SDCard！", Toast.LENGTH_SHORT).show();
            return null;
        }

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "RecordVideo");
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        return mediaFile;
    }

//    /**
//     * 启用系统相机录制
//     *
//     * @param view
//     */
//    public void reconverIntent(View view) {
//
//
//    }
}

