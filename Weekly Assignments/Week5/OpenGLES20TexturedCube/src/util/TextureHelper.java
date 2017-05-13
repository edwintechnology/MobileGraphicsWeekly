package util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import static android.opengl.GLES20.*;
import static android.opengl.GLUtils.*;

/**
 * Created by Todd on 1/5/14.
 */
public class TextureHelper {

    public static int loadTexture(Context context, int resourceId) {
        final int[] textureObjectIds = new int [1];

        glGenTextures(1, textureObjectIds, 0);

        // decompress the image into a bitmap
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        if(bitmap == null) {
            glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }

        // bind the texture
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        // set the texture filters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // load the bitmap data
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        // release the bitmap for garbage collection
        bitmap.recycle();

        // generate mipmaps
        glGenerateMipmap(GL_TEXTURE_2D);

        // unbind from the current texture (passing 0 will unbind the current texture)
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }
}
