How to use:
In your activity:

    private void initImageFetcher() {
        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(this, getResources().getDimensionPixelSize(R.dimen.team_logo_size));
        mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
        mImageFetcher.setImageFadeIn(false);
    }


    public ImageFetcher getImageFetcher() {
        if (mImageFetcher == null) {
            initImageFetcher();
        }
        return mImageFetcher;
    }

Somewhere:

    getImageFetcher.loadImage(url, imageView);
