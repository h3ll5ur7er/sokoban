package ch.bfh.sokoban.game;

import ch.bfh.sokoban.data.LevelData;
import ch.bfh.sokoban.pathfinding.AStarPathFinder;
import ch.bfh.sokoban.pathfinding.Mover;
import ch.bfh.sokoban.pathfinding.Path;
import ch.bfh.sokoban.pathfinding.TileBasedMap;
import ch.bfh.sokoban.screens.LevelSelection;
import ch.bfh.sokoban.screens.Settings;
import ch.bfh.sokoban.screens.Splash;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.Stack;

import static ch.bfh.sokoban.game.Constants.*;

/**
 * The level class contains all the game logic:
 * -  Creation,
 * -  Movement
 * -  Pathfinding for Automove
 * THE MONSTER (so far.. to be refactored)
 *
 **/
public class Level extends Actor implements TileBasedMap
{
    private int width;
    private int height;
    int tileSize;
    private final String levelId;

    private String stepsString = "";
    private Stack<Commands> steps;
    private Stack<Commands> undone;

    private Tile[][] tiles;

    boolean [][] visited;
    Path currentPath;
    int pathIndex;
    int walkCounter;
    int walkRate = 10;
    boolean moving = false;
    boolean walking = false;
    boolean undoing = false;
    boolean gettingTextInput = false;
    boolean terminating = false;

    private int moves = 0;
    private int pushes = 0;
    private int undoRedo = 0;
    private int score = 0;

    private Tile player;
    private ArrayList<Tile> goals;

    Table table;
    Skin skin;
    private boolean saved = false;

    public Level(String id, String name, int width, int height, String[] data, Skin skin)
    {
        this.levelId = id;
        this.width = width;
        this.height = height;

        tileSize = Settings.getInt("TileSize");

        this.skin = skin;

        goals = new ArrayList<Tile>();

        tiles = new Tile[height][];

        steps = new Stack<Commands>();
        undone = new Stack<Commands>();

        for (int y = data.length-1; y >= 0; y--)
        {
            tiles[(data.length-1)-y] = new Tile[width];
            char[] chars = data[y].toCharArray();
            for (int x = 0; x < data[y].length(); x++)
            {
                Tile t = new Tile(chars[x], x, y);
                tiles[(data.length-1)-y][x] = t;
                if(t.isPlayer())
                {
                    player = t;
                }
                if(t.isGoal())
                {
                    goals.add(t);
                }
            }
        }

        for (int x = 1; x < width-1; x++)
        {
            for (int y = 1; y < height-1; y++)
            {
                link(x,y);
            }
        }

        for (int x = 1; x < width-1; x++)
        {
            for (int y = 1; y < height-1; y++)
            {
                link(x,y);
            }
        }

        Table t = new Table(skin);

        for (int y = height-1; y >= 0; y--)
        {
            for (int x = 0; x < width; x++)
            {
                t.add(tiles[y][x]);
            }
            t.row();
        }

        t.invalidateHierarchy();

        table = t;

        Gdx.graphics.setTitle(name);
        if(Settings.get("StoredLevel").startsWith(id))
        {
            String storedPath = Settings.get("StoredLevel").substring(id.length()+1);

            for (int i = 0; i<storedPath.length();i++)
            {
                walk(storedPath.charAt(i));
            }
        }

    }

    private void walk(char c)
    {
        switch(c)
        {
            case 'u':
            case 'U':
                up();
                break;
            case 'd':
            case 'D':
                down();
                break;
            case 'l':
            case 'L':
                left();
                break;
            case 'r':
            case 'R':
                right();
                break;
            case '<':
                undo();
                break;
            case '>':
                redo();
                break;
        }
    }

    public Level(LevelData lvl, Skin skin)
    {
        this(lvl.id, lvl.name, lvl.width, lvl.height, lvl.data, skin);
    }

    /**
     * Up command, move up, push up or do nothing if blocked
     **/
    public void up()
    {
        if(terminating) return;
        moving = true;
        if(player.canMove(Directions.up))
        {
            player = player.move(Directions.up);
            pushCommand(Commands.MoveUp);
            stepsString+='u';
            moves++;
        }
        else if (player.canPush(Directions.up))
        {
            player = player.push(Directions.up);
            pushCommand(Commands.PushUp);
            stepsString+='U';
            pushes++;
        }
        moving = false;
        undoing = false;
    }

    /**
     * Down command, move down, push down or do nothing if blocked
     **/
    public void down()
    {
        if(terminating) return;
        moving = true;
        if(player.canMove(Directions.down))
        {
            player = player.move(Directions.down);
            pushCommand(Commands.MoveDown);
            stepsString+='d';
            moves++;
        }
        else if (player.canPush(Directions.down))
        {
            player = player.push(Directions.down);
            pushCommand(Commands.PushDown);
            stepsString+='D';
            pushes++;
        }
        moving = false;
        undoing = false;
    }

