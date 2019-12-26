package cn.jiguang.imui.chatinput;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.jiguang.imui.chatinput.camera.CameraNew;
import cn.jiguang.imui.chatinput.camera.CameraOld;
import cn.jiguang.imui.chatinput.camera.CameraSupport;
import cn.jiguang.imui.chatinput.emoji.Constants;
import cn.jiguang.imui.chatinput.emoji.DefEmoticons;
import cn.jiguang.imui.chatinput.emoji.EmojiBean;
import cn.jiguang.imui.chatinput.emoji.EmojiView;
import cn.jiguang.imui.chatinput.emoji.data.EmoticonPageEntity;
import cn.jiguang.imui.chatinput.emoji.data.EmoticonPageSetEntity;
import cn.jiguang.imui.chatinput.emoji.data.PageSetEntity;
import cn.jiguang.imui.chatinput.emoji.listener.EmoticonClickListener;
import cn.jiguang.imui.chatinput.emoji.data.EmoticonEntity;
import cn.jiguang.imui.chatinput.emoji.widget.EmoticonsEditText;
import cn.jiguang.imui.chatinput.emoji.EmoticonsKeyboardUtils;
import cn.jiguang.imui.chatinput.emoji.widget.EmoticonsToolBarView;
import cn.jiguang.imui.chatinput.listener.CameraControllerListener;
import cn.jiguang.imui.chatinput.listener.CameraEventListener;
import cn.jiguang.imui.chatinput.listener.ItemClickListener;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnClickEditTextListener;
import cn.jiguang.imui.chatinput.listener.CustomMenuEventListener;
import cn.jiguang.imui.chatinput.listener.OnFileSelectedListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.OnPhotoSendListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.menu.MenuManager;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.chatinput.photo.SelectPhotoView;
import cn.jiguang.imui.chatinput.record.ProgressButton;
import cn.jiguang.imui.chatinput.record.RecordControllerView;
import cn.jiguang.imui.chatinput.record.RecordVoiceButton;
import cn.jiguang.imui.chatinput.utils.SimpleCommonUtils;
import org.json.JSONArray;
import org.json.JSONException;

