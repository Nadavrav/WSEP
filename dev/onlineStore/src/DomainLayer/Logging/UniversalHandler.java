package DomainLayer.Logging;


import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class UniversalHandler {
    private static UniversalHandler handler = null;
    private FileHandler errorHandler;
    private static FileHandler infoHandler;

    private UniversalHandler(){

    }
    public static synchronized UniversalHandler GetInstance(){

            if (handler == null) {
                handler = new UniversalHandler();
            }
            return handler;
    }
    public void HandleInfo(Logger logger){
        if(infoHandler==null){
            try {
                infoHandler = new FileHandler("Info Log.txt");
                infoHandler.setFormatter(new SimpleFormatter());
                infoHandler.setLevel(Level.ALL);
                logger.setUseParentHandlers(false);
            }
            catch (Exception ignored){}
        }
        logger.addHandler(infoHandler);
    }
    public void HandleError(Logger logger){
        if(errorHandler==null){
            try {
                errorHandler = new FileHandler("Error Log.txt");
                errorHandler.setFormatter(new SimpleFormatter());
                errorHandler.setLevel(Level.SEVERE);
                logger.setUseParentHandlers(false);
            }
            catch (Exception ignored){}

        }
        logger.addHandler(errorHandler);
    }
}
