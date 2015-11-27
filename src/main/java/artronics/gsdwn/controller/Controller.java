package artronics.gsdwn.controller;

import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.packet.Packet;

import java.util.concurrent.BlockingQueue;

public interface Controller
{
    //TODO change this exp to controller exp
    void start() throws ChaparConnectionException;

    void stop();

    void processPacket(Packet packet);

    BlockingQueue<Packet> getCntRxPacketsQueue();

    BlockingQueue<Packet> getCntTxPacketsQueue();

    void setConfig(ControllerConfig controllerConfig);
}
