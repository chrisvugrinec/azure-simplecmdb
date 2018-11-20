package aus.microsoft.csu.cashregisterdemo;

import aus.microsoft.csu.cashregisterdemo.data.PaymentAtRegisterEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

public class EventHubSend {

    public static String eventhubnamespace;
    public static String eventhubname;
    public static String eventhubsasname;
    public static String eventhubsaskey;


    final static Gson gson = new GsonBuilder().create();
    static EventHubClient ehClient = null;
    private static final Logger LOGGER = Logger.getLogger( EventHubSend.class.getName() );



    public static void initialize() {

        final ConnectionStringBuilder connStr = new ConnectionStringBuilder()
                .setNamespaceName(eventhubnamespace)
                .setEventHubName(eventhubname)
                .setSasKeyName(eventhubsasname)
                .setSasKey(eventhubsaskey);
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        try{
            ehClient = EventHubClient.createSync(connStr.toString(), executorService);
        }catch (IOException ioe){
            LOGGER.log( Level.SEVERE, "IO exception: {0} ", ioe.getMessage() );
        }catch(EventHubException ehe){
            LOGGER.log( Level.SEVERE, "EventHUB related exception: {0} ", ehe.getMessage() );
        }

    }

    public static void sendMessage(PaymentAtRegisterEvent payload){
         try{
            byte[] payloadBytes = gson.toJson(payload).getBytes(Charset.defaultCharset());
            EventData sendEvent = EventData.create(payloadBytes);
            ehClient.sendSync(sendEvent);
        }catch(EventHubException ehe){
            LOGGER.log( Level.SEVERE, "EventHUB related exception: {0} ", ehe.getMessage() );
        }
    }
}
