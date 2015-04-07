package ch.bfh.sokoban.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import ch.bfh.sokoban.tweens.SpriteAccessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Splash screen that fades in and out a image and navigates to a given screen
 **/
public class Splash<T extends MyScreenAdapter> extends MyScreenAdapter
{
    private SpriteBatch batch;
    private Texture tex;
    private Sprite sprite;
    private TweenManager mgr;
    private String imagePath;
    private float fade;
    private float pause;
    private Class<T> target;

    /**
     * Creates a new splash screen, that shows given image for given time and
     * navigates to given screen after the animation has terminated
     * @param imagePath image to show
     * @param fade seconds to fade in and out (2x in total)
     * @param pause time to show the splash bevore fading out
     * @param target target screen to navigate to
     */
    public Splash(String imagePath, float fade, float pause, Class<T> target)
    {
        this.imagePath = imagePath;
        this.fade = fade;
        this.pause = pause;
        this.target = target;
    }

    /**
     * Launches the tween animation as soon as the screen shows
     */
    @Override
    public void show()
    {
        super.show();
        batch = new SpriteBatch();

        mgr  = new TweenManager();

        tex = new Texture(imagePath);
        sprite = new Sprite(tex);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        Tween
                .set(sprite, SpriteAccessor.alpha)
                .target(0)
                .start(mgr);
        Tween
                .to(sprite, SpriteAccessor.alpha, fade)
                .target(1)
                .repeatYoyo(1, pause)
                .setCallback(
                        (t, s) ->
                        {
                            try
                            {
                                //Create a new instance of the target screen and navigate to it;
                                target.newInstance().activate();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();

                                assert false; //Die if navigation fails
                            }
                        }
                )
                .start(mgr);
    }

    /**
     * Simply rendering the image, tweenMgr does the magic for us
     **/
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
        sprite.setSize(width, height);
    }

    @Override
    public void dispose()
    {
        super.dispose();
        tex.dispose();
    }
}
