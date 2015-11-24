package artronics.gsdwn.networkMap;

import artronics.gsdwn.node.Neighbor;
import artronics.gsdwn.node.Node;

public class FixedWeightCalculator implements WeightCalculator
{
    double weight;

    public FixedWeightCalculator(double weight)
    {
        this.weight = weight;
    }

    @Override
    public double getWeight(Node node, Neighbor neighbor)
    {
        return 100;
    }
}
