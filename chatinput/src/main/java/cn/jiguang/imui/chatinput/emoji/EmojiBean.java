package cn.jiguang.imui.chatinput.emoji;

/**
 * use XhsEmotionsKeyboard(https://github.com/w446108264/XhsEmoticonsKeyboard)
 * author: sj
 */
public class EmojiBean {
    public int icon;
    public String emoji;
    public int type;

    public EmojiBean(int icon, String emoji,int type) {
        this.icon = icon;
        this.emoji = emoji;
        this.type = type;
    }
}