package com.sq.futuralvoicesdkdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.jack.iatlib.IATCallback;
import com.jack.iatlib.XunFeiIATMgr;
import com.jack.nlplib.AIUIMgr;
import com.jack.nlplib.NlpResultCallBack;
import com.jack.ttslib.TTSManager;
import com.sq.futuralvoicesdkdemo.databinding.ActivityMainBinding;
import com.sq.futuralvoicesdkdemo.event.WakeupEvent;
import com.sq.futuralvoicesdkdemo.listeners.ISpeakListenerImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;

public class MainActivity extends PermissionActivity implements IATCallback, NlpResultCallBack {
    private String TAG = MainActivity.class.getSimpleName();
    private String iatText;
    private ActivityMainBinding uiBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //语音转文本
        XunFeiIATMgr.getInstance().init(getApplicationContext());
        XunFeiIATMgr.getInstance().setIatCallback(this);

        //文本转语义
        AIUIMgr.getInstance().init(getApplicationContext());
        AIUIMgr.getInstance().setNlpResultCallBack(this);

        //TTS使用
        TTSManager.getInstance().init(getApplicationContext(), TTSManager.TTS_TYPE_XUNFEI, "");
    }

    @Override
    protected void allPermissionGranted() {
        //获得所有权限后，再进行其它操作
        Log.e(TAG, "@@@@@@@@ allPermissionGranted");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 每次唤醒时进入
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WakeupEvent event) {
        Log.e(TAG, "@@@@@@@@ onMessageEvent");
        uiBinding.wakeupResult.setText(R.string.i_am_here);
        TTSManager.getInstance().speak(getString(R.string.i_am_here), new ISpeakListenerImpl() {
            @Override
            public void onComplete() {
                XunFeiIATMgr.getInstance().startRecord();
            }
        });
    }

    @Override
    public void onVoiceStart() {
        Log.e(TAG, "#### onVoiceStart");
    }

    @Override
    public void onVoiceEnd(String filePath) {
        Log.e(TAG, "#### onVoiceEnd");
        runOnUiThread(()->{
            uiBinding.iatResult.setText(iatText);
        });
        requestNlp(iatText);
    }

    @Override
    public void onVoiceError(String err) {

        Log.e(TAG, "#### onVoiceError: " + err);
    }

    @Override
    public void onVoiceResult(String text, boolean isLast) {
        Log.e(TAG, "#### onVoiceResult, text: " + text + ", isLast: " + isLast);
        iatText = text;
        runOnUiThread(()->{
            uiBinding.iatResult.setText(iatText);
        });
    }

    private void requestNlp(String text) {
        if (TextUtils.isEmpty(text)) return;

        //调用NLP api查询结果
        if (SpeechConstant.TYPE_CLOUD.equals(SpeechConstant.TYPE_CLOUD)) { //只有连网时才会有请求
            AIUIMgr.getInstance().sendText(text);
        }
    }

    @Override
    public void onNlpResult(String jsonText, String defaultAnswer, String service) {
        Log.e(TAG, "#### onVoiceResult, defaultAnswer: " + defaultAnswer + ", service: " + service);
        //在这里需要处理语义，以及自定义行为
        runOnUiThread(()->{
            uiBinding.nlpResult.setText(defaultAnswer);
        });
    }

    @Override
    public void onNlpError(String err) {

    }
}