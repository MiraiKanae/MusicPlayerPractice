package com.example;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.MediaController;
import androidx.media3.session.MediaLibraryService;
import androidx.media3.session.SessionToken;


import com.google.common.annotations.GwtCompatible;
import com.google.common.util.concurrent.ListenableFuture;
import com.unity3d.player.UnityPlayer;

import java.util.concurrent.ExecutionException;

public class MP_MainActivity extends Activity {

    MediaBrowser mediaBrowser;
    MediaController mediaController;

    //PlayerListenerの代わり
    Player.Listener playerlistener;

    MP_MusicService mService;
    Boolean mBound;
    Context context = UnityPlayer.currentActivity.getApplicationContext();
    //Context context = getApplicationContext();

    @Override
    @GwtCompatible
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MusicPlayer_A", "Start_onCreate");

        //MediaBrowserとMediaController用のsessionToken取得
        ComponentName componentName = new ComponentName(context, MP_MusicService.class);
        Log.d("MusicPlayer_A", "componentName");
        SessionToken sessionToken = new SessionToken(context, componentName);
        Log.d("MusicPlayer_A", "sessionToken");

        //サービスは開始しておく
        //Activity破棄と同時にServiceも停止して良いならこれは不要
        //Serviceのバインドもしておく
        Intent intent = new Intent(context, MP_MusicService.class);
        Log.d("MusicPlayer_A", "intent");
        startService(intent);
        Log.d("MusicPlayer_A", "startService");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.d("MusicPlayer_A", "bindService");


        //MediaBrowser初期化
        MediaBrowser.Builder builder_mb = new MediaBrowser.Builder(context, sessionToken);
        Log.d("MusicPlayer_A", "builder_mb");
        ListenableFuture<MediaBrowser> future_mb = builder_mb.buildAsync();
        Log.d("MusicPlayer_A", "future_mb");
        future_mb.addListener(() -> {
            try {
                mediaBrowser = future_mb.get();
                Log.d("MusicPlayer_A", "mediaBrowser");
                // The session accepted the connection.
            } catch (ExecutionException e) {
                if (e.getCause() instanceof SecurityException) {
                    // The session rejected the connection.
                    Log.d("MusicPlayer_A", "Null_Listener_SecurityException");
                }
            } catch (InterruptedException e) {
                // The session rejected the connection.
                Log.d("MusicPlayer_A", "Null_Listener_InterruptedException");
            }
        }, ContextCompat.getMainExecutor(context));
        Log.d("MusicPlayer_A", "mediaBrowser_addListener");

        //MediaController初期化
        MediaController.Builder builder_mc = new MediaController.Builder(context, sessionToken);
        Log.d("MusicPlayer_A", "builder_mc");
        ListenableFuture<MediaController> future_mc = builder_mc.buildAsync();
        Log.d("MusicPlayer_A", "future_mc");
        future_mc.addListener(() -> {
            try {
                mediaController = future_mc.get();
                Log.d("MusicPlayer_A", "mediaController");
                // The session accepted the connection.
            } catch (ExecutionException e) {
                if (e.getCause() instanceof SecurityException) {
                    // The session rejected the connection.
                    Log.d("MusicPlayer_A", "Null_Listener_SecurityException");
                }
            } catch (InterruptedException e) {
                // The session rejected the connection.
                Log.d("MusicPlayer_A", "Null_Listener_InterruptedException");
            }
        }, ContextCompat.getMainExecutor(context));
        Log.d("MusicPlayer_A", "mediaController_addListener");

    }

    //バインド確定後のサービスとの通信状況
    ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MP_MusicService.LocalBinder binder = (MP_MusicService.LocalBinder) service;
            Log.d("MusicPlayer_A", "bider");
            mService = binder.getService();
            Log.d("MusicPlayer_A", "getService");
            mBound = true;
            Log.d("MusicPlayer_A", "ServiceConnection_true");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
            Log.d("MusicPlayer_A", "ServiceConnection_false");
        }
    };

}