package com.qnmd.library_base.imageloader;

import android.app.ActivityManager;
import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.nio.ByteBuffer;

@GlideModule
public class XAppGlideModule extends AppGlideModule {


    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//
//        if (null != activityManager) {
//            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
//            activityManager.getMemoryInfo(memoryInfo);
//        }
//        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
//        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
//        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
//        int defaultArrayPoolSize = calculator.getArrayPoolSizeInBytes();
//
        if (null != activityManager) {
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            builder.setDefaultRequestOptions(
                    new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                            .format(memoryInfo.lowMemory ? DecodeFormat.PREFER_RGB_565 : DecodeFormat.PREFER_ARGB_8888));
        }
//
//        builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize / 2));
//        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize / 2));
//        builder.setArrayPool(new LruArrayPool(defaultArrayPoolSize / 2));

        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .setBitmapPoolScreens(3)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
        builder.setBitmapPool(new LruBitmapPool(calculator.getBitmapPoolSize()));

//        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context));
//        builder.setDefaultTransitionOptions(Drawable.class, DrawableTransitionOptions.withCrossFade());
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.prepend(String.class, ByteBuffer.class,new XModelLoaderFactory());
    }
}
