package artronics.gsdwn.networkMap;

import artronics.gsdwn.node.Neighbor;
import artronics.gsdwn.node.Node;

public interface WeightCalculator
{
    double getWeight(Node node, Neighbor neighbor);
}
