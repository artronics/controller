package artronics.gsdwn.networkMap;

import artronics.gsdwn.node.Neighbor;
import artronics.gsdwn.node.SdwnNode;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class RssiSimpleWeightCalculatorTest
{
    WeightCalculator weightCalculator = new RssiSimpleWeightCalculator();
    SdwnNode node = new SdwnNode(10);
    Neighbor neighbor = new Neighbor(20, 100);

    @Test
    public void it_should_return_right_weight()
    {
        double weight = weightCalculator.getWeight(node, neighbor);

        assertThat(weight, equalTo(156.0));

    }

}