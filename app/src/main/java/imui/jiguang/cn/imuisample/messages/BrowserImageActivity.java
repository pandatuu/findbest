package imui.jiguang.cn.imuisample.messages;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import app.findbest.vip.R;
import cn.jiguang.imui.view.RoundImageView;
import com.bumptech.glide.Glide;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jiguang.imui.commons.BitmapLoader;

import imui.jiguang.cn.imuisample.views.ImgBrowserViewPager;
import imui.jiguang.cn.imuisample.views.photoview.PhotoView;


public class BrowserImageActivity extends Activity {

    private ImgBrowserViewPager mViewPager;
    private List<String> mPathList = new ArrayList<>();
    private List<String> mMsgIdList = new ArrayList<>();
    private LruCache<String, Bitmap> mCache;
    private int mWidth;
    private int mHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);
        mPathList.clear();
        mPathList = getIntent().getStringArrayListExtra("pathList");


        mMsgIdList = getIntent().getStringArrayListExtra("idList");
        mViewPager=  findViewById(R.id.img_browser_viewpager);


        DisplayMetrics dm = getResources().getDisplayMetrics();
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;

        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        int cacheSize = maxMemory / 4;
        mCache = new LruCache<>(cacheSize);
        mViewPager.setAdapter(pagerAdapter);

        initCurrentItem();
    }

    private void initCurrentItem() {
      //  PhotoView photoView = new PhotoView(true, this);
        Intent intent=getIntent();
        String msgId = intent.getStringExtra("msgId");

        int position = mMsgIdList.indexOf(msgId);
//        String path = mPathList.get(position);

        //项目内图片
//        if (path.contains("R.drawable") || path.contains("R.mipmap-")) {
//
//        }else{
//            UploadPic.Companion.loadPicFromNet(path, photoView);
//        }


//        if (path != null) {
//            Bitmap bitmap = mCache.get(path);
//            if (bitmap != null) {
//                photoView.setImageBitmap(bitmap);
//            } else {
//                File file = new File(path);
//                if (file.exists()) {
//                    bitmap = BitmapLoader.getBitmapFromFile(path, mWidth, mHeight);
//                    if (bitmap != null) {
//                        photoView.setImageBitmap(bitmap);
//                        mCache.put(path, bitmap);
//                    } else {
//                        photoView.setImageResource(R.drawable.aurora_picture_not_found);
//                    }
//                } else {
//                    photoView.setImageResource(R.drawable.aurora_picture_not_found);
//                }
//            }
//        } else {
//            photoView.setImageResource(R.drawable.aurora_picture_not_found);
//        }
        mViewPager.setCurrentItem(position);
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mPathList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = View.inflate(mViewPager.getContext(), R.layout.photo_view, null);
            ImageView image=view.findViewById(R.id.bigerImageView);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();//返回
                    overridePendingTransition(R.anim.left_in,R.anim.right_out);
                }
            });
            System.out.println(getCount());

            String path = mPathList.get(position);
            if(path!=null){
               String s[]= path.split(";");
                path=s[0];
            }


//            if (path.contains("R.drawable") || path.contains("R.mipmap-")) {
//                Integer resId = getResources().getIdentifier(path.replace("R.drawable.", ""),
//                        "drawable", getPackageName());
//                image.setImageResource(resId);
//
//            }else{
//                UploadPic.Companion.loadPicFromNet(path, image);
//
//            }


            Glide.with(BrowserImageActivity.this)
                    .load(path)
                    .centerCrop()
                    .placeholder(R.mipmap.no_pic_show)
                    .into(image);



//           image.setTag(position);
//
//            PhotoView photoView = new PhotoView(true, BrowserImageActivity.this);
//            photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//
//            photoView.setTag(position);
//            String path = mPathList.get(position);
//
//
//            //项目内图片
//            if (path.contains("R.drawable") || path.contains("R.mipmap-")) {
//                if (path != null) {
//                    Bitmap bitmap = mCache.get(path);
//                    if (bitmap != null) {
//                        photoView.setImageBitmap(bitmap);
//                    } else {
//                        File file = new File(path);
//                        if (file.exists()) {
//                            bitmap = BitmapLoader.getBitmapFromFile(path, mWidth, mHeight);
//                            if (bitmap != null) {
//                                photoView.setImageBitmap(bitmap);
//                                mCache.put(path, bitmap);
//                            } else {
//                                photoView.setImageResource(R.drawable.aurora_picture_not_found);
//                            }
//                        } else {
//                            photoView.setImageResource(R.drawable.aurora_picture_not_found);
//                        }
//                    }
//                } else {
//                    photoView.setImageResource(R.drawable.aurora_picture_not_found);
//                }
//            }else{
//
//            }



            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            View view = (View) object;
            int currentPage = mViewPager.getCurrentItem();
            if (currentPage == (Integer) view.getTag()) {
                return POSITION_NONE;
            } else {
                return POSITION_UNCHANGED;
            }
        }
    };


}