    /**
     * Left command, move left, push left or do nothing if blocked
     **/
    public void left()
    {
        if(terminating) return;
        moving = true;
        if(player.canMove(Directions.left))
        {
            player = player.move(Directions.left);
            pushCommand(Commands.MoveLeft);
            stepsString+='l';
            moves++;
        }
        else if (player.canPush(Directions.left))
        {
            player = player.push(Directions.left);
            pushCommand(Commands.PushLeft);
            stepsString+='L';
            pushes++;
        }
        moving = false;
        undoing = false;
    }

    /**
     * Right command, move right, push right or do nothing if blocked
     **/
    public void right()
    {
        if(terminating) return;
        moving = true;
        if(player.canMove(Directions.right))
        {
            player = player.move(Directions.right);
            pushCommand(Commands.MoveRight);
            stepsString+='r';
            moves++;
        }
        else if (player.canPush(Directions.right))
        {
            player = player.push(Directions.right);
            pushCommand(Commands.PushRight);
            stepsString+='R';
            pushes++;
        }
        moving = false;
        undoing = false;
    }

    /**
     * Undoes the last command
     **/
    public boolean undo()
    {
        if (terminating) return false;
        if (steps.size() <1) return false;
        undoing = true;
        Commands step = steps.pop();

        switch(step)
        {
            case MoveUp:
            case MoveDown:
            case MoveLeft:
            case MoveRight:
                player = player.move(step.direction().reverse());
                break;
            case PushUp:
            case PushDown:
            case PushLeft:
            case PushRight:
                player = player.unpush(step.direction());
                break;
        }

        undone.push(step);
        stepsString+='<';
        undoRedo++;
        return true;
    }


    /**
     * Redoes the last undone command
     **/
    public void redo()
    {
        if (terminating) return;
        if (undone.size() <1) return;
        undoing = true;
        Commands step = undone.pop();

        switch(step.direction())
        {
            case up:
                up();
                break;
            case down:
                down();
                break;
            case left:
                left();
                break;
            case right:
                right();
                break;
        }
        stepsString+='>';
        undoRedo++;
    }

    /**
     * @return indicates if the player is automoving or player movement.
     */
    public boolean isWalking()
    {
        return walking || moving;
    }

    /**
     * @return getter for the table to add to the gui of the gamescreen
     */
    public Table getTable()
    {
        return table;
    }


    /**
     * @return indicates if the level is completed
     */
    public boolean isCompleted()
    {
        return terminating || goals.stream().allMatch(Tile::isBox);
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        if(walking) doAutomoveTimerStep();


    }

    /**
     * @param completed level completed
     **/
    public void terminate(boolean completed)
    {
        if(completed)
        {
            if(terminating)
            {
                new Splash("CompletedSplashScreen", 1.0f, .2f, LevelSelection.class).activate();
            }
            if (gettingTextInput) return;
            Settings.set("StoredLevel", "");
            if(Highscore.get(levelId).isHighscore(getScore()))
            {
                gettingTextInput = true;
                Gdx.input.getTextInput(new Input.TextInputListener() {
                    @Override
                    public void input(String text)
                    {
                        terminating = true;
                        while(text.length()<3)
                            text+=' ';
                        Highscore.get(levelId).add(levelId, getScore(), text.substring(0, 3).toUpperCase());
                    }

                    @Override
                    public void canceled()
                    {
                        terminating = true;
                        Highscore.get(levelId).add(levelId, getScore(), "AAA");
                    }
                }, "You earned a Highscore!", "", "Please enter your name (3 digits only, the rest will be ignored)");
            }
            else
                terminating = true;
        }
        else
        {
            new LevelSelection().activate();
        }
    }

    /**
     * Helper method that  pushes a performed command to the steps stack
     *
     * Note 1: this is one of the core functions of the undo/redo functionality
     *
     * Note 2: this works since undo and redo are not handled as commands
     **/
    private void pushCommand(Commands cmd)
    {
        if (undone.size()>0)
        {
            undone = new Stack<Commands>();
        }
        steps.push(cmd);

    }

    /**
     * Helper method that links a cell of the grid to its neighbors
     **/
    private void link(int x, int y)
    {
        tiles[y][x].up    = tiles[y+1][x];
        tiles[y][x].down  = tiles[y-1][x];
        tiles[y][x].left  = tiles[y]  [x-1];
        tiles[y][x].right = tiles[y]  [x+1];
    }

    // PATHFINDING

