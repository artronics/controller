package artronics.gsdwn.networkMap;

import artronics.gsdwn.node.Neighbor;
import artronics.gsdwn.node.Node;

public class RssiSimpleWeightCalculator implements WeightCalculator
{

    @Override
    public double getWeight(Node node, Neighbor neighbor)
    {
        return (double) (256 - neighbor.getRssi());
    }
}
