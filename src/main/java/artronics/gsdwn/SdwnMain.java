package artronics.gsdwn;

import artronics.chaparMini.exceptions.ChaparConnectionException;
import artronics.gsdwn.controller.SdwnController;

public class SdwnMain
{
    public static void main(String[] args)
    {

        SdwnController controller = new SdwnController();

        try {

            controller.start();

        }catch (ChaparConnectionException e) {
            e.printStackTrace();
        }
    }
}