    /**
     * Try to find a path on floor from player's position to the given tile coordinates using a*
     * Automoves there if there is a valid path, does nothing if not
     *
     * @param x Target tile x-coordinate
     * @param y Target tile y-coordinate
     **/
    public void tryMoveTo(int x, int y)
    {
        AStarPathFinder pathfinder = new AStarPathFinder(this, width*height, false);

        visited = new boolean[height][width];

        Path path = pathfinder.findPath(new FloorMover(), player.x, player.y, x, y);
        if(path == null) return;
        walkPath(path);
    }

    /**
     * Starts automove on given path
     * @param path path to move on
     **/
    private void walkPath(Path path)
    {
        walkCounter = 1;
        pathIndex = 1;
        currentPath = path;
        walking = true;
    }

    /**
     * Increments walkCounter, modulo it with the walkRate and step ahead on the current path if it hits 0
     **/
    public void doAutomoveTimerStep()
    {
        walkCounter = (walkCounter+1)%walkRate;
        if (walkCounter == 0)
            doStepOnCurrentPath();
    }

    /**
     * What could a method with such a name do.. Why do you even read the javadoc of it?
     *
     *
     * Ah yeah, it ends automove if the target is reached.
     **/
    private void doStepOnCurrentPath()
    {
        if(pathIndex == currentPath.getLength())
        {
            walking = false;
            return;
        }

        Path.Step step = currentPath.getStep(pathIndex++);
        if(step.getX()<player.x) left();
        else if(step.getX()>player.x) right();
        else if(step.getY()<player.y) down();
        else if(step.getY()>player.y) up();
    }

    /**
     * @see ch.bfh.sokoban.pathfinding.TileBasedMap#getWidthInTiles()
     **/
    @Override
    public int getWidthInTiles()
    {
        return width;
    }

    /**
     * @see ch.bfh.sokoban.pathfinding.TileBasedMap#getHeightInTiles()
     **/
    @Override
    public int getHeightInTiles()
    {
        return height;
    }

    /**
     * @see ch.bfh.sokoban.pathfinding.TileBasedMap#pathFinderVisited(int, int)
     **/
    @Override
    public void pathFinderVisited(int x, int y)
    {
        visited[y][x] = true;
    }

    /**
     * @see ch.bfh.sokoban.pathfinding.TileBasedMap#blocked(ch.bfh.sokoban.pathfinding.Mover, int, int)
     **/
    @Override
    public boolean blocked(Mover mover, int x, int y)
    {
        Tile t = tiles[y][x];
        //if(mover instanceof FloorMover)
        return t.isBox() || t.isWall();
    }

    /**
     * @see ch.bfh.sokoban.pathfinding.TileBasedMap#getCost(ch.bfh.sokoban.pathfinding.Mover, int, int, int, int)
     **/
    @Override
    public float getCost(Mover mover, int sx, int sy, int tx, int ty)
    {
        //if(mover instanceof FloorMover)
        return 1;
    }

    public void save()
    {
        Settings.set("StoredLevel",serialze(':'));
        saved = true;
    }

    /**
     * Mover used for Pathfinding supposed to move on floor only
     * (since there is only one mover so far the if's are commented out but can be used as soon as we go ahead to the solver..)
     **/
    public class FloorMover implements Mover {}

    // PATHFINDING

    public String serialze(char c)
    {
        return levelId+c+stepsString;
    }

    /**
     * Returns the current score
     * @return current score
     */
    public int getScore(){
    	//return this.score;
        return 10*getUndoRedoCount()+5*getPushCount()+getMoveCount();
    }
    
    /**
     * Returns the amount of moves done
     * @return movement count
     */
    public int getMoveCount(){
    	return this.moves;
    }
    
    /**
     * Returns the amount of pushes done
     * @return pushes count
     */
    public int getPushCount(){
    	return this.pushes;
    }
    
    /**
     * Returns the amount of undo/Redo's done
     * @return undo/redo count
     */
    public int getUndoRedoCount(){
    	return this.undoRedo;
    }

    /**
     * The directions movement is allowed
     */
    public enum Directions
    {
        up,down, left, right;

        /**
         * @return the opposite of given direction
         */
        public Directions reverse()
        {
            switch(this)
            {
                case up:
                    return down;
                case down:
                    return up;
                case left:
                    return right;
                case right:
                    return left;
            }
            return null;
        }
    }


    /**
     * All move/push commands possible
     */
    public enum Commands
    {
        MoveUp, MoveDown, MoveLeft, MoveRight, PushUp, PushDown, PushLeft, PushRight;

