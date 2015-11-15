package artronics.gsdwn.controller;

import artronics.gsdwn.packet.Packet;

public interface Controller extends Runnable
{
    void processPacket(Packet packet);
}
