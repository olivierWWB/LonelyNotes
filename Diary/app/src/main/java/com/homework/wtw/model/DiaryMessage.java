package com.homework.wtw.model;

/**
 * Created by ts on 17/3/7.
 */
import android.os.Parcel;
import android.os.Parcelable;

public class DiaryMessage {
    private int diary_message_id;//对话题评论的ID
    private int is_active; //-1-已经删除。1-没删除
    private int source_diary_id;//该日记ID
    private String content;
    private String date;
    private long create;

    public long getCreate() {
        return create;
    }

    public void setCreate(long create_time) {
        this.create = create;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


//    可以的，不用实现，啦啦啦啦啦啦
//    /** 下面实现的是对象的序列化*/
//    public int describeContents()
//    {
//        return 0;
//    }
//
//    public void writeToParcel(Parcel dest, int flags)
//    {
//        dest.writeString(content);
//        dest.writeInt(diary_message_id);
//        dest.writeInt(is_active);
//        dest.writeString(create_time);
//    }
//
//    public static final Creator<DiaryMessage> CREATOR = new Creator<DiaryMessage>()
//    {
//        public DiaryMessage createFromParcel(Parcel source)
//        {
//            DiaryMessage message = new DiaryMessage();
//            message.content = source.readString();
//            message.diary_message_id = source.readInt();
//            message.is_active = source.readInt();
//            message.create_time = source.readString();
//            return message;
//        }
//        public DiaryMessage[] newArray(int size)
//        {
//            return new DiaryMessage[size];
//        }
//    };

    public int getDiary_message_id() {
        return diary_message_id;
    }

    public void setDiary_message_id(int phone_topic_message_id) {
        this.diary_message_id = phone_topic_message_id;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getCreate_time() {
        return date;
    }

    public void setCreate_time(String date) {
        this.date = date;
    }

    public int getSource_diary_id() {
        return source_diary_id;
    }

    public void setSource_diary_id(int source_diary_id) {
        this.source_diary_id = source_diary_id;
    }
}
