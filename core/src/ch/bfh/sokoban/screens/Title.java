package ch.bfh.sokoban.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import static com.badlogic.gdx.Input.Keys.*;

public class Title extends MyScreenAdapter
{
    @Override
    public void show()
    {
        super.show();

        //TODO add gui

        InputDetectorMultiplexer inputDetectorMultiplexer = new InputDetectorMultiplexer(
                                                                new InputDetector(
                                                                    ()->new MainMenu().activate(),
                                                                    ENTER),
                                                                new InputDetector(
                                                                        ()->System.out.println("Ready to konami"),
                                                                        UP, UP, DOWN, DOWN, LEFT, RIGHT, LEFT, RIGHT, B, A));

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, inputDetectorMultiplexer));

        Table table = new Table(skin);
        Label temp = new Label("Press Enter to start", skin);

        table.setBounds(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        table.add(temp);

        table.invalidateHierarchy();

        stage.addActor(table);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
    }

    @Override
    public void pause()
    {
        super.pause();
    }

    @Override
    public void resume()
    {
        super.resume();
    }

    @Override
    public void hide()
    {
        super.hide();
    }

    @Override
    public void dispose()
    {
        super.dispose();
    }

    private interface Functer{ void run(); }

    private class InputDetectorMultiplexer implements InputProcessor
    {
        private InputDetector[] detectors;

        public InputDetectorMultiplexer(InputDetector...detectors)
        {
            this.detectors = detectors;
        }

        @Override
        public boolean keyDown(int keycode)
        {
            boolean b = false;
            for (InputDetector dec : detectors)
            {
               b |= dec.keyDown(keycode);
            }
            return b;
        }

        @Override
        public boolean keyUp(int keycode)
        {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }

    private class InputDetector implements InputProcessor
    {
        private Functer after;
        private int[] keys;
        int currentIndex = 0;

        private InputDetector(Functer after, int...keys)
        {
            this.after = after;
            this.keys = keys;
        }

        private void input(int key)
        {
            if(currentIndex<keys.length)
            {
                if(keys[currentIndex] == key)
                {
                    currentIndex++;
                    if(valid())
                        after.run();
                }
                else
                    currentIndex = 0;
            }
            else
                after.run();
        }

        private boolean valid()
        {
            return currentIndex == keys.length;
        }

        @Override
        public boolean keyDown(int keycode)
        {
            input(keycode);
            return false;
        }

        @Override
        public boolean keyUp(int keycode)
        {
            return false;
        }

        @Override
        public boolean keyTyped(char character)
        {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button)
        {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button)
        {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer)
        {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY)
        {
            return false;
        }

        @Override
        public boolean scrolled(int amount)
        {
            return false;
        }
    }
}
