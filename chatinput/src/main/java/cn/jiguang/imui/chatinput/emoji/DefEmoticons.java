package cn.jiguang.imui.chatinput.emoji;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.style.ImageSpan;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import cn.jiguang.imui.chatinput.R;
import cn.jiguang.imui.chatinput.R.mipmap;
public class DefEmoticons {
    public static final EmojiBean[] sEmojiArray;
    public static final Map<String,Integer> textToPic;

    public DefEmoticons() {
    }

    static {


        textToPic= new HashMap<String,Integer>();
        textToPic.put("[眨眼]",R.mipmap.blink);
        textToPic.put("[呲牙]",R.mipmap.ciya);
        textToPic.put("[加油]",R.mipmap.comeon);
        textToPic.put("[呆]",R.mipmap.dai);
        textToPic.put("[憨笑]",R.mipmap.hanxiao);
        textToPic.put("[坏笑]", mipmap.huaixiao);
        textToPic.put("[可怜]",R.mipmap.kelian);
        textToPic.put("[桃心眼]",R.mipmap.love_eye);
        textToPic.put("[震惊]",R.mipmap.shock);
        textToPic.put("[微笑]",R.mipmap.smile);
        textToPic.put("[委屈]",R.mipmap.weiqu);
        textToPic.put("[晕]",R.mipmap.yun);

        textToPic.put("[无语]",R.mipmap.wuyu);
        textToPic.put("[滑稽]",R.mipmap.huaji);
        textToPic.put("[痛苦]",R.mipmap.tongku);
        textToPic.put("[大哭]",R.mipmap.daku);
        textToPic.put("[害羞]",R.mipmap.haixiu);
        textToPic.put("[擦汗]",R.mipmap.cahan);
        textToPic.put("[憨笑2]",R.mipmap.hanxiao2);
        textToPic.put("[生气]",R.mipmap.shengqi);
        textToPic.put("[亲亲]",R.mipmap.kisskiss);
        textToPic.put("[可爱]",R.mipmap.cute);
        textToPic.put("[睡觉]",R.mipmap.sleep);



        sEmojiArray = new EmojiBean[]{
                new EmojiBean(textToPic.get("[眨眼]"), "[眨眼]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[呲牙]"), "[呲牙]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[加油]"),"[加油]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[呆]"), "[呆]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[憨笑]"),"[憨笑]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[坏笑]"), "[坏笑]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[可怜]"),"[可怜]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[桃心眼]"),"[桃心眼]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[震惊]"),"[震惊]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[微笑]"), "[微笑]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[委屈]"), "[委屈]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[晕]"), "[晕]",Constants.EMOTICON_CLICK_BIGIMAGE),

                new EmojiBean(textToPic.get("[无语]"), "[无语]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[滑稽]"), "[滑稽]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[痛苦]"),"[痛苦]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[大哭]"), "[大哭]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[害羞]"),"[害羞]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[擦汗]"), "[擦汗]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[憨笑2]"),"[憨笑2]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[生气]"),"[生气]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[亲亲]"),"[亲亲]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[可爱]"), "[可爱]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[委屈]"), "[委屈]",Constants.EMOTICON_CLICK_BIGIMAGE),
                new EmojiBean(textToPic.get("[睡觉]"), "[睡觉]",Constants.EMOTICON_CLICK_BIGIMAGE),









