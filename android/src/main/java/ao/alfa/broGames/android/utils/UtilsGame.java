package ao.alfa.broGames.android.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class UtilsGame {

    public static float WIDTH = Gdx.graphics.getWidth();
    public static float HEIGHTS = Gdx.graphics.getHeight();

    public static void cleanScreen(){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
