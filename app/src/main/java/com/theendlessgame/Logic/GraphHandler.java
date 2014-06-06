package com.theendlessgame.logic;

import java.io.IOException;
import java.util.*;

/**
 *
 * @author Luis
 */
public class GraphHandler {

    public GraphHandler(long pRootNodeId, int pStartLevel, byte pPosition) {
        _Graph = new HashMap();
        _CurrentLevel = pStartLevel;
        _LevelLimit = pStartLevel + 4;
        _CurrentNodeID = getNodeIdWithLocationMask(pRootNodeId,pStartLevel, pPosition);

        FileManager manager = new FileManager();
        try {
            _Visited = manager.existsOnFile(manager.readFile(".data"), ";"+_CurrentNodeID+";");
        } catch (IOException e) {
            System.out.printf(e.getMessage());
            _Visited = false;
        }

        if(!_Visited)
            try {
                manager.writeToFile(".data",_CurrentNodeID + ";");
            } catch (IOException e) {
                e.getMessage();
            }

        generateGraph(_Graph, pRootNodeId,pStartLevel,pPosition);
    }

    private void generateGraph(HashMap pGraph, long pNodeUniqueId, int pStartLevel, byte pPosition){
        if(pStartLevel <= _LevelLimit){
            long nodeId = getNodeIdWithLocationMask(pNodeUniqueId, pStartLevel, pPosition);

            pGraph.put(nodeId, new HashMap());
            byte nodesCount = (byte)(nodeId % 3);
            int nextLevel = pStartLevel + 1;

            if(pStartLevel%4 == 0 && nodesCount == 0)
                nodesCount++;

            while(nodesCount >= 0){
                if(pStartLevel%4 == 0 && nodesCount == 0)
                    setReturnNode((HashMap)pGraph.get(nodeId), pStartLevel);
                else{
                    long childNodeId = generateNewNodeId(nodeId);
                    generateGraph((HashMap)pGraph.get(nodeId),childNodeId,nextLevel, nodesCount);
                }
                nodesCount--;
            }
        }
    }

    private int getDigits(int pNumber){
        int count = 1;
        while(pNumber > 9){
            count++;
            pNumber /= 10;
        }
        return count;
    }

    private long generateNewNodeId(long pSeed){
        long newID = (A*pSeed+C)%M;
        if(newID < 0)
            newID += M;
        return newID;
    }

    private long getNodeRootId(long pNodeId){
        return (pNodeId/10)/(long)Math.pow(10, getDigits(_CurrentLevel + 1));
    }

    private long getNodeIdWithLocationMask(long pNodeId, int pStartLevel, byte pPosition){
        int levelOffset = (int)Math.pow(10, getDigits(pStartLevel) + 1);
        return(long) (pNodeId * levelOffset + pStartLevel*10 + pPosition );
    }

    private void setReturnNode(HashMap pGraph, int pSenderLevel){
        int levelToGo = pSenderLevel - (pSenderLevel%3 + 1);
        long returnNodeId = getNodeIdWithLocationMask(ROOT_SEED, GRAPH_START_LEVEL, NODE_START_POSITION);
        int counter = 2;
        while(counter <= levelToGo){
            returnNodeId = getNodeIdWithLocationMask(generateNewNodeId(returnNodeId), counter, (byte)0);
            counter++;
        }
        pGraph.put(returnNodeId, new HashMap());
    }

    private boolean isNextAReturnNode(long pNextNodeId){
        long generatedId;
        byte counter = 2;
        boolean isReturn = true;
        while(counter >=0){
            generatedId = getNodeIdWithLocationMask(generateNewNodeId(_CurrentNodeID), _CurrentLevel + 1,counter);
            if (generatedId == pNextNodeId) {isReturn = false; break;}
            counter--;
        }

        return isReturn;
    }

    private int calculateTotalNodesToLimit(HashMap pGraph){
        if(pGraph.isEmpty()){
            return 0;
        }
        else{
            int total = pGraph.size();
            for(Object value : pGraph.values()){
                total += calculateTotalNodesToLimit((HashMap)value);
            }
            return total;
        }
    }

    public long getCurrentNodeId() {
        return _CurrentNodeID;
    }

    public boolean wasVisitedBefore(){ return _Visited; }

    public HashMap getCurrentGraphChildren(){ return (HashMap)_Graph.get(_CurrentNodeID); }

    public GraphHandler goToNodeAtDirection(int pDirection){
        HashMap nextLevelGraph = (HashMap)_Graph.get(_CurrentNodeID);
        GraphHandler handler = null;
        int directionIndex = pDirection;
        while(handler == null){
            for (Object key : nextLevelGraph.keySet() ) {
                long id = (Long)key;
                if(id%10 == directionIndex){
                    int level = 0;
                    if(isNextAReturnNode(id))
                        level = _CurrentLevel - (_CurrentLevel%3 + 1);
                    else
                        level = _CurrentLevel + 1;
                    handler = new GraphHandler(getNodeRootId(id),level, (byte)directionIndex);
                    break;
                }
            }
            directionIndex--;
        }
        return handler;
    }

    public int getBestWay(){
        HashMap<Long, Integer> paths = new HashMap();
        HashMap currentGraphChildren = (HashMap)_Graph.get(_CurrentNodeID);
        for(Object key : currentGraphChildren.keySet()){
            paths.put((Long)key,calculateTotalNodesToLimit((HashMap)currentGraphChildren.get(key)));
        }

        int bestWayIndex;

        Map.Entry<Long, Integer> min = null;
        for (Map.Entry<Long, Integer> entry : paths.entrySet()) {
            if (!isNextAReturnNode((long)entry.getKey()) && (min == null || (int)min.getValue() > (int)entry.getValue())) {
                min = entry;
            }
        }
        bestWayIndex = (int)(min.getKey()%10);
        return bestWayIndex;
    }


    public static final byte NODE_START_POSITION = 0;
    public static final int GRAPH_START_LEVEL = 1;
    public static final long ROOT_SEED = 4398080065535L;

    private final long A = 4398046511104L;
    private final long C = 11;
    private final long M = 8796093022207L;

    private final HashMap _Graph;
    private int _LevelLimit = 0;
    private final int _CurrentLevel;
    private final long _CurrentNodeID;
    private boolean _Visited;

    /*RELACIÓN M - NIVELES:

         Valor de M | Maximo de Niveles
         ==============================
        |    2^58   |        9         |
        |    2^55   |        99        |
        |    2^52   |       999        |
        |    2^48   |      9.999       |
        |    2^45   |      99.999      |
        |    2^42   |     999.999      |
         ==============================
    */
}
