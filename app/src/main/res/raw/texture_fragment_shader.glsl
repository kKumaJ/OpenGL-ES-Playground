#version 120
precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinate;

void main() {
  gl_FragColor = texture2D(u_TextureUnit,v_TextureCoordinate);
}
