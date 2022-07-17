package com.qnmd.library_base.imageloader;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import java.nio.ByteBuffer;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class XDataFetcher implements DataFetcher<ByteBuffer> {

    private final String model;

    public XDataFetcher(String model) {
        this.model = model;
    }

    private Call call;

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super ByteBuffer> callback) {
        try {
            Request request = new Request.Builder().url(model)
                    .addHeader("referer", ImageLoader.INSTANCE.getReferer())
                    .build();
            call = new OkHttpClient.Builder().build().newCall(request);
            Response response = call.execute();
            if (response.code() == 200) {
                if (model.contains(".enc")) {
                    String responseBody = response.body().string().replace(" ", "");
                    HashMap<String, String> keymap = ImageLoader.INSTANCE.getKeyMap();
                    for (String key : keymap.keySet()) {
                        responseBody = responseBody.replace(keymap.get(key), key);
                    }
                    callback.onDataReady(ByteBuffer.wrap(Hex.decode(responseBody)));
                } else if (model.contains(".bnc")) {
                    byte[] result = ImageLoader.INSTANCE.decryptOrigin(response.body().bytes(), ImageLoader.INSTANCE.getAES_KEY());
                    callback.onDataReady(ByteBuffer.wrap(result));
                } else {
                    callback.onDataReady(ByteBuffer.wrap(response.body().bytes()));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onLoadFailed(e);
            Log.e("TAG", "loadData: " + model + "---" + e.toString());
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void cancel() {
        if (call != null && !call.isCanceled()) call.cancel();
    }

    @NonNull
    @Override
    public Class<ByteBuffer> getDataClass() {
        return ByteBuffer.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }

}
