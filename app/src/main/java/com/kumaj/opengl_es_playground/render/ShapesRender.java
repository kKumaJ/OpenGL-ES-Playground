package com.kumaj.opengl_es_playground.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import com.kumaj.opengl_es_playground.R;
import com.kumaj.opengl_es_playground.utils.LoggerConfig;
import com.kumaj.opengl_es_playground.utils.ShaderHelper;
import com.kumaj.opengl_es_playground.utils.TextResourceReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_LINE_LOOP;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static com.kumaj.opengl_es_playground.utils.Constants.BYTES_PER_FLOAT;

public class ShapesRender implements GLSurfaceView.Renderer {

    private static final String TAG = "RectangleRender";

    private static final String U_MATRIX = "u_Matrix"; // mvpMatrix
    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) *
        BYTES_PER_FLOAT;

    private static final float[] VERTEX = {
        // Order of coordinates: X, Y, R, G, B

        // Triangle Fan
        -0.5f, 0.5f, 1f, 0f, 0f, //1
        -0.75f, 0.25f, 0f, 1f, 0f, //2
        -0.25f, 0.25f, 0f, 0f, 1f, //3
        -0.25f, 0.75f, 0f, 1f, 0f, //4
        -0.75f, 0.75f, 0f, 0f, 1f, //5
        -0.75f, 0.25f, 0f, 1f, 0f, //6

        //  Triangle
        0.5f, 0.5f, 1f, 0f, 0f, //1
        0.25f, 0.25f, 0f, 1f, 0f, //2
        0.75f, 0.25f, 0f, 0f, 1f, //3

        // Triangle Fan Line Loop
        -0.5f, -0.5f, 1f, 0f, 0f, //1
        -0.75f, -0.75f, 0f, 1f, 0f, //2
        -0.25f, -0.75f, 0f, 0f, 1f, //3
        -0.25f, -0.25f, 0f, 1f, 0f, //4
        -0.75f, -0.25f, 0f, 0f, 1f, //5
        -0.75f, -0.75f, 0f, 1f, 0f, //6

        // Triangle Fan Line Loop
        0.5f, -0.5f, 1f, 0f, 0f, //1
        0.25f, -0.75f, 0f, 1f, 0f, //2
        0.75f, -0.75f, 0f, 0f, 1f, //3

        // Base Line
        -1f, 0f, 1f, 0f, 0f,//7
        1f, 0f, 1f, 0f, 0f,//8
        0f, 1f, 1f, 0f, 0f,//9
        0f, -1f, 1f, 0f, 0f,//10

        //(-1,0)
        -1f, 0f, 0f, 0f, 1f, //11
        //(-0.5,0)
        -0.5f, 0f, 0f, 0f, 1f, //12
        //(0,0)
        0f, 0f, 0f, 0f, 1f, //13
        //(0.5,0)
        0.5f, 0f, 0f, 0f, 1f, //14
        //(1,0)
        1f, 0f, 0f, 0f, 1f, //15
        //(0,-1)
        0f, -1f, 0f, 0f, 1f, //16
        //(0,-0.5)
        0f, -0.5f, 0f, 0f, 1f, //17
        //(0,0)
        //(0,0.5)
        0f, 0.5f, 0f, 0f, 1f, //18
        //(0,1)
        0f, 1f, 0f, 0f, 1f, //19
    };

    //绘制顺序
    private static final short[] VERTEX_INDEX = { 0, 1, 2, 2, 0, 3 };

    private Context mContext;
    private final FloatBuffer mVertexBuffer;

    //private final ShortBuffer mVertexIndexBuffer;

    private int mProgram;
    private int aPositionLocation;
    private int aColorLocation;
    private int uMatrixLocation;

    public ShapesRender(GLSurfaceView glSurfaceView) {
        mContext = glSurfaceView.getContext();

        mVertexBuffer = ByteBuffer.allocateDirect(VERTEX.length * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(VERTEX);
        mVertexBuffer.position(0);

        // mVertexIndexBuffer = ByteBuffer.allocateDirect(VERTEX_INDEX.length * BYTES_PER_SHORT)
        //     .order(ByteOrder.nativeOrder())
        //     .asShortBuffer()
        //     .put(VERTEX_INDEX);
        // mVertexIndexBuffer.position(0);
    }

    @Override public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.e(TAG, "onSurfaceCreated");
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        String vertexResource = TextResourceReader.readTextFileFromResource(mContext,
            R.raw.simple_vertex_shader);
        String fragmentResource = TextResourceReader.readTextFileFromResource(mContext,
            R.raw.simple_fragment_shader);

        int vertexResourceId = ShaderHelper.compileVertexShader(vertexResource);
        int fragmentResourceId = ShaderHelper.compileFragmentShader(fragmentResource);

        mProgram = ShaderHelper.linkProgram(vertexResourceId, fragmentResourceId);

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(mProgram);
        }

        glUseProgram(mProgram);

        //属性位置
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        aColorLocation = glGetAttribLocation(mProgram, A_COLOR);
        //uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);

        mVertexBuffer.position(0);
        //在 mVertexBuffer 中能找到 a_Position 对应的数据
        //2个分量vec2
        //数据类型
        //STRIDE 一个数组存储多个属性时
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE,
            mVertexBuffer);
        //使能属性
        glEnableVertexAttribArray(aPositionLocation);

        mVertexBuffer.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE,
            mVertexBuffer);
        glEnableVertexAttribArray(aColorLocation);
    }

    @Override public void onSurfaceChanged(GL10 gl, int width, int height) {
        //左下角原点，width,height
        glViewport(0, 0, width, height);

        // float ratio = (float) height / width;
        // //投影变换  p_matrix
        // Matrix.frustumM(mProjectionMatrix, 0, -1, 1, -ratio, ratio, 3, 7);
        // //视图变换 v_matrix
        // Matrix.setLookAtM(mCameraMatrix, 0, 0, 0, 3, 0, 0, 0, 0, 1, 0);
        // Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mCameraMatrix, 0);
    }

    @Override public void onDrawFrame(GL10 gl) {
        // 表明需要清除的缓冲 http://blog.csdn.net/shuaihj/article/details/7230138
        glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        glDrawArrays(GL_TRIANGLE_FAN, 6, 3);

        glDrawArrays(GL_LINE_LOOP, 9, 6);

        glDrawArrays(GL_LINE_LOOP, 15, 3);

        // Draw X line
        glDrawArrays(GL_LINES, 18, 2);

        //Draw Y line
        glDrawArrays(GL_LINES, 20, 2);

        //Draw points
        for (int i = 22; i < 31; i++) {
            glDrawArrays(GL_POINTS, i, 1);
        }
    }

}