//
//
//                new EmojiBean(mipmap.emoji_0x1f604, EmojiParse.fromCodePoint(128516),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f603, EmojiParse.fromCodePoint(128515),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f60a, EmojiParse.fromCodePoint(128522),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f609, EmojiParse.fromCodePoint(128521),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f60d, EmojiParse.fromCodePoint(128525),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f618, EmojiParse.fromCodePoint(128536),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f61a, EmojiParse.fromCodePoint(128538),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f61c, EmojiParse.fromCodePoint(128540),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f61d, EmojiParse.fromCodePoint(128541),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f633, EmojiParse.fromCodePoint(128563),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f601, EmojiParse.fromCodePoint(128513),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f614, EmojiParse.fromCodePoint(128532),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f60c, EmojiParse.fromCodePoint(128524),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f612, EmojiParse.fromCodePoint(128530),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f61e, EmojiParse.fromCodePoint(128542),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f623, EmojiParse.fromCodePoint(128547),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f622, EmojiParse.fromCodePoint(128546),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f602, EmojiParse.fromCodePoint(128514),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f62d, EmojiParse.fromCodePoint(128557),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f62a, EmojiParse.fromCodePoint(128554),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f625, EmojiParse.fromCodePoint(128549),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f630, EmojiParse.fromCodePoint(128560),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f613, EmojiParse.fromCodePoint(128531),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f628, EmojiParse.fromCodePoint(128552),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f631, EmojiParse.fromCodePoint(128561),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f620, EmojiParse.fromCodePoint(128544),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f621, EmojiParse.fromCodePoint(128545),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f616, EmojiParse.fromCodePoint(128534),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f637, EmojiParse.fromCodePoint(128567),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f632, EmojiParse.fromCodePoint(128562),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f47f, EmojiParse.fromCodePoint(128127),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f60f, EmojiParse.fromCodePoint(128527),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f466, EmojiParse.fromCodePoint(128102),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f467, EmojiParse.fromCodePoint(128103),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f468, EmojiParse.fromCodePoint(128104),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f469, EmojiParse.fromCodePoint(128105),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f31f, EmojiParse.fromCodePoint(127775),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f444, EmojiParse.fromCodePoint(128068),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f44d, EmojiParse.fromCodePoint(128077),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f44e, EmojiParse.fromCodePoint(128078),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f44c, EmojiParse.fromCodePoint(128076),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f44a, EmojiParse.fromCodePoint(128074),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x270a, EmojiParse.fromChar('✊'),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x270c, EmojiParse.fromChar('✌'),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f446, EmojiParse.fromCodePoint(128070),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f447, EmojiParse.fromCodePoint(128071),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f449, EmojiParse.fromCodePoint(128073),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f448, EmojiParse.fromCodePoint(128072),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f64f, EmojiParse.fromCodePoint(128591),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f44f, EmojiParse.fromCodePoint(128079),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f4aa, EmojiParse.fromCodePoint(128170),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f457, EmojiParse.fromCodePoint(128087),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f380, EmojiParse.fromCodePoint(127872),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x2764, EmojiParse.fromChar('❤'),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f494, EmojiParse.fromCodePoint(128148),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f48e, EmojiParse.fromCodePoint(128142),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f436, EmojiParse.fromCodePoint(128054),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f431, EmojiParse.fromCodePoint(128049),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f339, EmojiParse.fromCodePoint(127801),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f33b, EmojiParse.fromCodePoint(127803),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f341, EmojiParse.fromCodePoint(127809),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f343, EmojiParse.fromCodePoint(127811),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f319, EmojiParse.fromCodePoint(127769),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x2600, EmojiParse.fromChar('☀'),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x2601, EmojiParse.fromChar('☁'),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x26a1, EmojiParse.fromChar('⚡'),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x2614, EmojiParse.fromChar('☔'),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f47b, EmojiParse.fromCodePoint(128123),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f385, EmojiParse.fromCodePoint(127877),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f381, EmojiParse.fromCodePoint(127873),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f4f1, EmojiParse.fromCodePoint(128241),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f50d, EmojiParse.fromCodePoint(128269),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f4a3, EmojiParse.fromCodePoint(128163),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x26bd, EmojiParse.fromChar('⚽'),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x2615, EmojiParse.fromChar('☕'),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f37a, EmojiParse.fromCodePoint(127866),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f382, EmojiParse.fromCodePoint(127874),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f3e0, EmojiParse.fromCodePoint(127968),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f697, EmojiParse.fromCodePoint(128663),Constants.EMOTICON_CLICK_TEXT),
//                new EmojiBean(mipmap.emoji_0x1f559, EmojiParse.fromCodePoint(128345),Constants.EMOTICON_CLICK_TEXT)
        };


    }
}
