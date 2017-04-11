package cn.ucai.superwechat.db;

import android.content.Context;

import java.io.File;

/**
 * Created by clawpo on 2017/3/29.
 */

public interface IGroupModel {
    void newGroup(Context context, String hxid,String groupName,String des,String owner,boolean isPublic,
                  boolean isInvites,File file,OnCompleteListener<String> listener);
    void addMembers(Context context,String members,String hxid,OnCompleteListener<String> listener);
}
