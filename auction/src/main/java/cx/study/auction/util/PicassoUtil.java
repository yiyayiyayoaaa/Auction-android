package cx.study.auction.util;

import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import cx.study.auction.MainApplication;
import cx.study.auction.R;
import cx.study.auction.contants.HttpRest;
import cx.study.auction.model.rest.http.OkHttp3Downloader;
import cx.study.auction.model.rest.http.OkHttp3Wrapper;

/**
 *
 * Created by malei on 2016/6/29.
 */
public class PicassoUtil {
    private static Picasso picassoWithLastModified = null;
    private static Picasso retryPicassoWithoutLastModified = null;

    public static void show(ImageView imageView, String imageUrl) {
        show(imageView, null, imageUrl);
    }

    public static void show(ImageView imageView, ProgressBar loadingProgress, String imageUrl) {
        if (loadingProgress != null) {
            loadingProgress.setVisibility(View.GONE);
        }
        show(imageView, imageUrl, R.drawable.default_image);
    }



	public static void show(final ImageView targetImg, final String imageUrl, final @DrawableRes int nullImage) {
	    if (targetImg == null) {
		    return;
	    }
	    if (TextUtils.isEmpty(imageUrl)) {
		    targetImg.setImageResource(nullImage);
		    return;
	    }
	    if (picassoWithLastModified == null) {
		    Downloader okHttpDownloader = new OkHttp3Downloader(OkHttp3Wrapper.getImageLastModifiedSupportClientInstance());
		    picassoWithLastModified = new Picasso.Builder(MainApplication.getInstance()).downloader(okHttpDownloader).build();
	    }
	    if (retryPicassoWithoutLastModified == null) {
		    Downloader okHttpDownloader2 = new OkHttp3Downloader(OkHttp3Wrapper.getCachePreferredClientInstance());
		    retryPicassoWithoutLastModified = new Picasso.Builder(MainApplication.getInstance()).downloader(okHttpDownloader2).build();
	    }

		final Uri uri;
//		if (!imageUrl.startsWith(HttpRest.BASE_URL)) {
//			uri = Uri.fromFile(new File(imageUrl));
//		} else {
//			uri = Uri.parse(imageUrl);
//		}
        final String url = HttpRest.BASE_URL + imageUrl;
	    final WeakReference<ImageView> reference = new WeakReference<>(targetImg);
	    //  1st try with Last-Modified support
	    picassoWithLastModified.load(url)
			    .placeholder(R.drawable.pic_loading).error(R.drawable.pic_load_failed)
			    .into(targetImg, new Callback() {

				    @Override
				    public void onSuccess() {

				    }

				    @Override
				    public void onError() {
					    //  retry without Last-Modified support
					    if (reference.get() != null) {
						    final ImageView targetImgAgain = reference.get();
						    retryPicassoWithoutLastModified.load(url)
								    .placeholder(R.drawable.pic_loading).error(R.drawable.pic_load_failed)
								    .into(targetImgAgain, retryPicassoWithoutLastModifiedCallback);
					    }
				    }

				    private Callback retryPicassoWithoutLastModifiedCallback = new Callback() {

					    @Override
					    public void onSuccess() {

					    }

					    @Override
					    public void onError() {
						    if (reference.get() != null) {
							    reference.get().setImageResource(R.drawable.pic_load_failed);
							    Picasso.with(MainApplication.getInstance()).cancelRequest(reference.get());
						    }
					    }

				    };

			    });

    }


}
