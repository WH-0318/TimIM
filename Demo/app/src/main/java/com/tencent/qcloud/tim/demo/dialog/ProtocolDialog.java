package com.tencent.qcloud.tim.demo.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import static android.graphics.drawable.GradientDrawable.RECTANGLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import com.tencent.qcloud.tim.demo.R;
import com.tencent.qcloud.tim.demo.profile.ProtocolActivity;

public class ProtocolDialog extends AlertDialog {

	private View.OnClickListener agreeListener;
	private View.OnClickListener exitListener;

	public ProtocolDialog(@NonNull Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		setContentView(R.layout.dialog_protocol_layout);
		View contentLayout = findViewById(R.id.content_layout);
		TextView contentText = findViewById(R.id.protocol_text);
		TextView agreeText = findViewById(R.id.agree_text);
		TextView exitText = findViewById(R.id.exit_text);

		setProtocolContent(contentText);
		agreeText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (agreeListener != null) {
					agreeListener.onClick(v);
				}
				dismiss();
			}
		});
		exitText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (exitListener != null) {
					exitListener.onClick(v);
				}
				dismiss();
			}
		});
	}

	private void setProtocolContent(TextView contentText) {
		contentText.setText(Html.fromHtml("感谢您信任并使用众意IM App!<br/>我们非常重视您的个人信息和隐私保护，依据最新法律要求，我们更新了<a href='privacy_protocol.html'>《隐私政策》</a>。<br/>为向您提供更好的旅行服务，在使用我们的产品前，请您阅读完整版<a href='user_agreement.html'>《用户服务协议》</a>和<a href='privacy_protocol.html'>《隐私政策》</a>的所有条款，授权位置、设备信息、存储权限、其他权限包括:<br/>1.在您浏览时，我们可能会申请设备权限(部分手机称为“读取电话状态”权限)，以获取设备标识信息，用于设备ID识别、账号登录、安全风控:并申请存储权限用于下载及缓存相关文件，减少重复加载，节省您的流量;<br/>2.在您使用基于相机的附加功能(扫描二维码、拍照等)、基于图片上传的附加功能、基于麦克风的附加功能(语音咨询)、基于发送文件，音视频通话时，我们可能会申请相机、相册、麦克风以及日历的访问权限，上述权限均不会默认或强制开启收集信息;<br/>3未经您的同意，我们不会将您的个人信息共享给第三方;<br/>如您同意<a href='privacy_protocol.html'>《隐私政策》</a>和<a href='user_agreement.html'>《用户服务协议》</a>请点击“同意”开始使用我们的产品和服务，我们将尽全力保护您的个人信息和合法权益，再次感谢您的信任!"));
		contentText.setMovementMethod(LinkMovementMethod.getInstance());
		CharSequence content = contentText.getText();
		if(content instanceof Spannable){
			int end = content.length();
			Spannable sp = (Spannable)content;
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			SpannableStringBuilder style = new SpannableStringBuilder(content);
			style.clearSpans();
			for(URLSpan url : urls){
				MyClickSpan myURLSpan = new MyClickSpan(url.getURL());
				style.setSpan(myURLSpan,sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			contentText.setText(style);
		}

	}
	public void setAgreeListener(View.OnClickListener listener) {
		this.agreeListener = listener;
	}

	public void setExitListener(View.OnClickListener listener) {
		this.exitListener = listener;
	}

	private class MyClickSpan extends ClickableSpan {

		private String url = "";

		public MyClickSpan(String url) {
			this.url = url;
		}
		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			ds.setColor(getContext().getResources().getColor(R.color.colorPrimary));
			ds.setUnderlineText(false);
		}
		@Override
		public void onClick(View widget) {
			int protocolType = 0;
			if ("privacy_protocol.html".equals(url)) {
				protocolType = ProtocolActivity.PROTOCOL_TYPE_PRIVACY;
			} else if ("user_agreement.html".equals(url)) {
				protocolType = ProtocolActivity.PROTOCOL_TYPE_USER;
			}
			ProtocolActivity.enter(getContext(), protocolType);
		}
	}

}
