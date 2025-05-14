package ao.alfa.broGames.android.lego;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player {
    public float x;
    public float y;
    public float width;
    public float height;

    public Player(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.rect(this.x, this.y, this.width, this.height);
    }

    public boolean isTouchedPlayer(float touchX, float touchY) {
        return touchX >= this.x && touchX <= this.x + this.width &&
            touchY >= this.y && touchY <= this.y + this.height;
    }

}
