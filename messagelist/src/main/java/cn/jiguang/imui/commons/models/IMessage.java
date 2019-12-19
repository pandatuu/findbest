package cn.jiguang.imui.commons.models;


import cn.jiguang.imui.model.JobInfoModel;

import java.util.HashMap;

public interface IMessage {

    /**
     * Message id.
     * @return unique
     */
    String getMsgId();

    /**
     * Get user info of message.
     * @return UserInfo of message
     */
    IUser getFromUser();

    /**
     * Time string that display in message list.
     * @return Time string
     */
    String getTimeString();

    /**
     * Type of Message
     */

    boolean getHandled();

    JobInfoModel getJsobInfo();

    enum MessageType {
        EMPTY,
        EVENT,
        JOB_INFO,
        RESET,
        RECEIVE_COMMUNICATION_PHONE,
        RECEIVE_COMMUNICATION_LINE,

        RECEIVE_COMMUNICATION_VIDEO,

        RECEIVE_REQUEST_RESUME,
        RECEIVE_REQUEST_RESUME_HANDLED,

        RECEIVE_EXCHANGE_LINE_HANDLED,
        RECEIVE_EXCHANGE_PHONE_HANDLED,
        RECEIVE_INVITE_VIDEO_HANDLED,

        RECEIVE_INTERVIEW_VIDEO,
        RECEIVE_INTERVIEW_VIDEO_HANDLED,



        RECEIVE_NORMAL_INTERVIEW,
        RECEIVE_NORMAL_INTERVIEW_HANDLED,


        RECEIVE_ACCOUNT_PHONE,
        RECEIVE_ACCOUNT_LINE,


        INTERVIEW_SUCCESS,
        INTERVIEW_FAIL,
        SEND_OFFER,


        SEND_TEXT,
        RECEIVE_TEXT,

        SEND_IMAGE,
        RECEIVE_IMAGE,

        SEND_EMOTICON,
        RECEIVE_EMOTICON,

        SEND_VOICE,
        RECEIVE_VOICE,

        SEND_VIDEO,
        RECEIVE_VIDEO,

        SEND_LOCATION,
        RECEIVE_LOCATION,


        SEND_RESUME,
        SEND_RESUME_WORD,
        SEND_RESUME_PDF,
        SEND_RESUME_JPG,
        RECEIVE_RESUME,

        SEND_FILE,
        RECEIVE_FILE,

        SEND_CUSTOM,
        RECEIVE_CUSTOM;

        MessageType() {
        }
    }

    /**
     * Type of message
     * @return integer
     */
    int getType();


    /**
     *
     * @return
     */
    Integer getMimeType();

    /**
     * Status of message, enum.
     */
    enum MessageStatus {
        CREATED,
        SEND_GOING,
        SEND_SUCCEED,
        SEND_FAILED,
        SEND_DRAFT,
        RECEIVE_GOING,
        RECEIVE_SUCCEED,
        RECEIVE_FAILED;
    }

    MessageStatus getMessageStatus();


    /**
     * Text of message.
     * @return text
     */
    String getText();

    String getSize();

    /**
     * If message type is photo, voice, video or file,
     * get file path through this method.
     * @return file path
     */
    String getMediaFilePath();

    /**
     * If message type is voice or video, get duration through this method.
     * @return duration of audio or video, TimeUnit: SECONDS.
     */
    long getDuration();

    String getProgress();

    /**
     * Return extra key value of message
     * @return {@link HashMap<>}
     */
    HashMap<String, String> getExtras();



   int MIMETYPE_WORD=1;
   int MIMETYPE_PDF=2;
   int MIMETYPE_JPG=3;

}
