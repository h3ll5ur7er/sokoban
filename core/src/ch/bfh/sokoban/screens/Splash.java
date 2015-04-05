package ch.bfh.sokoban.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import ch.bfh.sokoban.tweens.SpriteAccessor;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Splash extends MyScreenAdapter
{
    SpriteBatch batch;
    Texture tex;
    Sprite sprite;
    TweenManager mgr;

    @Override
    public void show()
    {
        super.show();
        batch = new SpriteBatch();

        mgr  = new TweenManager();

        tex = new Texture("img/splash.png");
        sprite = new Sprite(tex);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        Tween
                .set(sprite, SpriteAccessor.alpha)
                .target(0)
                .start(mgr);
        Tween
                .to(sprite, SpriteAccessor.alpha, 2)
                .target(1)
                .repeatYoyo(1, 0.5f)
                .setCallback(
                        (t,s) -> new MainMenu().activate())
                .start(mgr);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mgr.update(delta);

        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height)
    {
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void dispose()
    {
        tex.dispose();
        //batch.dispose();
    }
}
