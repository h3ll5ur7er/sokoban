package ch.bfh.sokoban.game;

import ch.bfh.sokoban.data.LevelPack;
import ch.bfh.sokoban.pathfinding.AStarPathFinder;
import ch.bfh.sokoban.pathfinding.Mover;
import ch.bfh.sokoban.pathfinding.Path;
import ch.bfh.sokoban.pathfinding.TileBasedMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import static ch.bfh.sokoban.game.Constants.*;

import java.util.ArrayList;
import java.util.Stack;

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

    private Stack<Commands> steps;
    private Stack<Commands> undone;

    private Tile[][] tiles;

    boolean [][] visited;
    Path currentPath;
    int pathIndex;
    int walkCounter;
    int walkRate = 10;
    boolean walking = false;

    private Tile player;
    private ArrayList<Tile> goals;

    Table table;
    Skin skin;

    public Level(String name, int width, int height, String[] data, Skin skin)
    {
        this.width = width;
        this.height = height;

        this.skin = skin;

        goals = new ArrayList<Tile>();

        tiles = new Tile[height][];

        steps = new Stack<Commands>();
        undone = new Stack<Commands>();

        for (int y = 0; y < data.length; y++)
        {
            tiles[y] = new Tile[width];
            char[] chars = data[y].toCharArray();
            for (int x = 0; x < data[y].length(); x++)
            {
                Tile t = new Tile(chars[x], x, y);
                tiles[y][x] = t;
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
    }

    public Level(LevelPack.Level lvl, Skin skin)
    {
        this(lvl.name, lvl.width, lvl.height, lvl.data, skin);
    }

    public void up()
    {
        if(player.canMove(Directions.up))
        {
            player = player.move(Directions.up);
            pushCommand(Commands.MoveUp);
        }
        else if (player.canPush(Directions.up))
        {
            player = player.push(Directions.up);
            pushCommand(Commands.PushUp);
        }
    }

    public void down()
    {
        if(player.canMove(Directions.down))
        {
            player = player.move(Directions.down);
            pushCommand(Commands.MoveDown);
        }
        else if (player.canPush(Directions.down))
        {
            player = player.push(Directions.down);
            pushCommand(Commands.PushDown);
        }
    }

    public void left()
    {
        if(player.canMove(Directions.left))
        {
            player = player.move(Directions.left);
            pushCommand(Commands.MoveLeft);
        }
        else if (player.canPush(Directions.left))
        {
            player = player.push(Directions.left);
            pushCommand(Commands.PushLeft);
        }
    }

    public void right()
    {
        if(player.canMove(Directions.right))
        {
            player = player.move(Directions.right);
            pushCommand(Commands.MoveRight);
        }
        else if (player.canPush(Directions.right))
        {
            player = player.push(Directions.right);
            pushCommand(Commands.PushRight);
        }
    }

    public boolean undo()
    {
        if (steps.size() <1) return false;

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
        return true;
    }

    public void redo()
    {
        if (undone.size() <1) return;

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
    }

    public boolean isWalking()
    {
        return walking;
    }

    public Table getTable()
    {
        return table;
    }

    public boolean isCompleted()
    {
        return goals.stream().allMatch(Tile::isBox);
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        if(walking) doAutomoveTimerStep();
    }
    /**
     * Helper method that intelligently+ pushes a performed command to the steps stack
     * +) if the performed command is on top of the undone stack redo is called instead of pushing it to the steps stack.
     *    if not, the undone stack gets cleared and the command is pushed to the steps stack
     *
     * Note 1: this is one of the core functions of the undo/redo functionality
     *
     * Note 2: this works since undo and redo are not handled as commands
     **/
    private void pushCommand(Commands cmd)
    {
        if (undone.size()>0)
        {
            if(undone.peek() == cmd)
            {
                redo();
            }
            else
            {
                undone = new Stack<Commands>();
                steps.push(cmd);
            }
        }
        else
        {
            steps.push(cmd);
        }
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
        if(walkCounter == 0)
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

    /**
     * Mover used for Pathfinding supposed to move on floor only
     * (since there is only one mover so far the if's are commented out but can be used as soon as we go ahead to the solver..)
     **/
    public class FloorMover implements Mover {}

    // PATHFINDING

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

            setSize(Constants.TILE_WIDTH, Constants.TILE_HEIGHT);

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