public class ChatInputView extends LinearLayout
        implements View.OnClickListener, TextWatcher, RecordControllerView.OnRecordActionListener,
        OnFileSelectedListener, CameraEventListener, ViewTreeObserver.OnPreDrawListener, OnPhotoSendListener {

    int SHOW_BUTTON=1;

    private static final String TAG = "ChatInputView";
    private EmoticonsEditText mChatInput;
    private TextView mSendCountTv;
    private CharSequence mInput;


    private ImageButton mVoiceBtn;
    private ImageButton mPhotoBtn;
    private ImageButton mCameraBtn;
    private ImageButton mEmojiBtn;
    private ImageButton mSendBtn;

//    private TextView changyongyu;
    private TextView sendEmojiMessage;
//    private ImageView itemMenuShowOrHide;
    private ImageView voiceSelect;


    private ImageView smileSelect;
    private ImageView picSelect;
    private ImageView videoSelect;



//    private TextView messageSend;

    private LinearLayout myMenuitemContainer;
    private LinearLayout  aurora_ll_input_container;

    private LinearLayout mChatInputContainer;
    private LinearLayout mMenuItemContainer;
    private FrameLayout menuContainer;
    private FrameLayout  my_menu_area_container;
    private RelativeLayout mRecordVoiceRl;
    private LinearLayout mPreviewPlayLl;
    private ProgressButton mPreviewPlayBtn;
    private Button mSendAudioBtn;
    private Button mCancelSendAudioBtn;
    private LinearLayout mRecordContentLl;
    private RecordControllerView mRecordControllerView;
    private Chronometer mChronometer;
    private TextView mRecordHintTv;
    private RecordVoiceButton mRecordVoiceBtn;

    SelectPhotoView mSelectPhotoView;
    private ImageButton mSelectAlbumIb;

    private FrameLayout mCameraFl;
    private TextureView mTextureView;
    private ImageButton mCaptureBtn;
    private ImageButton mCloseBtn;
    private ImageButton mSwitchCameraBtn;
    private ImageButton mFullScreenBtn;
    private ImageButton mRecordVideoBtn;
    private OnMenuClickListener mListener;
    private OnCameraCallbackListener mCameraListener;
    private OnClickEditTextListener mEditTextListener;
    private CameraControllerListener mCameraControllerListener;
    private RecordVoiceListener mRecordVoiceListener;

    private EmojiView mEmojiRl;
    private RecyclerView chagnyongyu_list;
    private EmoticonsToolBarView view_etv;

    private LinearLayout chagnyongyu_container;
    private ChatInputStyle mStyle;

    private InputMethodManager mImm;
    private Window mWindow;

    private int mWidth;
    private int mHeight;
    private int mSoftKeyboardHeight;
    private int mNowh;
    private int mOldh;
    public static int sMenuHeight = 831;

    private boolean mPendingShowMenu;

    private long mRecordTime;
    private boolean mPlaying = false;
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    // To judge if it is record video mode
    private boolean mIsRecordVideoMode = false;

    // To judge if it is recording video now
    private boolean mIsRecordingVideo = false;

    // To judge if is finish recording video
    private boolean mFinishRecordingVideo = false;

    // Video file to be saved at
    private String mVideoFilePath;
    private int mVideoDuration;

    // If audio file has been set
    private boolean mSetData;
    private FileInputStream mFIS;
    private FileDescriptor mFD;
    private boolean mIsEarPhoneOn;
    private File mPhoto;
    private CameraSupport mCameraSupport;
    private int mCameraId = -1;
    private boolean mIsBackCamera = true;
    private boolean mIsFullScreen = false;
    private Context mContext;
    private Rect mRect = new Rect();
    private View mCameraBtnContainer;
    private View mVoiceBtnContainer;
    private View mPhotoBtnContainer;
    private View mEmojiBtnContainer;

    private ImageView aurora_ib_send_pic;
    private ImageView aurora_ib_retake;


    private MenuManager mMenuManager;



    private Handler handler=new Handler(){
        public void handleMessage(Message msg){

            if(msg.what==SHOW_BUTTON){
                mCaptureBtn.setVisibility(GONE);
                if(aurora_ib_send_pic!=null)
                    aurora_ib_send_pic.setVisibility(VISIBLE);
                if(aurora_ib_retake!=null)
                    aurora_ib_retake.setVisibility(VISIBLE);
            }

        }
    };


    public ChatInputView(Context context) {
        super(context);
        init(context);
    }

    public ChatInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChatInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context) {
        mContext = context;
        inflate(context, R.layout.view_chatinput, this);

        mChatInputContainer = (LinearLayout) findViewById(R.id.aurora_ll_input_container1);
        mMenuItemContainer = (LinearLayout) findViewById(R.id.aurora_ll_menuitem_container);


        menuContainer = (FrameLayout) findViewById(R.id.my_menu_container_fake);

        my_menu_area_container = (FrameLayout) findViewById(R.id.my_menu_area_container);


        mMenuManager = new MenuManager(this);


        aurora_ib_send_pic = findViewById(R.id.aurora_ib_send_pic);
        aurora_ib_retake= findViewById(R.id.aurora_ib_retake);


        //隐藏显示menu项
//        itemMenuShowOrHide =  findViewById(R.id.menuItemHideOrShow);
//        itemMenuShowOrHide.setOnClickListener(onMenuItemHideShowListener);

        //voiceSelect =  findViewById(R.id.menuItemVoice);
        smileSelect =  findViewById(R.id.menuItemSmile);
        picSelect =  findViewById(R.id.menuItemPic);
        videoSelect =  findViewById(R.id.menuItemVideo);

//        messageSend=  findViewById(R.id.messageSend);


        myMenuitemContainer =  findViewById(R.id.my_menuitem_container);



        view_etv=findViewById(R.id.view_etv);
        sendEmojiMessage=findViewById(R.id.sendEmojiMessage);

        sendEmojiMessage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


        // menu buttons
        mChatInput = findViewById(R.id.aurora_et_chat_input1);


        //mChatInput.setImeOptions(EditorInfo.IME_ACTION_SEND);

        mVoiceBtn = (ImageButton) findViewById(R.id.aurora_menuitem_ib_voice);
        mPhotoBtn = (ImageButton) findViewById(R.id.aurora_menuitem_ib_photo);
        mCameraBtn = (ImageButton) findViewById(R.id.aurora_menuitem_ib_camera);
        mEmojiBtn = (ImageButton) findViewById(R.id.aurora_menuitem_ib_emoji);
       // mSendBtn = (ImageButton) findViewById(R.id.aurora_menuitem_ib_emoji);
//        changyongyu=findViewById(R.id.changyongyu);

        mVoiceBtnContainer = findViewById(R.id.aurora_ll_menuitem_voice_container);
        mPhotoBtnContainer = findViewById(R.id.aurora_ll_menuitem_photo_container);
        mCameraBtnContainer = findViewById(R.id.aurora_ll_menuitem_camera_container);
      //  mEmojiBtnContainer = findViewById(R.id.aurora_ll_menuitem_emoji_container);
       mVoiceBtnContainer.setOnClickListener(onMenuItemClickListener);
        mPhotoBtnContainer.setOnClickListener(onMenuItemClickListener);
        mCameraBtnContainer.setOnClickListener(onMenuItemClickListener);
//        mEmojiBtnContainer.setOnClickListener(onMenuItemClickListener);
//        mSendBtn.setOnClickListener(onMenuItemClickListener);
//        changyongyu.setOnClickListener(onMenuItemClickListener);




//        messageSend.setOnClickListener(new OnClickListener(){
//
//            /**
//             * Called when a view has been clicked.
//             *
//             * @param v The view that was clicked.
//             */
//            @Override
//            public void onClick(View v) {
//                sendMessage();
//            }
//        });


        //voiceSelect.setOnClickListener(onMenuItemClickListener);
        smileSelect.setOnClickListener(onMenuItemClickListener);
        picSelect.setOnClickListener(onMenuItemClickListener);
        videoSelect.setOnClickListener(onMenuItemClickListener);



        mSendCountTv = (TextView) findViewById(R.id.aurora_menuitem_tv_send_count);



        mRecordVoiceRl = (RelativeLayout) findViewById(R.id.aurora_rl_recordvoice_container);
        mPreviewPlayLl = findViewById(R.id.aurora_ll_recordvoice_preview_container);
        mPreviewPlayBtn = (ProgressButton) findViewById(R.id.aurora_pb_recordvoice_play_audio);
        mRecordContentLl = (LinearLayout) findViewById(R.id.aurora_ll_recordvoice_content_container);

        mRecordControllerView = (RecordControllerView) findViewById(R.id.aurora_rcv_recordvoice_controller);
        mChronometer = (Chronometer) findViewById(R.id.aurora_chronometer_recordvoice);
        mRecordHintTv = (TextView) findViewById(R.id.aurora_tv_recordvoice_hint);
        mSendAudioBtn = (Button) findViewById(R.id.aurora_btn_recordvoice_send);
        mCancelSendAudioBtn = (Button) findViewById(R.id.aurora_btn_recordvoice_cancel);
        mRecordVoiceBtn = (RecordVoiceButton) findViewById(R.id.aurora_rvb_recordvoice_record);
        mCameraFl = (FrameLayout) findViewById(R.id.aurora_fl_camera_container);
        mTextureView = (TextureView) findViewById(R.id.aurora_txtv_camera_texture);
        mCloseBtn = (ImageButton) findViewById(R.id.aurora_ib_camera_close);
        mFullScreenBtn = (ImageButton) findViewById(R.id.aurora_ib_camera_full_screen);
        mRecordVideoBtn = (ImageButton) findViewById(R.id.aurora_ib_camera_record_video);
        mCaptureBtn = (ImageButton) findViewById(R.id.aurora_ib_camera_capture);
        mSwitchCameraBtn = (ImageButton) findViewById(R.id.aurora_ib_camera_switch);

        mSelectPhotoView = (SelectPhotoView) findViewById(R.id.aurora_view_selectphoto);
        mSelectAlbumIb = (ImageButton) findViewById(R.id.aurora_imagebtn_selectphoto_album);
        mSelectPhotoView.setOnFileSelectedListener(this);
        mSelectPhotoView.setOnPhotoSendListener(this);
        mSelectPhotoView.initData();
        mEmojiRl = (EmojiView) findViewById(R.id.aurora_rl_emoji_container);

        aurora_ll_input_container=findViewById(R.id.aurora_ll_input_container);


        chagnyongyu_container=findViewById(R.id.chagnyongyu_container);


        menuContainer.setVisibility(View.VISIBLE);

        mChatInput.addTextChangedListener(this);
        mChatInput.setOnBackKeyClickListener(new EmoticonsEditText.OnBackKeyClickListener() {
            @Override
            public void onBackKeyClick() {

                if (menuContainer.getVisibility() == VISIBLE) {
                    dismissMenuLayout();
                } else if (isKeyboardVisible()) {
                    EmoticonsKeyboardUtils.closeSoftKeyboard(mChatInput);
                }
            }
        });



        mRecordVoiceBtn.setRecordController(mRecordControllerView);
        mPreviewPlayBtn.setOnClickListener(this);
        mCancelSendAudioBtn.setOnClickListener(this);
        mSendAudioBtn.setOnClickListener(this);
        mCloseBtn.setOnClickListener(this);
        mFullScreenBtn.setOnClickListener(this);
        mRecordVideoBtn.setOnClickListener(this);
        mCaptureBtn.setOnClickListener(this);
        mSwitchCameraBtn.setOnClickListener(this);
        aurora_ib_send_pic.setOnClickListener(this);
        aurora_ib_retake.setOnClickListener(this);


        mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = ((Activity) context).getWindow();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;

        mRecordControllerView.setWidth(mWidth,dip2px(getContext(),90));
        mRecordControllerView.setOnControllerListener(this);
        getViewTreeObserver().addOnPreDrawListener(this);

        // mChatInput.setOnTouchListener(new OnTouchListener() {
        // @Override
        // public boolean onTouch(View view, MotionEvent motionEvent) {
        // if (mEditTextListener != null) {
        // mEditTextListener.onTouchEditText();
        // }
        //// if (!mChatInput.isFocused()) {
        //// mChatInput.setFocusable(true);
        //// mChatInput.setFocusableInTouchMode(true);
        //// }
        // return false;
        // }
        // });
        initEvents();

    }





    EmoticonClickListener emoticonClickListener = new EmoticonClickListener() {
        @Override
        public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {

            if (isDelBtn) {
                SimpleCommonUtils.delClick(mChatInput);
            } else {
                if (o == null) {
                    return;
                }
                if (actionType == Constants.EMOTICON_CLICK_BIGIMAGE) {
                    String emoji = ((EmojiBean) o).emoji;
                    Integer ico= DefEmoticons.textToPic.get(emoji);


//                    String path= ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
//                            getResources().getResourcePackageName(ico) +
//                            "/" + getResources().getResourceTypeName(ico) +
//                            "/" + getResources().getResourceEntryName(ico);
//                    if( mListener != null){
//                        mListener.onSendImageMessage(emoji,path);
//                    }

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ico);
                    ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
                    SpannableString spannableString = new SpannableString(emoji);
                    spannableString.setSpan(imageSpan, 0, emoji.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int index = mChatInput.getSelectionStart();
                    Editable editable = mChatInput.getText();
                    editable.insert(index, spannableString);

                } else {
                    String content = null;
                    if (o instanceof EmojiBean) {
                        content = ((EmojiBean) o).emoji;
                    } else if (o instanceof EmoticonEntity) {
                        content = ((EmoticonEntity) o).getContent();
                    }

                    if (TextUtils.isEmpty(content)) {
                        return;
                    }
                    int index = mChatInput.getSelectionStart();
                    Editable editable = mChatInput.getText();
                    editable.insert(index, content);
                }
            }
        }
    };


    //常用语
