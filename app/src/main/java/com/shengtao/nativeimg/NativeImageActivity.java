package com.shengtao.nativeimg;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shengtao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NativeImageActivity extends Activity {
    GridView native_image_grid;
    NativeImgListAdapter adapter;
    private final static int SCAN_OK = 1;
    ArrayList<String> imageList = new ArrayList<String>();
    public static LinkedHashMap<String, String> groupList;
    private static final int CAMERA_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_image);
        groupList = new LinkedHashMap<String, String>();
        Intent intent = this.getIntent();
        ArrayList<String> selectPhoto = (ArrayList<String>) intent.getSerializableExtra("select_photo");
        for (int i = 0; i < selectPhoto.size(); i++) {
            groupList.put(selectPhoto.get(i), selectPhoto.get(i));
        }
        sortMapByKey(groupList);
        imageList.add("");
        initHeader();
        initView();
        getImages();
    }

    private void initHeader() {

        TextView tvRight = (TextView) findViewById(R.id.tv_right);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                ArrayList<String> arrayList = new ArrayList<String>();
                Iterator iter = groupList.keySet().iterator();
                while (iter.hasNext()) {
                    Object key = iter.next();
                    arrayList.add(groupList.get(key));
                }
                data.putExtra("list", arrayList);
                setResult(RESULT_OK, data);
                recycleImage();
                finish();
                System.gc();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                ArrayList<String> arrayList = new ArrayList<String>();
                Iterator iter = groupList.keySet().iterator();
                while (iter.hasNext()) {
                    Object key = iter.next();
                    arrayList.add(groupList.get(key));
                }
                data.putExtra("list", arrayList);
                setResult(RESULT_OK, data);
                recycleImage();
                finish();
                System.gc();
            }
        });
    }

    String intentFromCaptureUrl = "";

    private void initView() {
        native_image_grid = (GridView) findViewById(R.id.native_image_grid);
        native_image_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 首先保存图片
                File appDir = new File(Environment.getExternalStorageDirectory(), "yym");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                File file = new File(appDir, fileName);
                intentFromCaptureUrl = file.getPath();
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intentFromCapture, Activity.DEFAULT_KEYS_DIALER);
            }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    adapter = new NativeImgListAdapter(NativeImageActivity.this, imageList);
                    native_image_grid.setAdapter(adapter);
                    break;
            }
        }
    };

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    private void getImages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Cursor mCursor = getContentResolver().query(mImageUri, new String[]{MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.SIZE}, null, null, null);
                List<String> temp = new ArrayList<>();//imageList
                while (mCursor.moveToNext()) {
                    //获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    temp.add(path);
                }
                mCursor.close();
                for (int i = temp.size() - 1; i >= 0; i--) {
                    imageList.add(temp.get(i));
                }
//                通知Handler扫描图片完成
                mHandler.sendEmptyMessage(SCAN_OK);
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                File appDir = new File(Environment.getExternalStorageDirectory(), "yym");
                MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath(), appDir.getPath()}, null, null);
                Intent intent = new Intent();
                ArrayList<String> arrayList = new ArrayList<String>();
                groupList.put("name", intentFromCaptureUrl);
                Iterator iter = groupList.keySet().iterator();
                while (iter.hasNext()) {
                    Object key = iter.next();
                    arrayList.add(groupList.get(key));
                }
                intent.putExtra("list", arrayList);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void recycleImage() {
        imageList.clear();
        adapter.map.clear();
        adapter = new NativeImgListAdapter(this, imageList);
        native_image_grid.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        native_image_grid = null;
        adapter = null;
        imageList = null;
        ImageLoader.getInstance().clearMemoryCache();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent data = new Intent();
            ArrayList<String> arrayList = new ArrayList<String>();
            Iterator iter = groupList.keySet().iterator();
            while (iter.hasNext()) {
                Object key = iter.next();
                arrayList.add(groupList.get(key));
            }
            data.putExtra("list", arrayList);
            setResult(RESULT_OK, data);
            recycleImage();
            finish();
            System.gc();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		recycleImage();
//		finish();
//		System.gc();
//	}

    public Map<String, String> sortMapByKey(Map<String, String> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, String> sortedMap = new TreeMap<String, String>(new Comparator<String>() {
            public int compare(String key1, String key2) {
                int intKey1 = 0, intKey2 = 0;
                try {
                    intKey1 = getInt(key1);
                    intKey2 = getInt(key2);
                } catch (Exception e) {
                    intKey1 = 0;
                    intKey2 = 0;
                }
                return intKey1 - intKey2;
            }});
        sortedMap.putAll(oriMap);
        return sortedMap;
    }

    private int getInt(String str) {
        int i = 0;
        try {
            Pattern p = Pattern.compile("^\\d+");
            Matcher m = p.matcher(str);
            if (m.find()) {
                i = Integer.valueOf(m.group());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }


}