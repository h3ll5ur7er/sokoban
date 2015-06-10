package ch.bfh.sokoban.game;

import ch.bfh.sokoban.GlobalAssets;
import ch.bfh.sokoban.security.Pseudo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Random;

public class Highscore
{
    public static Highscore inst;
    public static final Object padlock = new Object();



    private Highscore()
    {
        map = new ObjectMap<String, LevelHighScore>();

        FileHandle file = Gdx.files.external("Documents/My Games/Sokoban/SokobanHighscores.jshd");
        if(file.exists())
            load(Pseudo.loadMap(file));
    }

    public static void save()
    {
        Pseudo.storeMap(instance().store(), Gdx.files.external("Documents/My Games/Sokoban/SokobanHighscores.jshd"));
    }

    private void load(ObjectMap<String, String> data)
    {
        for (String key : data.keys())
        {
            map.put(key, LevelHighScore.deserialize(data.get(key)));
        }
    }
    private ObjectMap<String, String> store()
    {
        ObjectMap<String, String> data = new ObjectMap<String, String>();
        for (String key : map.keys())
        {
            data.put(key, map.get(key).serialize());
        }
        return data;
    }

    public static Highscore instance()
    {
        if(inst == null)
        {
            synchronized (padlock)
            {
                if(inst == null)
                {
                    inst = new Highscore();
                }
            }
        }
        return inst;
    }

    private static Random rng = new Random();

    private ObjectMap<String, LevelHighScore> map;

    public static void put(String levelid, int score, String name)
    {
        if(!instance().map.containsKey(levelid))
        {
            instance().map.put(levelid, new LevelHighScore(levelid));
        }
        instance().map.get(levelid).add(levelid, score, name);
    }
    public static Table getTable(String levelid)
    {
        Table tbl = new Table(GlobalAssets.getInstance().getSkin());
        LevelHighScore high = get(levelid);

        for (int i = 0; i < 3; i++) {
            Score s = high.get(i);
            tbl.add(""+(i+1)).pad(10);
            tbl.add(s.name).pad(10);
            tbl.add(""+s.score).pad(10);
            tbl.row();
        }
        return tbl;
    }
    public static LevelHighScore get(String levelid)
    {
        if(!instance().map.containsKey(levelid))
            instance().map.put(levelid, new LevelHighScore(levelid));
        return instance().map.get(levelid);

    }

    public static class LevelHighScore
    {
        public final String levelId;
        private Score s1;
        private Score s2;
        private Score s3;

        public LevelHighScore(String id)
        {
            levelId = id;
            s1 = new Score(id, 999999, randomName());
            s2 = new Score(id, 999999, randomName());
            s3 = new Score(id, 999999, randomName());
        }
        private String randomName()
        {
            char[] alpha = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9','0',' '};
            int i1 = rng.nextInt(alpha.length);
            int i2 = rng.nextInt(alpha.length);
            int i3 = rng.nextInt(alpha.length);
            return ""+alpha[i1]+alpha[i2]+alpha[i3];
        }
        public void add(Score score)
        {
            if(score.score<s1.score)
            {
                s3 = s2;
                s2 = s1;
                s1 = score;
            }
            else if(score.score<s2.score)
            {
                s3 = s2;
                s2 = score;
            }
            else if(score.score<s3.score)
            {
                s3 = score;
            }
        }

        public void add(String id, int score, String name)
        {
            add(new Score(id, score, name));
        }
        public Score get(int i)
        {
            switch(i)
            {
                case 0:
                    return s1;
                case 1:
                    return s2;
                case 2:
                    return s3;
                default:
                    return null;
            }
        }

        public String serialize()
        {
            return levelId+";"+s1.serialize()+";"+s2.serialize()+";"+s3.serialize();
        }
        public static LevelHighScore deserialize(String data)
        {
            String[] params = data.split(";");
            LevelHighScore lvl = new LevelHighScore(params[0]);
            lvl.add(Score.deserialize(params[1]));
            lvl.add(Score.deserialize(params[2]));
            lvl.add(Score.deserialize(params[3]));
            return lvl;
        }

        public boolean isHighscore(int score)
        {
            return score<s1.score || score<s2.score || score<s3.score;
        }
    }

    public static class Score
    {
        public final int score;
        public final String levelId, name;
        public Score(String levelId, int score, String name)
        {
            assert name.length() == 3;

            this.levelId = levelId;
            this.score = score;
            this.name = name;
        }

        public Score(Score score)
        {
            this.levelId = score.levelId;
            this.name = score.name;
            this.score = score.score;
        }

        public String serialize()
        {
            return levelId+":"+name+":"+score;
        }

        public static Score deserialize(String data)
        {
            String[] params = data.split(":");
            return new Score(params[0], Integer.parseInt(params[2]), params[1]);
        }
    }
}