//    public  void chagnyongyuAdapter(JSONArray list ){
//
//        for(int i=0;i<list.length();i++){
//            View v=  LayoutInflater.from(chagnyongyu_container.getContext()).inflate(R.layout.changyongyu, null);
//            TextView textView = v.findViewById(R.id.changyongyuItem);
//            try {
//                textView.setText(list.getJSONObject(i).getString("content"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            textView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TextView t= (TextView) v;
//                    mChatInput.setText(t.getText());
//                    sendMessage();
//                }
//            });
//
//            chagnyongyu_container.addView(v);
//        }
//
//
//
//
//    }





    private void init(Context context, AttributeSet attrs) {
        init(context);
        mStyle = ChatInputStyle.parse(context, attrs);
//        mChatInput.setMaxLines(mStyle.getInputMaxLines());
//       // mChatInput.setHint(mStyle.getInputHint());
//        mChatInput.setText(mStyle.getInputText());
//        mChatInput.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStyle.getInputTextSize());
//        mChatInput.setTextColor(mStyle.getInputTextColor());
//        mChatInput.setHintTextColor(mStyle.getInputHintColor());
//       // mChatInput.setBackgroundResource(mStyle.getInputEditTextBg());
//        mChatInput.setPadding(mStyle.getInputPaddingLeft(), mStyle.getInputPaddingTop(), mStyle.getInputPaddingRight(),
//                mStyle.getInputPaddingBottom());
//
//        mInputMarginLeft.getLayoutParams().width = mStyle.getInputMarginLeft();
//        mInputMarginRight.getLayoutParams().width = mStyle.getInputMarginRight();
//        mVoiceBtn.setImageResource(mStyle.getVoiceBtnIcon());
//        mVoiceBtn.setBackground(mStyle.getVoiceBtnBg());
//        mPhotoBtn.setBackground(mStyle.getPhotoBtnBg());
//        mPhotoBtn.setImageResource(mStyle.getPhotoBtnIcon());
//        mCameraBtn.setBackground(mStyle.getCameraBtnBg());
//        mCameraBtn.setImageResource(mStyle.getCameraBtnIcon());
//        mSendBtn.setBackground(mStyle.getSendBtnBg());
//        mSendBtn.setImageResource(mStyle.getSendBtnIcon());
//        mSendCountTv.setBackground(mStyle.getSendCountBg());
       // mSelectAlbumIb.setVisibility(mStyle.getShowSelectAlbum() ? VISIBLE : INVISIBLE);

//        LinearLayout.LayoutParams lp =(LayoutParams) mChatInputContainer.getLayoutParams();
//        lp.setMargins(0,mStyle.getInputMarginTop(),0,mStyle.getInputMarginBottom());
//        mChatInputContainer.setLayoutParams(lp);

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        //SimpleCommonUtils.initEmoticonsEditText(mChatInput);
        mEmojiRl.setAdapter(SimpleCommonUtils.getCommonAdapter(mContext, emoticonClickListener));

    }

    private void setCursor(Drawable drawable) {
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(mChatInput, drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMenuClickListener(OnMenuClickListener listener) {
        mListener = listener;
    }

    public void setCustomMenuClickListener(CustomMenuEventListener listener){
        mMenuManager.setCustomMenuClickListener(listener);
    }


    public void setRecordVoiceListener(RecordVoiceListener listener) {
        this.mRecordVoiceBtn.setRecordVoiceListener(listener);
        this.mRecordVoiceListener = listener;
    }

    public void setOnCameraCallbackListener(OnCameraCallbackListener listener) {
        mCameraListener = listener;
    }

    public void setCameraControllerListener(CameraControllerListener listener) {
        mCameraControllerListener = listener;
    }

    public void setOnClickEditTextListener(OnClickEditTextListener listener) {
        mEditTextListener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mInput = s;

        if (mSelectPhotoView.getSelectFiles() == null || mSelectPhotoView.getSelectFiles().size() == 0) {
            if (s.length() >= 1 && start == 0 && before == 0) { // Starting input
               // triggerSendButtonAnimation(mSendBtn, true, false);
            } else if (s.length() == 0 && before >= 1) { // Clear content
               // triggerSendButtonAnimation(mSendBtn, false, false);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public EditText getInputView() {
        return mChatInput;
    }

    public RecordVoiceButton getRecordVoiceButton() {
        return mRecordVoiceBtn;
    }














    private void initEvents() {
        mChatInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //myMenuitemContainer.setVisibility(View.VISIBLE);

                switch (i) {
                    case EditorInfo.IME_NULL:
                        System.out.println("null for default_content: "  );
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        System.out.println("action send for email_content: "  );
                        sendMessage();



                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        System.out.println("action done for number_content: ");
                        break;

                    default:
                            break;
                }
                return true;
            }
        });
    }







public  void sendMessage(){

    if( mChatInput.getText().toString().trim().equals("")){
        return;
    }
    EmoticonsKeyboardUtils.closeSoftKeyboard(mChatInput);
    my_menu_area_container.setVisibility(GONE);
    myMenuitemContainer.setVisibility(View.GONE);

    // Allow send text and photos at the same time.
    if (onSubmit()) {
        mChatInput.setText("");
    }
    if (mSelectPhotoView.getSelectFiles() != null && mSelectPhotoView.getSelectFiles().size() > 0) {
        mListener.onSendFiles(mSelectPhotoView.getSelectFiles());

      //  mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.aurora_menuitem_send));
        mSendCountTv.setVisibility(View.INVISIBLE);
        mSelectPhotoView.resetCheckState();
        dismissMenuLayout();
        mImm.hideSoftInputFromWindow(getWindowToken(), 0);
       // mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}




    private OnClickListener onMenuItemHideShowListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            mListener.switchToMenuItemHideShowMode();
            my_menu_area_container.setVisibility(GONE);
            EmoticonsKeyboardUtils.closeSoftKeyboard(mChatInput);

            if(myMenuitemContainer.getVisibility()!=View.VISIBLE){
                myMenuitemContainer.setVisibility(View.VISIBLE);
                //写在这里 效果更顺滑
//                if(mCameraSupport==null){
//                    initCamera();
//                    return;
//                }
            }else {
                myMenuitemContainer.setVisibility(View.GONE);
                //dismissMenuLayout();
            }
        }
    };



    private OnClickListener onMenuItemClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (false && view.getId() == R.id.aurora_ll_menuitem_voice_container ) {

               if( mChatInput.getText().toString().trim().equals("")){
                   return;
               }
                // Allow send text and photos at the same time.
                if (onSubmit()) {
                    mChatInput.setText("");
                }
                if (mSelectPhotoView.getSelectFiles() != null && mSelectPhotoView.getSelectFiles().size() > 0) {
                    mListener.onSendFiles(mSelectPhotoView.getSelectFiles());

                //    mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.aurora_menuitem_send));
                    mSendCountTv.setVisibility(View.INVISIBLE);
                    mSelectPhotoView.resetCheckState();
                    dismissMenuLayout();
                    mImm.hideSoftInputFromWindow(getWindowToken(), 0);
                   // mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }

            } else {
                mMenuManager.hideCustomMenu();
                mChatInput.clearFocus();
                if (view.getId() == R.id.aurora_ll_menuitem_voice_container) {

                    if (mListener != null && mListener.switchToMicrophoneMode()) {
                        myMenuitemContainer.setVisibility(View.GONE);
                        if (mRecordVoiceRl.getVisibility() == VISIBLE && my_menu_area_container.getVisibility() == VISIBLE) {
                            my_menu_area_container.setVisibility(GONE);
                            dismissMenuLayout();
                        } else if (isKeyboardVisible()) {
                            my_menu_area_container.setVisibility(VISIBLE);
                           // mPendingShowMenu = true;
                            EmoticonsKeyboardUtils.closeSoftKeyboard(mChatInput);
                            showRecordVoiceLayout();
                        } else {
                            my_menu_area_container.setVisibility(VISIBLE);
                            showMenuLayout();
                            showRecordVoiceLayout();
                        }
                    }
                }
//                else if (view.getId() == R.id.changyongyu) {
//
//                    if (mListener != null && mListener.switchToEmojiMode()) {
//
//                        myMenuitemContainer.setVisibility(View.GONE);
//
//                        if (chagnyongyu_container.getVisibility() == VISIBLE && my_menu_area_container.getVisibility() == VISIBLE) {
//                            my_menu_area_container.setVisibility(GONE);
//                            dismissMenuLayout();
//                        } else if (isKeyboardVisible()) {
//                            my_menu_area_container.setVisibility(VISIBLE);
//                         //   mPendingShowMenu = true;
//                            EmoticonsKeyboardUtils.closeSoftKeyboard(mChatInput);
//                            changyongyuShow();
//
//                        } else {
//                            my_menu_area_container.setVisibility(VISIBLE);
//
//                            showMenuLayout();
//                            changyongyuShow();
//
//                        }
//
//                    }
//                }

                else if (view.getId() == R.id.menuItemSmile) {
                    if (mListener != null && mListener.switchToEmojiMode()) {
                        myMenuitemContainer.setVisibility(View.GONE);
                        if (mEmojiRl.getVisibility() == VISIBLE && my_menu_area_container.getVisibility() == VISIBLE) {
                            my_menu_area_container.setVisibility(GONE);
                            dismissMenuLayout();
                        } else if (isKeyboardVisible()) {
                           // mPendingShowMenu = true;
                            my_menu_area_container.setVisibility(VISIBLE);
                            showEmojiLayout();
                        } else {
                            my_menu_area_container.setVisibility(VISIBLE);
                            showMenuLayout();
                            showEmojiLayout();
                        }
                        EmoticonsKeyboardUtils.closeSoftKeyboard(mChatInput);
                    }
                } else if (view.getId() == R.id.menuItemPic) {
                    if (mListener != null && mListener.switchToGalleryMode()) {
                        myMenuitemContainer.setVisibility(View.GONE);
                        if (ContextCompat.checkSelfPermission(mContext,
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        if (mSelectPhotoView.getVisibility() == VISIBLE && my_menu_area_container.getVisibility() == VISIBLE) {
                            my_menu_area_container.setVisibility(GONE);
                            dismissMenuLayout();
                        } else if (isKeyboardVisible()) {
                            //mPendingShowMenu = true;
                            my_menu_area_container.setVisibility(VISIBLE);
                            EmoticonsKeyboardUtils.closeSoftKeyboard(mChatInput);
                            showSelectPhotoLayout();
                        } else {
                            my_menu_area_container.setVisibility(VISIBLE);
                            showMenuLayout();
                            showSelectPhotoLayout();
                        }
                    }
                } else if (view.getId() == R.id.aurora_ll_menuitem_camera_container) {


                    if (mListener != null && mListener.switchToCameraMode()) {

                        myMenuitemContainer.setVisibility(View.GONE);
                        my_menu_area_container.setVisibility(VISIBLE);
                        EmoticonsKeyboardUtils.closeSoftKeyboard(mChatInput);
                        showCameraLayout();
                        makeSpaceForCamera();


                        mCaptureBtn.setVisibility(VISIBLE);
                        if(aurora_ib_send_pic!=null)
                            aurora_ib_send_pic.setVisibility(GONE);
                        if(aurora_ib_retake!=null)
                            aurora_ib_retake.setVisibility(GONE);

                        //会卡一下 前面代码没注释的话这里不会执行
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            if (mCameraSupport == null && mCameraFl.getVisibility() == VISIBLE) {
                                initCamera();
                            }
                        } else {
                            Toast.makeText(getContext(), getContext().getString(R.string.sdcard_not_exist_toast),
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                }else if (view.getId() == R.id.menuItemVideo) {
                    mListener.videoChat();

                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.aurora_pb_recordvoice_play_audio) {
            // press preview play audio button
            if (!mPlaying) {
                if (mSetData) {
                    mPreviewPlayBtn.startPlay();
                    mMediaPlayer.start();
                    mPlaying = true;
                    mChronometer.setBase(convertStrTimeToLong(mChronometer.getText().toString()));
                    mChronometer.start();
                } else {
                    playVoice();
                }
            } else {
                mSetData = true;
                mMediaPlayer.pause();
                mChronometer.stop();
                mPlaying = false;
                mPreviewPlayBtn.stopPlay();
            }

        } else if (view.getId() == R.id.aurora_btn_recordvoice_cancel) {
            // preview play audio widget cancel sending audio
            mPreviewPlayLl.setVisibility(GONE);
            mRecordContentLl.setVisibility(VISIBLE);
            mRecordVoiceBtn.cancelRecord();
            mChronometer.setText("00:00");
            if (mRecordVoiceListener != null) {
                mRecordVoiceListener.onPreviewCancel();
            }

        } else if (view.getId() == R.id.aurora_btn_recordvoice_send) {
            // preview play audio widget send audio
            mPreviewPlayLl.setVisibility(GONE);
             mRecordContentLl.setVisibility(VISIBLE);
            mRecordVoiceBtn.finishRecord();
            mChronometer.setText("00:00");
            if (mRecordVoiceListener != null) {
                mRecordVoiceListener.onPreviewSend();
            }

        } else if (view.getId() == R.id.aurora_ib_camera_full_screen) {
            // full screen/recover screen button in texture view
            if (!mIsFullScreen) {
                if (mCameraControllerListener != null) {
                    mCameraControllerListener.onFullScreenClick();
                }
                fullScreen();
            } else {
                if (mCameraControllerListener != null) {
                    mCameraControllerListener.onRecoverScreenClick();
                }
                recoverScreen();
            }

        } else if (view.getId() == R.id.aurora_ib_camera_record_video) {
            // click record video button
            // if it is not record video mode
            if (mCameraControllerListener != null) {
                mCameraControllerListener.onSwitchCameraModeClick(!mIsRecordVideoMode);
            }
            if (!mIsRecordVideoMode) {
                mIsRecordVideoMode = true;
                mCaptureBtn.setBackgroundResource(R.drawable.aurora_preview_record_video_start);
                mRecordVideoBtn.setBackgroundResource(R.drawable.aurora_preview_camera);
                fullScreen();
                mCloseBtn.setVisibility(VISIBLE);
            } else {
                mIsRecordVideoMode = false;
                mRecordVideoBtn.setBackgroundResource(R.drawable.aurora_preview_record_video);
                mCaptureBtn.setBackgroundResource(R.drawable.aurora_menuitem_send_pres);
                mFullScreenBtn.setBackgroundResource(R.drawable.aurora_preview_recover_screen);
               // mFullScreenBtn.setVisibility(VISIBLE);
               // mCloseBtn.setVisibility(GONE);
                mCloseBtn.setVisibility(VISIBLE);
            }

        } else if (view.getId() == R.id.aurora_ib_camera_capture) {
            // click capture button in preview camera view
            // is record video mode
            if (mIsRecordVideoMode) {
                if (!mIsRecordingVideo) { // start recording
                    mCameraSupport.startRecordingVideo();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mCaptureBtn.setBackgroundResource(R.drawable.aurora_preview_record_video_stop);
                            mRecordVideoBtn.setVisibility(GONE);
                            mSwitchCameraBtn.setVisibility(GONE);
                            mCloseBtn.setVisibility(VISIBLE);
                        }
                    }, 200);
                    mIsRecordingVideo = true;

                } else { // finish recording
                    mVideoFilePath = mCameraSupport.finishRecordingVideo();
                    mIsRecordingVideo = false;
                    mIsRecordVideoMode = false;
                    mFinishRecordingVideo = true;
                    mCaptureBtn.setBackgroundResource(R.drawable.aurora_menuitem_send_pres);
                    mRecordVideoBtn.setVisibility(GONE);
                    mSwitchCameraBtn.setBackgroundResource(R.drawable.aurora_preview_delete_video);
                    mSwitchCameraBtn.setVisibility(VISIBLE);
                    if (mVideoFilePath != null) {
                        playVideo();
                    }
                }
                // if finished recording video, send it
            } else if (mFinishRecordingVideo) {
                if (mListener != null && mVideoFilePath != null) {
                    File file = new File(mVideoFilePath);
                    VideoItem video = new VideoItem(mVideoFilePath, file.getName(), file.length() + "",
                            System.currentTimeMillis() + "", mMediaPlayer.getDuration() / 1000);
                    List<FileItem> list = new ArrayList<>();
                    list.add(video);
                    mListener.onSendFiles(list);
                    mVideoFilePath = null;
                }
                mFinishRecordingVideo = false;
                mMediaPlayer.stop();
                mMediaPlayer.release();
                recoverScreen();
                dismissMenuLayout();
                // take picture and send it
            } else {
                //照照片
                mCameraSupport.takePicture();
            }
        } else if (view.getId() == R.id.aurora_ib_camera_close) {

            if(mCameraSupport!=null){
                mCameraSupport.cancelPic();
                mCameraSupport.release();
                mCameraSupport=null;
            }
            closeSpaceOfCamera();
            my_menu_area_container.setVisibility(GONE);
//            try {
//                if (mCameraControllerListener != null) {
//                    mCameraControllerListener.onCloseCameraClick();
//                }
//                mMediaPlayer.stop();
//                mMediaPlayer.release();
//                if (mCameraSupport != null) {
//                    mCameraSupport.sendPic();
//                    mCameraSupport.cancelRecordingVideo();
//                }
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            }
//            //recoverScreen();
//            dismissMenuLayout();
//            mIsRecordVideoMode = false;
//            mIsRecordingVideo = false;
//            if (mFinishRecordingVideo) {
//                mFinishRecordingVideo = false;
//            }
        } else if (view.getId() == R.id.aurora_ib_camera_switch) {

            mCaptureBtn.setVisibility(VISIBLE);
            if(aurora_ib_send_pic!=null)
                aurora_ib_send_pic.setVisibility(GONE);
            if(aurora_ib_retake!=null)
                aurora_ib_retake.setVisibility(GONE);


            if (mFinishRecordingVideo) {
                mCameraSupport.cancelRecordingVideo();
                mSwitchCameraBtn.setBackgroundResource(R.drawable.aurora_preview_switch_camera);
                mRecordVideoBtn.setBackgroundResource(R.drawable.aurora_preview_camera);
                showRecordVideoBtn();

                mVideoFilePath = null;
                mFinishRecordingVideo = false;
                mIsRecordVideoMode = true;
                mCaptureBtn.setBackgroundResource(R.drawable.aurora_preview_record_video_start);
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mCameraSupport.open(mCameraId, mWidth, mHeight, mIsBackCamera, mStyle.getCameraQuality());
            } else {
                for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                    Camera.CameraInfo info = new Camera.CameraInfo();
                    Camera.getCameraInfo(i, info);
                    if (mIsBackCamera) {
                        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                            mCameraId = i;
                            mIsBackCamera = false;
                            mCameraSupport.release();
                            mCameraSupport.open(mCameraId, mTextureView.getWidth(), mTextureView.getHeight(),
                                    mIsBackCamera, mStyle.getCameraQuality());
                            break;
                        }
                    } else {
                        if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                            mCameraId = i;
                            mIsBackCamera = true;
                            mCameraSupport.release();
                            mCameraSupport.open(mCameraId, mTextureView.getWidth(), mTextureView.getHeight(),
                                    mIsBackCamera, mStyle.getCameraQuality());
                            break;
                        }
                    }
                }
            }
        }
        //发送拍摄的照片
         else if (view.getId() == R.id.aurora_ib_send_pic) {
             if(mCameraSupport!=null){
                 mCameraSupport.sendPic();
                 mCameraSupport.release();
                 mCameraSupport=null;
             }
            closeSpaceOfCamera();
            my_menu_area_container.setVisibility(GONE);
         }
         //重拍
        else if (view.getId() == R.id.aurora_ib_retake) {
            if(mCameraSupport!=null){
                mCameraSupport.release();
                mCameraSupport.open(mCameraId, mWidth, sMenuHeight, mIsBackCamera, mStyle.getCameraQuality());
                aurora_ib_retake.setVisibility(GONE);
                aurora_ib_send_pic.setVisibility(GONE);
                mCaptureBtn.setVisibility(VISIBLE);

            }
        }


    }

    // play audio
    private void playVoice() {
        try {
            mMediaPlayer.reset();
            mFIS = new FileInputStream(mRecordVoiceBtn.getRecordFile());
            mFD = mFIS.getFD();
            mMediaPlayer.setDataSource(mFD);
            if (mIsEarPhoneOn) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
            } else {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mp) {
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mPreviewPlayBtn.startPlay();
                    mChronometer.start();
                    mp.start();
                    mPlaying = true;
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mSetData = false;
                    mChronometer.stop();
                    mPlaying = false;
                    mPreviewPlayBtn.finishPlay();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
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

    private void playVideo() {
        try {
            mCameraSupport.release();
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(mVideoFilePath);
            Surface surface = new Surface(mTextureView.getSurfaceTexture());
            mMediaPlayer.setSurface(surface);
            surface.release();
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseVoice() {
        try {
            mMediaPlayer.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAudioPlayByEarPhone(int state) {
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        if (state == 0) {
            mIsEarPhoneOn = false;
            audioManager.setSpeakerphoneOn(true);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.STREAM_VOICE_CALL);
        } else {
            mIsEarPhoneOn = true;
            audioManager.setSpeakerphoneOn(false);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, currVolume, AudioManager.STREAM_VOICE_CALL);
        }
    }


//    public static int dip2px(Context context, float dpValue) {
//        try {
//            final float scale = context.getResources().getDisplayMetrics().density;
//            return (int) (dpValue * scale + 0.5f);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return (int) dpValue;
//    }




    //隐藏其他项目  使摄像机全屏   （配合主页面进行）
    public void makeSpaceForCamera(){

        aurora_ll_input_container.setVisibility(GONE);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        my_menu_area_container.setLayoutParams(layoutParams);

    }

    public void closeSpaceOfCamera(){

        aurora_ll_input_container.setVisibility(VISIBLE);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dip2px(getContext(),200));
        my_menu_area_container.setLayoutParams(layoutParams);

    }


    public void initCamera() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCameraSupport = new CameraNew(getContext(), mTextureView);
            //mCameraSupport = new CameraOld(getContext(), mTextureView);

        } else {
            mCameraSupport = new CameraOld(getContext(), mTextureView);
        }
 //       mCameraSupport = new CameraOld(getContext(), mTextureView);
//        ViewGroup.LayoutParams params = mTextureView.getLayoutParams();
//        params.height = mSoftKeyboardHeight == 0 ? sMenuHeight : mSoftKeyboardHeight;
//        mTextureView.setLayoutParams(params);
        Log.e(TAG, "TextureView height: " + mTextureView.getHeight());





        mCameraSupport.setCameraCallbackListener(mCameraListener);
        mCameraSupport.setCameraEventListener(this);
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCameraId = i;
                mIsBackCamera = true;
                break;
            }
        }
        if (mTextureView.isAvailable()) {
            mCameraSupport.open(mCameraId, mWidth, sMenuHeight, mIsBackCamera, mStyle.getCameraQuality());
        } else {
            mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                    Log.d("ChatInputView", "Opening camera");
                    if (mCameraSupport == null) {
                        initCamera();
                    } else {
                        mCameraSupport.open(mCameraId, width, height, mIsBackCamera, mStyle.getCameraQuality());
                    }

                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
                    Log.d("ChatInputView", "Texture size changed, Opening camera");
                    if (mTextureView.getVisibility() == VISIBLE && mCameraSupport != null) {
                        mCameraSupport.open(mCameraId, width, height, mIsBackCamera, mStyle.getCameraQuality());
                    }
                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                    if (null != mCameraSupport) {
                        mCameraSupport.release();
                    }
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

                }
            });
        }
    }

    /**
     * Full screen mode
     */
    private void fullScreen() {
        // hide top status bar
        Activity activity = (Activity) getContext();
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mFullScreenBtn.setBackgroundResource(R.drawable.aurora_preview_recover_screen);
      //  mFullScreenBtn.setVisibility(VISIBLE);
        mChatInputContainer.setVisibility(GONE);
        mMenuItemContainer.setVisibility(GONE);
        int height = mHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = mWindow.getWindowManager().getDefaultDisplay();
            DisplayMetrics dm = getResources().getDisplayMetrics();
            display.getRealMetrics(dm);
            height = dm.heightPixels;
        }
        MarginLayoutParams marginParams1 = new MarginLayoutParams(mCaptureBtn.getLayoutParams());
        marginParams1.setMargins(marginParams1.leftMargin, marginParams1.topMargin, marginParams1.rightMargin,
                dp2px(40));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(marginParams1);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;
        mCaptureBtn.setLayoutParams(params);

        MarginLayoutParams marginParams2 = new MarginLayoutParams(mRecordVideoBtn.getLayoutParams());
        marginParams2.setMargins(dp2px(20), marginParams2.topMargin, marginParams2.rightMargin, dp2px(48));
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(marginParams2);
        params2.gravity = Gravity.BOTTOM | Gravity.START;
        mRecordVideoBtn.setLayoutParams(params2);

