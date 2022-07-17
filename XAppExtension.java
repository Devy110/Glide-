package com.qnmd.library_base.imageloader;


import androidx.annotation.NonNull;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.qnmd.library_base.base.BaseApplication;
import com.qnmd.library_base.utils.DensityUtil;

@GlideExtension
public class XAppExtension {

    private XAppExtension() {
    } // utility class

    @NonNull
    @GlideOption
    public static BaseRequestOptions<?> circle(BaseRequestOptions<?> options) {
        return options.placeholder(ImageLoader.INSTANCE.getCacheRes_circle()).error(ImageLoader.INSTANCE.getCacheRes_circle()).circleCrop();
    }


    @NonNull
    @GlideOption
    public static BaseRequestOptions<?> portrait(BaseRequestOptions<?> options) {
        return options.placeholder(ImageLoader.INSTANCE.getCacheRes_portrait()).error(ImageLoader.INSTANCE.getCacheRes_portrait()).circleCrop();
    }

    @NonNull
    @GlideOption
    public static BaseRequestOptions<?> round(BaseRequestOptions<?> options, int roundDp) {
        return options.placeholder(ImageLoader.INSTANCE.getCacheRes()).error(ImageLoader.INSTANCE.getCacheRes()).centerCrop().transform(new MultiTransformation(new CenterCrop(),new RoundedCorners(DensityUtil.dpToPx(BaseApplication.instance, roundDp))));
    }

    @NonNull
    @GlideOption
    public static BaseRequestOptions<?> withPlaceHolder(BaseRequestOptions<?> options) {
        return options.placeholder(ImageLoader.INSTANCE.getCacheRes()).error(ImageLoader.INSTANCE.getCacheRes());
    }

    @NonNull
    @GlideOption
    public static BaseRequestOptions<?> blur(BaseRequestOptions<?> options, int sampling) {
        return options.transform(new GlideBlurTransformer(BaseApplication.instance, 90, sampling));
    }

    @NonNull
    @GlideOption
    public static BaseRequestOptions<?> roundCorner(BaseRequestOptions<?> options, XRoundedCorners roundedCorners) {
        return options.placeholder(ImageLoader.INSTANCE.getCacheRes()).error(ImageLoader.INSTANCE.getCacheRes()).transform(new RoundedCornersTransform(BaseApplication.instance, roundedCorners));
    }
}
