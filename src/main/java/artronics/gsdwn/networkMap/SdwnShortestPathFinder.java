package artronics.gsdwn.networkMap;

import artronics.gsdwn.graph.GraphDelegator;
import artronics.gsdwn.node.Node;

import java.util.List;

public class SdwnShortestPathFinder implements ShortestPathFinder
{
    private final NetworkMap networkMap;
    private final GraphDelegator graphDelegator;

    public SdwnShortestPathFinder(NetworkMap networkMap)
    {
        this.networkMap = networkMap;
        this.graphDelegator = new GraphDelegator(networkMap.getNetworkGraph());
    }

    @Override
    public List<Node> getShortestPath(Node src, Node dst)
    {
        return graphDelegator.getShortestPath(src, dst);
    }
}
