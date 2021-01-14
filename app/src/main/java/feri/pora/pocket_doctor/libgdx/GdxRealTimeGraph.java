package feri.pora.pocket_doctor.libgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GdxRealTimeGraph extends ApplicationAdapter {
    SpriteBatch batch;
    private BitmapFont font;


    @Override
    public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLUE);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        //batch.draw(img, 0, 0);
        font.getData().setScale(6.0f);
        font.draw(batch, "Hello World from libgdx running in a fragment! :)", 100, 300);

        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();
    }
}

