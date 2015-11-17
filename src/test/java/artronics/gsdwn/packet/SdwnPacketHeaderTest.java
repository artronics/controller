package artronics.gsdwn.packet;

import org.junit.Test;

import java.util.List;

import static artronics.gsdwn.packet.SdwnPacket.ByteMeaning.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SdwnPacketHeaderTest
{
    List<Integer> header;

    @Test
    public void it_should_create_a_header_with_default_values()
    {
        header = SdwnPacketHeader.create(12, 30, 257);
        assertThat(header.size(), equalTo(10));

        assertThat(header.get(LENGTH.getValue()), equalTo(12));

        assertThat(header.get(SOURCE_H.getValue()), equalTo(0)); //src_h
        assertThat(header.get(SOURCE_L.getValue()), equalTo(30)); //src_h

        assertThat(header.get(DESTINATION_H.getValue()), equalTo(1)); //dst_h
        assertThat(header.get(DESTINATION_L.getValue()), equalTo(1)); //dst_h
    }

}