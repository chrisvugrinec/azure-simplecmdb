package aus.microsoft.csu.cashregisterdemo.data;

import aus.microsoft.csu.cashregisterdemo.EventHubSend;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.control.TextField;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Data {


    private static JSONParser parser = new JSONParser();
    private static final Logger LOGGER = Logger.getLogger( Data.class.getName() );
    private ArrayList<Article> articleList = new ArrayList<Article>();

    static Gson gson = new GsonBuilder().create();
    static Article[] articleInventory = null;
    static Properties prop = new Properties();
    public ArrayList<Article>getSelectedArticles(){
        return articleList;
    }


    public static void initialize(){

        LOGGER.log(Level.INFO, "Reading file: ");
        try{
            String articleInventoryData = new String(Files.readAllBytes(Paths.get("src/main/resources/articles.json")));
            articleInventory = gson.fromJson(articleInventoryData, Article[].class);
        } catch (FileNotFoundException fnfe) {
            LOGGER.log( Level.SEVERE, "File not found exception: {0} could not find ARTICLE file articles.json ", fnfe.getMessage() );
        } catch (IOException ioe) {
            LOGGER.log( Level.SEVERE, "IO exception: {0} with loading articles ", ioe.getMessage() );
        } catch( Exception gex) {
            LOGGER.log( Level.SEVERE, "General Exception during inventory creation : {0} ", gex.getMessage() );
        }

        try {
            InputStream input = new FileInputStream("src/main/resources/application.properties");
            prop.load(input);
            EventHubSend.eventhubnamespace = prop.getProperty("eventhubnamespace");
            EventHubSend.eventhubname = prop.getProperty("eventhubname");
            EventHubSend.eventhubsasname = prop.getProperty("eventhubsasname");
            EventHubSend.eventhubsaskey = prop.getProperty("eventhubsaskey");
            EventHubSend.initialize();
        } catch (FileNotFoundException e) {
            LOGGER.log( Level.SEVERE, "File not found exception: {0} could not find application.properties ", e.getMessage() );
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "IO exception: {0} with loading properties ", ioe.getMessage());
        }



    }

    public static Article lookup(TextField articleId){
        String enteredArticleId = articleId.getText();
        Article result = null;
        //  This demo only accepts 0-9 as input
        if (!enteredArticleId.isEmpty() && enteredArticleId.length()<2 && enteredArticleId.chars().allMatch( Character::isDigit )){

            LOGGER.log(Level.INFO, "selected article = {0} looking it up now",enteredArticleId);
            for(Article article : articleInventory){
                if(article.getId()==new Integer(enteredArticleId).intValue()){
                    LOGGER.log(Level.INFO, "found article: {0} ",article.getName());
                    result = article;
                }
            }

        }
        return result;
    }



}
