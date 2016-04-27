package com.shengtao.nativeimg;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.shengtao.R;
import com.shengtao.utils.MyDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;


public class SelectImageAdapter extends BaseAdapter {

	private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象
	private ArrayList<String> data;
	private Context context;
	private Activity activity;
	protected LayoutInflater mInflater;
	DisplayImageOptions options;
	public HashMap<Integer, View> map = new HashMap<Integer, View>();
	private GridView gridView;
	public SelectImageAdapter(Context ctx, ArrayList<String> arrayList,GridView gridView) {
		this.context = ctx;
		this.activity = (Activity) ctx;
		this.gridView = gridView;
		this.data = arrayList;
		options = new DisplayImageOptions.Builder()
				// 初始化默认图片
				.showImageOnLoading(R.drawable.moren)
						// 加载异常时显示图片
				.showImageOnFail(R.drawable.moren)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
//				.displayer(new RoundedBitmapDisplayer(5))
						// Uri地址为空时显示图片
				.showImageForEmptyUri(R.drawable.moren)
				.build();
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data == null ? 1 : data.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (map.get(position) == null) {
			final ViewHolder viewHolder;

			convertView = mInflater.inflate(R.layout.repair_image_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (NativeImageView) convertView.findViewById(R.id.item_grida_image);
			viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
			//用来监听ImageView的宽和高
			viewHolder.mImageView.setOnMeasureListener(new NativeImageView.OnMeasureListener() {

				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});

			if (position == data.size()) {
				viewHolder.iv_delete.setVisibility(View.GONE);
				viewHolder.mImageView.setImageResource(R.drawable.icon_addpic_focused);
			} else {
				String path = data.get(position);
				viewHolder.iv_delete.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage("file://" + path, viewHolder.mImageView, options);
			}
			viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					HashMap<String,String> hashMap = new HashMap<>();
					hashMap.put("title", "是否删除图片");
					hashMap.put("cancel", "取消");
					hashMap.put("submit", "确认");
					hashMap.put("gravity","center");
					new MyDialog(context,hashMap){
						@Override
						public void dialogSubmit() {
							dismiss();
							data.remove(position);
//							viewHolder.mImageView.setImageResource(R.drawable.add_photo);
//							viewHolder.iv_delete.setVisibility(View.GONE);
//							notifyDataSetChanged();
							Message msg = mHandler.obtainMessage();
							msg.obj = data;
							mHandler.sendMessage(msg);

						}
					}.show();
				}
			});
			convertView.setTag(viewHolder);
			map.put(position, convertView);
		} else {
			convertView = map.get(position);
		}
//		convertView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context, NativeImageActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("select_photo", data);
//				intent.putExtras(bundle);
//				activity.startActivityForResult(intent, 101);
//			}
//		});
		return convertView;
	}

	public static class ViewHolder{
		public NativeImageView mImageView;
		private ImageView iv_delete;
	}
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ArrayList<String> arrayList = (ArrayList<String>) msg.obj;
			SelectImageAdapter adapter = new SelectImageAdapter(context,arrayList,gridView);
			gridView.setAdapter(adapter);
		}
	};
}
