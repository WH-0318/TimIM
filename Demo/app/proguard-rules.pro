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
# --------------------------------------------基本指令区--------------------------------------------#
-optimizationpasses 5                               # 指定代码的压缩级别(在0~7之间，默认为5)
-dontusemixedcaseclassnames                         # 是否使用大小写混合(windows大小写不敏感，建议加入)
-dontskipnonpubliclibraryclasses                    # 是否混淆非公共的库的类
-dontskipnonpubliclibraryclassmembers               # 是否混淆非公共的库的类的成员
-dontpreverify                                      # 混淆时是否做预校验(Android不需要预校验，去掉可以加快混淆速度)
-verbose                                            # 混淆时是否记录日志(混淆后会生成映射文件)

# 将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile

# 保持注解不被混淆
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation {*;}

# 保持泛型不被混淆
-keepattributes Signature
# 保持反射不被混淆
-keepattributes EnclosingMethod
# 保持异常不被混淆
-keepattributes Exceptions
# 保持内部类不被混淆
-keepattributes Exceptions,InnerClasses
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# --------------------------------------------默认保留区--------------------------------------------#
# 保持基本组件不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

# Support包规则
-dontwarn android.support.**
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

### 保留自定义控件(继承自View)不被混淆
#-keep public class * extends android.view.View {
#    *** get*();
#    void set*(***);
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
##
## 保留指定格式的构造方法不被混淆
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}

-keepclasseswithmembers class com.haibin.calendarview.** {
    public <init>(android.content.Context);
}

# 保留在Activity中的方法参数是view的方法(避免布局文件里面onClick被影响)
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保持R(资源)下的所有类及其方法不能被混淆
-keep class **.R$* { *; }

# 保持 Parcelable 序列化的类不被混淆(注：aidl文件不能去混淆)
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 需要序列化和反序列化的类不能被混淆(注：Java反射用到的类也不能被混淆)
-keepnames class * implements java.io.Serializable

# 保持 Serializable 序列化的类成员不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 保持 BaseAdapter 类不被混淆
-keep public class * extends android.widget.BaseAdapter { *; }

# --------------------------------------------webView区--------------------------------------------#
# WebView处理，项目中没有使用到webView忽略即可
# 保持Android与JavaScript进行交互的类不被混淆
-keep class **.AndroidJavaScript { *; }
-keepclassmembers class * extends android.webkit.WebViewClient {
     public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
     public boolean *(android.webkit.WebView,java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebChromeClient {
     public void *(android.webkit.WebView,java.lang.String);
}

# 网络请求相关
-keep public class android.net.http.SslError

# --------------------------------------------删除代码区--------------------------------------------#
# 删除代码中Log相关的代码
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

#---------------------------------2.第三方依赖--------------------------------
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# If you use the byType method to obtain Service, add the following rules to protect the interface:
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# If single-type injection is used, that is, no interface is defined to implement IProvider, the following rules need to be added to protect the implementation
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep public class * extends retrofit2.Converter {*;}

-keep class com.google.gson.stream.** { *; }

-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-keep interface okhttp3.**{*;}

-dontwarn okio.**
-keep class okio.**{*;}
-keep interface okio.**{*;}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
 **[] $VALUES;
 public *;
}

-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# If using AsyncExecutord, keep required constructor of default event used.
# Adjust the class name if a custom failure event type is used.
-keepclassmembers class org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# Accessed via reflection, avoid renaming or removal
-keep class org.greenrobot.eventbus.android.AndroidComponentsImpl


-keep class * implements androidx.viewbinding.ViewBinding {
    *;
}
-keepclassmembers class * {
    public <init>(android.view.View);
}


-keep class com.tencent.** { *; }
-keep class com.luck.picture.lib.** { *; }
-keep class com.luck.lib.camerax.** { *; }
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }



-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# With R8 full mode generic signatures are stripped for classes that are not kept.
-keep,allowobfuscation,allowshrinking class retrofit2.Response
#--------------------------------------------------------------------------

-dontwarn org.ahocorasick.trie.Trie
-dontwarn javax.lang.model.element.Element
-dontwarn org.ahocorasick.trie.Emit

## Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
## JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
#-keep class * extends com.google.gson.TypeAdapter
#-keep class * implements com.google.gson.TypeAdapterFactory
#-keep class * implements com.google.gson.JsonSerializer
#-keep class * implements com.google.gson.JsonDeserializer
## Prevent R8 from leaving Data object members always null
#-keep class * {
#  @com.google.gson.annotations.SerializedName <fields>;
#}
## Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
#-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
#-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn com.tencent.qcloud.tuicore.R$string
-dontwarn com.tencent.qcloud.tuikit.tuichat.R$string
-dontwarn com.tencent.qcloud.tuikit.tuicontact.R$dimen
-dontwarn com.tencent.qcloud.tuikit.tuicontact.R$string

# OkHttp3 框架混淆规则
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
# EasyHttp 框架混淆规则
-keep class com.hjq.http.** {*;}
# 必须要加上此规则，否则会导致泛型解析失败
-keep class * implements com.hjq.http.listener.OnHttpListener {
    *;
}
-keep class * extends com.hjq.http.model.ResponseClass {
    *;
}