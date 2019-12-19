package cn.jiguang.imui.messages;

import android.view.View;

import cn.jiguang.imui.commons.models.IMessage;

public class ResetViewHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder {

    private boolean mIsSender;

    public ResetViewHolder(View itemView, boolean isSender) {
        super(itemView);
        this.mIsSender = isSender;

    }

    @Override
    public void onBind(final MESSAGE message) {
    }

    @Override
    public void applyStyle(MessageListStyle style) {
    }



}