# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 #混淆时是否记录日志
-verbose
#保护泛型
-keepattributes Signature
#保护注解
-keepattributes *Annotation*
#保护行号，bug反馈不用上传mapping文件
-keepattributes SourceFile,LineNumberTable

-ignorewarnings -keep class * { public private *; }

-dontnote
-keepattributes *JavascriptInterface*
# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
	native <methods>;
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
	public void *(android.view.View);
}
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
	public static final android.os.Parcelable$Creator *;
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keep public class * extends android.app.Service
-keep public class * extends android.app.IntentService
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#保持Jpush不被混淆
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

#保持okhttp不被混淆
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**

-dontwarn org.apache.http.**

#Ping++模块
-dontwarn com.alipay.**
-keep class com.alipay.** {*;}

-dontwarn  com.ta.utdid2.**
-keep class com.ta.utdid2.** {*;}

-dontwarn  com.ut.device.**
-keep class com.ut.device.** {*;}

-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}

-dontwarn  com.unionpay.**
-keep class com.unionpay.** {*;}

-dontwarn com.pingplusplus.**
-keep class com.pingplusplus.** {*;}

#-dontwarn com.baidu.**
#-keep class com.baidu.**{*;}

-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}

#WebView中用到JS
-keepclassmembers class * {
	@android.webkit.JavascriptInterface <methods>;
}
#zxing
-dontwarn com.google.zxing.**
-keep class com.google.zxing.** {*; }

#facebook图片控件
-keep class com.facebook.imagepipeline.** { *;}

#Gson specific classes
-keep class sun.misc.Unsafe { *; }
-dontwarn com.google.gson.**
-keep class com.google.gson.** {*; }
#实体类
-keep class com.ihujia.hujia.network.entities.** { *;}
#JNI
-keep class com.ihujia.hujia.jni.**{*;}

-dontwarn u.aly.**
-keep class u.aly.** { *; }


-keep class * extends java.lang.annotation.Annotation
	-keepclasseswithmembernames class * {
		native <methods>;
	}


# banner 的混淆代码
-keep class com.youth.banner.** {
	*;
}

# Retrolambda
-dontwarn java.lang.invoke.*

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}