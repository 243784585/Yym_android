package com.shengtao.foundation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.shengtao.utils.BitmapUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @date： 15/9/29 18:47
 * @package： com.yym7niu.yym7niu
 * @classname： Img7niuHttpUploadTask.java
 * @description： 7牛图片资源上传自定义任务类
 */
public abstract class Img7niuHttpUploadTask extends AsyncTask<String, Integer, String> {
    private UploadManager uploadManager;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    private static class Parameters {

        public Parameters(String token, String key) {
            this.token = token;
            this.key = key;
        }

        String token;
        String key;
    }

    /**
     * 获取授权信息
     *
     * @param httpUri
     * @return
     */
    private Parameters GetToken(String httpUri) {
        DefaultHttpClient client = new DefaultHttpClient(); // 自己去初始化，通常会自己写一个DefaultHttpClient
        HttpGet get = new HttpGet(httpUri);
        HttpResponse response = null;
        try {
            response = client.execute(get);
            String result = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(result.toString());
//            JSONObject jsonInfo = jsonObject.getJSONObject("info");
            String token = jsonObject.getString("uptoken");
            String key = jsonObject.getString("key");
            return new Parameters(token, key);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行上传图片
     *
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(String... params) {
        try {
            final Parameters parameters = this.GetToken(params[0].toString());
            if (parameters != null) {
                //将图片上传至7牛，7牛返回上传结果
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                String localPath = params[1].toString();
                Bitmap bm = BitmapFactory.decodeFile(localPath); //图片本地路径
                int degree = BitmapUtil.readPictureDegree(localPath);
                Bitmap bitmap = BitmapUtil.rotaingImageView(degree, bm);
                byte[] bytes = BitmapUtil.zoomImage(bitmap);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
//                Log.e("-doInBackground", b.toByteArray().length + "");
//                byte[] bytes = b.toByteArray();
                Log.e("-doInBackground", bytes.length + "");
                uploadManager = new UploadManager();
                uploadManager.put(bytes, parameters.key, parameters.token, new UpCompletionHandler() {
                    @Override
                    public void complete(String s, com.qiniu.android.http.ResponseInfo responseInfo, JSONObject jsonObject) {
                        completeListener(parameters.key); //7牛返回的图片地址
                    }
                }, new UploadOptions(null, null, false, new UpProgressHandler() {
                    @Override
                    public void progress(String s, double v) {
                        Log.e("progress=", v + "");
                        //7牛暂时没有进度
                        //上传进度回调
                    }
                }, null));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新进度
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    /**
     * 图片上传完成后回调
     *
     * @param s
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }

    protected abstract void completeListener(String key);
}
