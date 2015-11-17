package artronics.gsdwn.networkMap;

import artronics.gsdwn.graph.GraphDelegator;
import artronics.gsdwn.log.Log;
import artronics.gsdwn.node.Neighbor;
import artronics.gsdwn.node.Node;
import artronics.gsdwn.node.SdwnNode;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.PoisonPacket;
import artronics.gsdwn.packet.SdwnReportPacket;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class NetworkMapUpdater implements Runnable
{
    private final static Packet POISON_PILL = new PoisonPacket();
    private final GraphDelegator graphDelegator;

    private final BlockingQueue<Packet> queue;
    private final NetworkMap networkMap;
    private final WeightCalculator weightCalculator;

    public NetworkMapUpdater(BlockingQueue<Packet> queue,
                             NetworkMap networkMap,
                             WeightCalculator weightCalculator)
    {
        this.queue = queue;
        this.networkMap = networkMap;
        this.weightCalculator = weightCalculator;
        this.graphDelegator = new GraphDelegator(networkMap.getNetworkGraph());
    }


    @Override
    public void run()
    {
        while (true) {
            try {
                Packet packet = queue.take();
                if (packet == POISON_PILL)
                    break;
                updateMap(packet);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMap(Packet packet)
    {
        switch (packet.getType()) {
            case REPORT:
                processReportPacket((SdwnReportPacket) packet);
                break;
        }
    }

    private void processReportPacket(SdwnReportPacket packet)
    {
        Report report = new Report(packet);
        Node srcNode = report.src;

        if (!networkMap.contains(srcNode)) {
            networkMap.addNode(srcNode);
            Log.SDWN.debug("New Node:" + srcNode.getAddress());
        }

        Set<Node> currentNeighbors = graphDelegator.getNeighbors(srcNode);
        for (Neighbor neighbor : report.neighbors) {
            if (!networkMap.contains(neighbor)) {
                networkMap.addNode(neighbor);
                connect(srcNode, neighbor);
                Log.SDWN.debug("New Node:" + neighbor.getAddress());
            }else if (currentNeighbors.contains(neighbor)) {
                currentNeighbors.remove(neighbor);
                connect(srcNode, neighbor);
            }else {
                connect(srcNode, neighbor);
            }
        }

        if (!currentNeighbors.isEmpty()) {
            for (Node neighbor : currentNeighbors) {
                networkMap.removeLink(srcNode, neighbor);
                //Look for other link with this node if
                //there is no link this node just became islan
                //and should be removed from graph
                if (graphDelegator.isIsland(neighbor)) {
                    networkMap.removeNode(neighbor);
                    Log.SDWN.debug("Remove Node:" + neighbor.getAddress());
                }
            }
        }
    }

    private void connect(Node node, Neighbor neighbor)
    {
        double weight = weightCalculator.getWeight(node, neighbor);
        networkMap.addLink(node, neighbor, weight);
    }

    public void stop()
    {
        queue.add(POISON_PILL);
    }

    private class Report
    {
        private final Node src;
        private final Node dst;
        private final Set<Neighbor> neighbors;

        public Report(SdwnReportPacket packet)
        {
            src = new SdwnNode(packet.getSrcShortAddress());
            dst = new SdwnNode(packet.getDstShortAddress());
            neighbors = new HashSet<>(Neighbor.createNeighbors(packet));
        }
    }
}
