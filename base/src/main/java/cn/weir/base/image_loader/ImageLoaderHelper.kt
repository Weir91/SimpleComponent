package cn.weir.base.image_loader

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import cn.weir.library.GlideApp

/**
 * @author Weir.
 * @date   2019/2/14.
 */
object ImageLoaderHelper {

    fun displayImage(context: Context, url: String, view: ImageView) {
        GlideApp.with(context).load(url).into(view)
    }

    fun displayImage(
        context: Context,
        url: String,
        view: ImageView, @DrawableRes errorRes: Drawable
    ) {
        GlideApp.with(context).load(url).error(errorRes).into(view)
    }
}