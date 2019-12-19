package cn.jiguang.imui.messages;

import android.view.View;

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.view.RoundTextView;


public class EmptyViewHolder<MESSAGE extends IMessage>
        extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder {


    public EmptyViewHolder(View itemView, boolean isSender) {
        super(itemView);
    }

    @Override
    public void onBind(MESSAGE message) {

    }

    @Override
    public void applyStyle(MessageListStyle style) {

    }
}
