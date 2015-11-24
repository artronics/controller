package artronics.gsdwn.controller;

import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.packet.Packet;

import java.util.concurrent.BlockingQueue;

public interface Controller
{
    BlockingQueue<Packet> getCntRxPacketsQueue();

    BlockingQueue<Packet> getCntTxPacketsQueue();

    //TODO change this exp to controller exp
    void start() throws ChaparConnectionException;

    void stop();

    void processPacket(Packet packet);
}
