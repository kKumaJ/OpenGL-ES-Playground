package com.kumaj.opengl_es_playground.sample;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.kumaj.opengl_es_playground.R;
import com.kumaj.opengl_es_playground.utils.OpenGLUtils;

public abstract class BaseRenderActivity extends AppCompatActivity {
    private static final String TAG = "BaseRenderActivity";
    private boolean mIsRenderSet;
    private GLSurfaceView.Renderer mRender;
    private GLSurfaceView mGlSurfaceView;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_render);
        if (OpenGLUtils.isSupportsEs2(this)) {
            mGlSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
            mGlSurfaceView.setEGLContextClientVersion(2); //must be set
            mGlSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
            mRender = setupRender(mGlSurfaceView);
            mGlSurfaceView.setRenderer(mRender);
            setupGLSurfaceView(mGlSurfaceView);
            mIsRenderSet = true;
        } else {
            Log.e(TAG, "opengl es 2.0 not supported");
            return;
        }
    }

    abstract GLSurfaceView.Renderer setupRender(GLSurfaceView glSurfaceView);

    protected void setupGLSurfaceView(GLSurfaceView glSurfaceView){

    }

    @Override protected void onResume() {
        super.onResume();
        if (mIsRenderSet) {
            mGlSurfaceView.onResume();
        }
    }

    @Override protected void onPause() {
        super.onPause();
        if (mIsRenderSet) {
            mGlSurfaceView.onPause();
        }
    }
}
