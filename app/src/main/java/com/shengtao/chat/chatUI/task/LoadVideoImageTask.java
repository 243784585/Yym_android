package com.shengtao.chat.chatUI.task;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.util.ImageUtils;
import com.shengtao.utils.CommonUtils;

import java.io.File;

public class LoadVideoImageTask extends AsyncTask<Object, Void, Bitmap> {

	private ImageView iv = null;
	String thumbnailPath = null;
	String thumbnailUrl = null;
	Activity activity;
	EMMessage message;
	BaseAdapter adapter;

	@Override
	protected Bitmap doInBackground(Object... params) {
		thumbnailPath = (String) params[0];
		thumbnailUrl = (String) params[1];
		iv = (ImageView) params[2];
		activity = (Activity) params[3];
		message = (EMMessage) params[4];
		adapter = (BaseAdapter) params[5];
		if (new File(thumbnailPath).exists()) {
			return ImageUtils.decodeScaleImage(thumbnailPath, 120, 120);
		} else {
			return null;
		}
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		if (result != null) {

		} else {
			if (message.status == EMMessage.Status.FAIL
					|| message.direct == EMMessage.Direct.RECEIVE) {
				if (CommonUtils.isNetWorkConnected(activity)) {
					new AsyncTask<Void, Void, Void>() {
						 
						
						@Override
						protected Void doInBackground(Void... params) {
							EMChatManager.getInstance().asyncFetchMessage(
									message);
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							adapter.notifyDataSetChanged();
						};
					}.execute();
				}
			}

		}
	}

}
