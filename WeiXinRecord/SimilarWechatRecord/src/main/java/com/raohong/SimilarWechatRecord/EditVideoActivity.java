package com.raohong.SimilarWechatRecord;

import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yixia.camera.MediaRecorderBase;
import com.yixia.videoeditor.adapter.UtilityAdapter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by raohong on 17/2/21.
 * 视频编辑界面
 */

public class EditVideoActivity extends BaseActivity {

    private MyVideoView vv_play;
    private LinearLayout ll_color;

    private int[] drawableBg = new int[]{R.drawable.color1, R.drawable.color2, R.drawable.color3, R.drawable.color4, R.drawable.color5};
    private int[] colors = new int[]{R.color.color1, R.color.color2, R.color.color3, R.color.color4, R.color.color5};
    private int[] expressions = new int[]{R.mipmap.logo};
    private int dp100;
    private int dp90;
    private int dp30;
    private int dp25;
    private int dp20;
    private int dp10;

    private int currentColorPosition;
    private TuyaView tv_video;
    private RelativeLayout rl_touch_view;
    private RelativeLayout rl_edit_text;
    private EditText et_tag;
    private TextView tv_tag;
    private InputMethodManager manager;
    private int windowHeight;
    private int windowWidth;
    private String path;
    private RelativeLayout rl_tuya;
    private RelativeLayout rl_close;
    private RelativeLayout rl_title;
    private RelativeLayout rl_bottom;
    private TextView tv_hint_delete;
    private float videoSpeed = 1;
    private int videoWidth;
    private int videoHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_video);

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        windowWidth = getWindowManager().getDefaultDisplay().getWidth();
        windowHeight = getWindowManager().getDefaultDisplay().getHeight();

        dp100 = (int) getResources().getDimension(R.dimen.dp100);
        dp90 = (int) getResources().getDimension(R.dimen.dp90);
        dp30 = (int) getResources().getDimension(R.dimen.dp30);
        dp10 = (int) getResources().getDimension(R.dimen.dp10);

        initUI();
        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        vv_play.setVideoPath(path);
        vv_play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv_play.setLooping(true);
                vv_play.start();
            }
        });

        vv_play.setOnPlayStateListener(new MyVideoView.OnPlayStateListener() {
            @Override
            public void onStateChanged(boolean isPlaying) {
                if (isPlaying) {
                    videoWidth = vv_play.getVideoWidth();
                    videoHeight = vv_play.getVideoHeight();

                    float ra = videoWidth * 1f / videoHeight;

                    float widthF = videoWidth * 1f / MediaRecorderBase.VIDEO_HEIGHT;
                    float heightF = videoHeight * 1f / MediaRecorderBase.VIDEO_WIDTH;
                    ViewGroup.LayoutParams layoutParams = vv_play.getLayoutParams();
                    layoutParams.width = (int) (windowWidth * widthF);
                    layoutParams.height = (int) (layoutParams.width / ra);
                    vv_play.setLayoutParams(layoutParams);

                    ViewGroup.LayoutParams layoutParams1 = rl_tuya.getLayoutParams();
                    layoutParams1.width = layoutParams.width;
                    layoutParams1.height = layoutParams.height;
                    rl_tuya.setLayoutParams(layoutParams1);
                }
            }
        });
    }

    private void initUI() {

        vv_play = (MyVideoView) findViewById(R.id.vv_play);
        ll_color = (LinearLayout) findViewById(R.id.ll_color);
        tv_video = (TuyaView) findViewById(R.id.tv_video);
        rl_touch_view = (RelativeLayout) findViewById(R.id.rl_touch_view);
        TextView tv_close = (TextView) findViewById(R.id.tv_close);
        TextView tv_finish = (TextView) findViewById(R.id.tv_finish);
        rl_edit_text = (RelativeLayout) findViewById(R.id.rl_edit_text);
        et_tag = (EditText) findViewById(R.id.et_tag);
        tv_tag = (TextView) findViewById(R.id.tv_tag);
        TextView tv_finish_video = (TextView) findViewById(R.id.tv_finish_video);
        rl_tuya = (RelativeLayout) findViewById(R.id.rl_tuya);
        rl_close = (RelativeLayout) findViewById(R.id.rl_close);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        tv_hint_delete = (TextView) findViewById(R.id.tv_hint_delete);
        RelativeLayout rl_cut_size = (RelativeLayout) findViewById(R.id.rl_cut_size);
        RelativeLayout rl_cut_time = (RelativeLayout) findViewById(R.id.rl_cut_time);

        RelativeLayout rl_back = (RelativeLayout) findViewById(R.id.rl_back);

        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_video.backPath();
            }
        });


        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_tag.getText().length() > 0) {
                    addTextToWindow();
                }
            }
        });

        tv_finish_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishVideo();
            }
        });

        rl_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rl_cut_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCutState();
            }
        });

        rl_cut_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditVideoActivity.this, CutTimeActivity.class);
                intent.putExtra("path", path);
                startActivityForResult(intent, 2);
            }
        });

        initColors();
        initExpression();

        et_tag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_tag.setText(s.toString());
            }
        });
    }

    //更改界面模式
    private void changeMode(boolean flag) {
        if (flag) {
            rl_title.setVisibility(View.VISIBLE);
            rl_bottom.setVisibility(View.VISIBLE);
        } else {
            rl_title.setVisibility(View.GONE);
            rl_bottom.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化表情
     */
    private void initExpression() {

        ImageView imageView = new ImageView(this);
        imageView.setPadding(dp10, dp10, dp10, dp10);
        final int result = expressions[0];
        addExpressionToWindow(result);
    }

    /**
     * 添加表情到界面上
     */
    private void addExpressionToWindow(int result) {

        TouchView touchView = new TouchView(this);
        touchView.setBackgroundResource(result);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dp90, dp30);
        /* 上下左右居中 */
        //layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        touchView.setLayoutParams(layoutParams);

        rl_touch_view.addView(touchView);
    }

    /**
     * 添加文字到界面上
     */
    private void addTextToWindow() {

        TouchView touchView = new TouchView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tv_tag.getWidth(), tv_tag.getHeight());
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        touchView.setLayoutParams(layoutParams);
        Bitmap bitmap = Bitmap.createBitmap(tv_tag.getWidth(), tv_tag.getHeight(), Bitmap.Config.ARGB_8888);
        tv_tag.draw(new Canvas(bitmap));
        touchView.setBackground(new BitmapDrawable(bitmap));

        touchView.setLimitsX(0, windowWidth);
        touchView.setLimitsY(0, windowHeight - dp100 / 2);
        touchView.setOnLimitsListener(new TouchView.OnLimitsListener() {
            @Override
            public void OnOutLimits(float x, float y) {
                tv_hint_delete.setTextColor(Color.RED);
            }

            @Override
            public void OnInnerLimits(float x, float y) {
                tv_hint_delete.setTextColor(Color.WHITE);
            }
        });
        touchView.setOnTouchListener(new TouchView.OnTouchListener() {
            @Override
            public void onDown(TouchView view, MotionEvent event) {
                tv_hint_delete.setVisibility(View.VISIBLE);
                changeMode(false);
            }

            @Override
            public void onMove(TouchView view, MotionEvent event) {

            }

            @Override
            public void onUp(TouchView view, MotionEvent event) {
                tv_hint_delete.setVisibility(View.GONE);
                changeMode(true);
                if (view.isOutLimits()) {
                    rl_touch_view.removeView(view);
                }
            }
        });

        rl_touch_view.addView(touchView);

        et_tag.setText("");
        tv_tag.setText("");
    }

    /**
     * 初始化底部颜色选择器
     */
    private void initColors() {

        dp20 = (int) getResources().getDimension(R.dimen.dp20);
        dp25 = (int) getResources().getDimension(R.dimen.dp25);

        for (int x = 0; x < drawableBg.length; x++) {
            RelativeLayout relativeLayout = new RelativeLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            relativeLayout.setLayoutParams(layoutParams);

            View view = new View(this);
            view.setBackgroundDrawable(getResources().getDrawable(drawableBg[x]));
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(dp20, dp20);
            layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT);
            view.setLayoutParams(layoutParams1);
            relativeLayout.addView(view);

            final View view2 = new View(this);
            view2.setBackgroundResource(R.mipmap.color_click);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(dp25, dp25);
            layoutParams2.addRule(RelativeLayout.CENTER_IN_PARENT);
            view2.setLayoutParams(layoutParams2);
            if (x != 0) {
                view2.setVisibility(View.GONE);
            }
            relativeLayout.addView(view2);

            final int position = x;
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentColorPosition != position) {
                        view2.setVisibility(View.VISIBLE);
                        ViewGroup parent = (ViewGroup) v.getParent();
                        ViewGroup childView = (ViewGroup) parent.getChildAt(currentColorPosition);
                        childView.getChildAt(1).setVisibility(View.GONE);
                        tv_video.setNewPaintColor(getResources().getColor(colors[position]));
                        currentColorPosition = position;
                    }
                }
            });

            ll_color.addView(relativeLayout, x);
        }
    }

    boolean isFirstShowEditText;

    /**
     * 弹出键盘
     */
    public void popupEditText() {

        isFirstShowEditText = true;
        et_tag.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isFirstShowEditText) {
                    isFirstShowEditText = false;
                    et_tag.setFocusable(true);
                    et_tag.setFocusableInTouchMode(true);
                    et_tag.requestFocus();
                    isFirstShowEditText = !manager.showSoftInput(et_tag, 0);
                }
            }
        });
    }

    /**
     * 执行文字编辑区域动画
     */
    private void startAnim(float start, float end, AnimatorListenerAdapter listenerAdapter) {

        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(200);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                rl_edit_text.setY(value);
            }
        });
        if (listenerAdapter != null) va.addListener(listenerAdapter);
        va.start();
    }

    /**
     * 更改视频剪切界面
     */
    private void changeCutState() {

        Intent intent = new Intent(this, CutSizeActivity.class);
        intent.putExtra("path", path);
        startActivityForResult(intent, 1);
    }

    /**
     * 合成图片到视频里
     */
    private String mergeImage(String path) {

        //得到涂鸦view的bitmap图片
        Bitmap bitmap = Bitmap.createBitmap(rl_tuya.getWidth(), rl_tuya.getHeight(), Bitmap.Config.ARGB_8888);
        rl_tuya.draw(new Canvas(bitmap));
        //这步是根据视频尺寸来调整图片宽高,和视频保持一致
        Matrix matrix = new Matrix();
        matrix.postScale(videoWidth * 1f / bitmap.getWidth(), videoHeight * 1f / bitmap.getHeight());
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        String imagePath = SDKUtil.VIDEO_PATH + "/tuya.png";
        File file = new File(imagePath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String mergeVideo = SDKUtil.VIDEO_PATH + "/mergeVideo.mp4";


        //ffmpeg -i videoPath -i imagePath -filter_complex overlay=0:0 -vcodec libx264 -profile:v baseline -preset ultrafast -b:v 3000k -g 30 -f mp4 outPath
        StringBuilder sb = new StringBuilder();
        sb.append("ffmpeg");
        sb.append(" -i");
        sb.append(" " + path);
        sb.append(" -i");
        sb.append(" " + imagePath);
        sb.append(" -filter_complex");
        sb.append(" overlay=0:0");
        sb.append(" -vcodec libx264 -profile:v baseline -preset ultrafast -b:v 3000k -g 25");
        sb.append(" -f mp4");
        sb.append(" " + mergeVideo);

        int i = UtilityAdapter.FFmpegRun("", sb.toString());
        if (i == 0) {
            return mergeVideo;
        } else {
            return "";
        }
    }


    /**
     * 获取本地或者url视频的第一帧
     * @param fileData
     * @return
     */
    public static Bitmap getVideoThumbnail(String fileData) {
        Bitmap bitmap = null;
        /*
         * MediaMetadataRetriever 是android中定义好的一个类，提供了统一的接口，
         * 用于从输入的媒体文件中取得帧和元数据
         */
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if( fileData.contains("http://") || fileData.contains("https://") ) {
                System.out.println("包含该字符串");
                //根据文件路径获取缩略图
                retriever.setDataSource(fileData, new HashMap());
            } else {
                //根据文件路径获取缩略图
                retriever.setDataSource(fileData);
            }
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }

        System.out.println("保存成功");
        return bitmap;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        if(rl_edit_text.getVisibility() == View.VISIBLE){
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        vv_play.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vv_play.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case 1:
                vv_play.setVideoPath(path);
                vv_play.start();
                break;
            case 2:
                vv_play.setVideoPath(path);
                vv_play.start();
                break;
        }
    }

    private boolean isPen;
    private boolean isImage;

    @SuppressLint("StaticFieldLeak")
    private void finishVideo() {

        if(tv_video.getPathSum() ==0){
            isPen = false;
        }else{
            isPen = true;
        }

        if(rl_touch_view.getChildCount() == 0){
            isImage = false;
        }else{
            isImage = true;
        }

        if(!isPen && !isImage){
            Intent intent = new Intent();
            intent.putExtra("videoPath", path);
            setResult(RESULT_OK, intent);
            finish();
            return ;
        }

        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                showProgressDialog();
            }

            @Override
            protected String doInBackground(Void... params) {
                String videoPath = mergeImage(path);
                return videoPath;
            }

            @Override
            protected void onPostExecute(String result) {
                closeProgressDialog();
                if (!TextUtils.isEmpty(result)) {
                    Intent intent = new Intent();
                    intent.putExtra("videoPath", result);
                    setResult(RESULT_OK, intent);


                    String mergeVideo = SDKUtil.VIDEO_PATH + "/mergeVideo.mp4";

                    String imagePaths = SDKUtil.VIDEO_PATH + "/ThumIMG.png";

                    Bitmap bitmaps = getVideoThumbnail(mergeVideo);

                    System.out.println("已保存视频"+mergeVideo);

                    File files = new File(imagePaths);//将要保存图片的路径
                    try {
                        BufferedOutputStream boss = new BufferedOutputStream(new FileOutputStream(files));
                        bitmaps.compress(Bitmap.CompressFormat.PNG, 100, boss);
                        boss.flush();
                        boss.close();
                        System.out.println("已保存缩略图"+imagePaths);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "视频编辑失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
