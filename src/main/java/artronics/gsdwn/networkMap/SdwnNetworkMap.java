package artronics.gsdwn.networkMap;

import artronics.gsdwn.node.Node;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

import java.util.ArrayList;
import java.util.List;

public class SdwnNetworkMap implements NetworkMap
{
    protected final ListenableUndirectedWeightedGraph<Node, DefaultWeightedEdge> graph =
            new ListenableUndirectedWeightedGraph
                    <Node, DefaultWeightedEdge>(DefaultWeightedEdge.class);

    @Override
    public void addNode(Node node)
    {
        graph.addVertex(node);
    }

    @Override
    public void removeNode(Node node)
    {
        graph.removeVertex(node);
    }

    @Override
    public void removeLink(Node srcNode, Node neighbor)
    {
        graph.removeEdge(srcNode, neighbor);
    }

    @Override
    public void addLink(Node source, Node target, double weight)
    {
        DefaultWeightedEdge edge = graph.addEdge(source, target);

        if (edge != null) {
            this.graph.setEdgeWeight(edge, weight);
        }
    }

    @Override
    public boolean hasLink(Node source, Node target)
    {
        return graph.containsEdge(source, target);
    }

    @Override
    public boolean contains(Node node)
    {
        return graph.containsVertex(node);
    }

    @Override
    public List<Node> getAllNodes()
    {
        return new ArrayList<>(graph.vertexSet());
    }

    @Override
    public Graph<Node, DefaultWeightedEdge> getNetworkGraph()
    {
        return this.graph;
    }
}
