package com.example;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaLibraryService;
import androidx.media3.session.MediaSession;
import androidx.media3.session.SessionCommand;
import androidx.media3.session.SessionCommands;
import androidx.media3.session.SessionResult;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class MP_MusicService extends MediaLibraryService
{
    Context context;
    MediaLibraryService mediaLibraryService;
    MediaLibrarySession mediaSession;

    IBinder binder_s;

    ExoPlayer exoPlayer;
    String mpsId = "root";
    Bundle extras;

    Bundle bundle;
    String REWIND_30;
    String FAST_FWD_30;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MusicPlayer_S","Start_onCreate");

        exoPlayer = new ExoPlayer.Builder(context).build();
        Log.d("MusicPlayer_S","new_ExoPlayer");
        mediaSession = new  MediaLibraryService.MediaLibrarySession.Builder(mediaLibraryService, exoPlayer,customCallback)
                .build();
        Log.d("MusicPlayer_S","new_MediaSession");

        bundle = new Bundle();
        Log.d("MusicPlayer_S","new_Bundle");
    }


    //Activityのバインドのコールバック
    @Nullable
    @Override
    public IBinder onBind(@Nullable Intent intent) {
        super.onBind(intent);
        Log.d("MusicPlayer_S","Start_onBind");

        binder_s = new LocalBinder();
        Log.d("MusicPlayer_S","LocalBinder");
        return binder_s;
    }

    //ActivityのServiceConnection用のインスタンスを返すクラス
    public class LocalBinder extends Binder {
        MP_MusicService getService() {
            Log.d("MusicPlayer_S","getService");
            // Return this instance of LocalService so clients can call public methods
            return MP_MusicService.this;
        }
    }

    @Nullable
    @Override
    public MediaLibrarySession onGetSession(MediaSession.ControllerInfo controllerInfo) {
        Log.d("MusicPlayer_S","onGetSession");
        return mediaSession;
    }

    MediaLibrarySession.Callback customCallback = new MediaLibrarySession.Callback() {

        @Override
        public MediaSession.ConnectionResult onConnect(MediaSession session, MediaSession.ControllerInfo controller) {
            MediaSession.ConnectionResult connectionResult = MediaLibrarySession.Callback.super.onConnect(session, controller);
            Log.d("MusicPlayer_S","ConnectionResult");
            SessionCommands sessionCommands =
                    connectionResult.availableSessionCommands
                            .buildUpon()
                            // Add custom commands
                            .add(new SessionCommand(REWIND_30,bundle))
                            .add(new SessionCommand(FAST_FWD_30,bundle))
                            .build();
            Log.d("MusicPlayer_S","SessionCommand");

            return MediaLibrarySession.Callback.super.onConnect(session, controller);
        }

        @Override
        public ListenableFuture<SessionResult> onCustomCommand(MediaSession session, MediaSession.ControllerInfo controller, SessionCommand customCommand, Bundle args) {

            if (customCommand.customAction == REWIND_30) {
                session.getPlayer().seekTo(-30000);
                Log.d("MusicPlayer_S","SeekBack_30s");
            }

            else if (customCommand.customAction == FAST_FWD_30) {
                session.getPlayer().seekTo(30000);
                Log.d("MusicPlayer_S","SeekNext_30s");
            }

            return Futures.immediateFuture(new  SessionResult(SessionResult.RESULT_SUCCESS));
        }
    };
}
