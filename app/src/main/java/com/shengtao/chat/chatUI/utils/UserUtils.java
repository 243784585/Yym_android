package com.shengtao.chat.chatUI.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.application.UIApplication;
import com.shengtao.chat.applib.controller.HXSDKHelper;
import com.shengtao.chat.chatUI.DemoHXSDKHelper;
import com.shengtao.chat.chatUI.domain.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserUtils {
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */
    public static User getUserInfo(String username){
        User user = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(username);
        if(user == null){
            user = new User(username);
        }

        if(user != null){
            //demo没有这些数据，临时填充
            if(TextUtils.isEmpty(user.getNick()))
                user.setNick(username);
        }
        return user;
    }

    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
        User user = getUserInfo(username);
        ImageLoader.getInstance().displayImage(user.getAvatar(),imageView, UIApplication.getAvatar());
//        if(user != null && user.getAvatar() != null){
//            Picasso.with(context).load().placeholder(R.drawable.default_avatar).into();
//        }else{
//            Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
//        }
    }

    /**
     * 设置当前用户头像
     */
    public static void setCurrentUserAvatar(Context context, ImageView imageView,String url) {
        User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
        ImageLoader.getInstance().displayImage(url,imageView, UIApplication.getAvatar());
//        if (user != null && user.getAvatar() != null) {
//            Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
//        } else {
//            Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
//        }
    }

    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
        User user = getUserInfo(username);
        if(user != null){
            textView.setText(user.getNick());
        }else{
            textView.setText(username);
        }
    }

    /**
     * 设置当前用户昵称
     */
    public static void setCurrentUserNick(TextView textView){
        User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
        if(textView != null){
            textView.setText(user.getNick());
        }
    }

    /**
     * 保存或更新某个用户
     * @param user
     */
    public static void saveUserInfo(User newUser) {
        if (newUser == null || newUser.getUsername() == null) {
            return;
        }
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).saveContact(newUser);
    }
    
}
