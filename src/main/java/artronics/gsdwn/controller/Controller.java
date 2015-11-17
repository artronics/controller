package artronics.gsdwn.controller;

import artronics.gsdwn.packet.Packet;

public interface Controller
{
    void processPacket(Packet packet);
}
