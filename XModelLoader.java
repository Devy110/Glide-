package com.qnmd.library_base.imageloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;

import java.nio.ByteBuffer;

public class XModelLoader implements ModelLoader<String, ByteBuffer> {
    @Nullable
    @Override
    public LoadData<ByteBuffer> buildLoadData(@NonNull String s, int width, int height, @NonNull Options options) {
        return new LoadData(new ObjectKey(s),new XDataFetcher(s));
    }

    @Override
    public boolean handles(@NonNull String s) {
        return s.contains(".enc") || s.contains(".bnc");
    }
}
