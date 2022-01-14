package be.nilsdhont.driemannen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/** Created by franciscogallardo on 17/8/17. */
public class Dice {

  private FloatBuffer vertexBuffer;
  private FloatBuffer texBuffer;

  private int numFaces = 6;
  private int[] imageFileIDs = {
    R.drawable.dice1,
    R.drawable.dice2,
    R.drawable.dice3,
    R.drawable.dice4,
    R.drawable.dice5,
    R.drawable.dice6
  };

  private int[] textureIDs = new int[numFaces];
  private Bitmap[] bitmap = new Bitmap[numFaces];
  private float diceHalfSize = 1.2f;

  public Dice(Context context) {
    ByteBuffer vbb = ByteBuffer.allocateDirect(12 * 4 * numFaces);
    vbb.order(ByteOrder.nativeOrder());
    vertexBuffer = vbb.asFloatBuffer();

    for (int face = 0; face < numFaces; face++) {

      InputStream is = context.getResources().openRawResource(imageFileIDs[face]);
      try {
        bitmap[face] = BitmapFactory.decodeStream(is);
      } finally {
        try {
          is.close();
          is = null;
        } catch (IOException e) {
        }
      }

      int imgWidth = bitmap[face].getWidth();
      int imgHeight = bitmap[face].getHeight();
      float faceWidth = 2.0f;
      float faceHeight = 2.0f;

      if (imgWidth > imgHeight) {
        faceHeight = faceHeight * imgHeight / imgWidth;
      } else {
        faceWidth = faceWidth * imgWidth / imgHeight;
      }
      float faceLeft = (float) (-faceWidth / 1.62);
      float faceRight = -faceLeft;
      float faceTop = (float) (faceHeight / 1.62);
      float faceBottom = -faceTop;

      float[] vertices = {
        faceLeft, faceBottom, 0.0f,
        faceRight, faceBottom, 0.0f,
        faceLeft, faceTop, 0.0f,
        faceRight, faceTop, 0.0f,
      };
      vertexBuffer.put(vertices);
    }
    vertexBuffer.position(0);

    float[] texCoords = {
      0.0f, 1.0f,
      1.0f, 1.0f,
      0.0f, 0.0f,
      1.0f, 0.0f
    };
    ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4 * numFaces);
    tbb.order(ByteOrder.nativeOrder());
    texBuffer = tbb.asFloatBuffer();
    for (int face = 0; face < numFaces; face++) {
      texBuffer.put(texCoords);
    }
    texBuffer.position(0);
  }

  public void draw(GL10 gl) {
    gl.glFrontFace(GL10.GL_CCW);

    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);

    gl.glPushMatrix();
    gl.glTranslatef(0f, 0f, diceHalfSize);
    gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);
    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
    gl.glPopMatrix();

    gl.glPushMatrix();
    gl.glRotatef(270.0f, 0f, 1f, 0f);
    gl.glTranslatef(0f, 0f, diceHalfSize);
    gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[1]);
    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
    gl.glPopMatrix();

    gl.glPushMatrix();
    gl.glRotatef(180.0f, 0f, 1f, 0f);
    gl.glTranslatef(0f, 0f, diceHalfSize);
    gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[2]);
    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
    gl.glPopMatrix();

    gl.glPushMatrix();
    gl.glRotatef(90.0f, 0f, 1f, 0f);
    gl.glTranslatef(0f, 0f, diceHalfSize);
    gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[3]);
    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
    gl.glPopMatrix();

    gl.glPushMatrix();
    gl.glRotatef(270.0f, 1f, 0f, 0f);
    gl.glTranslatef(0f, 0f, diceHalfSize);
    gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[4]);
    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
    gl.glPopMatrix();

    gl.glPushMatrix();
    gl.glRotatef(90.0f, 1f, 0f, 0f);
    gl.glTranslatef(0f, 0f, diceHalfSize);
    gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[5]);
    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
    gl.glPopMatrix();

    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
  }

  public void loadTexture(GL10 gl) {
    gl.glGenTextures(6, textureIDs, 0);

    for (int face = 0; face < numFaces; face++) {
      gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[face]);
      gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
      gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

      GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap[face], 0);
    }
  }
}
