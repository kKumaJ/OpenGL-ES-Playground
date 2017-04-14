package com.kumaj.opengl_es_playground.sample;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.kumaj.opengl_es_playground.R;
import com.kumaj.opengl_es_playground.render.TextureRender;

public class TextureRenderActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setRenderer(new TextureRender());
        setContentView(mGLSurfaceView);
    }
}
