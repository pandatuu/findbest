package imui.jiguang.cn.imuisample.messages;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.*;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import app.findbest.vip.R;
import app.findbest.vip.application.App;
import app.findbest.vip.message.activity.videoRequestActivity;
import app.findbest.vip.message.listener.RecieveMessageListener;
import cgland.job.sk_android.utils.UploadPic;
import cgland.job.sk_android.utils.UploadVoice;
import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.emoji.DefEmoticons;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.ViewHolderController;
import cn.jiguang.imui.messages.ptr.PtrHandler;
import cn.jiguang.imui.messages.ptr.PullToRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import com.jaeger.library.StatusBarUtil;
import com.neovisionaries.ws.client.WebSocketState;

import github.ll.view.FloatOnKeyboardLayout;
import imui.jiguang.cn.imuisample.fragment.common.ShadowFragment;
import imui.jiguang.cn.imuisample.listener.VideoChatControllerListener;
import imui.jiguang.cn.imuisample.models.DefaultUser;
import imui.jiguang.cn.imuisample.models.InterviewState;
import imui.jiguang.cn.imuisample.models.MyMessage;
import imui.jiguang.cn.imuisample.models.ResumeListItem;
import imui.jiguang.cn.imuisample.utils.Http;
import imui.jiguang.cn.imuisample.views.ChatView;
import io.github.sac.Ack;
import io.github.sac.Socket;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;

import org.jetbrains.annotations.NotNull;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.jiguang.imui.messages.BaseMessageViewHolder.*;

@SuppressWarnings("ALL")
public class MessageListActivity extends Activity implements View.OnTouchListener,
        EasyPermissions.PermissionCallbacks, SensorEventListener, ShadowFragment.ShadowScreen
//        , DropMenuFragment.DropMenu, ResumeMenuFragment.ResumeMenu
{

    private final static String TAG = "MessageListActivity";
    private final int RC_RECORD_VOICE = 0x0001;
    private final int RC_CAMERA = 0x0002;
    private final int RC_PHOTO = 0x0003;

    int LINE_EXCHANGE = 1;
    int PHONE_EXCHANGE = 2;
    int SCROLL = 1;

    private ChatView mChatView;
    private MsgListAdapter<MyMessage> mAdapter;
    private InputMethodManager mImm;
    private Window mWindow;
    private HeadsetDetectReceiver mReceiver;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private PullToRefreshLayout pullToRefreshLayout;
//    private LinearLayout message_middle_select_bar4;
//    private LinearLayout message_middle_select_bar3;
//    private LinearLayout message_middle_select_bar2;
//    private LinearLayout message_middle_select_bar1;

//    private ImageView message_middle_select_bar_image1;
//    private ImageView message_middle_select_bar_image2;
//    private ImageView message_middle_select_bar_image3;


    private LinearLayout topPart;
    private FloatOnKeyboardLayout bottomPartContainer;
    private MessageList msg_list;
    private TextView hisName;


    private TextView translateText;
//    private TextView hisCompany;

    boolean isInitHistory = true;
    boolean isFirstRequestHistory = true;
    Integer now_groupId = -100;

    //是初次聊天吗，是的话需要打招呼
    Boolean isFirstChat = false;

    JSONArray historyMessage;
    String lastShowedMessageId;
    String topBlankMessageId = null;
    private ArrayList<String> mPathList = new ArrayList<>();
    private ArrayList<String> mMsgIdList = new ArrayList<>();

    ShadowFragment fragmentShadow = null;
//    DropMenuFragment dropMenuFragment = null;
//
//    ResumeMenuFragment resumeMenuFragment = null;

    JSONObject sendMessageModel = new JSONObject();

    App application;
    Socket socket;
    String messageId = "";

    Socket.Channel channelSend = null;

    String MY_ID = "";

    String HIS_ID = "";
    //token
    String authorization = "";

    //职位信息是否可已经显示  是否还可以显示
    Boolean positionshowedFlag = true;

    Context thisContext = this;


    String thisCommunicationPositionId = "";
    String hisLogo = "";
    String myLogo = "";

    JitsiMeetActivitySon jitsiMeetActivitySon = new JitsiMeetActivitySon();
    VideoChatControllerListener videoChatControllerListener;


    Date startVideoTime;

    //最新的视频邀请消息，对方主动关闭后，手机端不能再点击
    List<MyMessage> latestVideoMessage = new ArrayList<MyMessage>();
    //视频y邀请的interviewId
    List<String> latestVideoMessageInterviewId = new ArrayList<String>();

    //是否允许交换信息
    Boolean exchangeInfoEnabled = false;

    Boolean sendMeesage_me = false;
    Boolean sendMessage_he = false;


    SimpleDateFormat sdf_show_hour = new SimpleDateFormat("HH:mm");
    SimpleDateFormat sdf_show_month = new SimpleDateFormat("MM月dd日 HH:mm");
    SimpleDateFormat sdf_show_year = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

    String nowTimeString = sdf_show_year.format(new Date());


    Long latestMessageTime = null;

    @Override
    protected void onStart() {
        super.onStart();


        Toolbar toolbar = findViewById(R.id.message_toolBar);
        setActionBar(toolbar);
        getActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });

        StatusBarUtil.setTranslucentForImageView(this, 0, toolbar);
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initMessageChannel();
        initVideoInterview();
        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = getWindow();
        registerProximitySensorListener();

        //主聊天控件
        mChatView = (ChatView) findViewById(R.id.chat_view);
        mChatView.initModule();

        mChatView.setOnTouchListener(this);
        initChatViewMenuClickListener();
        initChatViewRecordVoiceListener();
        initChatViewCameraCallbackListener();
        mChatView.getChatInputView().
                getInputView().
                setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        scrollToBottom();
                        return false;
                    }
                });
        mChatView.getSelectAlbumBtn().
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

        setGreeting();

        pullToRefreshLayout = findViewById(R.id.pull_to_refresh_layout);
        topPart = findViewById(R.id.topPart);
        bottomPartContainer = findViewById(R.id.bottomPartContainer);
        bottomPartContainer.setMarginKeyboard(100);
        msg_list = findViewById(R.id.msg_list);
        msg_list.setScrollToTopListener(new MessageList.ScrollToTopListener() {
            @Override
            public void hitTop() {
                loadNextPage(lastShowedMessageId);
            }
        });


        translateText = findViewById(R.id.translateText);
        translateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast toast = Toast.makeText(getApplicationContext(), "这里是翻译", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();


            }
        });


        //顶部菜单
//        message_middle_select_bar1 = findViewById(R.id.message_middle_select_bar1);
//        message_middle_select_bar2 = findViewById(R.id.message_middle_select_bar2);
//        message_middle_select_bar3 = findViewById(R.id.message_middle_select_bar3);
//        message_middle_select_bar4 = findViewById(R.id.message_middle_select_bar4);
        //顶部菜单 图标
//        message_middle_select_bar_image1 = findViewById(R.id.message_middle_select_bar_image1);
//        message_middle_select_bar_image2 = findViewById(R.id.message_middle_select_bar_image2);
//        message_middle_select_bar_image3 = findViewById(R.id.message_middle_select_bar_image3);


        //初始化顶部菜单的点击事件
//        initTopMenuClickListener();

        //顶部用户名和公司名
        hisName = findViewById(R.id.chat_user_name);
