package DomainLayer.Logging;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.*;

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
                PrepareFolder();
                String s="dev/logs/"+DateTimeFormatter.ofPattern("yy-MM-dd HH-mm-ss").format(LocalDateTime.now())+" Info Log.txt";
                infoHandler = new FileHandler(s);
                infoHandler.setFormatter(new SimpleFormatter());
                infoHandler.setLevel(Level.ALL);
                logger.setUseParentHandlers(false);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        logger.addHandler(infoHandler);
    }
    public void HandleError(Logger logger){

        if(errorHandler==null){
            try {
                PrepareFolder();
                String s="dev/logs/"+DateTimeFormatter.ofPattern("yy-MM-dd HH-mm-ss").format(LocalDateTime.now())+" Error Log.txt";
                errorHandler = new FileHandler(s);
                errorHandler.setFormatter(new SimpleFormatter());
                errorHandler.setLevel(Level.SEVERE);
                logger.setUseParentHandlers(false);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
        logger.addHandler(errorHandler);
    }

    private void PrepareFolder() throws IOException {
        Handler[] handlers = Logger.getLogger("").getHandlers();
        for (Handler handler : handlers) {
            if (handler instanceof ConsoleHandler) {
                Logger.getLogger("").removeHandler(handler);
            }
        }
        Files.createDirectories(Paths.get("dev/logs"));
        File folder = new File("dev/logs");
        File[] files = folder.listFiles();
        if(files!=null && files.length>=10) {
            Arrays.sort(files);
            files[0].delete();
            files[1].delete();
        }
    }
}
