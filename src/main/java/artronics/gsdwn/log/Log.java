package artronics.gsdwn.log;

import org.apache.log4j.Logger;

public interface Log
{
    Logger MAIN = Logger.getLogger("main");
    Logger FILE = Logger.getLogger("file");

}