//        MarginLayoutParams marginParams3 = new MarginLayoutParams(mSwitchCameraBtn.getLayoutParams());
//        marginParams3.setMargins(marginParams3.leftMargin, marginParams3.topMargin, dp2px(20), dp2px(48));
//        FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(marginParams3);
//        params3.gravity = Gravity.BOTTOM | Gravity.END;
//        mSwitchCameraBtn.setLayoutParams(params3);

    //   mMenuContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
 //       mMenuContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
  //      mTextureView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        mIsFullScreen = true;
    }

    public int dp2px(float value) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    /**
     * Recover screen
     */
    private void recoverScreen() {
        final Activity activity = (Activity) getContext();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
                attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                activity.getWindow().setAttributes(attrs);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                mIsFullScreen = false;
                mIsRecordingVideo = false;
                mIsRecordVideoMode = false;
               // mCloseBtn.setVisibility(GONE);
                mFullScreenBtn.setBackgroundResource(R.drawable.aurora_preview_full_screen);
             //   mFullScreenBtn.setVisibility(VISIBLE);
                mChatInputContainer.setVisibility(VISIBLE);
                mMenuItemContainer.setVisibility(isShowBottomMenu()?VISIBLE:GONE);
                int height = sMenuHeight;
                if (mSoftKeyboardHeight != 0) {
                    height = mSoftKeyboardHeight;
                }
                setMenuContainerHeight(height);
//                ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        height);
//                mTextureView.setLayoutParams(params);
                mRecordVideoBtn.setBackgroundResource(R.drawable.aurora_preview_record_video);
                showRecordVideoBtn();
                mSwitchCameraBtn.setBackgroundResource(R.drawable.aurora_preview_switch_camera);
                mSwitchCameraBtn.setVisibility(VISIBLE);
                mCaptureBtn.setBackgroundResource(R.drawable.aurora_menuitem_send_pres);

                MarginLayoutParams marginParams1 = new MarginLayoutParams(mCaptureBtn.getLayoutParams());
                marginParams1.setMargins(marginParams1.leftMargin, marginParams1.topMargin, marginParams1.rightMargin,
                        dp2px(12));
                FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(marginParams1);
                params1.gravity = Gravity.BOTTOM | Gravity.CENTER;
                mCaptureBtn.setLayoutParams(params1);

                MarginLayoutParams marginParams2 = new MarginLayoutParams(mRecordVideoBtn.getLayoutParams());
                marginParams2.setMargins(dp2px(20), marginParams2.topMargin, marginParams2.rightMargin, dp2px(20));
                FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(marginParams2);
                params2.gravity = Gravity.BOTTOM | Gravity.START;
                mRecordVideoBtn.setLayoutParams(params2);

//                MarginLayoutParams marginParams3 = new MarginLayoutParams(mSwitchCameraBtn.getLayoutParams());ke
//                marginParams3.setMargins(marginParams3.leftMargin, marginParams3.topMargin, dp2px(20), dp2px(20));
//                FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(marginParams3);
//                params3.gravity = Gravity.BOTTOM | Gravity.END;
//                mSwitchCameraBtn.setLayoutParams(params3);
            }
        });
    }

    public void dismissMenuLayout() {


        mMenuManager.hideCustomMenu();
        menuContainer.setVisibility(GONE);

        if (mCameraSupport != null) {
            mCameraSupport.release();
            mCameraSupport = null;
        }
    }

    public void invisibleMenuLayout() {
        menuContainer.setVisibility(INVISIBLE);
    }

    public void showMenuLayout() {
        EmoticonsKeyboardUtils.closeSoftKeyboard(mChatInput);
        menuContainer.setVisibility(VISIBLE);
    }


    public void closeKeyBoard() {
        EmoticonsKeyboardUtils.closeSoftKeyboard(mChatInput);
    }


    int dip2px(Context context , int dipValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)((dipValue * scale + 0.5f));
    }



    public void showRecordVoiceLayout() {


        mSelectPhotoView.setVisibility(GONE);
        mCameraFl.setVisibility(GONE);
        mEmojiRl.setVisibility(GONE);
        mRecordVoiceRl.setVisibility(VISIBLE);
        mRecordContentLl.setVisibility(VISIBLE);

        mPreviewPlayLl.setVisibility(GONE);
        chagnyongyu_container.setVisibility(GONE);

    }

    public void dismissRecordVoiceLayout() {
        mRecordVoiceRl.setVisibility(GONE);
    }

    public void showSelectPhotoLayout() {
        mRecordVoiceRl.setVisibility(GONE);
        mCameraFl.setVisibility(GONE);
        mEmojiRl.setVisibility(GONE);
        chagnyongyu_container.setVisibility(GONE);

        mSelectPhotoView.setVisibility(VISIBLE);
    }

    public void dismissPhotoLayout() {
        mSelectPhotoView.setVisibility(View.GONE);
    }

    public void showCameraLayout() {
        mRecordVoiceRl.setVisibility(GONE);
        mSelectPhotoView.setVisibility(GONE);
        mEmojiRl.setVisibility(GONE);
        chagnyongyu_container.setVisibility(GONE);

        mCameraFl.setVisibility(VISIBLE);
    }

    public void showRecordVideoBtn(){
        if(mRecordVideoBtn.getTag()!=null && mRecordVideoBtn.getTag() instanceof String && ((String)mRecordVideoBtn.getTag()).equals("GONE")){
            mRecordVideoBtn.setVisibility(GONE);
        }else {
           // mRecordVideoBtn.setVisibility(VISIBLE);
        }

    }


    public void dismissCameraLayout() {
        if (mCameraSupport != null) {
            mCameraSupport.release();
            mCameraSupport = null;
        }
        mCameraFl.setVisibility(GONE);
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, sMenuHeight);
        mTextureView.setLayoutParams(params);
    }

    public void showEmojiLayout() {
        mRecordVoiceRl.setVisibility(GONE);
        mSelectPhotoView.setVisibility(GONE);
        mCameraFl.setVisibility(GONE);
        chagnyongyu_container.setVisibility(GONE);

        mEmojiRl.setVisibility(VISIBLE);
    }



    public void changyongyuShow() {
        mRecordVoiceRl.setVisibility(GONE);
        mSelectPhotoView.setVisibility(GONE);
        mCameraFl.setVisibility(GONE);
        mEmojiRl.setVisibility(GONE);
        chagnyongyu_container.setVisibility(VISIBLE);


    }

    public void hideDefaultMenuLayout(){
        mRecordVoiceRl.setVisibility(GONE);
        mSelectPhotoView.setVisibility(GONE);
        mCameraFl.setVisibility(GONE);
        mEmojiRl.setVisibility(GONE);
        chagnyongyu_container.setVisibility(GONE);

    }


    public void dismissEmojiLayout() {
        mEmojiRl.setVisibility(GONE);
    }

    /**
     * Set menu container's height, invoke this method once the menu was
     * initialized.
     *
     * @param height Height of menu, set same height as soft keyboard so that
     *               display to perfection.
     */
    public void setMenuContainerHeight(int height) {
        if (height > 0) {
            sMenuHeight = height;
            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    height);
           // mMenuContainer.setLayoutParams(params);
        }
    }

    private boolean onSubmit() {
        return mListener != null && mListener.onSendTextMessage(mInput);
    }

    public int getMenuState() {
        return menuContainer.getVisibility();
    }


    //发送图片
    @Override
    public void photoSend(List<FileItem> list) {
        my_menu_area_container.setVisibility(GONE);
        myMenuitemContainer.setVisibility(View.GONE);
        mListener.onSendFiles(list);
    }


    /**
     * Select photo callback
     */
    @Override
    public void onFileSelected() {

        int size = mSelectPhotoView.getSelectFiles().size();
//        Log.i("ChatInputView", "select file size: " + size);
//        if (mInput.length() == 0 && size == 1) {
//          //  triggerSendButtonAnimation(mSendBtn, true, true);
//        } else if (mInput.length() > 0 && mSendCountTv.getVisibility() != View.VISIBLE) {
//            mSendCountTv.setVisibility(View.VISIBLE);
//        }
//        mSendCountTv.setText(String.valueOf(size));
//

//        Toast.makeText(getContext(), mSelectPhotoView.getSelectFiles().get(0)+"",
//                Toast.LENGTH_SHORT).show();
    }

    /**
     * Cancel select photo callback
     */
    @Override
    public void onFileDeselected() {
//        int size = mSelectPhotoView.getSelectFiles().size();
//        Log.i("ChatInputView", "deselect file size: " + size);
//        if (size > 0) {
//            mSendCountTv.setText(String.valueOf(size));
//        } else {
//            mSendCountTv.setVisibility(View.INVISIBLE);
//            if (mInput.length() == 0) {
//               // triggerSendButtonAnimation(mSendBtn, false, true);
//            }
//        }
    }

    /**
     * Trigger send button animation
     *
     * @param sendBtn       send button
     * @param hasContent    EditText has content or photos have been selected
     * @param isSelectPhoto check if selecting photos
     */
    private void triggerSendButtonAnimation(final ImageButton sendBtn, final boolean hasContent,
            final boolean isSelectPhoto) {
        float[] shrinkValues = new float[] { 0.6f };
        AnimatorSet shrinkAnimatorSet = new AnimatorSet();
        shrinkAnimatorSet.playTogether(ObjectAnimator.ofFloat(sendBtn, "scaleX", shrinkValues),
                ObjectAnimator.ofFloat(sendBtn, "scaleY", shrinkValues));
        shrinkAnimatorSet.setDuration(100);

        float[] restoreValues = new float[] { 1.0f };
        final AnimatorSet restoreAnimatorSet = new AnimatorSet();
        restoreAnimatorSet.playTogether(ObjectAnimator.ofFloat(sendBtn, "scaleX", restoreValues),
                ObjectAnimator.ofFloat(sendBtn, "scaleY", restoreValues));
        restoreAnimatorSet.setDuration(100);

        restoreAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mSendCountTv.bringToFront();
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    requestLayout();
                    invalidate();
                }
                if (mSelectPhotoView.getSelectFiles() != null && mSelectPhotoView.getSelectFiles().size() > 0) {
                    mSendCountTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        shrinkAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (!hasContent && isSelectPhoto) {
                    mSendCountTv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
//                if (hasContent) {
//                    mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), mStyle.getSendBtnPressedIcon()));
//                } else {
//                    mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), mStyle.getSendBtnIcon()));
//                }
                restoreAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        shrinkAnimatorSet.start();
    }

    /**
     * Set camera capture file path and file name. If user didn't invoke this
     * method, will save in default path.
     *
     * @param path     Photo to be saved in.
     * @param fileName File name.
     */
    @Deprecated
    public void setCameraCaptureFile(String path, String fileName) {
        File destDir = new File(path);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        mPhoto = new File(path, fileName + ".png");
    }

    /**
     * Record audio widget finger on touch record button callback
     */
    @Override
    public void onStart() {
        Log.e("ChatInputView", "starting chronometer");
        mChronometer.setVisibility(VISIBLE);
        mRecordHintTv.setVisibility(INVISIBLE);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    /**
     * Recording audio mode, finger moving callback
     */
    @Override
    public void onMoving() {
        mChronometer.setVisibility(VISIBLE);
        mRecordHintTv.setVisibility(INVISIBLE);
    }

    /**
     * Recording audio mode, finger moved left button (preview button) callback
     */
    @Override
    public void onMovedLeft() {
        mChronometer.setVisibility(INVISIBLE);
        mRecordHintTv.setVisibility(VISIBLE);
        mRecordHintTv.setText(getContext().getString(R.string.preview_play_audio_hint));
    }

    /**
     * Recording audio mode, finger moved right button (cancel button)
     */
    @Override
    public void onMovedRight() {
        mChronometer.setVisibility(INVISIBLE);
        mRecordHintTv.setVisibility(VISIBLE);
        mRecordHintTv.setText(getContext().getString(R.string.cancel_record_voice_hint));
    }

    /**
     * Recording audio mode, finger moved left button and release
     */
    @Override
    public void onLeftUpTapped() {
        mChronometer.stop();
        mRecordTime = SystemClock.elapsedRealtime() - mChronometer.getBase();
        mPreviewPlayBtn.setMax(Math.round(mRecordTime / 1000));
       // mChronometer.setVisibility(VISIBLE);
        mRecordHintTv.setText(getContext().getString(R.string.preview_play_audio_hint));

        mRecordHintTv.setVisibility(VISIBLE);
        mPreviewPlayLl.setVisibility(VISIBLE);
        mRecordContentLl.setVisibility(GONE);
    }

    /**
     * Recording audio mode, finger moved right button and release
     */
    @Override
    public void onRightUpTapped() {
        mChronometer.stop();
        mChronometer.setText("00:00");
        mChronometer.setVisibility(INVISIBLE);
        mRecordHintTv.setText(getContext().getString(R.string.record_voice_hint));
        mRecordHintTv.setVisibility(VISIBLE);
    }

    @Override
    public void onFinish() {
        mChronometer.stop();
        mChronometer.setText("00:00");
        mChronometer.setVisibility(GONE);
        mRecordHintTv.setText(getContext().getString(R.string.record_voice_hint));
        mRecordHintTv.setVisibility(VISIBLE);
    }

    private long convertStrTimeToLong(String strTime) {
        String[] timeArray = strTime.split(":");
        long longTime = 0;
        if (timeArray.length == 2) { // If time format is MM:SS
            longTime = Integer.parseInt(timeArray[0]) * 60 * 1000 + Integer.parseInt(timeArray[1]) * 1000;
        }
        return SystemClock.elapsedRealtime() - longTime;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCameraSupport != null) {
            mCameraSupport.release();
        }
        if (mMediaPlayer != null){
            mMediaPlayer.release();
        }
        getViewTreeObserver().removeOnPreDrawListener(this);
         mMediaPlayer = null;
    }



    @Override
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == GONE) {
            if (mCameraSupport != null) {
                mCameraSupport.release();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && mHeight <= 0) {
            this.getRootView().getGlobalVisibleRect(mRect);
            mHeight = mRect.bottom;
            Log.d(TAG, "Window focus changed, height: " + mHeight);
        }
    }

    public boolean isKeyboardVisible() {
        return (getDistanceFromInputToBottom() > 300 && menuContainer.getVisibility() == GONE)
                || (getDistanceFromInputToBottom() > (menuContainer.getHeight() + 300)
                        && menuContainer.getVisibility() == VISIBLE);
    }

    @Override
    public void onFinishTakePicture() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    Message message =new Message();
                    message.what=SHOW_BUTTON;
                    handler.sendMessage(message);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

            }
        }) {
        }.start();

    }

    public boolean isFullScreen() {
        return this.mIsFullScreen;
    }

    public void setPendingShowMenu(boolean flag) {
        this.mPendingShowMenu = flag;
    }

    @Override
    public boolean onPreDraw() {
        if (mPendingShowMenu) {
            if (isKeyboardVisible()) {
                ViewGroup.LayoutParams params = menuContainer.getLayoutParams();
                int distance = getDistanceFromInputToBottom();
                Log.d(TAG, "Distance from bottom: " + distance);
                if (distance < mHeight / 2 && distance > 300 && distance != params.height) {
                    params.height = distance;
                    mSoftKeyboardHeight = distance;



                  //  mMenuContainer.setLayoutParams(params);
                }
                return false;
            } else {
                showMenuLayout();
                mPendingShowMenu = false;
                return false;
            }
        } else {
            if (menuContainer.getVisibility() == VISIBLE && isKeyboardVisible()) {
                dismissMenuLayout();
                return false;
            }
        }
        return true;
    }


    private boolean showBottomMenu = true;

    public void setShowBottomMenu(Boolean showBottomMenu){
        this.showBottomMenu = showBottomMenu;
        mMenuItemContainer.setVisibility(showBottomMenu?View.VISIBLE:View.GONE);
    }

    public boolean isShowBottomMenu(){
        return showBottomMenu;
    }

    public int getDistanceFromInputToBottom() {
        if(isShowBottomMenu()){
            mMenuItemContainer.getGlobalVisibleRect(mRect);
        }else {
            //mChatInputContainer.getGlobalVisibleRect(mRect);
        }
        return mHeight - mRect.bottom;
    }

    private final Runnable measureAndLayout = new Runnable() {
        @Override
        public void run() {
            measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };

    @Override
    public void requestLayout() {
        super.requestLayout();

        // React Native Override requestLayout, since we refresh our layout in native,
        // RN catch the
        // requestLayout event, so that the view won't refresh at once, we simulate
        // layout here.
        post(measureAndLayout);
    }

    public int getSoftKeyboardHeight() {
        return mSoftKeyboardHeight > 0 ? mSoftKeyboardHeight : sMenuHeight;
    }

    public FrameLayout getCameraContainer() {
        return mCameraFl;
    }

    public RelativeLayout getVoiceContainer() {
        return mRecordVoiceRl;
    }

    public FrameLayout getSelectPictureContainer() {
        return mSelectPhotoView;
    }

    public EmojiView getEmojiContainer() {
        return mEmojiRl;
    }

    public View getCameraBtnContainer() {
        return this.mCameraBtnContainer;
    }

    public View getVoiceBtnContainer() {
        return this.mVoiceBtnContainer;
    }

    public View getEmojiBtnContainer() {
        return this.mEmojiBtnContainer;
    }

    public View getPhotoBtnContainer() {
        return this.mPhotoBtnContainer;
    }

    public ChatInputStyle getStyle() {
        return this.mStyle;
    }

    public ImageButton getVoiceBtn() {
        return this.mVoiceBtn;
    }

    public ImageButton getPhotoBtn() {
        return this.mPhotoBtn;
    }

    public ImageButton getCameraBtn() {
        return this.mCameraBtn;
    }

    public ImageButton getEmojiBtn() {
        return this.mEmojiBtn;
    }

    public ImageButton getSendBtn() {
        return this.mSendBtn;
    }

    public SelectPhotoView getSelectPhotoView() {
        return this.mSelectPhotoView;
    }

    public ImageButton getSelectAlbumBtn() {
        return this.mSelectAlbumIb;
    }

    public ImageButton getRecordVideoBtn() {
        return this.mRecordVideoBtn;
    }

    public MenuManager getMenuManager(){
        return  this.mMenuManager;
    }

    public LinearLayout getChatInputContainer(){
        return  this.mChatInputContainer;
    }

    public LinearLayout getMenuItemContainer(){
        return  this.mMenuItemContainer;
    }

    public FrameLayout getMenuContainer(){
        return  this.my_menu_area_container;
    }

    public LinearLayout getMyMenuitemContainer(){
        return  this.myMenuitemContainer;
    }



    public void setCameraQuality(float cameraQuality){
        mStyle.setCameraQuality(cameraQuality);
    }


}
