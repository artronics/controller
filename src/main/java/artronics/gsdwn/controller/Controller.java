package artronics.gsdwn.controller;

import artronics.gsdwn.packet.Packet;

public interface Controller
{
    Packet processPacket(Packet packet);
}
