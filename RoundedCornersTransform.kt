package com.qnmd.library_base.imageloader

import android.content.Context
import android.graphics.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import java.security.MessageDigest

data class XRoundedCorners(
    var radius: Float, var leftTop: Boolean = true,
    var rightTop: Boolean = true,
    var leftBottom: Boolean = true,
    var rightBottom: Boolean = true
)

class RoundedCornersTransform(
    context: Context?,
    var roundedCorners: XRoundedCorners
) :
    Transformation<Bitmap?> {
    private val mBitmapPool: BitmapPool = Glide.get(context!!).bitmapPool

    private val id = javaClass.name
    private val idBytes = id.toByteArray(Charsets.UTF_8);

    override fun transform(
        context: Context,
        resource: Resource<Bitmap?>,
        outWidth: Int,
        outHeight: Int
    ): Resource<Bitmap?> {
        val source: Bitmap = resource.get()
        var finalWidth: Int
        var finalHeight: Int
        //输出目标的宽高或高宽比例
        var scale: Float
        if (outWidth > outHeight) {
            //如果 输出宽度 > 输出高度 求高宽比
            scale = outHeight.toFloat() / outWidth.toFloat()
            finalWidth = source.width
            //固定原图宽度,求最终高度
            finalHeight = (source.width.toFloat() * scale).toInt()
            if (finalHeight > source.height) {
                //如果 求出的最终高度 > 原图高度 求宽高比
                scale = outWidth.toFloat() / outHeight.toFloat()
                finalHeight = source.height
                //固定原图高度,求最终宽度
                finalWidth = (source.height.toFloat() * scale).toInt()
            }
        } else if (outWidth < outHeight) {
            //如果 输出宽度 < 输出高度 求宽高比
            scale = outWidth.toFloat() / outHeight.toFloat()
            finalHeight = source.height
            //固定原图高度,求最终宽度
            finalWidth = (source.height.toFloat() * scale).toInt()
            if (finalWidth > source.width) {
                //如果 求出的最终宽度 > 原图宽度 求高宽比
                scale = outHeight.toFloat() / outWidth.toFloat()
                finalWidth = source.width
                finalHeight = (source.width.toFloat() * scale).toInt()
            }
        } else {
            //如果 输出宽度=输出高度
            finalHeight = source.height
            finalWidth = finalHeight
        }

        //修正圆角
        roundedCorners.radius *= finalHeight.toFloat() / outHeight.toFloat()
        val outBitmap: Bitmap = mBitmapPool[finalWidth, finalHeight, Bitmap.Config.ARGB_8888]
        val canvas = Canvas(outBitmap)
        val paint = Paint()
        //关联画笔绘制的原图bitmap
        val shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        //计算中心位置,进行偏移
        val width: Int = (source.width - finalWidth) / 2
        val height: Int = (source.height - finalHeight) / 2
        if (width != 0 || height != 0) {
            val matrix = Matrix()
            matrix.setTranslate((-width).toFloat(), (-height).toFloat())
            shader.setLocalMatrix(matrix)
        }
        paint.shader = shader
        paint.isAntiAlias = true
        val rectF = RectF(0.0f, 0.0f, canvas.width.toFloat(), canvas.height.toFloat())
        //先绘制圆角矩形
        canvas.drawRoundRect(rectF, roundedCorners.radius, roundedCorners.radius, paint)

        //左上角圆角
        if (!roundedCorners.leftTop) {
            canvas.drawRect(0f, 0f, roundedCorners.radius, roundedCorners.radius, paint)
        }
        //右上角圆角
        if (!roundedCorners.rightTop) {
            canvas.drawRect(canvas.width - roundedCorners.radius, 0f, canvas.width.toFloat(), roundedCorners.radius, paint)
        }
        //左下角圆角
        if (!roundedCorners.leftBottom) {
            canvas.drawRect(0f, canvas.height - roundedCorners.radius, roundedCorners.radius, canvas.height.toFloat(), paint)
        }
        //右下角圆角
        if (!roundedCorners.rightBottom) {
            canvas.drawRect(
                canvas.width - roundedCorners.radius,
                canvas.height - roundedCorners.radius,
                canvas.width.toFloat(),
                canvas.height.toFloat(),
                paint
            )
        }
        return BitmapResource.obtain(outBitmap, mBitmapPool)!!
    }

    /** must override */
    override fun equals(other: Any?): Boolean {
        return other is RoundedCornersTransform
    }

    /** must override */
    override fun hashCode(): Int {
        return id.hashCode()
    }

    /** must override */
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(idBytes)
    }
}
