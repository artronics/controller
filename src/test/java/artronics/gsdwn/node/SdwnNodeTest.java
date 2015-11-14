package artronics.gsdwn.node;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class SdwnNodeTest
{
    Node aNode;
    Node sameNode;
    Node otherNode;

    @Before
    public void setUp() throws Exception
    {
        aNode = new SdwnNode(0);
        sameNode = new SdwnNode(0);
        otherNode = new SdwnNode(7);
    }

    @Test
    public void test_all_equality_and_hash()
    {
        assertThat(aNode, equalTo(aNode));
        assertThat(aNode, equalTo(sameNode));
        assertNotEquals(aNode, otherNode);

        assertThat(aNode.hashCode(), equalTo(aNode.hashCode()));
        assertThat(aNode.hashCode(), equalTo(sameNode.hashCode()));
        assertNotEquals(aNode.hashCode(), otherNode.hashCode());
    }

}