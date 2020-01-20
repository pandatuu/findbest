package app.findbest.vip;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.react.modules.core.PermissionListener;

import org.jetbrains.annotations.NotNull;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions.Builder;
import org.jitsi.meet.sdk.JitsiMeetFragment;
import org.jitsi.meet.sdk.JitsiMeetView;
import org.jitsi.meet.sdk.JitsiMeetViewListener;
import org.jitsi.meet.sdk.R.id;
import org.jitsi.meet.sdk.R.layout;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import imui.jiguang.cn.imuisample.listener.VideoChatControllerListener;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class JitsiMeetActivitySon2 extends FragmentActivity implements ActivityCompat.OnRequestPermissionsResultCallback, JitsiMeetActivityInterface, JitsiMeetViewListener, VideoChatControllerListener {
    protected static final String TAG = JitsiMeetActivitySon2.class.getSimpleName();
    public static final String ACTION_JITSI_MEET_CONFERENCE = "org.jitsi.meet.CONFERENCE";
    public static final String JITSI_MEET_CONFERENCE_OPTIONS = "JitsiMeetConferenceOptions";

    private Fragment backgroundFragment;
    private Fragment shareFragment;

    private static int leaveType = 2;

    Context context;

    final Handler cwjHandler = new Handler();
    View view;

    Boolean shareClickFlag = true;

    public JitsiMeetActivitySon2() {
    }

    public void launch(Context context, JitsiMeetConferenceOptions options, String interviewId) {

        Intent intent = new Intent(context, JitsiMeetActivitySon2.class);
        intent.setAction("org.jitsi.meet.CONFERENCE");
        intent.putExtra("JitsiMeetConferenceOptions", options);
        startActivity(intent);

        overridePendingTransition(
                R.anim.right_in,
                R.anim.left_out
        );
    }

//    public static void launch(Context context, String url) {
//        JitsiMeetConferenceOptions options = (new Builder()).setRoom(url).build();
//        launch(context, options);
//    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        this.setContentView(layout.activity_jitsi_meet);
        if (!this.extraInitialize()) {
            this.initialize();
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("resultCode----");

    }

    public void finishVideo(int type) {
        leaveType = type;
        System.out.println("离开视频！！！！！！！！！！！！！！！！！");
        finishFlag = true;
        JitsiMeetActivitySon2.this.runOnUiThread(() -> {
            try {
                leave();
            } catch (Exception e) {
                System.out.println("被动离开视频时，报错了！！！！！");
            }

            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        });


    }

    protected JitsiMeetView getJitsiView() {

        JitsiMeetFragment fragment = (JitsiMeetFragment) this.getSupportFragmentManager().findFragmentById(id.jitsiFragment);
        return fragment.getJitsiView();
        //return new JitsiMeetView(this);
    }

    public void join(@Nullable String url) {
        JitsiMeetConferenceOptions options = (new Builder()).setRoom(url).build();
        this.join(options);
    }

    public void join(JitsiMeetConferenceOptions options) {
        this.getJitsiView().join(options);
    }

    public void leave() {
        System.out.println("离开视频！！！！！");

        try {
            this.getJitsiView().leave();
        } finally {

        }


    }

    @Nullable
    private JitsiMeetConferenceOptions getConferenceOptions(Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.VIEW".equals(action)) {
            Uri uri = intent.getData();
            if (uri != null) {
                return (new Builder()).setRoom(uri.toString()).build();
            }
        } else if ("org.jitsi.meet.CONFERENCE".equals(action)) {
            return (JitsiMeetConferenceOptions) intent.getParcelableExtra("JitsiMeetConferenceOptions");
        }

        return null;
    }

    protected boolean extraInitialize() {
        return false;
    }

    protected void initialize() {
        this.getJitsiView().setListener(this);
        this.join(this.getConferenceOptions(this.getIntent()));
    }

    public void onBackPressed() {
        JitsiMeetActivityDelegate.onBackPressed();
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        JitsiMeetConferenceOptions options;
        if ((options = this.getConferenceOptions(intent)) != null) {
            this.join(options);
        } else {
            JitsiMeetActivityDelegate.onNewIntent(intent);
        }
    }

    protected void onUserLeaveHint() {
        this.getJitsiView().enterPictureInPicture();
    }



    int count = 60;
    int min = 0;
    int second = 0;
    String minStr = "";
    String secStr = "";
    Boolean finishFlag = false;
    String showString = "";
    // 加入视频后的返回
    public void onConferenceJoined(Map<String, Object> data) {
        Log.d(TAG, "Conference joined: " + data);


//        AlertDialog dialog = new AlertDialog.Builder(JitsiMeetActivitySon.this).setTitle("警告")
//                .setIcon(android.R.drawable.ic_dialog_info)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d(TAG, "onClick");
//                        JitsiMeetActivitySon.this.finish();
//                    }
//                }).show();
//        Log.e(TAG, "AlertDialog");
//        dialog.setCanceledOnTouchOutside(false);


    }

    public void onConferenceTerminated(Map<String, Object> data) {
        Log.d(TAG, "Conference terminated: " + data);
        System.out.println("onConferenceTerminated");

        if (leaveType != 2) {
            //thiscontext.sendMessageToHimToshutDownVideo(thisInterviewId);
            // this.finishVideo(1);
        }

        leaveType = 0;


        this.finishVideo(1);
    }

    public void onConferenceWillJoin(Map<String, Object> data) {
        Log.d(TAG, "Conference will join: " + data);
    }

    @Override
    public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void closeVideo() {
        this.finishVideo(2);
    }
}