//        hisCompany = findViewById(R.id.chat_user_company);
        initTopName();


        initMsgAdapter();
        mReceiver = new HeadsetDetectReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mReceiver, intentFilter);

        thisContext = this;


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //设置已读
                setAsRead(HIS_ID);
                //加载历史
                loadNextPage(null);
            }
        }, 10);

    }


    @SuppressLint("InvalidWakeLockTag")
    private void registerProximitySensorListener() {
        try {
            mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        try {
            if (audioManager.isBluetoothA2dpOn() || audioManager.isWiredHeadsetOn()) {
                return;
            }
            if (mAdapter.getMediaPlayer().isPlaying()) {
                float distance = event.values[0];
                if (distance >= mSensor.getMaximumRange()) {
                    mAdapter.setAudioPlayByEarPhone(0);
                    setScreenOn();
                } else {
                    mAdapter.setAudioPlayByEarPhone(2);
                    ViewHolderController.getInstance().replayVoice();
                    setScreenOff();
                }
            } else {
                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                    mWakeLock = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setScreenOn() {
        if (mWakeLock != null) {
            mWakeLock.setReferenceCounted(false);
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    private void setScreenOff() {
        if (mWakeLock == null) {
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
        }
        mWakeLock.acquire();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void shadowOnclick() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


//    @Override
//    public void shadowOnclick() {
//        if (dropMenuFragment != null) {
//            hideDropMenu();
//        } else if (resumeMenuFragment != null) {
//            hideResumeMenu();
//        }
//    }


//    @SuppressLint("ResourceType")
//    public void hideDropMenu() {
//        FragmentTransaction mTransaction = getFragmentManager().beginTransaction();
//
//        //  mTransaction.setCustomAnimations(R.anim.fade_in_out_a,  R.anim.fade_in_out_a);
//        if (fragmentShadow != null)
//            mTransaction.remove(fragmentShadow);
//
//        mTransaction.setCustomAnimations(R.anim.top_in_a, R.anim.top_out_a);
//
//        if (dropMenuFragment != null)
//            mTransaction.remove(dropMenuFragment);
//
//
//        dropMenuFragment = null;
//        fragmentShadow = null;
//
//        mTransaction.commit();
//    }


//    @SuppressLint("ResourceType")
//    public void hideResumeMenu() {
//        FragmentTransaction mTransaction = getFragmentManager().beginTransaction();
//
//
//        //  mTransaction.setCustomAnimations(R.anim.fade_in_out_a,  R.anim.fade_in_out_a);
//        if (fragmentShadow != null)
//            mTransaction.remove(fragmentShadow);
//
//
//        mTransaction.setCustomAnimations(R.anim.bottom_out_a, R.anim.bottom_out_a);
//        if (resumeMenuFragment != null)
//            mTransaction.remove(resumeMenuFragment);
//
//
//        resumeMenuFragment = null;
//        fragmentShadow = null;
//
//        mTransaction.commit();
//    }

    private class HeadsetDetectReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                if (intent.hasExtra("state")) {
                    int state = intent.getIntExtra("state", 0);
                    mAdapter.setAudioPlayByEarPhone(state);
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    private void initTopName() {
        Intent intent = getIntent();
        String companyName = intent.getStringExtra("companyName");
        String name = intent.getStringExtra("hisName");

        hisName.setText(name);
//        hisCompany.setText(companyName);
    }


//    Resources res = getResources();
//    String[] messages = res.getStringArray(R.array.messages_array);


    private void initMsgAdapter() {
        final float density = getResources().getDisplayMetrics().density;
        final float MIN_WIDTH = 60 * density;
        final float MAX_WIDTH = 200 * density;
        final float MIN_HEIGHT = 60 * density;
        final float MAX_HEIGHT = 200 * density;
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadAvatarImage(ImageView avatarImageView, String string, String picType) {


                //加载展示图片
                // You can use other image load libraries.
                if (string.contains("R.drawable")) {
                    Integer resId = getResources().getIdentifier(string.replace("R.drawable.", ""),
                            "drawable", getPackageName());
                    avatarImageView.setImageResource(resId);
                } else if (string.contains("R.mipmap")) {
                    Integer resId = getResources().getIdentifier(string.replace("R.mipmap.", ""),
                            "mipmap", getPackageName());
                    avatarImageView.setImageResource(resId);
                } else {

                    if (!string.contains("https")) {
                        string = string.replace("http", "https");
                    }

                    System.out.println("图片显示，图片路径：" + string);

                    if (string != null) {
                        String[] str = string.split(";");
                        string = str[0];
                    }


                    if (!picType.equals("CIRCLE")) {
                        //   UploadPic.Companion.loadPicFromNet(string, avatarImageView);

                        try {
                            Glide.with(MessageListActivity.this)
                                    .load(string)
                                    .centerCrop()
                                    .placeholder(R.mipmap.no_pic_show)
                                    .into(avatarImageView);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        System.out.println("圆形");
                        System.out.println(string);
                        //   UploadPic.Companion.loadPicNormal(string, avatarImageView);


                        // You cannot start a load for a destroyed activity
                        try {
                            Glide.with(MessageListActivity.this)
                                    .load(string)
                                    .centerCrop()
                                    .placeholder(R.mipmap.default_avatar)
                                    .into(avatarImageView);
                        } catch (Exception e) {
                            System.out.println("异常处理--You cannot start a load for a destroyed activity");
                        }


                    }


                }
            }

            //Load image message
            @Override
            public void mLoadImage(final ImageView imageView, String string) {
                // You can use other image load libraries.
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(string)
                        .centerCrop()
                        .apply(new RequestOptions().fitCenter().placeholder(R.drawable.aurora_picture_not_found))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                int imageWidth = resource.getWidth();
                                int imageHeight = resource.getHeight();
                                Log.d(TAG, "Image width " + imageWidth + " height: " + imageHeight);

                                // 裁剪 bitmap
                                float width, height;
                                if (imageWidth > imageHeight) {
                                    if (imageWidth > MAX_WIDTH) {
                                        float temp = MAX_WIDTH / imageWidth * imageHeight;
                                        height = temp > MIN_HEIGHT ? temp : MIN_HEIGHT;
                                        width = MAX_WIDTH;
                                    } else if (imageWidth < MIN_WIDTH) {
                                        float temp = MIN_WIDTH / imageWidth * imageHeight;
                                        height = temp < MAX_HEIGHT ? temp : MAX_HEIGHT;
                                        width = MIN_WIDTH;
                                    } else {
                                        float ratio = imageWidth / imageHeight;
                                        if (ratio > 3) {
                                            ratio = 3;
                                        }
                                        height = imageHeight * ratio;
                                        width = imageWidth;
                                    }
                                } else {
                                    if (imageHeight > MAX_HEIGHT) {
                                        float temp = MAX_HEIGHT / imageHeight * imageWidth;
                                        width = temp > MIN_WIDTH ? temp : MIN_WIDTH;
                                        height = MAX_HEIGHT;
                                    } else if (imageHeight < MIN_HEIGHT) {
                                        float temp = MIN_HEIGHT / imageHeight * imageWidth;
                                        width = temp < MAX_WIDTH ? temp : MAX_WIDTH;
                                        height = MIN_HEIGHT;
                                    } else {
                                        float ratio = imageHeight / imageWidth;
                                        if (ratio > 3) {
                                            ratio = 3;
                                        }
                                        width = imageWidth * ratio;
                                        height = imageHeight;
                                    }
                                }
                                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                                params.width = (int) width;
                                params.height = (int) height;
                                imageView.setLayoutParams(params);
                                Matrix matrix = new Matrix();
                                float scaleWidth = width / imageWidth;
                                float scaleHeight = height / imageHeight;
                                matrix.postScale(scaleWidth, scaleHeight);
                                imageView.setImageBitmap(Bitmap.createBitmap(resource, 0, 0, imageWidth, imageHeight, matrix, true));
                            }
                        });
            }

            /**
             * Load video message
             * @param imageCover Video message's image cover
             * @param uri Local path or url.
             */
            @Override
            public void loadVideo(ImageView imageCover, String uri) {
                long interval = 5000 * 1000;
                Glide.with(MessageListActivity.this)
                        .load(uri)
                        .centerCrop()
                        // Resize image view by change override size.
                        .apply(new RequestOptions().frame(interval).override(200, 400))
                        .into(imageCover);
            }
        };

        // Use default layout
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        mAdapter = new MsgListAdapter<>("0", holdersConfig, imageLoader);
        // If you want to customise your layout, try to create custom ViewHolder:
        // holdersConfig.setSenderTxtMsg(CustomViewHolder.class, layoutRes);
        // holdersConfig.setReceiverTxtMsg(CustomViewHolder.class, layoutRes);
        // CustomViewHolder must extends ViewHolders defined in MsgListAdapter.
        // Current ViewHolders are TxtViewHolder, VoiceViewHolder.

        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
            //交换消息,点击结果
            @Override
            public void onConfirmMessageClick(MyMessage message, boolean result, int type) {

                reconnectSocket();


                if (type == EXCHANGE_PHONE) {
                    if (result) {
                        //同意
                        acceptToExchangeContact(message, type, "あなたは電話交換を受けました!", "相手は電話交換を受けました");
                        //修改交换信息状态
                        updateStateOfExchangeInfo(message.getInterviewId(), "EXCHANGED", null);
                    } else {
                        //拒绝
                        notifyChoiceResult(message, "あなたは電話交換を断りました!", "相手は電話交換を断りました", false);
                        //修改交换信息状态
                        updateStateOfExchangeInfo(message.getInterviewId(), "REJECTED", null);

                    }
                    message.setType(IMessage.MessageType.RECEIVE_EXCHANGE_PHONE_HANDLED.ordinal());
                } else if (type == EXCHANGE_LINE) {
                    if (result) {
                        //同意
                        acceptToExchangeContact(message, type, "あなたはLine交換を受けました!", "相手はLine交換を受けました");
                        //修改交换信息状态
                        updateStateOfExchangeInfo(message.getInterviewId(), "EXCHANGED", null);

                    } else {
                        //拒绝
                        notifyChoiceResult(message, "あなたはLine交換を断りました!", "相手はLine交換を断りました", false);
                        //修改交换信息状态
                        updateStateOfExchangeInfo(message.getInterviewId(), "REJECTED", null);

                    }
                    message.setType(IMessage.MessageType.RECEIVE_EXCHANGE_LINE_HANDLED.ordinal());

                } else if (type == INVITE_VIDEO) {
                    //视频 面试 邀约
                    if (result) {
                        //同意对方的邀请,把面试状态改为[已约定]
                        updateStateOfInterviewInfo(message.getInterviewId(), InterviewState.APPOINTED, "", message, "interviewAgree", "あなたはビデオ面接に同意しました", "相手はビデオ面接の招待に受けます", false);

                    } else {
                        //拒绝
                        //拒绝对方的邀请,把面试状态改为[已拒绝]
                        updateStateOfInterviewInfo(message.getInterviewId(), InterviewState.REJECTED, "", message, "system", "あなたはビデオ面接の招待を断りました!", "相手はビデオ面接の招待を断りました", false);

                    }
                    message.setType(IMessage.MessageType.RECEIVE_INVITE_VIDEO_HANDLED.ordinal());

                } else if (type == INVITE_NORMAL_INTERVIEW) {
                    //邀请 普通 面试
                    if (result) {
                        //同意,预约成功
                        updateStateOfInterviewInfo(message.getInterviewId(), InterviewState.APPOINTED, "", message, "interviewAgree", "あなたは面接招待に受けます", "相手は面接招待に受けます", false);
                    } else {
                        //拒绝 预约失败
                        updateStateOfInterviewInfo(message.getInterviewId(), InterviewState.REJECTED, "", message, "system", "あなたは面接の招待を断りました", "相手は面接の招待を断りました", false);
                    }
                    message.setType(IMessage.MessageType.RECEIVE_NORMAL_INTERVIEW_HANDLED.ordinal());

                } else if (type == INTERVIEW_VIDEO) {
                    //视频请求
                    //同意进入视频房间
                    if (result) {
                        //进入视频,修改面试开始时间
                        updateStateOfInterviewInfo(message.getInterviewId(), InterviewState.APPOINTED, "", message, "videoAgree", "あなたはビデオ面接に承りました", "相手はビデオ面接の招待に受けます", false);
                        gotoVideoInterview(message);
                    } else {
                        //拒绝进入视频房间
                        updateStateOfInterviewInfo(message.getInterviewId(), InterviewState.REJECTED, "", message, "system", "あなたはビデオ面接の招待を断りました!", "相手はビデオ面接の招待を断りました", true);
                    }
                    message.setType(IMessage.MessageType.RECEIVE_INTERVIEW_VIDEO_HANDLED.ordinal());

                } else if (type == REQUEST_RESUME) {
                    //简历请求
//                    if (result) {
//                        //同意  弹出简历选择
//                        //弹出窗口
//
//
//                        showResumeList(2, message.getInterviewId());
//                    } else {
//                        //拒绝
//
//                        //修改交换信息状态
//                        updateStateOfExchangeInfo(message.getInterviewId(), "REJECTED", null);
//
//
//                        //requestCreateExchangesInfoApi("RESUME", null, false);
//
//
//                        notifyChoiceResult(null, "あなたは履歴書の請求を断りました", "相手は履歴書の請求を断りました", false);
//
//
//                    }
//                    message.setType(IMessage.MessageType.RECEIVE_REQUEST_RESUME_HANDLED.ordinal());

                } else if (type == DO_THING) {


                    Toast toast = Toast.makeText(getApplicationContext(), "このメッセージは期限切れです", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                }


                //更改界面
                final MyMessage message_callBack = message;
                final String thisMessageId = message.getMsgId();
                MessageListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.updateMessage(thisMessageId, message_callBack);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onMessageClick(MyMessage message) {
                // do something
                if (message.getType() == IMessage.MessageType.RECEIVE_VIDEO.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_VIDEO.ordinal()) {
                    if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                        Intent intent = new Intent(MessageListActivity.this, VideoActivity.class);
                        intent.putExtra(VideoActivity.VIDEO_PATH, message.getMediaFilePath());
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);

                    }
                } else if (message.getType() == IMessage.MessageType.RECEIVE_IMAGE.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_IMAGE.ordinal()) {
                    //放大图片
                    //点击图片，放大/缩小
                    Intent intent = new Intent(MessageListActivity.this, BrowserImageActivity.class);
                    intent.putExtra("msgId", message.getMsgId());


                    intent.putStringArrayListExtra("pathList", mPathList);
                    intent.putStringArrayListExtra("idList", mMsgIdList);
                    startActivity(intent);

                    overridePendingTransition(R.anim.right_in, R.anim.left_out);


                } else if (message.getType() == IMessage.MessageType.SEND_VOICE.ordinal()
                        || message.getType() == IMessage.MessageType.RECEIVE_VOICE.ordinal()) {
                    //语音消息被点击
                    final MyMessage message_f = message;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
//                            playVoice(message_f);
                        }
                    }) {
                    }.start();

                } else if (message.getType() == IMessage.MessageType.SEND_RESUME_WORD.ordinal()
                        || message.getType() == IMessage.MessageType.RECEIVE_RESUME.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_RESUME_PDF.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_RESUME_JPG.ordinal()) {


                    //简历被点击


                    String url = message.getMediaFilePath();//路径
                    String id = message.getInterviewId();//记录id

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);

                } else if (message.getType() == IMessage.MessageType.JOB_INFO.ordinal()) {
//                    JobInfoModel item = message.getJsobInfo();
//                    //跳转到职位详情
//                    Intent intent = new Intent(thisContext, JobInfoDetailActivity.class);
//                    intent.putExtra("positionName", item.getName());
//                    intent.putExtra("salaryType", item.getSalaryType());
//                    intent.putExtra("showSalaryMinToMax", item.getShowSalaryMinToMax());
//                    intent.putExtra("address", item.getAddress());
//                    intent.putExtra("workingExperience", item.getWorkingExperience());
//                    intent.putExtra("educationalBackground", item.getEducationalBackground());
//                    intent.putExtra("skill", item.getSkill());
//                    intent.putExtra("content", item.getContent());
//                    intent.putExtra("organizationId", item.getOrganizationId());
//                    intent.putExtra("companyName", item.getCompanyName());
//                    intent.putExtra("userName", item.getUserName());
//                    intent.putExtra("userPositionName", item.getUserPositionName());
//                    intent.putExtra("avatarURL", item.getAvatarURL());
//                    intent.putExtra("userId", item.getUserId());
//                    intent.putExtra("isCollection", item.getCollection());
//                    intent.putExtra("recruitMessageId", item.getRecruitMessageId());
//                    intent.putExtra("collectionId", item.getCollectionId());
//                    intent.putExtra("position", -1);
//                    intent.putExtra("fromType", "CHAT");
//
//
//                    startActivityForResult(intent, 2);
//                    overridePendingTransition(R.anim.right_in, R.anim.left_out);

                } else if (message.getType() == IMessage.MessageType.SEND_OFFER.ordinal()) {

//                    Intent intent = new Intent(MessageListActivity.this, SeeOffer.class);
//                    intent.putExtra("offerId", message.getInterviewId());
//                    intent.putExtra("channelMsgId", message.getMessageChannelMsgId());
//
//                    startActivityForResult(intent, 1);
//                    overridePendingTransition(R.anim.right_in, R.anim.left_out);

                } else if (message.getType() == IMessage.MessageType.RECEIVE_COMMUNICATION_VIDEO.ordinal()
                        || message.getType() == IMessage.MessageType.RECEIVE_NORMAL_INTERVIEW.ordinal()
                        || message.getType() == IMessage.MessageType.RECEIVE_INVITE_VIDEO_HANDLED.ordinal()
                        || message.getType() == IMessage.MessageType.RECEIVE_NORMAL_INTERVIEW_HANDLED.ordinal()) {
                    //视频面试邀请 ,线上面试
                    //武
//                    String id = message.getInterviewId();
//
//                    Intent intent = new Intent(MessageListActivity.this, FaceActivity.class);
//                    intent.putExtra("id", id);
//                    intent.putExtra("type", "2");
//
//                    startActivity(intent);
//                    MessageListActivity.this.overridePendingTransition(R.anim.right_in, R.anim.left_out);


                } else if (message.getType() == IMessage.MessageType.RECEIVE_ACCOUNT_LINE.ordinal()) {

                    Toast toast = Toast.makeText(getApplicationContext(), "コピーできました", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else {
                    //Message click event


//                    Toast.makeText(getApplicationContext(),
//                            getApplicationContext().getString(R.string.message_click_hint),
//                            Toast.LENGTH_SHORT).show();
                }
            }

        });

        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(View view, MyMessage message) {

                //消息被长按
                // Toast.makeText(getApplicationContext(),
                //         getApplicationContext().getString(R.string.message_long_click_hint),
                //         Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = (DefaultUser) message.getFromUser();
                // Toast.makeText(getApplicationContext(),
                //         getApplicationContext().getString(R.string.avatar_click_hint),
                //         Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        mAdapter.setMsgStatusViewClickListener(new MsgListAdapter.OnMsgStatusViewClickListener<MyMessage>() {
            @Override
            public void onStatusViewClick(MyMessage message) {
                // message status view click, resend or download here
            }
        });


//        MyMessage voiceMessage = new MyMessage("", IMessage.MessageType.RECEIVE_VOICE.ordinal());
//        voiceMessage.setUserInfo(new DefaultUser("0", "Deadpool", "R.drawable.deadpool"));
//        voiceMessage.setMediaFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/voice/2018-02-28-105103.m4a");
//        voiceMessage.setDuration(4);
//        mAdapter.addToStart(voiceMessage, true);
//
//        MyMessage sendVoiceMsg = new MyMessage("", IMessage.MessageType.SEND_VOICE.ordinal());
//        sendVoiceMsg.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
//        sendVoiceMsg.setMediaFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/voice/2018-02-28-105103.m4a");
//        sendVoiceMsg.setDuration(4);
//        sendVoiceMsg.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
//        mAdapter.addToStart(sendVoiceMsg, true);
//
//        MyMessage eventMsg = new MyMessage("haha", IMessage.MessageType.EVENT.ordinal());
//        mAdapter.addToStart(eventMsg, true);
//
//
//        MyMessage RESET = new MyMessage("haha", IMessage.MessageType.RESET.ordinal());
//        mAdapter.addToStart(RESET, true);
//
//
//        MyMessage RESET1 = new MyMessage("相手の電話番号交換申請を同意しまし", IMessage.MessageType.EVENT.ordinal());
//        mAdapter.addToStart(RESET1, true);
//
//
//        MyMessage RESET2 = new MyMessage("交換電話の送信を要求します", IMessage.MessageType.EVENT.ordinal());
//        mAdapter.addToStart(RESET2, true);
//
//
//        MyMessage RESET3 = new MyMessage("相手はあなたとの電話交換に同意しました", IMessage.MessageType.EVENT.ordinal());
//        mAdapter.addToStart(RESET3, true);
//
//
//        MyMessage jobInfo = new MyMessage("", IMessage.MessageType.JOB_INFO.ordinal());
//        mAdapter.addToStart(jobInfo, true);
//
//
//        MyMessage communicationRequest = new MyMessage("向こうはあなたに電話番号交換の申請を出しました。同意しますか。", IMessage.MessageType.RECEIVE_COMMUNICATION_PHONE.ordinal());
//        communicationRequest.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
//        mAdapter.addToStart(communicationRequest, true);
//
//
//        MyMessage communicationRequest1 = new MyMessage("向こうはあなたにline交換の申請を出しました。同意しますか。", IMessage.MessageType.RECEIVE_COMMUNICATION_LINE.ordinal());
//        communicationRequest1.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
//        mAdapter.addToStart(communicationRequest1, true);
//
//        MyMessage communicationRequest2 = new MyMessage("向こうはあなたをビデオ面接にさそっていますが、受けてよろしいですか。", IMessage.MessageType.RECEIVE_COMMUNICATION_VIDEO.ordinal());
//        communicationRequest2.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
//        mAdapter.addToStart(communicationRequest2, true);
//
//
//        MyMessage communicationResult = new MyMessage("電話番号交換は成功しました。●●様の電話番号は：13888888888", IMessage.MessageType.RECEIVE_ACCOUNT_PHONE.ordinal());
//        communicationResult.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.ironman"));
//        mAdapter.addToStart(communicationResult, true);
//
//
//        MyMessage communicationResult1 = new MyMessage("line交換は成功しました。●●様の電話番号は：13888888888", IMessage.MessageType.RECEIVE_ACCOUNT_LINE.ordinal());
//        communicationResult1.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.deadpool"));
//        mAdapter.addToStart(communicationResult1, true);


//        MyMessage receiveVideo = new MyMessage("", IMessage.MessageType.RECEIVE_VIDEO.ordinal());
//        receiveVideo.setMediaFilePath(Environment.getExternalStorageDirectory().getPath() + "/Pictures/Hangouts/video-20170407_135638.3gp");
//        receiveVideo.setDuration(4);
//        receiveVideo.setUserInfo(new DefaultUser("0", "Deadpool", "R.drawable.deadpool"));
//        mAdapter.addToStart(receiveVideo, true);

//
//        MyMessage EVENT1 = new MyMessage("相手とのビデオ面接申請を同意しました、まもなくビデオがアクセスします。", IMessage.MessageType.EVENT.ordinal());
//        mAdapter.addToStart(EVENT1, true);
//
//
//        MyMessage interview = new MyMessage("厳選なる審査の結果、あなたを正社員として採用することになりました。後ほどoffをお送りいたします。おめでとうございます！", IMessage.MessageType.INTERVIEW_SUCCESS.ordinal());
//        interview.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.deadpool"));
//        mAdapter.addToStart(interview, true);
//
//
//        MyMessage offer = new MyMessage("清水さんからのofferが着信しました！！！！！！！", IMessage.MessageType.SEND_OFFER.ordinal());
//        offer.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.deadpool"));
//        mAdapter.addToStart(offer, true);
//
//
//        MyMessage interview1 = new MyMessage("今回は採用を見送る事になりましたのでご了承のほど、宜しくお願い致します", IMessage.MessageType.INTERVIEW_FAIL.ordinal());
//        interview1.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.deadpool"));
//        mAdapter.addToStart(interview1, true);
//
//
//        MyMessage pic = new MyMessage("今回は採用を見送る事になりましたのでご了承のほど、宜しくお願い致します", IMessage.MessageType.SEND_IMAGE.ordinal());
//        pic.setMediaFilePath("R.drawable.ppp");
//        pic.setUserInfo(new DefaultUser("1", "Ironman", "R.drawable.deadpool"));
//        mAdapter.addToStart(pic, true);
//


        PullToRefreshLayout layout = mChatView.getPtrLayout();
        layout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PullToRefreshLayout layout) {
                Log.i("MessageListActivity", "Loading next page");
                loadNextPage(lastShowedMessageId);
            }
        });
        // Deprecated, should use onRefreshBegin to load next page
        mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalCount) {
//                Log.i("MessageListActivity", "Loading next page");
//                loadNextPage();
            }
        });

        mChatView.setAdapter(mAdapter);
        mAdapter.getLayoutManager().scrollToPosition(0);
        scrollToBottom();

    }


    /**
     * 得到返回的值
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String offerState = data.getStringExtra("offerState");
            if (offerState != null && !"".equals(offerState)) {
                String offerId = data.getStringExtra("offerId");
                String channelMsgId = data.getStringExtra("channelMsgId");

                MyMessage message = new MyMessage();
                message.setMessageChannelMsgId(channelMsgId);
                //同意OFFER
                if (offerState.equals("OK")) {
                    notifyChoiceResult(message, "あなたはオファーを承認しました", "相手は採用通知書を承認しました", false);

                } else {
                    notifyChoiceResult(message, "あなたはオファーを辞退しました。", "相手はオファーを辞退しました。", false);//您已拒绝了对方的offer    对方拒绝了您发送的offer
                }

            }
        }
    }


    //改变面试信息的状态
    private void updateStateOfInterviewInfo(String id, String type, String cancelReason,
                                            MyMessage message, String SendMessageType, String toMe, String toHim, Boolean sendInterviewId) {


//        JSONObject detail = new JSONObject();
//        try {
//            detail.put("state", type);
//            detail.put("cancelReason", cancelReason + "xxxx");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(mediaType, detail.toString());
//
//
//        //测试地址 https://interview.sk.cgland.top/
//        //正式地址 https://interview.sk.skjob.jp/
//        RetrofitUtils retrofitUils = new RetrofitUtils(thisContext, this.getString(R.string.interUrl));
//        retrofitUils.create(Infoexchanges.class)
//                .updateInterviewState(
//                        id, body
//                ).subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
//                .observeOn(AndroidSchedulers.mainThread()) //观察者 切换到主线程
//                .subscribe(new Consumer() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        System.out.println("修改面试信息状态成功");
//                        System.out.println(o.toString());
//
//                        notifyChoiceResult(message, toMe, toHim, sendInterviewId);
//
//                    }
//                }, new Consumer() {
//
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        System.out.println("修改面试信息状态失败");
//                        System.out.println(o.toString());
//                    }
//                });
    }


    //改变交换信息的状态
    private void updateStateOfExchangeInfo(String id, String type, String resumeId) {
//
//        System.out.println("updateStateOfExchangeInfo");
//
//        System.out.println(id);
//        System.out.println(type);
//        System.out.println(resumeId);
//
//
//        JSONObject request = new JSONObject();
//        JSONObject detail = new JSONObject();
//        try {
//
//            if (resumeId != null) {
//                detail.put("resumeId", resumeId);
//            }
//            detail.put("state", type);
//
//            request.put("body", detail);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(mediaType, detail.toString());
//
//        //测试地址 https://interview.sk.cgland.top/
//        //正式地址 https://interview.sk.skjob.jp/
//        RetrofitUtils retrofitUils = new RetrofitUtils(this, this.getString(R.string.interUrl));
//        retrofitUils.create(Infoexchanges.class)
//                .updateExchangeInfoState(
//                        id, body
//                ).subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
//                .observeOn(AndroidSchedulers.mainThread()) //观察者 切换到主线程
//                .subscribe(new Consumer() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                    }
//                }, new Consumer() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//
//                    }
//                });

    }


    //发送请求交换Line的信息
//    private void sendPhoneExchangeRequestMessage(String interviewId) {
//
//        reconnectSocket();
//
//        System.out.println("给双方发送交换PHONE信息");
//
//        try {
//            //电话交换
//            JSONObject requestJson = new JSONObject();
//            requestJson.put("receiver_id", HIS_ID);
//
//            JSONObject message = new JSONObject(sendMessageModel.toString());
//            message.getJSONObject("content").put("type", "exchangePhone");
//            message.getJSONObject("content").put("msg", "向こうはあなた電話交換の申請を出しました");
//            message.getJSONObject("content").put("interviewId", interviewId);
//
//            requestJson.put("message", message);
//
//            socket.emit("forwardSystemMsg", requestJson);
//
//
//            //系统消息
//            JSONObject systemMessage = new JSONObject();
//            systemMessage.put("receiver_id", MY_ID);
//
//            JSONObject system = new JSONObject(sendMessageModel.toString());
//            system.getJSONObject("receiver").put("id", MY_ID);
//            system.getJSONObject("sender").put("id", HIS_ID);
//            system.getJSONObject("content").put("type", "system");
//            system.getJSONObject("content").put("msg", "電話交換の申請を送信しました");
//            systemMessage.put("message", system);
//
//            socket.emit("forwardSystemMsg", systemMessage);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }


    //发送请求交换Line的信息
//    private void sendLineExchangeRequestMessage(String interviewId) {
//        reconnectSocket();
//        //给双方发送交换LINE信息
//        System.out.println("给双方发送交换LINE信息");
//        try {
//            //Line交换
//            JSONObject requestJson = new JSONObject();
//            requestJson.put("receiver_id", HIS_ID);
//
//            JSONObject message = new JSONObject(sendMessageModel.toString());
//            message.getJSONObject("content").put("type", "exchangeLine");
//            message.getJSONObject("content").put("msg", "向こうはあなたにline交換の申請を出しました。同意しますか？");
//            message.getJSONObject("content").put("interviewId", interviewId);
//
//            requestJson.put("message", message);
//
//            socket.emit("forwardSystemMsg", requestJson);
//
//            //系统消息
//            JSONObject systemMessage = new JSONObject();
//            systemMessage.put("receiver_id", MY_ID);
//
//            JSONObject system = new JSONObject(sendMessageModel.toString());
//            system.getJSONObject("receiver").put("id", MY_ID);
//            system.getJSONObject("sender").put("id", HIS_ID);
//            system.getJSONObject("content").put("type", "system");
//            system.getJSONObject("content").put("msg", "Line交換の申請を送信しました");
//            systemMessage.put("message", system);
//
//            socket.emit("forwardSystemMsg", systemMessage);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }


    /**
     * 发送系统消息给自己
     */
    private void sendSystemMessageToMyself(String message) {
        reconnectSocket();

        System.out.println("发送!!!!!!!!!!!!!!");

        //系统消息
        try {
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("receiver_id", MY_ID);

            JSONObject system = new JSONObject(sendMessageModel.toString());
            system.getJSONObject("receiver").put("id", MY_ID);
            system.getJSONObject("sender").put("id", HIS_ID);
            system.getJSONObject("content").put("type", "system");
            system.getJSONObject("content").put("msg", message);
            systemMessage.put("message", system);

            socket.emit("forwardSystemMsg", systemMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (message.startsWith("TIME")) {
            //是时间的系统消息

            MyMessage messageTime = null;

            String messageTimeString = sdf_show_year.format(new Date(Long.parseLong(message.substring(4, message.length()))));
            if (nowTimeString.substring(0, 4).equals(messageTimeString.substring(0, 4))) {
                if (nowTimeString.substring(8, 10).equals(messageTimeString.substring(8, 10))) {
                    messageTimeString = messageTimeString.substring(12, 17);
                } else {
                    messageTimeString = messageTimeString.substring(5, 17);
                }
            }
            messageTime = new MyMessage(messageTimeString, IMessage.MessageType.EVENT.ordinal());
            mAdapter.addToStart(messageTime, true);
        }

    }


    /**
     * 发送系统消息给
     */
    private void sendSystemMessageToOther(String mes) {
        reconnectSocket();
        //系统消息
        try {
            //Line交换
            JSONObject requestJson = new JSONObject();
            requestJson.put("receiver_id", HIS_ID);

            JSONObject message = new JSONObject(sendMessageModel.toString());
            message.getJSONObject("content").put("type", "exchangeLine");
            message.getJSONObject("content").put("msg", mes);
            message.getJSONObject("content").put("interviewId", "");

            requestJson.put("message", message);

            socket.emit("forwardSystemMsg", requestJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //请求创建交换信息的接口
    //针对 phone 和 line 先通过positionId 查询公司id  ,然后创建交换信息 ,最后发送交换的信息
    //针对 简历 然后创建交换信息 直接 根据 resumeSendOk修改状态
    private void requestCreateExchangesInfoApi(String type, String resumeId, Boolean resumeSendOk) {

//
//        System.out.println("thisCommunicationPositionId" + thisCommunicationPositionId);
//
//
//        if (thisCommunicationPositionId != null && !"".equals(thisCommunicationPositionId)) {
        //测试地址 https://organization-position.sk.cgland.top/
        //正式地址 https://organization-position.sk.skjob.jp/
//            RetrofitUtils requestForPosition = new RetrofitUtils(thisContext, this.getString(R.string.organizationUrl));
//            requestForPosition.create(RecruitInfoApi.class)
//                    .getRecruitInfoById(
//                            thisCommunicationPositionId
//                    )
//                    .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
//                    .observeOn(AndroidSchedulers.mainThread()) //观察者 切换到主线程
//                    .subscribe(new Consumer() {
//                        @Override
//                        public void accept(Object o) throws Exception {
//                            System.out.println("请求职位单个信息成功");
//                            JSONObject jsonOut = new JSONObject(o.toString());
//                            System.out.println(o.toString());
//                            JSONObject json = jsonOut.getJSONObject("organization");
//                            if (json.has("organizationId")) {
//                                String companyId = json.getString("organizationId");
//
//                                JSONObject detail = new JSONObject();
//                                try {
//                                    detail.put("type", type);//类型：简历，电话，line，等 RESUME, TELEPHONE, LINE
//                                    detail.put("toUserId", HIS_ID);
//                                    detail.put("userId", MY_ID);
//                                    detail.put("toOrganizationId", companyId);
//                                    detail.put("attributes", new JSONObject());
//
//                                    if (resumeId != null) {
//                                        detail.put("resumeId", resumeId);//简历ID（类型为简历则必传）
//                                        detail.put("organizationPositionId", thisCommunicationPositionId);
//
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                                System.out.println("开始创建交换信息");
//
//                                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
//                                RequestBody body = RequestBody.create(mediaType, detail.toString());
//                                //测试地址 https://interview.sk.cgland.top/
//                                //正式地址 https://interview.sk.skjob.jp/
//                                RetrofitUtils retrofitUils = new RetrofitUtils(thisContext, thisContext.getString(R.string.interUrl));
//                                retrofitUils.create(Infoexchanges.class)
//                                        .createExchangeInfo(
//                                                body
//                                        ).subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
//                                        .observeOn(AndroidSchedulers.mainThread()) //观察者 切换到主线程
//                                        .subscribe(new Consumer() {
//                                            @Override
//                                            public void accept(Object o) throws Exception {
//                                                System.out.println("创建交换信息成功");
//                                                System.out.println(o.toString());
//
//                                                if ("LINE".equals(type)) {
//                                                    sendLineExchangeRequestMessage(o.toString());
//                                                } else if ("TELEPHONE".equals(type)) {
//                                                    sendPhoneExchangeRequestMessage(o.toString());
//                                                }
//
////                                                else if ("RESUME".equals(type)) {
////                                                    //在这里调用  修改简历状态的接口
////                                                    String type = "";
////                                                    if (resumeSendOk) {
////                                                        type = "EXCHANGED";
////                                                    } else {
////                                                        type = "REJECTED";
////                                                    }
////                                                    updateStateOfExchangeInfo(o.toString(), type,null);
////
////                                                }
//
//
//                                            }
//                                        }, new Consumer() {
//                                            @Override
//                                            public void accept(Object o) throws Exception {
//                                                System.out.println("创建交换信息失败");
//                                                System.out.println(o.toString());
//                                            }
//                                        });
//
//
//                            } else {
//                                System.out.println("请求单个职位信息时,数据异常:缺少organizationId");
//                            }
//                        }
//                    }, new Consumer() {
//                        @Override
//                        public void accept(Object o) throws Exception {
//                            System.out.println("请求职位单个信息失败");
//                            System.out.println(o.toString());
//                        }
//                    });
//        }


    }


    //改变视频面试状态
    private void changeInterviewState(String roomId, String state) {
//        JSONObject userJson = new JSONObject();
//        try {
//            JSONObject body = new JSONObject();
//            body.put("state", state);
//            body.put("cancelReason", "");
//
//            userJson.put("id", roomId);
//            userJson.put("body", body);
//
//            //测试地址 https://interview.sk.cgland.top/
//            //正式地址 https://interview.sk.skjob.jp/
//            String res = Http.put(this.getString(R.string.interUrl)+"api/interview-agendas/asdasd/state", userJson);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    //初始化视频面试
    private void initVideoInterview() {

        URL serverURL;
        try {
            //测试地址 https://jitsi.sk.cgland.top/
            //正式地址 https://jitsi.sk.skjob.jp/
            serverURL = new URL(this.getString(R.string.jitsiUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }

    }

    //调用接受交换联系方式接口
    private void acceptToExchangeContact(MyMessage message, int type, String messageToMe, String messageToHim) {
//        reconnectSocket();
//        System.out.println("接受交换联系方式");
//        String eventName = "";
//        String massageType = "";
//        if (type == EXCHANGE_PHONE) {
//            eventName = "agreeExchangePhone";
//            massageType = "phoneAgree";
//        } else if (type == EXCHANGE_LINE) {
//            eventName = "agreeExchangeLine";
//            massageType = "lineAgree";
//        }
//        try {
//            //调用同意接口
//            JSONObject json = new JSONObject();
//            json.put("token", application.getMyToken());
//            json.put("applicant_id", HIS_ID);
//            json.put("approver_id", MY_ID);
//            socket.emit(eventName, json, new Ack() {
//                public void call(String eventName, Object error, Object data) {
//                    System.out.println("Got message for :" + eventName + " error is :" + error + " data is :" + data);
//                }
//            });
//
//
//            notifyChoiceResult(message, messageToMe, messageToHim, false);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


    }

    //转向视频界面
    private void gotoVideoInterview(MyMessage message) {


//        Intent intent = new Intent(MessageListActivity.this, JitsiMeetViewContainer.class);
//        intent.putExtra("roomNumber",  message.getRoomNumber()+"");
//        startActivity(intent);
//        overridePendingTransition(R.anim.right_in, R.anim.left_out);
//
//


        URL serverURL;
        try {
            //测试地址 https://jitsi.sk.cgland.top/
            //正式地址 https://jitsi.sk.skjob.jp/
            serverURL = new URL(this.getString(R.string.jitsiUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);


        String room = message.getRoomNumber();

        //链接视频
        JitsiMeetConferenceOptions options
                = new JitsiMeetConferenceOptions.Builder()
                .setRoom(room)
                .setUserInfo(new JitsiMeetUserInfo())
                .build();
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.


        jitsiMeetActivitySon.launch(thisContext, options, message.getInterviewId());
        startVideoTime = new Date();

    }


    //通知对方关闭视频
    public void sendMessageToHimToshutDownVideo(String sendInterviewId) {
        reconnectSocket();


        System.out.println("sendInterviewIdsendInterviewIdsendInterviewId");
        System.out.println(sendInterviewId);


        Date now = new Date();

        long time = now.getTime() - startVideoTime.getTime();

        int minute = (int) time / 1000 / 60;

        try {
            //通知他结果
            JSONObject systemMessageToHim = new JSONObject();
            systemMessageToHim.put("receiver_id", HIS_ID);

            JSONObject systemToHim = new JSONObject(sendMessageModel.toString());
            systemToHim.getJSONObject("receiver").put("id", HIS_ID);
            systemToHim.getJSONObject("sender").put("id", MY_ID);
            systemToHim.getJSONObject("content").put("type", "videoOver");


            systemToHim.getJSONObject("content").put("interviewId", sendInterviewId);
            systemToHim.getJSONObject("content").put("duration", "0");


            systemToHim.getJSONObject("content").put("msg", "相手はビデオ面接を終了しました、合計" + minute + "分間");
            systemMessageToHim.put("message", systemToHim);
            socket.emit("forwardSystemMsg", systemMessageToHim);

            //通知自己结果
            JSONObject systemMessageToMe = new JSONObject();
            systemMessageToMe.put("receiver_id", MY_ID);

            JSONObject systemToMe = new JSONObject(sendMessageModel.toString());
            systemToMe.getJSONObject("receiver").put("id", MY_ID);
            systemToMe.getJSONObject("sender").put("id", HIS_ID);
            systemToMe.getJSONObject("content").put("type", "system");
            systemToMe.getJSONObject("content").put("msg", "あなたはビデオ面接を終了しました、合計" + minute + "分間");
            systemMessageToMe.put("message", systemToMe);
            socket.emit("forwardSystemMsg", systemMessageToMe);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        System.out.println(sendInterviewId + "sendMessageToHimToshutDownVideo");
    }


    //通知双方选择结果
    private void notifyChoiceResult(MyMessage message, String messageToMe, String messageToHim, Boolean sendInterviewId) {
//        reconnectSocket();
//
//        try {
//            if (message != null) {
//                //标记为已处理
//                JSONObject json = new JSONObject();
//                json.put("msg_id", message.getMessageChannelMsgId());
//                json.put("applicant_id", HIS_ID);
//                json.put("approver_id", MY_ID);
//                socket.emit("modifyMessageAsHandled", json, new Ack() {
//                    public void call(String eventName, Object error, Object data) {
//                        System.out.println("Got message for :" + eventName + " error is :" + error + " data is :" + data);
//                    }
//                });
//            }
//
//            //通知他结果
//            JSONObject systemMessageToHim = new JSONObject();
//            systemMessageToHim.put("receiver_id", HIS_ID);
//
//            JSONObject systemToHim = new JSONObject(sendMessageModel.toString());
//            systemToHim.getJSONObject("receiver").put("id", HIS_ID);
//            systemToHim.getJSONObject("sender").put("id", MY_ID);
//            systemToHim.getJSONObject("content").put("type", "system");
//            if (sendInterviewId) {
//                systemToHim.getJSONObject("content").put("interviewId", message.getInterviewId());
//
//                systemToHim.getJSONObject("content").put("duration", "0");
//            }
//
//            systemToHim.getJSONObject("content").put("msg", messageToHim);
//            systemMessageToHim.put("message", systemToHim);
//            socket.emit("forwardSystemMsg", systemMessageToHim);
//
//            //通知自己结果
//            JSONObject systemMessageToMe = new JSONObject();
//            systemMessageToMe.put("receiver_id", MY_ID);
//
//            JSONObject systemToMe = new JSONObject(sendMessageModel.toString());
//            systemToMe.getJSONObject("receiver").put("id", MY_ID);
//            systemToMe.getJSONObject("sender").put("id", HIS_ID);
//            systemToMe.getJSONObject("content").put("type", "system");
//            systemToMe.getJSONObject("content").put("msg", messageToMe);
//            systemMessageToMe.put("message", systemToMe);
//            socket.emit("forwardSystemMsg", systemMessageToMe);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }


    //得到指定路径的表情
    private String getEmotion(String str) {
        Integer ico = DefEmoticons.textToPic.get(str);
        if (ico != null) {
            String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    getResources().getResourcePackageName(ico) +
                    "/" + getResources().getResourceTypeName(ico) +
                    "/" + getResources().getResourceEntryName(ico);
            return path;
        }
        return null;
    }

    //播放音频
    public void playVoice(MyMessage message) {
        FileInputStream mFIS = null;
        try {
            MediaPlayer mMediaPlayer = new MediaPlayer();
            String mediaPath = message.getMediaFilePath();
            if (mediaPath.contains("https")) {
                mMediaPlayer.setDataSource(message.getMediaFilePath());
            } else {
                mFIS = new FileInputStream(message.getMediaFilePath());
                mMediaPlayer.setDataSource(mFIS.getFD());
            }
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (mFIS != null) {
                    mFIS.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //聊天控件相机回调
    private void initChatViewCameraCallbackListener() {
        //发送照相机照的照片
        mChatView.setOnCameraCallbackListener(new OnCameraCallbackListener() {
            @Override
            public void onTakePictureCompleted(String photoPath) {
                //发送图片

                topPart.setVisibility(View.VISIBLE);
                scrollToBottom();
                if (photoPath != null) {
                    //发送照片
                    UploadPic uploadPic = new UploadPic();
                    String[] str = photoPath.split("\\/");

                    if (str != null && str.length > 0) {
                        sendImageMessage(uploadPic, photoPath, str[str.length - 1]);
                    }
                }
            }

            @Override
            public void onStartVideoRecord() {

            }

            @Override
            public void onFinishVideoRecord(String videoPath) {
                topPart.setVisibility(View.VISIBLE);
                scrollToBottom();
            }

            @Override
            public void onCancelVideoRecord() {
                topPart.setVisibility(View.VISIBLE);
                scrollToBottom();
            }

            @Override
            public void onCancelTakePicture() {
                topPart.setVisibility(View.VISIBLE);
                scrollToBottom();
            }

            @Override
            public void openRecord() {
                //打开摄像机 为摄像机布局腾出空间
                topPart.setVisibility(View.GONE);

            }
        });
    }

    //聊天控件录音事件
    private void initChatViewRecordVoiceListener() {
        mChatView.setRecordVoiceListener(new RecordVoiceListener() {
            @Override
            public void onStartRecord() {
                // set voice file path, after recording, audio file will save here
                String path = Environment.getExternalStorageDirectory().getPath() + "/voice";
                File destDir = new File(path);
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                mChatView.setRecordVoiceFile(destDir.getPath(), DateFormat.format("yyyy-MM-dd-hhmmss",
                        Calendar.getInstance(Locale.CHINA)) + "");
            }

            @Override
            public void onFinishRecord(File voiceFile, int duration) {

                sendVoiceMessage(voiceFile, duration);


            }

            @Override
            public void onCancelRecord() {

            }

            /**
             * In preview record voice layout, fires when click cancel button
             * Add since chatinput v0.7.3
             */
            @Override
            public void onPreviewCancel() {

            }

            /**
             * In preview record voice layout, fires when click send button
             * Add since chatinput v0.7.3
             */
            @Override
            public void onPreviewSend() {

            }
        });
    }

    //聊天控件菜单点击事件
    private void initChatViewMenuClickListener() {
        mChatView.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public void videoChat() {
                //视频聊天的代码


                Toast toast = Toast.makeText(getApplicationContext(), "这里是视频", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();


                Intent intent = new Intent(MessageListActivity.this, videoRequestActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in_out, R.anim.fade_in_out);








            }

            //发送文字消息
            @Override
            public boolean onSendTextMessage(CharSequence input) {
                if (input.length() == 0) {
                    return false;
                }

                sendTimeBar();
                String text = input.toString();
                sendTextMessage(text, null);
                return true;
            }

            //发送图片消息
            @Override
            public void onSendFiles(List<FileItem> list) {
                if (list == null || list.isEmpty()) {
                    return;
                }

                sendTimeBar();
                UploadPic uploadPic = new UploadPic();
                //遍历选择的文件
                for (FileItem item : list) {
                    if (item.getType() == FileItem.Type.Image) {
                        sendImageMessage(uploadPic, item.getFilePath(), item.getFileName());
                    }
//                  else if (it.getType() == FileItem.Type.Video) {
//                        message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO.ordinal());
//                        message.setDuration(((VideoItem) it).getDuration());
//                  } else {
//                        throw new RuntimeException("Invalid FileItem type. Must be Type.Image or Type.Video");
//                  }
                }
            }

            @Override
            public boolean switchToMicrophoneMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                if (!EasyPermissions.hasPermissions(MessageListActivity.this, perms)) {
                    EasyPermissions.requestPermissions(MessageListActivity.this,
                            getResources().getString(R.string.rationale_record_voice),
                            RC_RECORD_VOICE, perms);
                }
                return true;
            }

            @Override
            public boolean switchToGalleryMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                };

                if (!EasyPermissions.hasPermissions(MessageListActivity.this, perms)) {
                    EasyPermissions.requestPermissions(MessageListActivity.this,
                            getResources().getString(R.string.rationale_photo),
                            RC_PHOTO, perms);
                }
                // If you call updateData, select photo view will try to update data(Last update over 30 seconds.)
                mChatView.getChatInputView().getSelectPhotoView().updateData();
                return true;
            }

            @Override
            public boolean switchToCameraMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                };
//
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(MessageListActivity.this,Manifest.permission.CAMERA)){
//                    ActivityCompat.requestPermissions(MessageListActivity.this, new String[]{Manifest.permission.CAMERA},0);
//                }

                if (!EasyPermissions.hasPermissions(MessageListActivity.this, perms)) {
                    EasyPermissions.requestPermissions(MessageListActivity.this,
                            getResources().getString(R.string.rationale_camera),
                            RC_CAMERA, perms);


                    return false;
                } else {
                    File rootDir = getFilesDir();
                    String fileDir = rootDir.getAbsolutePath() + "/photo";
                    mChatView.setCameraCaptureFile(fileDir, new SimpleDateFormat("yyyy-MM-dd-hhmmss",
                            Locale.getDefault()).format(new Date()));
                }
                scrollToBottom();
                return true;
            }

            @Override
            public boolean switchToEmojiMode() {
                scrollToBottom();
                return true;
            }

            @Override
            public void switchToMenuItemHideShowMode() {
                scrollToBottom();
            }

            @Override
            public boolean onSendImageMessage(String iconText, String path) {
                //表情包
                if (path == null) {
                    return false;
                } else {
                    sendTextMessage(iconText, path);
                    return true;
                }
            }
        });
    }

//
//    //初始化顶部菜单点击事件
//    private void initTopMenuClickListener() {
//
//        //发送电话交换请求
//        message_middle_select_bar1.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceType")
//            @Override
//            public void onClick(View v) {
//                if (exchangeInfoEnabled) {
//                    requestCreateExchangesInfoApi("TELEPHONE", null, false);
//                }
//            }
//        });
//
//        //发送Line交换请求
//        message_middle_select_bar2.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceType")
//            @Override
//            public void onClick(View v) {
//                if (exchangeInfoEnabled) {
//                    requestCreateExchangesInfoApi("LINE", null, false);
//                }
//            }
//        });
//
//        //简历上弹框的显示与关闭
//        message_middle_select_bar3.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceType")
//            @Override
//            public void onClick(View v) {
////                if (exchangeInfoEnabled) {
////                    showResumeList(1, "");
////                }
//            }
//        });
//
//        //标记对方
//        message_middle_select_bar4.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceType")
//            @Override
//            public void onClick(View v) {
//                hideResumeMenu();
//                mChatView.getChatInputView().closeKeyBoard();
//
//
//
//                if (dropMenuFragment == null && fragmentShadow == null) {
//                    FragmentTransaction mTransaction = getFragmentManager().beginTransaction();
//                    fragmentShadow = new ShadowFragment();
//                    mTransaction.add(R.id.chat_view, fragmentShadow);
//
//                    if (now_groupId == -100) {
//                        Intent intent = getIntent();
//                        now_groupId = intent.getIntExtra("groupId", -100);
//                    }
//
//                    dropMenuFragment = new DropMenuFragment(now_groupId);
//                    mTransaction.setCustomAnimations(R.anim.top_in_a, R.anim.top_out_a);
//                    mTransaction.add(R.id.chat_view, dropMenuFragment);
//
//                    mTransaction.commit();
//                } else {
//                    hideDropMenu();
//                }
//            }
//        });
//    }


    private void sendTextMessage(String str, String ico) {


        reconnectSocket();
        try {
            JSONObject sendMessage = new JSONObject(sendMessageModel.toString());
            ((JSONObject) sendMessage.get("content")).put("msg", str);
            //Socket.Channel channelSend = socket.getChannelByName("p_e42c10f3-f005-403d-81d6-bac73edc6673");
            MyMessage message = null;
            if (ico == null) {
                message = new MyMessage(str, IMessage.MessageType.SEND_TEXT.ordinal());
            } else {
                //收藏的图片
                message = new MyMessage(str, IMessage.MessageType.SEND_EMOTICON.ordinal());
                message.setMediaFilePath(ico);


                mPathList.add(ico + "");
                mMsgIdList.add(message.getMsgId());
            }
            message.setUserInfo(new DefaultUser("1", "", myLogo));
            //  message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
            message.setMessageStatus(IMessage.MessageStatus.SEND_GOING);
            mAdapter.addToStart(message, true);

            final String thisMessageId = message.getMsgId();

            if (channelSend == null && HIS_ID != null && !HIS_ID.trim().equals("")) {
                channelSend = socket.createChannel("f_" + HIS_ID);
            }


            if (HIS_ID == null || HIS_ID.trim().equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "No His ID", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }

            channelSend.publish(sendMessage, new Ack() {
                public void call(String channelName, Object error, Object data) {
                    if (error == null) {
                        //成功 修改信息状态
                        System.out.println("Published message to channel " + channelName + " successfully");
                        try {
                            JSONObject getData = new JSONObject(data.toString());
                            JSONObject messageJson = getData.getJSONObject("data");

                            String senderId = messageJson.getJSONObject("sender").get("id").toString();
                            String type = messageJson.get("type").toString();
                            JSONObject content = messageJson.getJSONObject("content");
                            if (senderId != null && senderId.equals(MY_ID)) {
                                //我发送的信息更新状态
                                if (type != null && type.equals("p2p") && content.getString("type") != null && content.getString("type").equals("text")) {
                                    //更新状态
                                    MyMessage message = mAdapter.getMessageById(thisMessageId);
                                    message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);

                                    final MyMessage message_callBack = message;
                                    MessageListActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.updateMessage(thisMessageId, message_callBack);
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //失败
                        System.out.println("发送消息出错了");
                        System.out.println(error);
                    }
                }
            });
        } catch (Exception R) {
        }

    }

    //标记为已读
    private void setAsRead(String hisId) {
        reconnectSocket();
        if (hisId != null) {
            socket.emit("setStatusAsRead", hisId);
        }
    }

    //下一页,请求历史
    private void loadNextPage(String lastMsgId) {
        reconnectSocket();
        //String jstr = "{\"uids\":[\"" + MY_ID + "\",\"" + HIS_ID + "\"]}";
        try {
            JSONObject request = new JSONObject();
            request.put("lastMsgId", lastMsgId);
            request.put("type", "p2p");
            request.put("contact_id", HIS_ID);

            System.out.println("发送的请求历史的参数" + request);


            socket.emit("queryHistoryData", request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Boolean isFirstLoadHistoryData = true;

    //加载历史消息
    private void initHistoryMessageList(JSONArray data) {

        System.out.println("得到的历史消息" + data);
        for (int i = 0; i < data.length(); i++) {
            try {
                System.out.println("得到的历史消息" + data.getJSONObject(i).getString("_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        historyMessage = data;
        //
        //只要有数据，就不是初次聊天
        if (isFirstLoadHistoryData) {
            isFirstLoadHistoryData = false;
            if (historyMessage.length() <= 0) {
                isFirstChat = true;
            }
        }

        Message message = new Message();
        historyMessageHandler.sendMessage(message);
    }

    //展示当前接收或发送的消息到页面上
    private void showMessageOnScreen(JSONObject jsono) {

        System.out.println("--最新消息--");
        System.out.println(jsono);


        String senderId = null;
        try {

            if (!jsono.has("sender"))
                return;
            JSONObject sender = jsono.getJSONObject("sender");
            senderId = sender.get("id").toString();

            JSONObject content = new JSONObject(jsono.get("content").toString());
            String type = jsono.get("type").toString();
            if (senderId != null && senderId.equals(MY_ID)) {
                //我发送的信息
                System.out.println("我发送的");
                sendMeesage_me = true;

            } else if (senderId != null && senderId.equals(HIS_ID)) {
                //我当前接收的
                System.out.println("我接收的");
                System.out.println(content);
                sendMessage_he = true;

                String interviewId = "";
                if (content.has("interviewId")) {
                    interviewId = content.get("interviewId").toString();
                }


                if (type != null && type.equals("p2p")) {
                    MyMessage message = null;
                    String contentMsg = content.get("msg").toString();
                    String msgType = content.get("type").toString();
                    if (msgType != null && msgType.equals("text")) {
                        //文字消息
//                      String path=getEmotion(contentMsg);
//                      if(path==null){
                        message = new MyMessage(contentMsg, IMessage.MessageType.RECEIVE_TEXT.ordinal());
//                        }
//                        else{
//                            message = new MyMessage(contentMsg, IMessage.MessageType.RECEIVE_EMOTICON.ordinal());
//                            message.setMediaFilePath(path);
//                            mPathList.add(path+"");
//                            mMsgIdList.add(message.getMsgId());
//                        }
                    } else if (msgType != null && msgType.equals("image")) {
                        //图片消息
                        message = new MyMessage(null, IMessage.MessageType.RECEIVE_IMAGE.ordinal());
                        message.setMediaFilePath(contentMsg);

                        mPathList.add(contentMsg);
                        mMsgIdList.add(message.getMsgId());
                    } else if (msgType != null && msgType.equals("system")) {

                        if (contentMsg.startsWith("TIME")) {
                            //时间
                            //不能在这里显示,因为当此系统消息显示时间的时候,是在回调的时候才显示,而相对应的消息是秒显示在屏幕
                            //历史消息没有问题可以这样,应为历史消息是按照顺序显示的
//                            String messageTimeString=sdf_show_year.format(new Date(Long.parseLong(contentMsg.substring(4,contentMsg.length()))));
//                            if (nowTimeString.substring(0, 4).equals(messageTimeString.substring(0, 4))) {
//                                if (nowTimeString.substring(8, 10).equals(messageTimeString.substring(8, 10))) {
//                                    messageTimeString = messageTimeString.substring(12, 17);
//                                } else {
//                                    messageTimeString = messageTimeString.substring(5, 17);
//                                }
//                            }
//                            message = new MyMessage(messageTimeString, IMessage.MessageType.EVENT.ordinal());


                        } else {
                            //系统消息
                            message = new MyMessage(contentMsg, IMessage.MessageType.EVENT.ordinal());
                            if (content.has("interviewId") && content.has("duration")) {
                                if (content.getInt("duration") == 1) {
                                    latestVideoMessageInterviewId.add(content.getString("interviewId"));
                                    setVideoInviteHandled();
                                }
                            }
                        }


                    } else if (msgType != null && msgType.equals("exchangePhone")) {
                        //对方的电话交换请求
                        message = new MyMessage(contentMsg, IMessage.MessageType.RECEIVE_COMMUNICATION_PHONE.ordinal());
                        message.setInterviewId(interviewId);
                        message.setMessageChannelMsgId(jsono.getString("_id"));
                    } else if (msgType != null && msgType.equals("phoneAgree")) {
                        //同意电话交换请求
                        message = new MyMessage(contentMsg, IMessage.MessageType.RECEIVE_ACCOUNT_PHONE.ordinal());
                    } else if (msgType != null && msgType.equals("exchangeLine")) {
                        //对方的Line交换请求
                        message = new MyMessage(contentMsg, IMessage.MessageType.RECEIVE_COMMUNICATION_LINE.ordinal());
                        message.setInterviewId(interviewId);
                        message.setMessageChannelMsgId(jsono.getString("_id"));
                    } else if (msgType != null && msgType.equals("lineAgree")) {
                        //同意Line交换请求
                        message = new MyMessage(contentMsg, IMessage.MessageType.RECEIVE_ACCOUNT_LINE.ordinal());
                    } else if (msgType != null && msgType.equals("inviteInterview")) {
                        //面试 请求
                        String interviewType = content.get("interviewType").toString();
                        if (interviewType != null && !"".equals(interviewType)) {
                            if (interviewType.equals("ONLINE")) {
                                //线上  视频
                                message = new MyMessage(contentMsg, IMessage.MessageType.RECEIVE_COMMUNICATION_VIDEO.ordinal());
                                message.setRoomNumber(interviewId);
                                message.setInterviewId(interviewId);

                            } else {
                                //线下  普通面试
                                message = new MyMessage(contentMsg, IMessage.MessageType.RECEIVE_NORMAL_INTERVIEW.ordinal());
                                message.setInterviewId(interviewId);

                            }
                        }
                        message.setMessageChannelMsgId(jsono.getString("_id"));
                    } else if (msgType != null && msgType.equals("inviteVideo")) {
                        //进入视频面试邀请
                        message = new MyMessage(contentMsg, IMessage.MessageType.RECEIVE_INTERVIEW_VIDEO.ordinal());
                        message.setMessageChannelMsgId(jsono.getString("_id"));
                        message.setInterviewId(interviewId);
                        message.setRoomNumber(interviewId);
                    } else if (msgType != null && msgType.equals("interviewResult")) {
                        //其他面试结果  当前处理为面试通过!
                        message = new MyMessage(contentMsg, IMessage.MessageType.INTERVIEW_SUCCESS.ordinal());
                    } else if (msgType != null && msgType.equals("interviewReject")) {
                        //其他面试结果  面试不通过
                        message = new MyMessage(contentMsg, IMessage.MessageType.INTERVIEW_FAIL.ordinal());
                    } else if (msgType != null && msgType.equals("sendOffer")) {
                        //offer
                        message = new MyMessage(contentMsg, IMessage.MessageType.SEND_OFFER.ordinal());
                        message.setInterviewId(interviewId);
                        message.setMessageChannelMsgId(jsono.getString("_id"));
                    } else if (msgType != null && msgType.equals("sendResume")) {
                        //请求简历
                        message = new MyMessage(contentMsg, IMessage.MessageType.RECEIVE_REQUEST_RESUME.ordinal());
                        message.setInterviewId(interviewId);
                    } else if (msgType != null && msgType.equals("videoOver")) {

                        //对方主动视频结束
                        videoChatControllerListener.closeVideo();
                        System.out.println("对方主动视频结束");
                        System.out.println(contentMsg);
                        message = new MyMessage(contentMsg, IMessage.MessageType.EVENT.ordinal());


                    }
                    if (msgType != null && msgType.equals("system")) {
                    }else{
                        sendTimeBar();
                    }

                    final MyMessage message_recieve = message;
                    final String msgType_f = msgType;
                    if (message_recieve != null) {
                        MessageListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!msgType_f.equals("system") && !msgType_f.equals("videoOver")) {
                                    //系统消息没有头像
                                    //剔除系统消息
                                    message_recieve.setUserInfo(new DefaultUser("1", "", hisLogo));
                                   // message_recieve.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                                    message_recieve.setMessageStatus(IMessage.MessageStatus.RECEIVE_SUCCEED);
                                }
                                message_recieve.setHandled(false);
                                mAdapter.addToStart(message_recieve, true);
                                mAdapter.notifyDataSetChanged();
                                mChatView.getMessageListView().smoothScrollToPosition(0);


                                if (message_recieve.getType() == IMessage.MessageType.RECEIVE_INTERVIEW_VIDEO.ordinal()) {
                                    //没有处理的视频邀请
                                    //当对方主动关闭，而我方还没有处理的时候，要将其设置为不可点击
                                    latestVideoMessage.add(message_recieve);
                                }

                            }
                        });
                    }
                }
                setAsRead(HIS_ID);
            }
            //没有历史消息时，把接受或者发送的第一条消息作为lastShowedMessageId
            if (lastShowedMessageId == null) {
                lastShowedMessageId = jsono.getString("_id");
            }


            latestMessageTime = Long.parseLong(jsono.get("created").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (!exchangeInfoEnabled && sendMessage_he && sendMeesage_me) {
            exchangeInfoEnabled = true;
            //setEnabledStyleOfExchangeIcon();
        }
    }

    //初始化信息
    private void initMessageChannel() {
        application = App.Companion.getInstance();
        socket = application.getSocket();


        if( socket!=null && socket.isconnected()   &&(WebSocketState.OPEN == socket.getCurrentState() || WebSocketState.CREATED == socket.getCurrentState())) {

        } else {
            System.out.println("socket失效，重连中！！！！！！！--");


//                Toast toast = Toast.makeText(getApplicationContext(), "ネットワーク異常、再接続中", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();

            application.closeMessage();


            while (true) {
                if( socket!=null && socket.isconnected()   &&(WebSocketState.OPEN == socket.getCurrentState() || WebSocketState.CREATED == socket.getCurrentState())) {

                    break;

                }
            }
        }


        Intent intent = getIntent();
        String hisId = intent.getStringExtra("hisId");

        thisCommunicationPositionId = intent.getStringExtra("position_id");
        String company_id = intent.getStringExtra("company_id");
        //设置他的logo
        hisLogo = intent.getStringExtra("hislogo");
        authorization = "Bearer " + application.getMyToken();
        MY_ID = application.getMyId();
        HIS_ID = hisId;

        System.out.println("他的ID" + HIS_ID);
        System.out.println("我的ID" + MY_ID);

        //设置我的头像
        myLogo = application.getMyLogoUrl();

        //myLogo="https://findbest-test-1258431445.cos.ap-chengdu.myqcloud.com/14e103b6-97e6-45b1-8d1b-ad5852bff725.jpg";


        try {
            sendMessageModel = new JSONObject("{ \"sender\":{\"id\": \"" + MY_ID + "\",\"name\": \"\" }," +
                    "\"receiver\":{ \"id\": \"" + HIS_ID + "\", \"name\": \"\" }," +
                    "\"content\":{ \"type\": \"text\", \"msg\": \"\" }, " +
                    "\"type\":\"p2p\"}}");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        application.setRecieveMessageListener(new RecieveMessageListener() {
            @Override
            public void getNormalMessage(@NotNull String str) {
                try {
                    JSONObject jsono = new JSONObject(str);
                    showMessageOnScreen(jsono);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("普通消息");
            }

            @Override
            public void getHistoryMessage(@NotNull String str) {
                try {
                    JSONObject jsono = new JSONObject(str);
                    initHistoryMessageList(jsono.getJSONArray("content"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("历史消息");
            }
        });

        if (HIS_ID != null && !HIS_ID.trim().equals("")) {
            channelSend = socket.createChannel("f_" + HIS_ID);
        } else {
            Toast toast1 = Toast.makeText(getApplicationContext(), "No ID", Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.CENTER, 0, 0);
            toast1.show();
        }

        if (company_id != null && !"".equals(company_id)) {
            //添加联系人
            JSONObject contact = new JSONObject();
            try {
                contact.put("contact_id", HIS_ID);
                contact.put("position_id", thisCommunicationPositionId);
                contact.put("company_id", company_id);
                contact.put("role", "personal");

                socket.emit("addContact", contact);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    //发送语音消息
    private void sendVoiceMessage(File voiceFile, int duration) {

        UploadVoice uploadVoice = new UploadVoice();

        final String voidPath = voiceFile.getPath();
        final RequestBody voice_file = uploadVoice.getVoiceData(voidPath);

        final String fileName = voiceFile.getName();
        final int voiceDuration = duration;
        //token

        final OkHttpClient client = new OkHttpClient();


//        Toast.makeText(MessageListActivity.this, voidPath,
//                Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MyMessage message;
                    message = new MyMessage(null, IMessage.MessageType.SEND_VOICE.ordinal());
                    message.setUserInfo(new DefaultUser("1", "", myLogo));
                    message.setMediaFilePath(voidPath);
                    message.setDuration(voiceDuration);
                  //  message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                    message.setMessageStatus(IMessage.MessageStatus.SEND_GOING);

                    final MyMessage fMsg_sending = message;
                    MessageListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addToStart(fMsg_sending, true);
                            scrollToBottom();
                        }
                    });


                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("files", fileName, voice_file)
                            .build();

                    //测试地址 https://storage.sk.cgland.top/api/v1/storage
                    //正式地址 https://storage.sk.skjob.jp/api/v1/storage
                    Request request = new Request.Builder()
                            .url(thisContext.getString(R.string.storageUrl) + "api/v1/storage")
                            .addHeader("Authorization", authorization)
                           // .addHeader("Content-Type", "multipart/form-data")
                            .post(requestBody)
                            .build();


                    Response response;
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        //请求成功
                        String jsonString = response.body().string();
                        System.out.println("发送语音成功!返回数据");
                        System.out.println(jsonString);


                        JSONObject result = new JSONObject(jsonString);

                        JSONObject sendMessage = new JSONObject(sendMessageModel.toString());
                        sendMessage.getJSONObject("content").put("msg", result.getString("url"));
                        sendMessage.getJSONObject("content").put("type", "voice");
                        sendMessage.getJSONObject("content").put("duration", voiceDuration);

                        final MyMessage message_f = message;

                        if (channelSend == null && HIS_ID != null && !HIS_ID.trim().equals("")) {
                            channelSend = socket.createChannel("f_" + HIS_ID);
                        }


                        if (HIS_ID == null || HIS_ID.trim().equals("")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "No his ID", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;
                        }


                        channelSend.publish(sendMessage, new Ack() {
                            public void call(String channelName, Object error, Object data) {
                                if (error == null) {
                                    //成功
                                    System.out.println("Published message to channel " + channelName + " successfully");
                                    System.out.println(data);

                                  //  message_f.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                                    message_f.setMediaFilePath(voidPath);
                                    message_f.setUserInfo(new DefaultUser("1", "", myLogo));
                                    message_f.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                    message_f.setDuration(voiceDuration);

                                    final MyMessage fMsg_success = message_f;
                                    MessageListActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.updateMessage(fMsg_success.getMsgId(), fMsg_success);
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });

                                } else {
                                    //失败
                                }
                            }
                        });

                    } else {
                        System.out.println("发送语音请求失败");
                    }
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 发送图片
     *
     * @param uploadPic
     * @param imagePath
     * @param imageName
     */
    private void sendImageMessage(UploadPic uploadPic, String imagePath, String imageName) {
        try {
            //图片二进制数据
            final RequestBody image_file = uploadPic.getImageDate(imagePath);
            //文件名
            final String fileName = imageName;
            final OkHttpClient client = new OkHttpClient();
            final String path = imagePath;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MyMessage message;
                        message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());

                        mPathList.add(path);
                        mMsgIdList.add(message.getMsgId());
                     //   message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        message.setMediaFilePath(path);
                        message.setUserInfo(new DefaultUser("1", "", myLogo));
                        message.setMessageStatus(IMessage.MessageStatus.SEND_GOING);

                        final MyMessage fMsg_sending = message;
                        MessageListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.addToStart(fMsg_sending, true);
                                scrollToBottom();
                            }
                        });

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                ///                .addPart(
                                //                        Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\"" + fileName + "\""),
                                //                        RequestBody.create(MEDIA_TYPE_PNG, file))
                                //                .addPart(
                                //                        Headers.of("Content-Disposition", "form-data; name=\"imagetype\""),
                                //                        RequestBody.create(null, imageType))
                                //                .addPart(
                                //                        Headers.of("Content-Disposition", "form-data; name=\"userphone\""),
                                //                        RequestBody.create(null, userPhone))

                                .addFormDataPart("files", fileName, image_file)

//                                                .addFormDataPart("authorization", authorization)
                                .build();

                        //测试地址 https://storage.sk.cgland.top/api/v1/storage
                        //正式地址 https://storage.sk.skjob.jp/api/v1/storage
                        Request request = new Request.Builder()
                                .url(thisContext.getString(R.string.storageUrl) + "api/v1/storage")
                                .addHeader("Authorization", authorization)
                               // .addHeader("Content-Type", "multipart/form-data")
                                .post(requestBody)
                                .build();



                        System.out.println("---------------++++");
                        System.out.println(request.url().query());
                        System.out.println(request.body().contentType());


                        Response response;
                        response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            //请求成功
                            String jsonString = response.body().string();
                            System.out.println("发送图片返回数据");
                            System.out.println(jsonString);


                            JSONArray array = new JSONArray(jsonString);
                            JSONObject result=array.getJSONObject(0);
                            JSONObject sendMessage = new JSONObject(sendMessageModel.toString());
                            sendMessage.getJSONObject("content").put("msg", result.getString("url"));
                            sendMessage.getJSONObject("content").put("type", "image");

                            final MyMessage message_f = message;


                            if (channelSend == null && HIS_ID != null && !HIS_ID.trim().equals("")) {
                                channelSend = socket.createChannel("f_" + HIS_ID);
                            }


                            if (HIS_ID == null || HIS_ID.trim().equals("")) {
                                Toast toast = Toast.makeText(getApplicationContext(), "No his ID", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return;
                            }
                            channelSend.publish(sendMessage, new Ack() {
                                public void call(String channelName, Object error, Object data) {
                                    if (error == null) {
                                        //成功
                                        System.out.println("Published message to channel " + channelName + " successfully");
                                        System.out.println(data);

                                        //message_f.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                                        message_f.setMediaFilePath(path);
                                        message_f.setUserInfo(new DefaultUser("1", "", myLogo));
                                        message_f.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);

                                        final MyMessage fMsg_success = message_f;
                                        MessageListActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mAdapter.updateMessage(fMsg_success.getMsgId(), fMsg_success);
                                                mAdapter.notifyDataSetChanged();
                                            }
                                        });

                                    } else {
                                        //失败
                                    }
                                }
                            });

                        } else {

                            System.out.println("发送图片请求失败");
                            System.out.println(response.code());
                            System.out.println(response.body().string());
                            System.out.println(response.message());

                        }
                        response.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //滑动到底部
    private void scrollToBottom() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                mAdapter.getLayoutManager().scrollToPosition(0);
                mChatView.getMessageListView().smoothScrollToPosition(0);
            }
        }, 10);
    }

    //滑动到底部
    private void scrollToBottom(int m) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                mAdapter.getLayoutManager().scrollToPosition(0);
                mChatView.getMessageListView().smoothScrollToPosition(0);
            }
        }, m);
    }

    //销毁消息通道
    public void DestroyMessageChannel() {

    }

    //加载历史消息
    @SuppressLint("HandlerLeak")
    private Handler historyMessageHandler = new Handler() {
        public void handleMessage(Message mes) {

            System.out.println("进入历史消息！！！");


            List<MyMessage> list = new ArrayList<>();
            try {
                //因为  如果  职位信息都展示出来了  那么必定没有更多的历史消息展示在它上面
                if (positionshowedFlag) {

                    //展示
                    for (int i = 0; i < historyMessage.length(); i++) {
                        String senderId;
                        //发送者Id
                        senderId = historyMessage.getJSONObject(i).getJSONObject("sender").getString("id");

                        JSONObject content = historyMessage.getJSONObject(i).getJSONObject("content");

                        if (historyMessage.getJSONObject(i).has("type")) {
                        } else {
                            continue;
                        }
                        //
                        String type = historyMessage.getJSONObject(i).getString("type");
                        //消息ID
                        String msg_id = historyMessage.getJSONObject(i).getString("_id");

                        MyMessage message = null;

                        if (type != null && type.equals("p2p")) {

                            String msg = content.getString("msg");
                            String contetType = content.get("type").toString();
                            String handled = null;
                            if (content.has("handled")) {
                                handled = content.get("handled").toString();
                            }

                            if (senderId != null && senderId.equals(MY_ID)) {


                                sendMeesage_me = true;
                                //我发的消息
                                if (contetType != null && contetType.equals("text")) {
                                    //文字消息
//                                String path=getEmotion(msg);
//                                if(path==null){
                                    message = new MyMessage(msg, IMessage.MessageType.SEND_TEXT.ordinal());
//                                }
//                                else{
//                                    message = new MyMessage(msg, IMessage.MessageType.SEND_EMOTICON.ordinal());
//                                    message.setMediaFilePath(path);
//
//                                    mPathList.add(path+"");
//                                    mMsgIdList.add(message.getMsgId());
//                                }
                                } else if (contetType != null && contetType.equals("image")) {
                                    //图片
                                    message = new MyMessage("", IMessage.MessageType.SEND_IMAGE.ordinal());
                                    message.setMediaFilePath(msg);
                                } else if (contetType != null && contetType.equals("voice")) {
                                    //语音
                                    int voiceDuration = content.getInt("duration");
                                    message = new MyMessage("", IMessage.MessageType.SEND_VOICE.ordinal());
                                    message.setMediaFilePath(msg);
                                    message.setDuration(voiceDuration);
                                } else if (contetType != null && contetType.equals("sendResumeAgree")) {
                                    //简历信息
                                    int messageType = IMessage.MessageType.SEND_RESUME_WORD.ordinal();

                                    String attachmentType = content.get("attachmentType").toString();
                                    String url = content.get("url").toString();


                                    if (attachmentType != null && attachmentType.contains("pdf")) {
                                        messageType = IMessage.MessageType.SEND_RESUME_PDF.ordinal();
                                    } else if (attachmentType != null && attachmentType.contains("word")) {
                                        messageType = IMessage.MessageType.SEND_RESUME_WORD.ordinal();
                                    } else if (attachmentType != null && attachmentType.contains("jpg")) {
                                        messageType = IMessage.MessageType.SEND_RESUME_JPG.ordinal();
                                    }

                                    message = new MyMessage(msg, messageType);
                                    if (content.has("interviewId")) {
                                        String interviewId = content.get("interviewId").toString();
                                        message.setInterviewId(interviewId);
                                    } else {

                                    }
                                    message.setMediaFilePath(url);
                                } else {
                                    //其他消息
                                    message = new MyMessage(msg, IMessage.MessageType.SEND_TEXT.ordinal());
                                }
                                message.setUserInfo(new DefaultUser("1", "", myLogo));
                                message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                            } else {
                                sendMessage_he = true;
                                //这个id  交换信息的id  用于修改信息状态
                                String interviewId = "";
                                if (content.has("interviewId")) {
                                    interviewId = content.get("interviewId").toString();
                                }

                                //我接收的消息
                                if (contetType != null && contetType.equals("text")) {
                                    //文字
//                                String path=getEmotion(msg);
//                                if(path==null){
                                    message = new MyMessage(msg, IMessage.MessageType.RECEIVE_TEXT.ordinal());
//                                }else{
//                                    message = new MyMessage(msg, IMessage.MessageType.RECEIVE_EMOTICON.ordinal());
//                                    message.setMediaFilePath(path);
//                                    mPathList.add(path+"");
//                                    mMsgIdList.add(message.getMsgId());
//                                }
                                } else if (contetType != null && contetType.equals("image")) {
                                    //图片
                                    message = new MyMessage("", IMessage.MessageType.RECEIVE_IMAGE.ordinal());
                                    message.setMediaFilePath(msg);
                                } else if (contetType != null && contetType.equals("voice")) {
                                    //语音
                                    int voiceDuration = content.getInt("duration");
                                    message = new MyMessage("", IMessage.MessageType.RECEIVE_VOICE.ordinal());
                                    message.setMediaFilePath(msg);
                                    message.setDuration(voiceDuration);
                                } else if (contetType != null && contetType.equals("system")) {

                                    if (msg.startsWith("TIME")) {
                                        //时间



                                        String messageTimeString = sdf_show_year.format(new Date(Long.parseLong(msg.substring(4, msg.length()))));

                                        if (nowTimeString.substring(0, 4).equals(messageTimeString.substring(0, 4))) {
                                            if (nowTimeString.substring(8, 10).equals(messageTimeString.substring(8, 10))) {
                                                messageTimeString = messageTimeString.substring(12, 17);
                                            } else {
                                                messageTimeString = messageTimeString.substring(5, 17);
                                            }
                                        }

                                        message = new MyMessage(messageTimeString, IMessage.MessageType.EVENT.ordinal());

                                    } else {
                                        //系统消息
                                        message = new MyMessage(msg, IMessage.MessageType.EVENT.ordinal());

                                        if (content.has("interviewId") && content.has("duration")) {
                                            if (content.getInt("duration") == 1) {
                                                latestVideoMessageInterviewId.add(content.getString("interviewId"));
                                                setVideoInviteHandled();
                                            }
                                        }
                                    }


                                } else if (contetType != null && contetType.equals("exchangePhone")) {
                                    //对方请求交换电话
                                    //消息已经被处理了
                                    if (handled != null && handled.equals("true")) {
                                        message = new MyMessage(msg, IMessage.MessageType.RECEIVE_EXCHANGE_PHONE_HANDLED.ordinal());
                                    } else {
                                        //消息没有被处理了
                                        message = new MyMessage(msg, IMessage.MessageType.RECEIVE_COMMUNICATION_PHONE.ordinal());
                                        message.setInterviewId(interviewId);
                                        message.setMessageChannelMsgId(msg_id);
                                    }

                                } else if (contetType != null && contetType.equals("phoneAgree")) {
                                    //同意电话交换请求
                                    message = new MyMessage(msg, IMessage.MessageType.RECEIVE_ACCOUNT_PHONE.ordinal());
                                } else if (contetType != null && contetType.equals("exchangeLine")) {
                                    //对方请求交换LINE
                                    //消息已经被处理了
                                    if (handled != null && handled.equals("true")) {
                                        message = new MyMessage(msg, IMessage.MessageType.RECEIVE_EXCHANGE_LINE_HANDLED.ordinal());
                                    } else {
                                        //消息没有被处理了
                                        message = new MyMessage(msg, IMessage.MessageType.RECEIVE_COMMUNICATION_LINE.ordinal());
                                        message.setInterviewId(interviewId);
                                        message.setMessageChannelMsgId(msg_id);
                                    }
                                } else if (contetType != null && contetType.equals("lineAgree")) {
                                    //同意Line交换请求
                                    message = new MyMessage(msg, IMessage.MessageType.RECEIVE_ACCOUNT_LINE.ordinal());
                                } else if (contetType != null && contetType.equals("inviteInterview")) {
                                    //邀请 面试 分为  线上/线下
                                    //消息已经被处理了
                                    String interviewType = content.get("interviewType").toString();
                                    if (interviewType != null && !"".equals(interviewType) && handled != null && handled.equals("true")) {
                                        if (interviewType.equals("ONLINE")) {
                                            //线上面试 完成
                                            message = new MyMessage(msg, IMessage.MessageType.RECEIVE_INVITE_VIDEO_HANDLED.ordinal());
                                            message.setInterviewId(interviewId);
                                            message.setRoomNumber(interviewId);
                                        } else {
                                            //线下面试 完成
                                            message = new MyMessage(msg, IMessage.MessageType.RECEIVE_NORMAL_INTERVIEW_HANDLED.ordinal());
                                            message.setInterviewId(interviewId);
                                        }
                                    } else {
                                        //消息没有被处理了
                                        if (interviewType != null && !"".equals(interviewType)) {
                                            if (interviewType.equals("ONLINE")) {
                                                //视频面试邀请
                                                message = new MyMessage(msg, IMessage.MessageType.RECEIVE_COMMUNICATION_VIDEO.ordinal());
                                                message.setInterviewId(interviewId);
                                                message.setRoomNumber(interviewId);
                                            } else {
                                                //普通面试邀请
                                                message = new MyMessage(msg, IMessage.MessageType.RECEIVE_NORMAL_INTERVIEW.ordinal());
                                                message.setInterviewId(interviewId);

                                            }
                                        }
                                        message.setMessageChannelMsgId(msg_id);
                                    }


                                } else if (contetType != null && contetType.equals("inviteVideo")) {
                                    //进入视频邀请
                                    //消息已经被处理了
                                    if (handled != null && handled.equals("true")) {
                                        message = new MyMessage(msg, IMessage.MessageType.RECEIVE_INTERVIEW_VIDEO_HANDLED.ordinal());
                                    } else {
                                        //消息没有被处理了
                                        message = new MyMessage(msg, IMessage.MessageType.RECEIVE_INTERVIEW_VIDEO.ordinal());
                                        message.setMessageChannelMsgId(msg_id);
                                        message.setInterviewId(interviewId);
                                        message.setRoomNumber(interviewId);
                                    }
                                } else if (contetType != null && contetType.equals("interviewResult")) {
                                    //其他面试结果  当前处理为面试通过!
                                    message = new MyMessage(msg, IMessage.MessageType.INTERVIEW_SUCCESS.ordinal());
                                } else if (contetType != null && contetType.equals("interviewReject")) {
                                    //其他面试结果  面试不通过
                                    message = new MyMessage(msg, IMessage.MessageType.INTERVIEW_FAIL.ordinal());
                                } else if (contetType != null && contetType.equals("sendOffer")) {
                                    //offer
                                    message = new MyMessage(msg, IMessage.MessageType.SEND_OFFER.ordinal());
                                    message.setInterviewId(interviewId);
                                    message.setMessageChannelMsgId(msg_id);
                                } else if (contetType != null && contetType.equals("sendResume")) {
                                    //请求简历
                                    //消息已经被处理了
                                    if (handled != null && handled.equals("true")) {
                                        message = new MyMessage(msg, IMessage.MessageType.RECEIVE_REQUEST_RESUME_HANDLED.ordinal());
                                    } else {
                                        //消息没有被处理了
                                        message = new MyMessage(msg, IMessage.MessageType.RECEIVE_REQUEST_RESUME.ordinal());


                                        message.setInterviewId(interviewId);
                                        message.setMessageChannelMsgId(msg_id);
                                    }

                                } else if (contetType != null && contetType.equals("videoOver")) {

                                    if (i == historyMessage.length() - 1) {
                                        lastShowedMessageId = historyMessage.getJSONObject(i).getString("_id");
                                    }
                                    message = new MyMessage(msg, IMessage.MessageType.EVENT.ordinal());
                                } else {
                                    //其他消息
                                    message = new MyMessage(msg, IMessage.MessageType.RECEIVE_TEXT.ordinal());
                                }


                                if (!contetType.equals("system") && message != null) {
                                    //系统消息没有头像
                                    message.setUserInfo(new DefaultUser("0", "", hisLogo));
                                }

                            }

                            if (contetType != null && contetType.equals("image")) {

                                mPathList.add(msg);
                                mMsgIdList.add(message.getMsgId());
                            }


                            //替换空项
                            if (i == 0 && topBlankMessageId != null && message != null) {
                                mAdapter.updateMessage(topBlankMessageId, message);
                                mAdapter.notifyDataSetChanged();
                                continue;
                            }

                        }

                        if (message != null) {
                            if (i == historyMessage.length() - 1) {
                                //展示时间
                                try {

                                    lastShowedMessageId = historyMessage.getJSONObject(i).getString("_id");
                                    System.out.println("最后一条消息！！！");

                                    System.out.println(lastShowedMessageId);

//                                String created = historyMessage.getJSONObject(i).getString("created");//又变成毫秒了
//                                created = created.replace('T', ' ');
//                                created = created.substring(0, created.length() - 1);
//
//
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//                                Date createdDate = sdf.parse(created);
//                                SimpleDateFormat sdf_show = new SimpleDateFormat("HH:mm");

                                    //原来做的把历史记录每一页的最后一条就设一个时间显示
                                    //现在把时间显示用system的方式作为消息显示了

//                                    Long created = historyMessage.getJSONObject(i).getLong("created");//又变成毫秒了
//
//                                    String createdDate = sdf_show_year.format(new Date(created));
//
//
//                                    // System.out.println(createdDate.getTime());
//                                    if (nowTimeString.substring(0, 4).equals(createdDate.substring(0, 4))) {
//                                        if (nowTimeString.substring(8, 10).equals(createdDate.substring(8, 10))) {
//                                            createdDate = createdDate.substring(12, 17);
//                                        } else {
//                                            createdDate = createdDate.substring(5, 17);
//                                        }
//                                    }
//
//                                    message.setTimeString(createdDate);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }


                            if (message.getType() == IMessage.MessageType.RECEIVE_INTERVIEW_VIDEO.ordinal()) {
                                //没有处理的视频邀请
                                //当对方主动关闭，而我方还没有处理的时候，要将其设置为不可点击

                                latestVideoMessage.add(message);

                            }

                            list.add(message);
                        }
                        //最后一条历史记录后面，添加空项，下载加载历史记录的第一条取代他，以便历史记录加载出来后，在界面上有所体现
                        if (i == historyMessage.length() - 1) {
                            MyMessage RESET2 = new MyMessage("", IMessage.MessageType.EMPTY.ordinal());
                            list.add(RESET2);
                            topBlankMessageId = RESET2.getMsgId();
                        }


                        if (i == 0) {
                            //最新一条消息的时间
                            latestMessageTime = historyMessage.getJSONObject(i).getLong("created");
                        }


                    }


                    System.out.println("请求历史记录.....................");

                    //在最后添加职位信息
//                    if (historyMessage.length() < 15 && positionshowedFlag) {
//                        //lastPositionId
//
//                        if (isFirstChat) {
//
//
//                        }
//
//                        if (thisCommunicationPositionId != null && !"".equals(thisCommunicationPositionId)) {
//
//
//                            System.out.println("thisCommunicationPositionId.....................");
//                            System.out.println(thisCommunicationPositionId);
//
//
//                            Intent intent = getIntent();
//                            MyMessage jobInfo = new MyMessage(thisCommunicationPositionId, IMessage.MessageType.JOB_INFO.ordinal());
//                            list.add(jobInfo);
//                            //requestPosisionInfo(thisCommunicationPositionId, jobInfo.getMsgId());
//                        } else {
//                            System.out.println("聊天界面在展示职位信息时,数据异常:缺少positionId");
//                        }
//
//                        positionshowedFlag = false;
//
//
//                        MyMessage RESET2 = new MyMessage("", IMessage.MessageType.EMPTY.ordinal());
//                        list.add(RESET2);
//                        topBlankMessageId = RESET2.getMsgId();
//
//                    }
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }

            mAdapter.addHistoryList(list);
            mChatView.getPtrLayout().refreshComplete();
            if (isInitHistory) {
                scrollToBottom();
                isInitHistory = false;
            }
            mChatView.getMessageListView().setScrollY(1000);


            if (HIS_ID != null) {
                if (isFirstChat && !"000000000000000000000000".equals(HIS_ID) && !HIS_ID.equals(MY_ID)) {
                    isFirstChat = false;
                    sendGreeting();
                    // sendTextMessage(text, null);
                }


            }

            if (!exchangeInfoEnabled && sendMessage_he && sendMeesage_me) {
                exchangeInfoEnabled = true;
                //setEnabledStyleOfExchangeIcon();

            }
        }
    };

    //聊天标记
//    @SuppressLint("ResourceType")
//    @Override
//    public void dropMenuOnclick(int i) {
//        reconnectSocket();
//        hideDropMenu();
//        now_groupId = i;
//        try {
//            //接口参数
//            int param = i + 4;
//            JSONObject json = new JSONObject();
//            json.put("contact_id", HIS_ID);
//            json.put("group_id", param);
//            socket.emit("setContactGroup", json, new Ack() {
//                public void call(String eventName, Object error, Object data) {
//                    System.out.println("Got message for :" + eventName + " error is :" + error + " data is :" + data);
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        // Toast.makeText(MessageListActivity.this, i + "",
//        //         Toast.LENGTH_SHORT).show();
//    }

    //选择简历点击
//    @Override
//    public void resumeMenuOnclick(ResumeListItem choosenOne, String InterviewId) {
//        hideResumeMenu();
//
//        try {
//            JSONObject sendMessage = new JSONObject(sendMessageModel.toString());
//            sendMessage.getJSONObject("content").put("msg", choosenOne.getTitle());
//            sendMessage.getJSONObject("content").put("type", "sendResumeAgree");
//            sendMessage.getJSONObject("content").put("attachmentType", choosenOne.getAttachmentType());
//            sendMessage.getJSONObject("content").put("url", choosenOne.getUrl());
//
//            System.out.println("简历的ID" + choosenOne.getMediaId());
//
//            sendMessage.getJSONObject("content").put("interviewId", choosenOne.getMediaId());
//
//
//            System.out.println("简历信息:\n" + sendMessage.toString());
//
//            int messageType = IMessage.MessageType.SEND_RESUME_WORD.ordinal();
//            if (choosenOne.getType() == IMessage.MIMETYPE_PDF) {
//                messageType = IMessage.MessageType.SEND_RESUME_PDF.ordinal();
//            } else if (choosenOne.getType() == IMessage.MIMETYPE_WORD) {
//                messageType = IMessage.MessageType.SEND_RESUME_WORD.ordinal();
//            } else if (choosenOne.getType() == IMessage.MIMETYPE_JPG) {
//                messageType = IMessage.MessageType.SEND_RESUME_JPG.ordinal();
//            }
//
//            //显示消息(发送中)
//            final MyMessage message_f = new MyMessage(choosenOne.getTitle(), messageType);
//            message_f.setUserInfo(new DefaultUser("1", "", myLogo));
//            message_f.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
//            message_f.setMessageStatus(IMessage.MessageStatus.SEND_GOING);
//            message_f.setMimeType(choosenOne.getType());
//            message_f.setMediaFilePath(choosenOne.getUrl());
//            message_f.setInterviewId(choosenOne.getId());
//
//
//            mAdapter.addToStart(message_f, true);
//
//            if (channelSend == null && HIS_ID != null && !HIS_ID.trim().equals("")) {
//                channelSend = socket.createChannel("f_" + HIS_ID);
//            }
//
//
//            if (HIS_ID == null || HIS_ID.trim().equals("")) {
//                Toast toast = Toast.makeText(getApplicationContext(), "No his ID", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//                return;
//            }
//            channelSend.publish(sendMessage, new Ack() {
//                public void call(String channelName, Object error, Object data) {
//                    if (error == null) {
//                        //简历发送成功
//                        System.out.println("Published message to channel " + channelName + " successfully");
//                        System.out.println(data);
//                        message_f.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
//                        final MyMessage fMsg_success = message_f;
//                        MessageListActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mAdapter.updateMessage(fMsg_success.getMsgId(), fMsg_success);
//                                mAdapter.notifyDataSetChanged();
//                            }
//                        });
//
//
//                        System.out.println("改变求简历的状态！！！！！！！！！！！！");
//
//
//                        if (choosenOne.getChooseType() == 1) {
//                            System.out.println("改变求简历的状态1111");
//
//                            //主动发  不用发消息
//                            requestCreateExchangesInfoApi("RESUME", choosenOne.getId(), true);
//                        } else {
//                            //创建 并 改变简历发送状态 为发送成功
//                            //上面是旧的逻辑
//                            //新做法  他们创建  我只修改
//                            System.out.println("改变求简历的状态2222");
//
//                            updateStateOfExchangeInfo(InterviewId, "EXCHANGED", choosenOne.getId());
//                            // requestCreateExchangesInfoApi("RESUME", choosenOne.getId(), true);
//
//                            notifyChoiceResult(null, "あなたは履歴書を送信しました。", "相手は履歴書を送信しました。", false);//你同意向对方发送简历   对方同意并向你发送了简历
//
//                        }
//
//
//                    } else {
//                        //失败
//                    }
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
////        MyMessage message = new MyMessage(choosenOne.getTitle(), IMessage.MessageType.RECEIVE_RESUME.ordinal());
////        message.setSize(choosenOne.getSize());
////        mAdapter.addToStart(message, true);
////
//
//
//    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mChatView.requestFocus();
                mChatView.getChatInputView().getMenuContainer().setVisibility(View.GONE);
                mChatView.getChatInputView().getMyMenuitemContainer().setVisibility(View.GONE);


                mChatView.getChatInputView().closeKeyBoard();


                ChatInputView chatInputView = mChatView.getChatInputView();
                if (chatInputView.getMenuState() == View.VISIBLE) {
                    chatInputView.dismissMenuLayout();
                }
                try {
                    View v = getCurrentFocus();
                    if (mImm != null && v != null) {
                        mImm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        view.clearFocus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                view.performClick();
                break;
        }
        return false;
    }


    //标记
//    Boolean requestUserComplete = false;
//    Boolean requestCompanyComplete = false;
//    Boolean requestAddressComplete = false;
//    Boolean requestUserPositionComplete = false;
//    //福利
//    Boolean haveCanteen = false;
//    Boolean haveClub = false;
//    Boolean haveSocialInsurance = false;
//    Boolean haveTraffic = false;
//    //需要得到的数据
//    String companyName = "";
//    String organizationId = "";//公司Id
//    String recruitMessageId = "";//职位信息的id
//    Boolean isCollection = false;//是否是最新
//    String positionName = "";//职位名称
//    String salaryType = "";//薪水类型
//    String showSalaryMinToMax = "";//展示的薪水
//    String address = "";
//    String workingExperience = "";//工作经验
//    String educationalBackground = "";//教育背景
//    String skill = "";
//    String content = "";//职位的详细描述
//    String userName = "";//用户名
//    String userPositionName = "";//用户职位名
//    String avatarURL = "";//用户头像
//    String userId = "";//用户ID
//    String collectionId = "";//搜藏记录的id
//    String areaId = "";//地区ID
//    String addressId = "";//地点ID
//    String currencyType = "";//货币累心
//    int salaryMin = 0;//薪水min
//    int salaryMax = 0;//薪水max
//    Boolean isNew = false;
//
//    /**
//     * 请求职位详情
//     *
//     * @param lastPositionId
//     */
//    private void requestPosisionInfo(String lastPositionId, String messageId) {
//
//        //搜藏
//        List collectionList = new ArrayList<String>();
//        //记录Id
//        List collectionRecordIdList = new ArrayList<String>();
//
//        //请求FLAG
//
//        final Boolean[] isCollectionComplete = new Boolean[1];
//
//        //测试地址 https://organization-position.sk.cgland.top/
//        //正式地址 https://organization-position.sk.skjob.jp/
//        RetrofitUtils request = new RetrofitUtils(thisContext, this.getString(R.string.organizationUrl));
//        request.create(RecruitInfoApi.class)
//                .getRecruitInfoById(
//                        lastPositionId
//                )
//                .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
//                .observeOn(AndroidSchedulers.mainThread()) //观察者 切换到主线程
//                .subscribe(new Consumer() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        System.out.println("请求职位单个信息成功");
//                        JSONObject jsonOut = new JSONObject(o.toString());
//                        System.out.println(o.toString());
//
//                        JSONObject json = jsonOut.getJSONObject("organization");
//                        isNew = jsonOut.getBoolean("new");
//
//                        if (json.has("name")) {
//                            positionName = json.getString("name");
//                        }
//                        if (json.has("salaryType")) {
//                            salaryType = json.getString("salaryType");
//                        }
//                        if (json.has("organizationId")) {
//                            organizationId = json.getString("organizationId");
//                        }
//                        if (json.has("userId")) {
//                            userId = json.getString("userId");
//                        }
//                        if (json.has("areaId")) {
//                            areaId = json.getString("areaId");
//                        }
//                        if (json.has("addressId")) {
//                            addressId = json.getString("addressId");
//                        }
//                        if (json.has("content")) {
//                            content = json.getString("content");
//                        }
//                        if (json.has("educationalBackground")) {
//                            educationalBackground = json.getString("educationalBackground");
//                        }
//                        if (json.has("salaryMin")) {
//                            salaryMin = json.getInt("salaryMin");
//                        }
//                        if (json.has("salaryMax")) {
//                            salaryMax = json.getInt("salaryMax");
//                        }
//                        if (json.has("currencyType")) {
//                            currencyType = json.getString("currencyType");
//                        }
//                        if (json.has("id")) {
//                            recruitMessageId = json.getString("id");
//                        }
//                        if (json.has("workingExperience")) {
//                            workingExperience = json.getString("workingExperience");
//                        }
//                        if (json.has("skill")) {
//                            skill = json.getString("skill");
//                        }
//
//
//                        //得到教育背景显示的值
//                        educationalBackground = EducationalBackground.Companion.getEducationalBackground(educationalBackground);
//                        showSalaryMinToMax = getSalaryMinToMaxString(salaryMin, salaryMax);
//
//                        if (salaryType != null && salaryType.equals(SalaryType.Key.HOURLY.toString())) {
//                            salaryType = SalaryType.Value.时.toString();
//                        } else if (salaryType != null && salaryType.equals(SalaryType.Key.DAILY.toString())) {
//                            salaryType = SalaryType.Value.日.toString();
//                        } else if (salaryType != null && salaryType.equals(SalaryType.Key.MONTHLY.toString())) {
//                            salaryType = SalaryType.Value.月.toString();
//                        } else if (salaryType != null && salaryType.equals(SalaryType.Key.YEARLY.toString())) {
//                            salaryType = SalaryType.Value.年.toString();
//                        }
//
//
//                        //请求搜藏
//                        //测试地址 https://job.sk.cgland.top/
//                        //正式地址 https://job.sk.skjob.jp/
//                        RetrofitUtils requestCollection = new RetrofitUtils(thisContext, thisContext.getString(R.string.jobUrl));
//                        requestCollection.create(JobApi.class)
//                                .getFavorites(
//                                        1, 1000000, FavoriteType.Key.ORGANIZATION_POSITION.toString()
//                                )
//                                .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
//                                .observeOn(AndroidSchedulers.mainThread()) //观察者 切换到主线程
//                                .subscribe(new Consumer() {
//                                    @Override
//                                    public void accept(Object o) throws Exception {
//                                        System.out.println("搜藏请求成功(消息模块)");
//                                        System.out.println(o.toString());
//                                        isCollectionComplete[0] = true;
//
//                                        JSONObject responseStr = new JSONObject(o.toString());
//                                        JSONArray soucangData = responseStr.getJSONArray("data");
//
//                                        collectionList.clear();
//                                        collectionRecordIdList.clear();
//
//                                        for (int i = 0; i < soucangData.length(); i++) {
//                                            JSONObject item = soucangData.getJSONObject(i);
//                                            String targetEntityId = item.getString("targetEntityId");
//                                            String id = item.getString("id");
//
//                                            collectionList.add(targetEntityId);
//                                            collectionRecordIdList.add(id);
//
//                                        }
//
//
//                                        //请求公司信息
//                                        //测试地址 https://org.sk.cgland.top/
//                                        //正式地址 https://org.sk.skjob.jp/
//                                        RetrofitUtils requestCompany = new RetrofitUtils(thisContext, thisContext.getString(R.string.orgUrl));
//                                        requestCompany.create(RecruitInfoApi.class)
//                                                .getCompanyInfo(
//                                                        organizationId
//                                                )
//                                                .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
//                                                .observeOn(AndroidSchedulers.mainThread()) //观察者 切换到主线程
//
//                                                .subscribe(new Consumer() {
//                                                    @Override
//                                                    public void accept(Object o) throws Exception {
//                                                        System.out.println("单个公司信息请求成功");
//                                                        System.out.println(o.toString());
//                                                        requestCompanyComplete = true;
//
//                                                        JSONObject json = new JSONObject(o.toString());
//                                                        companyName = json.getString("name");
//                                                        String benifitsStr = json.getString("benifits");
//
//                                                        //剃选 福利
//                                                        if (benifitsStr != null && !benifitsStr.equals("null")) {
//                                                            JSONArray benifits = new JSONArray(benifitsStr);
//                                                            for (int i = 0; i < benifits.length(); i++) {
//                                                                String str = benifits.get(i).toString();
//
//                                                                if (str != null && str.equals(Benifits.Key.CANTEEN.toString())) {
//                                                                    haveCanteen = true;
//                                                                } else if (str != null && str.equals(Benifits.Key.CLUB.toString())) {
//                                                                    haveClub = true;
//                                                                } else if (str != null && str.equals(Benifits.Key.SOCIAL_INSURANCE.toString())) {
//                                                                    haveSocialInsurance = true;
//                                                                } else if (str != null && str.equals(Benifits.Key.TRAFFIC.toString())) {
//                                                                    haveTraffic = true;
//                                                                }
//                                                            }
//                                                        }
//
//
//                                                        if (requestCompanyComplete && requestAddressComplete && requestUserComplete && requestUserPositionComplete) {
//                                                            //存在问题 ,暂时这样做
//                                                            if (isCollectionComplete[0]) {
//                                                                for (int i = 0; i < collectionList.size(); i++) {
//                                                                    if (collectionList.get(i) != null && collectionList.get(i).equals(recruitMessageId)) {
//                                                                        isCollection = true;
//                                                                        collectionId = collectionRecordIdList.get(i).toString();
//                                                                    }
//                                                                }
//                                                            }
//
//                                                            showJobInfoData(
//                                                                    messageId,
//                                                                    workingExperience,
//                                                                    currencyType,
//                                                                    salaryType,
//                                                                    showSalaryMinToMax,
//                                                                    educationalBackground,
//                                                                    address, content,
//                                                                    isNew,
//                                                                    positionName,
//                                                                    companyName,
//                                                                    haveCanteen,
//                                                                    haveClub,
//                                                                    haveSocialInsurance,
//                                                                    haveTraffic,
//                                                                    userPositionName,
//                                                                    avatarURL,
//                                                                    userId,
//                                                                    userName,
//                                                                    isCollection,
//                                                                    recruitMessageId,
//                                                                    skill,
//                                                                    organizationId,
//                                                                    collectionId
//                                                            );
//                                                        }
//
//
//                                                    }
//                                                }, new Consumer() {
//                                                    @Override
//                                                    public void accept(Object o) throws Exception {
//                                                        System.out.println("单个公司信息请求失败");
//                                                        System.out.println(o.toString());
//                                                        requestCompanyComplete = true;
//                                                        if (requestCompanyComplete && requestAddressComplete && requestUserComplete && requestUserPositionComplete) {
//                                                            //存在问题 ,暂时这样做
//                                                            if (isCollectionComplete[0]) {
//                                                                for (int i = 0; i < collectionList.size(); i++) {
//                                                                    if (collectionList.get(i) != null && collectionList.get(i).equals(recruitMessageId)) {
//                                                                        isCollection = true;
//                                                                        collectionId = collectionRecordIdList.get(i).toString();
//                                                                    }
//                                                                }
//                                                            }
//
//                                                            showJobInfoData(
//                                                                    messageId,
//                                                                    workingExperience,
//                                                                    currencyType,
//                                                                    salaryType,
//                                                                    showSalaryMinToMax,
//                                                                    educationalBackground,
//                                                                    address, content,
//                                                                    isNew,
//                                                                    positionName,
//                                                                    companyName,
//                                                                    haveCanteen,
//                                                                    haveClub,
//                                                                    haveSocialInsurance,
//                                                                    haveTraffic,
//                                                                    userPositionName,
//                                                                    avatarURL,
//                                                                    userId,
//                                                                    userName,
//                                                                    isCollection,
//                                                                    recruitMessageId,
//                                                                    skill,
//                                                                    organizationId,
//                                                                    collectionId
//                                                            );
//                                                        }
//
//                                                    }
//                                                });
//
//
//                                        //请求地址
//                                        //测试地址 https://basic-info.sk.cgland.top/
//                                        //正式地址 https://basic-info.sk.skjob.jp/
//                                        RetrofitUtils requestAddress = new RetrofitUtils(thisContext, thisContext.getString(R.string.baseUrl));
//                                        requestAddress.create(CityInfoApi.class)
//                                                .getAreaInfo(
//                                                        areaId
//                                                )
//                                                .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
//                                                .observeOn(AndroidSchedulers.mainThread()) //观察者 切换到主线程
//
//
//                                                .subscribe(new Consumer() {
//                                                    @Override
//                                                    public void accept(Object o) throws Exception {
//                                                        System.out.println("单个地址信息请求成功");
//                                                        System.out.println(o.toString());
//                                                        requestAddressComplete = true;
//
//                                                        JSONObject json = new JSONObject(o.toString());
//                                                        address = json.getString("name");
//
//
//                                                        if (requestCompanyComplete && requestAddressComplete && requestUserComplete && requestUserPositionComplete) {
//                                                            //存在问题 ,暂时这样做
//                                                            if (isCollectionComplete[0]) {
//                                                                for (int i = 0; i < collectionList.size(); i++) {
//                                                                    if (collectionList.get(i) != null && collectionList.get(i).equals(recruitMessageId)) {
//                                                                        isCollection = true;
//                                                                        collectionId = collectionRecordIdList.get(i).toString();
//                                                                    }
//                                                                }
//                                                            }
//
//                                                            showJobInfoData(
//                                                                    messageId,
//                                                                    workingExperience,
//                                                                    currencyType,
//                                                                    salaryType,
//                                                                    showSalaryMinToMax,
//                                                                    educationalBackground,
//                                                                    address, content,
//                                                                    isNew,
//                                                                    positionName,
//                                                                    companyName,
//                                                                    haveCanteen,
//                                                                    haveClub,
//                                                                    haveSocialInsurance,
//                                                                    haveTraffic,
//                                                                    userPositionName,
//                                                                    avatarURL,
//                                                                    userId,
//                                                                    userName,
//                                                                    isCollection,
//                                                                    recruitMessageId,
//                                                                    skill,
//                                                                    organizationId,
//                                                                    collectionId
//                                                            );
//                                                        }
//
//
//                                                    }
//                                                }, new Consumer() {
//                                                    @Override
//                                                    public void accept(Object o) throws Exception {
//                                                        System.out.println("单个地址信息请求失败");
//                                                        System.out.println(o.toString());
//                                                        requestAddressComplete = true;
//                                                        if (requestCompanyComplete && requestAddressComplete && requestUserComplete && requestUserPositionComplete) {
//                                                            //存在问题 ,暂时这样做
//                                                            if (isCollectionComplete[0]) {
//                                                                for (int i = 0; i < collectionList.size(); i++) {
//                                                                    if (collectionList.get(i) != null && collectionList.get(i).equals(recruitMessageId)) {
//                                                                        isCollection = true;
//                                                                        collectionId = collectionRecordIdList.get(i).toString();
//                                                                    }
//                                                                }
//                                                            }
//
//                                                            showJobInfoData(
//                                                                    messageId,
//                                                                    workingExperience,
//                                                                    currencyType,
//                                                                    salaryType,
//                                                                    showSalaryMinToMax,
//                                                                    educationalBackground,
//                                                                    address, content,
//                                                                    isNew,
//                                                                    positionName,
//                                                                    companyName,
//                                                                    haveCanteen,
//                                                                    haveClub,
//                                                                    haveSocialInsurance,
//                                                                    haveTraffic,
//                                                                    userPositionName,
//                                                                    avatarURL,
//                                                                    userId,
//                                                                    userName,
//                                                                    isCollection,
//                                                                    recruitMessageId,
//                                                                    skill,
//                                                                    organizationId,
//                                                                    collectionId
//                                                            );
//                                                        }
//
//                                                    }
//                                                });
//
//
//                                        //用户信息请求
//                                        //测试地址 https://user.sk.cgland.top/
//                                        //正式地址 https://user.sk.skjob.jp/
//                                        RetrofitUtils requestUser = new RetrofitUtils(thisContext, thisContext.getString(R.string.userUrl));
//                                        requestUser.create(UserApi.class)
//                                                .getUserInfo(
//                                                        userId
//                                                )
//                                                .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
//                                                .observeOn(AndroidSchedulers.mainThread()) //观察者 切换到主线程
//
//
//                                                .subscribe(new Consumer() {
//                                                    @Override
//                                                    public void accept(Object o) throws Exception {
//                                                        System.out.println("单个用户信息请求成功");
//                                                        System.out.println(o.toString());
//                                                        requestUserComplete = true;
//
//                                                        JSONObject json = new JSONObject(o.toString());
//
//                                                        avatarURL = json.getString("avatarURL");
//                                                        userName = json.getString("displayName");
//
//
//                                                        if (requestCompanyComplete && requestAddressComplete && requestUserComplete && requestUserPositionComplete) {
//                                                            //存在问题 ,暂时这样做
//                                                            if (isCollectionComplete[0]) {
//                                                                for (int i = 0; i < collectionList.size(); i++) {
//                                                                    if (collectionList.get(i) != null && collectionList.get(i).equals(recruitMessageId)) {
//                                                                        isCollection = true;
//                                                                        collectionId = collectionRecordIdList.get(i).toString();
//                                                                    }
//                                                                }
//                                                            }
//
//                                                            showJobInfoData(
//                                                                    messageId,
//                                                                    workingExperience,
//                                                                    currencyType,
//                                                                    salaryType,
//                                                                    showSalaryMinToMax,
//                                                                    educationalBackground,
//                                                                    address, content,
//                                                                    isNew,
//                                                                    positionName,
//                                                                    companyName,
//                                                                    haveCanteen,
//                                                                    haveClub,
//                                                                    haveSocialInsurance,
//                                                                    haveTraffic,
//                                                                    userPositionName,
//                                                                    avatarURL,
//                                                                    userId,
//                                                                    userName,
//                                                                    isCollection,
//                                                                    recruitMessageId,
//                                                                    skill,
//                                                                    organizationId,
//                                                                    collectionId
//                                                            );
//                                                        }
//
//
//                                                    }
//                                                }, new Consumer() {
//                                                    @Override
//                                                    public void accept(Object o) throws Exception {
//                                                        System.out.println("单个用户信息请求失败");
//                                                        System.out.println(o.toString());
//                                                        requestUserComplete = true;
//                                                        if (requestCompanyComplete && requestAddressComplete && requestUserComplete && requestUserPositionComplete) {
//                                                            if (isCollectionComplete[0]) {
//                                                                for (int i = 0; i < collectionList.size(); i++) {
//                                                                    if (collectionList.get(i) != null && collectionList.get(i).equals(recruitMessageId)) {
//                                                                        isCollection = true;
//                                                                        collectionId = collectionRecordIdList.get(i).toString();
//                                                                    }
//                                                                }
//                                                            }
//
//                                                            showJobInfoData(
//                                                                    messageId,
//                                                                    workingExperience,
//                                                                    currencyType,
//                                                                    salaryType,
//                                                                    showSalaryMinToMax,
//                                                                    educationalBackground,
//                                                                    address, content,
//                                                                    isNew,
//                                                                    positionName,
//                                                                    companyName,
//                                                                    haveCanteen,
//                                                                    haveClub,
//                                                                    haveSocialInsurance,
//                                                                    haveTraffic,
//                                                                    userPositionName,
//                                                                    avatarURL,
//                                                                    userId,
//                                                                    userName,
//                                                                    isCollection,
//                                                                    recruitMessageId,
//                                                                    skill,
//                                                                    organizationId,
//                                                                    collectionId
//                                                            );
//                                                        }
//
//                                                    }
//                                                });
//
//
//                                        //用户角色信息
//                                        //测试地址 https://org.sk.cgland.top/
//                                        //正式地址 https://org.sk.skjob.jp/
//                                        RetrofitUtils requestUserPosition = new RetrofitUtils(thisContext, thisContext.getString(R.string.orgUrl));
//                                        requestUserPosition.create(UserApi.class)
//                                                .getUserPosition(
//                                                        organizationId, userId
//                                                )
//                                                .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
//                                                .observeOn(AndroidSchedulers.mainThread()) //观察者 切换到主线程
//
//
//                                                .subscribe(new Consumer() {
//                                                    @Override
//                                                    public void accept(Object o) throws Exception {
//                                                        System.out.println("单个用户角色信息请求成功");
//                                                        System.out.println(o.toString());
//                                                        requestUserPositionComplete = true;
//
//                                                        JSONObject json = new JSONObject(o.toString());
//
//                                                        userPositionName = json.getString("name");
//
//
//                                                        if (requestCompanyComplete && requestAddressComplete && requestUserComplete && requestUserPositionComplete) {
//                                                            if (isCollectionComplete[0]) {
//                                                                for (int i = 0; i < collectionList.size(); i++) {
//                                                                    if (collectionList.get(i) != null && collectionList.get(i).equals(recruitMessageId)) {
//                                                                        isCollection = true;
//                                                                        collectionId = collectionRecordIdList.get(i).toString();
//                                                                    }
//                                                                }
//                                                            }
//
//
//                                                            showJobInfoData(
//                                                                    messageId,
//                                                                    workingExperience,
//                                                                    currencyType,
//                                                                    salaryType,
//                                                                    showSalaryMinToMax,
//                                                                    educationalBackground,
//                                                                    address, content,
//                                                                    isNew,
//                                                                    positionName,
//                                                                    companyName,
//                                                                    haveCanteen,
//                                                                    haveClub,
//                                                                    haveSocialInsurance,
//                                                                    haveTraffic,
//                                                                    userPositionName,
//                                                                    avatarURL,
//                                                                    userId,
//                                                                    userName,
//                                                                    isCollection,
//                                                                    recruitMessageId,
//                                                                    skill,
//                                                                    organizationId,
//                                                                    collectionId
//                                                            );
//
//                                                        }
//
//
//                                                    }
//                                                }, new Consumer() {
//                                                    @Override
//                                                    public void accept(Object o) throws Exception {
//                                                        System.out.println("单个用户角色信息请求失败");
//                                                        System.out.println(o.toString());
//                                                        requestUserComplete = true;
//                                                        if (requestCompanyComplete && requestAddressComplete && requestUserComplete && requestUserPositionComplete) {
//                                                            //存在问题 ,暂时这样做
//                                                            if (isCollectionComplete[0]) {
//                                                                for (int i = 0; i < collectionList.size(); i++) {
//                                                                    if (collectionList.get(i) != null && collectionList.get(i).equals(recruitMessageId)) {
//                                                                        isCollection = true;
//                                                                        collectionId = collectionRecordIdList.get(i).toString();
//                                                                    }
//                                                                }
//                                                            }
//
//                                                            showJobInfoData(
//                                                                    messageId,
//                                                                    workingExperience,
//                                                                    currencyType,
//                                                                    salaryType,
//                                                                    showSalaryMinToMax,
//                                                                    educationalBackground,
//                                                                    address, content,
//                                                                    isNew,
//                                                                    positionName,
//                                                                    companyName,
//                                                                    haveCanteen,
//                                                                    haveClub,
//                                                                    haveSocialInsurance,
//                                                                    haveTraffic,
//                                                                    userPositionName,
//                                                                    avatarURL,
//                                                                    userId,
//                                                                    userName,
//                                                                    isCollection,
//                                                                    recruitMessageId,
//                                                                    skill,
//                                                                    organizationId,
//                                                                    collectionId
//                                                            );
//
//
//                                                        }
//
//                                                    }
//                                                });
//
//
//                                    }
//                                }, new Consumer() {
//                                    @Override
//                                    public void accept(Object o) throws Exception {
//                                        System.out.println("搜藏请求失败(消息模块)");
//                                        System.out.println(o.toString());
//                                        requestCompanyComplete = true;
//
//                                    }
//                                });
//
//                    }
//                }, new Consumer() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        System.out.println("请求职位单个信息失败");
//                        System.out.println(o.toString());
//                    }
//                });
//    }

//    private void showJobInfoData(
//            String thisMessageId,
//            String workingExperience,
//            String currencyType,
//            String salaryType,
//            String showSalaryMinToMax,
//            String educationalBackground,
//            String address,
//            String content,
//            Boolean isNew,
//            String positionName,
//            String companyName,
//            Boolean haveCanteen,
//            Boolean haveClub,
//            Boolean haveSocialInsurance,
//            Boolean haveTraffic,
//            String userPositionName,
//            String avatarURL,
//            String userId,
//            String userName,
//            Boolean isCollection,
//            String recruitMessageId,
//            String skill,
//            String organizationId,
//            String collectionId
//
//    ) {
//        JobInfoModel model = new JobInfoModel(
//                workingExperience,
//                currencyType,
//                salaryType,
//                showSalaryMinToMax,
//                educationalBackground,
//                address,
//                content,
//                isNew,
//                positionName,
//                companyName,
//                haveCanteen,
//                haveClub,
//                haveSocialInsurance,
//                haveTraffic,
//                userPositionName,
//                avatarURL,
//                userId,
//                userName,
//                isCollection,
//                recruitMessageId,
//                skill,
//                organizationId,
//                collectionId
//        );
//
//
//        JSONObject json = new JSONObject();
//        try {
//            reconnectSocket();
//            json.put("position_id", thisCommunicationPositionId);
//            json.put("contact_id", HIS_ID);
//            socket.emit("firstChatTimeByPosition", json, new Ack() {
//                public void call(String eventName, Object error, Object data) {
//                    System.out.println("firstChatTimeByPosition Got message for :" + eventName + " error is :" + error + " data is :" + data);
//                    if (error == null) {
//                        try {
//                            String timeStr = new JSONObject(data.toString()).getString("data");
//                            if (timeStr != null && !timeStr.equals("null") && !timeStr.equals("")) {
//                                Date date = new Date(Long.parseLong(timeStr));
//                                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
//                                String timeData = sdf.format(date);
//                                System.out.println(timeData);
//                                model.setDateTimeStr(timeData);
//                            } else {
//                                model.setDateTimeStr(null);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        model.setDateTimeStr(null);
//
//                    }
//                    MyMessage jobInfo = new MyMessage("", IMessage.MessageType.JOB_INFO.ordinal());
//                    jobInfo.setJsobInfo(model);
//                    mAdapter.updateMessage(thisMessageId, jobInfo);
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }


//    private void showResumeList(int type, String interviewId) {
//        hideDropMenu();
//        if (resumeMenuFragment == null && fragmentShadow == null) {
//            FragmentTransaction mTransaction = getFragmentManager().beginTransaction();
//            fragmentShadow = new ShadowFragment();
//            mTransaction.add(R.id.mainBody, fragmentShadow);
//
//            resumeMenuFragment = new ResumeMenuFragment();
//            Bundle bundle = new Bundle();
//            bundle.putInt("type", type);
//            bundle.putString("InterviewId", interviewId);
//
//            resumeMenuFragment.setArguments(bundle);
//
//            mTransaction.setCustomAnimations(R.anim.bottom_in_a, R.anim.bottom_in_a);
//            mTransaction.add(R.id.mainBody, resumeMenuFragment);
//
//            mTransaction.commit();
//        } else {
//            hideResumeMenu();
//        }
//
//    }

//
//    //得到薪资范围
//    private String getSalaryMinToMaxString(
//            Integer salaryMin,
//            Integer salaryMax
//    ) {
//
//        String min = salaryMin.toString();
//        String max = salaryMax.toString();
//
//        String thousand = "";
//        String tenthousand = "";
//        String million = "";
//
//
//        thousand = "千";
//        tenthousand = "万";
//        million = "台";
//
//
//        if (salaryMin >= 1000000) {
//            min = (salaryMin / 1000000) + million;
//        } else if (salaryMin >= 10000) {
//            min = (salaryMin / 10000) + tenthousand;
//        } else if (salaryMin >= 1000) {
//            min = (salaryMin / 1000) + thousand;
//        }
//
//
//        if (salaryMax >= 1000000) {
//            max = (salaryMax / 1000000) + million;
//        } else if (salaryMax >= 10000) {
//            max = (salaryMax / 10000) + tenthousand;
//        } else if (salaryMax >= 1000) {
//            max = (salaryMax / 1000) + thousand;
//        }
//
//        String showSalaryMinToMax =
//                min + "~" + max;
//        return showSalaryMinToMax;
//    }


    //设置常用语
    private void setGreeting() {
        //用户信息请求
        //测试地址 https://user.sk.cgland.top/
        //正式地址 https://user.sk.skjob.jp/
//        RetrofitUtils requestUser = new RetrofitUtils(thisContext, this.getString(R.string.userUrl));
//        requestUser.create(SystemSetupApi.class)
//                .getGreetings()
//                .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
//                .observeOn(AndroidSchedulers.mainThread()) //观察者 切换到主线程
//                .subscribe(new Consumer() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        System.out.println("获取招呼语成功");
//                        System.out.println((((retrofit2.Response) o).body()));
//                        JSONArray array = new JSONArray(((retrofit2.Response) o).body().toString());
//
//
//                        mChatView.getChatInputView().chagnyongyuAdapter(array);
//
//
//                    }
//                }, new Consumer() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        System.out.println("获取招呼语请求失败");
//                        System.out.println(o.toString());
//
//                    }
//                });

    }


    private void sendGreeting() {
        //用户信息请求
        //测试地址 https://user.sk.cgland.top/
        //正式地址 https://user.sk.skjob.jp/

//        System.out.println("开始!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//        RetrofitUtils requestUser = new RetrofitUtils(thisContext, this.getString(R.string.userUrl));
//        requestUser.create(SystemSetupApi.class)
//                .getGreetings()
//                .subscribeOn(Schedulers.io()) //被观察者 开子线程请求网络
//                .observeOn(AndroidSchedulers.mainThread()) //观察者 切换到主线程
//                .subscribe(new Consumer() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        System.out.println("获取招呼语成功");
//                        System.out.println((((retrofit2.Response) o).body()));
//                        JSONArray array = new JSONArray(((retrofit2.Response) o).body().toString());
//                        //测试地址 https://user.sk.cgland.top/
//                        //正式地址 https://user.sk.skjob.jp/
//                        RetrofitUtils retrofitUils = new RetrofitUtils(thisContext, thisContext.getString(R.string.userUrl));
//                        retrofitUils.create(SystemSetupApi.class)
//                                .getUserInformation()
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread()) //观察者 切换到主线程
//                                .subscribe(new Consumer() {
//                                    @Override
//                                    public void accept(Object o) throws Exception {
//                                        System.out.println("获取用户设置成功");
//                                        System.out.println((((retrofit2.Response) o).body()));
//
//
//
//                                        UserSystemSetup item = ((UserSystemSetup)((retrofit2.Response) o).body());
//
//
//                                        Boolean greeting = item.getGreeting();
//                                        if (greeting != null && greeting) {
//                                            //需要打招呼
//                                            String greetingId = item.getGreetingId().toString();
//                                            if (array != null) {
//                                                for (int i = 0; i < array.length(); i++) {
//                                                    if (greetingId != null && greetingId.equals(array.getJSONObject(i).getString("id"))) {
//                                                        String content = array.getJSONObject(i).getString("content");
//
//                                                        sendTextMessage(content, null);
//
//                                                    }
//                                                }
//                                            }
//
//
//                                        }
//                                    }
//                                }, new Consumer() {
//                                    @Override
//                                    public void accept(Object o) throws Exception {
//                                        System.out.println("获取用户设置失败");
//                                        System.out.println(o.toString());
//
//                                    }
//                                });
//                    }
//                }, new Consumer() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        System.out.println("获取招呼语请求失败");
//                        System.out.println(o.toString());
//
//                    }
//                });

    }

    //把视频邀请消息设为已读不可再点击
    public void setVideoInviteHandled() {

        for (int i = 0; i < latestVideoMessage.size(); i++) {
            for (int j = 0; j < latestVideoMessageInterviewId.size(); j++) {
                if (latestVideoMessage.get(i).getInterviewId().equals(latestVideoMessageInterviewId.get(j))) {
                    latestVideoMessage.get(i).setType(IMessage.MessageType.RECEIVE_INVITE_VIDEO_HANDLED.ordinal());
                    //更改界面
                    final MyMessage message_callBack = latestVideoMessage.get(i);
                    final String thisMessageId = latestVideoMessage.get(i).getMsgId();
                    MessageListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.updateMessage(thisMessageId, message_callBack);
                            mAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        }
    }


    public void setVideoChatControllerListener(VideoChatControllerListener videoChatControllerListener) {
        this.videoChatControllerListener = videoChatControllerListener;
    }

    public void reconnectSocket() {

        if( socket!=null && socket.isconnected()   &&(WebSocketState.OPEN == socket.getCurrentState() || WebSocketState.CREATED == socket.getCurrentState())) {

        } else {
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }

    }


//    public void setEnabledStyleOfExchangeIcon() {
//
//        message_middle_select_bar_image3.setImageResource(R.drawable.self_introduce_icon);
//
//
//    }


    private void sendTimeBar(){
        Date now = new Date();
        //大于5分钟,显示时间定位消息




        if (latestMessageTime == null || now.getTime() - latestMessageTime > 1000 * 60 * 5) {
            sendSystemMessageToMyself("TIME" + now.getTime() + "");
        }
    }

    //销毁时
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        mSensorManager.unregisterListener(this);
        //销毁消息通道
        DestroyMessageChannel();
        HIS_ID = null;
    }

}
