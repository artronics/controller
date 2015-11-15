package artronics.gsdwn.packet;

public interface SdwnPacket
{
    int START_BYTE = 122;
    int STOP_BYTE = 126;

    //this indicate header length which is the same
    // for all packets (ie min header length)
    int HEADER_INDEX = 10;

    int SINK_ADDRESS = 0;


    int TIMER_DENOMINATOR = 250; //see RoundTripTimeCalculator

    enum ByteMeaning
    {
        LENGTH(0),
        NET_ID(1),
        SOURCE_H(2),
        SOURCE_L(3),
        DESTINATION_H(4),
        DESTINATION_L(5),
        TYPE(6),
        TTL(7),
        NEXT_HOP_H(8),
        NEXT_HOP_L(9),
        //        DISTANCE(10),
        BATTERY(11),
//        NEIGHBOUR(12),
//        START_TIME_H(11),
//        START_TIME_L(12),
//        STOP_TIME_H(13),
//        STOP_TIME_L(14),;
        ;
        private int value;

        ByteMeaning(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }
}
