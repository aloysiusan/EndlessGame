package com.theendlessgame.logic;

import com.theendlessgame.gameobjects.Intersection;

import java.util.HashMap;

/**
 * Created by Luis on 02/06/14.
 */
public class GameController {

    private GameController(){}

    public static GameController getInstance(){
        if(_Instance == null)
            _Instance = new GameController();
        return  _Instance;
    }

    public void startGame(){
        _CurrentNodeGraph = new GraphHandler(GraphHandler.ROOT_SEED, GraphHandler.GRAPH_START_LEVEL, GraphHandler.NODE_START_POSITION);
    }

    public void goToDirection(GameController.Direction pDirection){
        _CurrentNodeGraph = _CurrentNodeGraph.goToNodeAtDirection(pDirection.ordinal());
        _CurrentInterception = null;
    }

    public Direction getBestWay(){
        HashMap<Integer, Direction> directionHashMap = new HashMap<Integer, Direction>();
        directionHashMap.put(0,Direction.LEFT);
        directionHashMap.put(1,Direction.CENTER);
        directionHashMap.put(2, Direction.RIGHT);

        return directionHashMap.get(_CurrentNodeGraph.getBestWay());
    }
    public Intersection getCurrentIntersection(){
        if(_CurrentInterception == null)
            _CurrentInterception = new Intersection(_CurrentNodeGraph.getCurrentNodeId());
        return _CurrentInterception;
    }

    public boolean wasCurrentIntersectionVisited(){
        return _CurrentNodeGraph.wasVisitedBefore();
    }

    public int getNextIntersectionPathsCount(){
        return _CurrentNodeGraph.getCurrentGraphChildren().size();
    }

    public enum Direction{
        LEFT, CENTER, RIGHT
    }

    private static GameController _Instance;

    private GraphHandler _CurrentNodeGraph;
    private Intersection _CurrentInterception;
}
