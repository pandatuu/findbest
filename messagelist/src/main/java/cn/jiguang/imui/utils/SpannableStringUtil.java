package cn.jiguang.imui.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.TypedValue;

import java.util.Iterator;

import cn.jiguang.imui.chatinput.emoji.DefEmoticons;


public class SpannableStringUtil {


    public static SpannableString stringToSpannableString(Context c,String msg) {
        Iterator iterator= DefEmoticons.textToPic.keySet().iterator();
        SpannableString spannableString = new SpannableString(msg);
        while(iterator.hasNext()){
            String key=iterator.next().toString();
            int start=0;
            while(true){
                int index=msg.indexOf(key,start);
                if(index>=0){
                    ImageSpan img = new ImageSpan(c, DefEmoticons.textToPic.get(key));
                    spannableString.setSpan(img, index, index+key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start=index+key.length();
                }else{
                    break;
                }
            }
        }
        return spannableString;
    }
}
