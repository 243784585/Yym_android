package com.shengtao.nativeimg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shengtao.R;
import com.shengtao.domain.Config;
import com.shengtao.foundation.AsyncHttpTask;
import com.shengtao.foundation.Img7niuHttpUploadTask;
import com.shengtao.utils.LYDBHeader;
import com.shengtao.utils.Session;
import com.shengtao.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class SingleImageActivity extends Activity {
    GridView single_image_grid;
    SingleImgListAdapter adapter;
    private final static int SCAN_OK = 1;
    ArrayList<String> imageList = new ArrayList<String>();
    private static final int CAMERA_REQUEST_CODE = 1;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);

        imageList.add("");
        initHeader();
        initView();
        getImages();
    }

    private void initHeader() {
        LYDBHeader header = (LYDBHeader) findViewById(R.id.single_img_header);
        HashMap<String, Object> headMap = new HashMap<String, Object>();
        headMap.put("left", "left");
        headMap.put("center", "选择照片");
        header.headerLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("path", "");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        header.setHeaderView(SingleImageActivity.this, headMap);
    }

    private void initView() {


        single_image_grid = (GridView) findViewById(R.id.single_image_grid);
        single_image_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                } else {
                    //一起上传方案--返回到上级页面
//                    Intent intent = new Intent();
//                    intent.putExtra("path", view.findViewById(R.id.single_child_image).getTag().toString());
//                    setResult(RESULT_OK, intent);
                    //单独上传方案
                    pd = new ProgressDialog(SingleImageActivity.this);
                    pd.setCanceledOnTouchOutside(false);
                    pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    });
                    pd.setMessage(getString(R.string.Is_upload));
                    pd.show();
                    String path = view.findViewById(R.id.single_child_image).getTag().toString();
                    upLoad(path);
                }
            }
        });
    }

    //上传数据到后台
    private void upLoad(String path) {
        //查询图片localPath
        Img7niuHttpUploadTask uploadTask = new Img7niuHttpUploadTask() {
            /**
             * 7牛上传成功的回调函数，看这个Img7niuHttpUploadTask类就明白了
             * @param key
             */
            @Override
            protected void completeListener(String key) {
                //完成上传 Key：7牛返回的图片地址相关
                //拼接图片url
                String url = (Config.HTTP + key).replace("?", "%3F");
                Message msg = mHandler.obtainMessage();
                msg.what = 2;
                msg.obj = url;
                mHandler.sendMessage(msg);

                //这一步是已经将图片url上传到7牛了？图片已经上传成功，并且已经将有效的可以访问的url地址返回了
            }
        };
        uploadTask.execute("http://112.126.75.199/xingluo/qiniu.do?uptoken", path);
        //获取参数信息
    }

    private Handler mHandler = new Handler() {

        private String url;

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    adapter = new SingleImgListAdapter(SingleImageActivity.this, imageList, single_image_grid);
                    single_image_grid.setAdapter(adapter);
                    break;
                case 2:
                    url = msg.obj.toString();
                    RequestParams reqParams = new RequestParams();
                    reqParams.put("headImg", url);
                    AsyncHttpTask.post(Config.HTTP_URL_HEAD+"user/updateInfo",reqParams, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                String code = response.getString("code");
                                if("0".equals(code)){
                                    pd.dismiss();
                                    Session.SetString("headimg",url);
                                    finish();
                                }else{
                                    ToastUtil.showTextToast(response.optString("error"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
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
                ContentResolver mContentResolver = SingleImageActivity.this.getContentResolver();
                //只查询jpeg和png的图片

                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
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
                //通知Handler扫描图片完成
                mHandler.sendEmptyMessage(SCAN_OK);
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == CAMERA_REQUEST_CODE) {

//                pd = new ProgressDialog(SingleImageActivity.this);
//                pd.setCanceledOnTouchOutside(false);
//                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//
//                    }
//                });
//                pd.setMessage(getString(R.string.Is_upload));
//                pd.show();
                String name = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date()) + ".jpg";
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式


                FileOutputStream b = null;
                String pathName = "";
                if (HomeCircleTools.hasSdcard()) {
                    pathName = Environment.getExternalStorageDirectory() + "/yym/";
                    File file = new File(pathName);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    pathName += "photo/";
                } else {
                    pathName = HomeCircleTools.getAbsolutePath(this) + "/photo/";
                }
                File file = new File(pathName);
                //
//                startImageZoom(Uri.fromFile(file));
                //
                if (!file.exists()) {
                    file.mkdirs();// 创建文件夹
                }
                pathName += name;
                try {
                    b = new FileOutputStream(pathName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                    Intent intent = new Intent();
                    intent.putExtra("path", pathName);
                    setResult(RESULT_OK, intent);

//                      upLoad(pathName);
//                    finish();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        b.flush();
                        b.close();
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//            }else if (requestCode == 3) {
//                if (data == null) {
//                    return;
//                }
//                String name = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date()) + ".jpg";
//                Bundle bundle = data.getExtras();
//                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
//
//
//                FileOutputStream b = null;
//                String pathName = "";
//                if (HomeCircleTools.hasSdcard()) {
//                    pathName = Environment.getExternalStorageDirectory() + "/yym/";
//                    File file = new File(pathName);
//                    if (!file.exists()) {
//                        file.mkdirs();
//                    }
//                    pathName += "photo/";
//                } else {
//                    pathName = HomeCircleTools.getAbsolutePath(this) + "/photo/";
//                }
//                File file = new File(pathName);
//
//                if (!file.exists()) {
//                    file.mkdirs();// 创建文件夹
//                }
//                pathName += name;
//                try {
//                    b = new FileOutputStream(pathName);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
//                    Intent intent = new Intent();
//                    intent.putExtra("path", pathName);
//                    setResult(RESULT_OK, intent);
////                    upLoad("file:///" + pathName);
////                    Session.SetString("headimg","file:///" + pathName);
//
//                    finish();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        b.flush();
//                        b.close();
//                        finish();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
                //sendImage(bm);
            }
        }
    }


    //剪裁
//    private void startImageZoom(Uri uri) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, 3);
//    }

    @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.putExtra("path", "");
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}