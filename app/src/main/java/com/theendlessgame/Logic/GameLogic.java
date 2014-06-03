package com.theendlessgame.Logic;

/**
 * Created by Luis on 02/06/14.
 */
public class GameLogic {

    private GameLogic(){}

    public static GameLogic getInstance(){
        if(_Instance == null)
            _Instance = new GameLogic();
        return  _Instance;
    }

    public void startGame(){
        _CurrentNodeGraph = new GraphHandler(GraphHandler.ROOT_SEED, GraphHandler.GRAPH_START_LEVEL, GraphHandler.NODE_START_POSITION);
    }

    private static GameLogic _Instance;

    private GraphHandler _CurrentNodeGraph;
}
