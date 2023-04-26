package DomainLayer.Logging;


import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class UniversalHandler {
    private static UniversalHandler handler = null;
    private static FileHandler errorHandler;
    private static FileHandler infoHandler;

    private UniversalHandler(){

    }
    public static synchronized UniversalHandler GetInstance(){

            if (handler == null) {
                try {
                    errorHandler = new FileHandler("Error Log.txt");
                    infoHandler = new FileHandler("Info Log.txt");

                }
                catch (Exception ignored){}
                handler = new UniversalHandler();
            }
            return handler;
    }
    public void HandleInfo(Logger logger){
        logger.addHandler(infoHandler);
    }
    public void HandleError(Logger logger){
        logger.addHandler(errorHandler);
    }
}
