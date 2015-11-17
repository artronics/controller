package artronics.gsdwn.log;

import org.apache.log4j.Logger;

public interface Log
{
    Logger SDWN = Logger.getLogger("sdwn");
    Logger PACKET = Logger.getLogger("packet");

    Logger FILE = Logger.getLogger("file");

}
