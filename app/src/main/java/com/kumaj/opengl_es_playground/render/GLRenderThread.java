package com.kumaj.opengl_es_playground.render;

import android.opengl.GLSurfaceView;

public abstract class GLRenderThread extends Thread {
    protected GLSurfaceView mGlSurfaceView;
    protected ShapesRender mRectRender;

    public GLRenderThread(GLSurfaceView glSurfaceView, ShapesRender rectRender) {
        this.mGlSurfaceView = glSurfaceView;
        this.mRectRender = rectRender;
    }
}
