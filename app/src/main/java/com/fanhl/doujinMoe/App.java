package com.fanhl.doujinMoe;

import android.app.Application;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.fanhl.doujinMoe.util.FileCacheManager;

import java.io.File;

/**
 * Created by fanhl on 15/11/18.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initFresco();
    }

    private void initFresco() {
        File cacheDir = FileCacheManager.getInstance(this).getmCacheDir();

        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder().setBaseDirectoryPath(cacheDir)
                .setBaseDirectoryName("cache")
                .setMaxCacheSize(100 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(10 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(5 * ByteConstants.MB)
                .setVersion(1)
                .build();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this).setMainDiskCacheConfig(diskCacheConfig).build();

        Fresco.initialize(this, config);
    }
}
