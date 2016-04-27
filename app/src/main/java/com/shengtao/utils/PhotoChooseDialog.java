package com.shengtao.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shengtao.R;


public class PhotoChooseDialog extends Dialog {

	private TextView tv_photo, tv_camera, tv_cancel;

	private Context mContext;
	private View.OnClickListener mListener;

	public PhotoChooseDialog(Context context, View.OnClickListener listener) {
		super(context, R.style.Alert_Dialog_Style);
		mListener = listener;
		mContext = context;
		init(context);
	}

	private void init(Context context) {
		setCanceledOnTouchOutside(true);

		View view = View.inflate(context, R.layout.widget_dialog_select_photo,
				null);
		setContentView(view);
		init(view);
	}
	public void noCamera(){
		tv_camera.setVisibility(View.GONE);
	}

	private void init(View view) {
		tv_photo = (TextView) view.findViewById(R.id.tv_photo);
		tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_photo.setOnClickListener(onClickListener);
		tv_camera.setOnClickListener(onClickListener);
		tv_cancel.setOnClickListener(onClickListener);

		tv_photo.setCompoundDrawables(null, null, null, null);
		tv_camera.setCompoundDrawables(null, null, null, null);

		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.BOTTOM;
		params.alpha = 1.0f;
		params.width = window.getWindowManager().getDefaultDisplay().getWidth();
		window.setAttributes(params);
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();
			if (mListener != null)
				mListener.onClick(v);
		}
	};
}
