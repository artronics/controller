package artronics.gsdwn.networkMap;

import artronics.gsdwn.node.Node;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public interface NetworkMap
{
    void addNode(Node node);

    void removeNode(Node node);

    void addLink(Node source, Node target, double weight);

    boolean hasLink(Node source, Node target);

    boolean contains(Node node);

    List<Node> getAllNodes();

    Graph<Node, DefaultWeightedEdge> getNetworkGraph();

    void removeLink(Node srcNode, Node neighbor);
}
