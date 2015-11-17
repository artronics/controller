package artronics.gsdwn.networkMap;

import artronics.gsdwn.node.Node;

import java.util.List;

public interface ShortestPathFinder
{
    List<Node> getShortestPath(Node src, Node dst);
}
