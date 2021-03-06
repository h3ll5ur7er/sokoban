//*********************************************************//
// Author: K�ser Robin, Knecht Emanuel, Kevin Glass        //
// Berner Fachhochschule                                   //
//*********************************************************//

package ch.bfh.sokoban.pathfinding;

public class ManhattanHeuristic implements AStarHeuristic
{

    @Override
    public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty)
    {
        float dx = Math.abs(tx - x);
        float dy = Math.abs(ty - y);
        return dx+dy;
    }
}
