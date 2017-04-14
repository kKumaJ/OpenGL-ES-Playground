uniform mat4 u_MVPmatrix;

attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;

varying vec2 v_TextureCoordinates;

void main() {
  gl_postion = u_MVPmatrix * a_Position;
  v_TextureCoordinates = a_TextureCoordinates;
}
