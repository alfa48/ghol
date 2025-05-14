package ao.alfa.broGames.android;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ao.alfa.broGames.android.screens.GameScreen;

public class YouGame extends Game {

    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new GameScreen(this)); // Tela do jogo

    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
