package com.kumaj.opengl_es_playground.sample;

import android.opengl.GLSurfaceView;
import com.kumaj.opengl_es_playground.render.ShapesRender;

public class RectangleRenderActivity extends BaseRenderActivity {

    private ShapesRender mRender;

    @Override GLSurfaceView.Renderer setupRender(GLSurfaceView glSurfaceView) {
        return mRender = new ShapesRender(glSurfaceView);
    }

    @Override protected void setupGLSurfaceView(GLSurfaceView glSurfaceView) {

    }
}
