package com.naga.filemanager.adapters.glide;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.naga.filemanager.GlideApp;
import com.naga.filemanager.GlideRequest;
import com.naga.filemanager.adapters.data.IconDataParcelable;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Collections;
import java.util.List;

/**
 * @author Emmanuel Messulam <emmanuelbendavid@gmail.com>
 *         on 6/12/2017, at 15:15.
 */

public class RecyclerPreloadModelProvider implements ListPreloader.PreloadModelProvider<IconDataParcelable> {

    private List<IconDataParcelable> urisToLoad;
    private GlideRequest<Drawable> request;

    public RecyclerPreloadModelProvider(@NonNull Fragment fragment, @NonNull List<IconDataParcelable> uris) {
        urisToLoad = uris;
        request = GlideApp.with(fragment).asDrawable().centerCrop();
    }

    @Override
    @NonNull
    public List<IconDataParcelable> getPreloadItems(int position) {
        IconDataParcelable iconData = urisToLoad.get(position);
        if (iconData == null) return Collections.emptyList();
        return Collections.singletonList(iconData);
    }

    @Override
    @Nullable
    public RequestBuilder<Drawable> getPreloadRequestBuilder(IconDataParcelable iconData) {
        RequestBuilder<Drawable> requestBuilder;
        if (iconData.type == IconDataParcelable.IMAGE_FROMFILE) {
            requestBuilder = request.load(iconData.path);
        } else if (iconData.type == IconDataParcelable.IMAGE_FROMCLOUD) {
            requestBuilder = request.load(iconData.path).diskCacheStrategy(DiskCacheStrategy.NONE);
        } else {
            requestBuilder = request.load(iconData.image);
        }
        return requestBuilder;
    }
}
