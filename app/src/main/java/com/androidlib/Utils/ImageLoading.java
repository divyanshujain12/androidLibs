package com.androidlib.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.locationlib.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ImageLoading {

    private ImageLoadingListener imageListener;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    ProgressBar imageProgress;

    HashMap<ImageView, ProgressBar> imageProgressBar;

    public ImageLoading(Context context) {
        // TODO Auto-generated constructor stub

        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.mipmap.ic_launcher)
                .showStubImage(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher).cacheInMemory()
                .cacheOnDisc().build();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        imageListener = new ImageDisplayListener();

        imageProgressBar = new HashMap<ImageView, ProgressBar>();
    }

    public ImageLoading(Context context, boolean addInCache) {
        // TODO Auto-generated constructor stub

        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.noimage)
                .showStubImage(R.drawable.noimage)
                .showImageForEmptyUri(R.drawable.noimage).build();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        imageListener = new ImageDisplayListener();

        imageProgressBar = new HashMap<ImageView, ProgressBar>();
    }

    public ImageLoading(Context context, int radius) {
        // TODO Auto-generated constructor stub

        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.noimage)
                .showStubImage(R.drawable.noimage)
                .showImageForEmptyUri(R.drawable.noimage).displayer(new RoundedBitmapDisplayer(radius)).cacheInMemory()
                .cacheOnDisc().build();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageListener = new ImageDisplayListener();
        imageProgressBar = new HashMap<ImageView, ProgressBar>();
    }

    public void LoadImage(String Url, ImageView imageView,
                          ProgressBar imageProgress) {
        imageProgressBar.put(imageView, imageProgress);
        imageLoader.displayImage(Url, imageView, options, imageListener);
    }

    private class ImageDisplayListener extends
            SimpleImageLoadingListener {

        final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            imageProgress = imageProgressBar.get(view);

            if (imageProgress != null) {
                imageProgress.setVisibility(View.GONE);
                imageProgressBar.remove(view);
                ((RelativeLayout) imageProgress.getParent()).removeView(imageProgress);
            }
            if (loadedImage != null) {

                ImageView imageView = (ImageView) view;

                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }

        @Override
        public void onLoadingFailed(String imageUri, View view,
                                    FailReason failReason) {
            // Empty implementation
            ((ImageView) view).setImageResource(R.drawable.noimage);
            imageProgress = imageProgressBar.get(view);
            if (imageProgress != null) {
                imageProgress.setVisibility(View.GONE);
                imageProgressBar.remove(view);
                ((RelativeLayout) imageProgress.getParent()).removeView(imageProgress);
            }
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            // Empty implementation
            imageProgress = imageProgressBar.get(view);
            if (imageProgress != null) {
                imageProgress.setVisibility(View.GONE);
                imageProgressBar.remove(view);
                ((RelativeLayout) imageProgress.getParent()).removeView(imageProgress);
            }
        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            imageProgress = imageProgressBar.get(view);
            if (imageProgress != null) {
                imageProgress.setVisibility(View.VISIBLE);
            }
        }
    }
}
