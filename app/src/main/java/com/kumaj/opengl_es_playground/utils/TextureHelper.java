package com.kumaj.opengl_es_playground.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.support.annotation.DrawableRes;
import android.util.Log;
import com.kumaj.opengl_es_playground.R;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;

public final class TextureHelper {

    private static final String TAG = "TextureHelper";

    public static int loadTexture(Context context, @DrawableRes int resId) {
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "could not generate a new OpenGL texture object");
            }
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resId,
            options);

        if(bitmap == null){
            if(LoggerConfig.ON){
                Log.w(TAG,"Resource ID " + resId + " could not be decode");
            }
            glDeleteTextures(1,textureObjectIds,0);
            return 0;
        }
        //后面的纹理调用，应该运用于这个纹理对象
        glBindTexture(GL_TEXTURE_2D,textureObjectIds[0]);

        //缩小情况 三线性过滤
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR_MIPMAP_LINEAR);
        //放大情况 双线性过滤
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
        GLUtils.texImage2D(GL_TEXTURE_2D,0,bitmap,0);
        bitmap.recycle();

        //生成mip贴图
        glGenerateMipmap(GL_TEXTURE_2D);

        //解除绑定
        glBindTexture(GL_TEXTURE_2D,0);

        return textureObjectIds[0];
    }
}
