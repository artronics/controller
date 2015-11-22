package artronics.gsdwn.controller;

import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.packet.Packet;

public interface Controller
{
    //TODO change this exp to controller exp
    void start() throws ChaparConnectionException;

    void stop();

    void processPacket(Packet packet);
}
