package ch.bfh.sokoban.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * TweenAccessor for fading sprites
 **/
public class SpriteAccessor implements TweenAccessor<Sprite>
{
    public static final int alpha = 0;
    @Override
    public int getValues(Sprite target, int tweenType, float[] returnValues)
    {
        switch(tweenType)
        {
            case alpha:
                returnValues[0] = target.getColor().a;
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Sprite target, int tweenType, float[] newValues)
    {
        switch(tweenType)
        {
            case alpha:
                target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
                break;
            default:
                assert false;
                break;
        }
    }
}
