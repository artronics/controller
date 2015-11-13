package artronics.gsdwn.graph;

import artronics.gsdwn.node.Node;
import org.jgrapht.Graph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GraphDelegator
{
    private final Graph<Node, DefaultWeightedEdge> graph;

    public GraphDelegator(Graph graph)
    {
        this.graph = graph;
    }

    public List<Node> getShortestPath(Node source, Node target)
    {
        DijkstraShortestPath dijkstra = new DijkstraShortestPath(graph, source, target);
        List<DefaultWeightedEdge> links = dijkstra.getPathEdgeList();

        /*
            Dijkstra returns a list of all links but
            we need a list of nodes. A LinkedHashSet
            preserve the ordering of element and also
            ignore duplicated nodes. At the end we remove
            the source from set. This is because in case
            reversed direction from source to target
            we'll get a wrong order. We'll add source to
            final list.
        */
        Set<Node> nodes = new LinkedHashSet<>();

        for (DefaultWeightedEdge link : links) {
            nodes.add(graph.getEdgeSource(link));
            nodes.add(graph.getEdgeTarget(link));
        }
        nodes.remove(source);

        List<Node> nodesList = new ArrayList<>(nodes);
        nodesList.add(0, source);

        return nodesList;
    }
}