        /**
         * @return reduce the command to a direction
         */
        Directions direction()
        {
            switch (this)
            {
                case MoveUp:
                case PushUp:
                    return Directions.up;
                case MoveDown:
                case PushDown:
                    return Directions.down;
                case MoveLeft:
                case PushLeft:
                    return Directions.left;
                case MoveRight:
                case PushRight:
                    return Directions.right;
            }
            return null;
        }
        public char toChar()
        {
            return toChar(this);
        }
        public static Commands fromChar(char c)
        {
            switch (c)
            {
                case 'u':
                    return MoveUp;
                case 'U':
                    return PushUp;
                case 'd':
                    return MoveDown;
                case 'D':
                    return PushDown;
                case 'l':
                    return MoveLeft;
                case 'L':
                    return PushLeft;
                case 'r':
                    return MoveRight;
                case 'R':
                    return PushRight;
                default : return null;
            }
        }
        public static char toChar(Commands c)
        {
            switch(c)
            {
                case MoveUp:
                    return 'u';
                case MoveDown:
                    return 'd';
                case MoveLeft:
                    return 'l';
                case MoveRight:
                    return 'r';
                case PushUp:
                    return 'U';
                case PushDown:
                    return 'D';
                case PushLeft:
                    return 'L';
                case PushRight:
                    return 'R';
                default : return '\0';
            }
        }
    }

    /**
     * The tile class represents a cell of the grid the level is composed of.
     * It's implemented as a gdx actor so clickListeners can be attached easily and it alines nice within the gui table.
     * Each cell knows its four neighbors.
     *
     * The underlaying data stays the char of the original level data, since a char is small in size and easy to handle.
     * Each cell has functionalities to move, push and unpush to a given direction
     * Each cell also has a clickListener to start pathfinding/automove
     *
     * THE SUBMONSTER (so far.. to be refactored)
     */
    public class Tile extends Actor
    {
        int x, y;
        private char textureId;
        private Tile up, down, left, right;

        public Tile(char textureId, int x, int y)
        {
            this.textureId = textureId;
            this.x = x;
            this.y = y;

            setSize(tileSize, tileSize);

            addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    super.clicked(event, x, y);
                    Level.this.tryMoveTo(Tile.this.x, Tile.this.y);
                }
            });
        }

        private Tile get(Directions direction)
        {
            switch (direction)
            {
                case up:    return up;
                case down:  return down;
                case left:  return left;
                case right: return right;
                default:    return null;
            }
        }
        public boolean isGoal()
        {
            return textureId == GOAL || textureId == PLAYER_GOAL || textureId == BOX_GOAL;
        }
        public boolean isPlayer()
        {
            return textureId == PLAYER_FLOOR || textureId == PLAYER_GOAL;
        }
        public boolean isBox()
        {
            return textureId == BOX_FLOOR || textureId == BOX_GOAL;
        }
        public boolean isWall()
        {
            return textureId == WALL;
        }
        public boolean canMove(Directions direction)
        {
            return !(get(direction).isBox() || get(direction).isWall());
        }
        public boolean canPush(Directions direction)
        {
            return get(direction).isBox() && get(direction).canMove(direction);
        }
        public Tile move(Directions direction)
        {
            get(direction).togglePlayer();
            togglePlayer();
            return get(direction);
        }
        public Tile push(Directions direction)
        {
            get(direction).get(direction).toggleBox();
            get(direction).toggleBox();
            return move(direction);
        }
        public Tile unpush(Directions direction)
        {
            move(direction.reverse());
            toggleBox();
            get(direction).toggleBox();
            return get(direction.reverse());
        }
        private void togglePlayer()
        {
            switch(textureId)
            {
                case FLOOR:
                    textureId = PLAYER_FLOOR;
                    break;
                case PLAYER_FLOOR:
                    textureId = FLOOR;
                    break;
                case GOAL:
                    textureId = PLAYER_GOAL;
                    break;
                case PLAYER_GOAL:
                    textureId = GOAL;
                    break;
            }
        }
        private void toggleBox()
        {
            switch(textureId)
            {
                case FLOOR:
                    textureId = BOX_FLOOR;
                    break;
                case BOX_FLOOR:
                    textureId = FLOOR;
                    break;
                case GOAL:
                    textureId = BOX_GOAL;
                    break;
                case BOX_GOAL:
                    textureId = GOAL;
                    break;
            }
        }

        public String texture()
        {
            switch(textureId)
            {
                case FLOOR:
                    return FLOOR_D;
                case GOAL:
                    return GOAL_D;
                case PLAYER_FLOOR:
                    return PLAYER_FLOOR_D;
                case PLAYER_GOAL:
                    return PLAYER_GOAL_D;
                case BOX_FLOOR:
                    return BOX_FLOOR_D;
                case BOX_GOAL:
                    return BOX_GOAL_D;
                case WALL:
                    return WALL_D;
            }
            return "";
        }

        @Override
        public void draw(Batch batch, float parentAlpha)
        {
            super.draw(batch, parentAlpha);
            batch.draw(Level.this.skin.getRegion(texture()), getX(), getY(), getWidth(), getHeight());
        }
    }
}
