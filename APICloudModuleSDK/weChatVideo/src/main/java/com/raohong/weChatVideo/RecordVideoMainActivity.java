package com.raohong.weChatVideo;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;
import com.yixia.camera.MediaRecorderBase;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * 该类映射至Javascript中moduleDemo对象<br><br>
 * <strong>Js Example:</strong><br>
 * var module = api.require('moduleDemo');<br>
 * module.xxx();
 * @author APICloud
 *
 */
public class RecordVideoMainActivity extends UZModule {

	static final int ACTIVITY_REQUEST_CODE_A = 100;

	private AlertDialog.Builder mAlert;
	private Bitmap thumbnailBitmap = null;
	private UZModuleContext mJsCallback;
	private String thumbnailPath;
	private File thumbnailFile;
	private int maxTime;

	public RecordVideoMainActivity(UZWebView webView) {
		super(webView);
	}

	/**
	 * <strong>函数</strong><br><br>
	 * 该函数映射至Javascript中moduleDemo对象的showAlert函数<br><br>
	 * <strong>JS Example：</strong><br>
	 * moduleDemo.showAlert(argument);
	 *
	 * @param moduleContext  (Required)
	 */
	public void jsmethod_showAlert(final UZModuleContext moduleContext){
		if(null != mAlert){
			return;
		}
		String showMsg = moduleContext.optString("msg");
		mAlert = new AlertDialog.Builder(context());
		mAlert.setTitle("这是标题");
		mAlert.setMessage(showMsg);
		mAlert.setCancelable(false);
		mAlert.setPositiveButton("确定", new OnClickListener() {
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

	/**
	 * <strong>函数</strong><br><br>
	 * 该函数映射至Javascript中moduleDemo对象的startActivityForResult函数<br><br>
	 * <strong>JS Example：</strong><br>
	 * moduleDemo.startActivityForResult(argument);
	 *
	 * @param moduleContext  (Required)
	 */
	public void jsmethod_startActivityForResult(UZModuleContext moduleContext){
		mJsCallback = moduleContext;
		Intent intent = new Intent(context(), StartRecordActivity.class);
		intent.putExtra("maxTime", moduleContext.optString("maxTime"));
		startActivityForResult(intent, ACTIVITY_REQUEST_CODE_A);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK && requestCode == ACTIVITY_REQUEST_CODE_A){
//			String imagePath = data.getStringExtra("imagePath");
			String videoPath = data.getStringExtra("videoPath");

			if(!TextUtils.isEmpty(videoPath)){
				thumbnailPath = SDKUtil.VIDEO_PATH + "/thumbnail.png";
				MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
				metadataRetriever.setDataSource(videoPath);
				thumbnailBitmap = metadataRetriever.getFrameAtTime(1*1000*1000, MediaMetadataRetriever.OPTION_CLOSEST);
				thumbnailFile = new File(thumbnailPath);//将要保存图片的路径
			}

			JSONObject json = new JSONObject();
			try {
				if(!TextUtils.isEmpty(videoPath)) {
					BufferedOutputStream thumbnailFileBuff = new BufferedOutputStream(new FileOutputStream(thumbnailFile));
					thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 100, thumbnailFileBuff);
					thumbnailFileBuff.flush();
					thumbnailFileBuff.close();
				}

				json.put("videoPath", !TextUtils.isEmpty(videoPath)?videoPath:"null");
				json.put("thumbnailPath", !TextUtils.isEmpty(videoPath) ? thumbnailPath : "null");
				json.put("status", !TextUtils.isEmpty(videoPath)?"true":"false");

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if(null != mJsCallback){
				mJsCallback.success(json, true);
				mJsCallback = null;
			}
		}
	}
}
