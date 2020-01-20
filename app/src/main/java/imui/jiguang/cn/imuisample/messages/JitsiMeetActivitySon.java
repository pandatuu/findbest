package imui.jiguang.cn.imuisample.messages;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.facebook.react.modules.core.PermissionListener;
import java.util.Map;

import imui.jiguang.cn.imuisample.listener.VideoChatControllerListener;
import org.jitsi.meet.sdk.*;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions.Builder;
import org.jitsi.meet.sdk.R.id;
import org.jitsi.meet.sdk.R.layout;
import app.findbest.vip.R;

public class JitsiMeetActivitySon extends FragmentActivity implements JitsiMeetActivityInterface, JitsiMeetViewListener, VideoChatControllerListener {
    protected static final String TAG = JitsiMeetActivitySon.class.getSimpleName();
    public static final String ACTION_JITSI_MEET_CONFERENCE = "org.jitsi.meet.CONFERENCE";
    public static final String JITSI_MEET_CONFERENCE_OPTIONS = "JitsiMeetConferenceOptions";


    private static int leaveType ;

    public JitsiMeetActivitySon() {
    }

    public static void launch(Context context, JitsiMeetConferenceOptions options) {


        Intent intent = new Intent(context,JitsiMeetActivitySon.class);
        intent.setAction("org.jitsi.meet.CONFERENCE");
        intent.putExtra("JitsiMeetConferenceOptions", options);
        context.startActivity(intent);
    }

//    public static void launch(Context context, String url) {
//        JitsiMeetConferenceOptions options = (new Builder()).setRoom(url).build();
//        launch(context, options);
//    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.activity_jitsi_meet);
        if (!this.extraInitialize()) {
            this.initialize();
        }
    }

    public void finishVideo(int  type) {
        leaveType=type;
        System.out.println("离开视频！！！！！！！！！！！！！！！！！");
        JitsiMeetActivitySon.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    leave();
                }catch (Exception e){
                    System.out.println("被动离开视频时，报错了！！！！！");
                }
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });


    }

    protected JitsiMeetView getJitsiView() {
        JitsiMeetFragment fragment = (JitsiMeetFragment)this.getSupportFragmentManager().findFragmentById(id.jitsiFragment);
        return fragment.getJitsiView();
    }

    public void join(@Nullable String url) {
        JitsiMeetConferenceOptions options = (new Builder()).setRoom(url).build();
        this.join(options);
    }

    public void join(JitsiMeetConferenceOptions options) {
        this.getJitsiView().join(options);
    }

    public void leave() {
        this.getJitsiView().leave();
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
            return (JitsiMeetConferenceOptions)intent.getParcelableExtra("JitsiMeetConferenceOptions");
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

    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        JitsiMeetActivityDelegate.requestPermissions(this, permissions, requestCode, listener);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onConferenceJoined(Map<String, Object> data) {
        Log.d(TAG, "Conference joined: " + data);
    }

    public void onConferenceTerminated(Map<String, Object> data) {
        Log.d(TAG, "Conference terminated: " + data);
        System.out.println("onConferenceTerminated");

        if(leaveType!=2){
            this.finishVideo(1);
        }
       
        leaveType=0;
    }

    public void onConferenceWillJoin(Map<String, Object> data) {
        Log.d(TAG, "Conference will join: " + data);
    }

    @Override
    public void closeVideo() {
        this.finishVideo(2);
    }
}
