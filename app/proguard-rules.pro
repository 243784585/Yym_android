# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Users\TT\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#环信
-keep class com.easemob.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}
-dontwarn  com.easemob.**
#2.0.9后的不需要加下面这个keep
#-keep class org.xbill.DNS.** {*;}
#另外，demo中发送表情的时候使用到反射，需要keep SmileUtils
-keep class com.shengtao.chat.chatUI.utils.SmileUtils {*;}
#注意前面的包名，如果把这个类复制到自己的项目底下，比如放在com.example.utils底下，应该这么写(实际要去掉#)
#-keep class com.example.utils.SmileUtils {*;}
#如果使用easeui库，需要这么写
#-keep class com.easemob.easeui.utils.EaseSmileUtils {*;}

#2.0.9后加入语音通话功能，如需使用此功能的api，加入以下keep
-dontwarn ch.imvs.**
-dontwarn org.slf4j.**
-keep class org.ice4j.** {*;}
-keep class net.java.sip.** {*;}
-keep class org.webrtc.voiceengine.** {*;}
-keep class org.bitlet.** {*;}
-keep class org.slf4j.** {*;}
-keep class ch.imvs.** {*;}

#极光
#-dontwarn cn.jpush.**
#-keep class cn.jpush.** { *; }
-dontwarn cn.jpush.**
-keepattributes  EnclosingMethod,Signature
-keep class cn.jpush.** { *; }
-keepclassmembers class ** {
    public void onEvent*(**);
}
#七牛
-dontwarn com.qiniu.**
-keep class com.qiniu.**{*;}
#友盟
-dontwarn com.umeng.**
-keep class com.umeng.** {*;}

-dontwarn com.tencent.**
-keep class com.tencent.** {*;}
#metadata-extractor
-dontwarn com.drew.**
-keep class com.drew.** {*;}
#parse-1.9.4
-dontwarn com.parse.**
-keep class com.parse.** {*;}

-keep class com.shengtao.** {*;}
-keep class widget.sidebar.** {*;}

-keep class com.mob.** {*;}
-keep class cn.smssdk.** {*;}

#爱贝支付
#-libraryjars ./libs/iapppay_plugin.jar

#-----------keep-------------------

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepattributes Exceptions,InnerClasses
-keep public class com.alipay.android.app.** {
    public <fields>;
    public <methods>;
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

#--------------unionpay 3.1.0--------------
-keep class com.unionpay.** {*;}
-keep class com.UCMobile.PayPlugin.** {*;}
-keep class cn.gov.pbc.tsm.client.mobile.android.bank.service.** {*;}

#--------------ecopay2.0--------------
-keep class com.payeco.android.plugin.** {*;}
-keep class org.payeco.http.entity.mime.** {*;}
-dontwarn com.payeco.android.plugin.**

-keepclassmembers class com.payeco.android.plugin {
  public *;
}

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*


#-----------keep iapppay-------------------
-keep class com.iapppay.utils.RSAHelper {*;}
-keep class com.iapppay.sdk.main.** {*;}
-keep class com.iapppay.interfaces.callback.** {*;}

-keep class com.iapppay.interfaces.** {
    public <fields>;
    public <methods>;
}


#iapppay UI
-keep public class com.iapppay.ui.activity.** {
    public <fields>;
    public <methods>;
}

# View
-keep public class com.iapppay.ui.widget.**{*;}

#iapppay_sub_pay
-keep public class com.iapppay.pay.channel.** {*;}



-ignorewarning
-keep public class * extends android.widget.TextView

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*



