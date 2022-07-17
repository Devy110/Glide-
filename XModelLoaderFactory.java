package com.qnmd.library_base.imageloader;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.nio.ByteBuffer;

public class XModelLoaderFactory implements ModelLoaderFactory<String, ByteBuffer> {
    @NonNull
    @Override
    public ModelLoader<String, ByteBuffer> build(@NonNull MultiModelLoaderFactory multiFactory) {
        return new XModelLoader();
    }

    @Override
    public void teardown() {
    }
}
