package aus.microsoft.csu.cashregisterdemo.controller;

import aus.microsoft.csu.cashregisterdemo.EventHubSend;
import aus.microsoft.csu.cashregisterdemo.data.Article;
import aus.microsoft.csu.cashregisterdemo.data.Data;
import aus.microsoft.csu.cashregisterdemo.data.PaymentAtRegisterEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable {


    private static Data data = new Data();
    private static final Logger LOGGER = Logger.getLogger( Controller.class.getName() );

    private Stage stage;
    private Scene scene;
    private Parent parent;

    @FXML private ListView<String> listView;

    @FXML private TextField articleId;
    @FXML private TextField totalField;
    @FXML private TextField customerId;

    @FXML
    private ComboBox<String> subscriptionCombo;

    @FXML private TableView<Article> shoppingList;
    @FXML private TableColumn<Article, String> name;
    @FXML private TableColumn<Article, Double> price;

    double total = 0;
    ArrayList<Article> localShoppingList = new ArrayList<>();

    public void initialize(URL location, ResourceBundle resources) {

        //  Init of Data(sources)
        Data.initialize();


        //  Init of frontend components
        articleId.setTooltip(new Tooltip("1 = Apple, 2 = Banana, 3 = Cola"));
        articleId.setCache(false);

        name.setCellValueFactory(new PropertyValueFactory<Article, String>("name"));
        price.setCellValueFactory(new PropertyValueFactory<Article, Double>("price"));

        shoppingList.getItems().setAll(data.getSelectedArticles());
        totalField.setText(new Double(total).toString());

    }

    public void handleArticleLookup(KeyEvent keyEvent) {
       // String enteredArticleId = articleId.getText();

        // Only add found articles to the list
        Article article = Data.lookup(articleId);
        if(article!=null) {
            total += article.getPrice();
            shoppingList.getItems().add(article);
            localShoppingList.add(article);
            totalField.setText(new Double(total).toString());
        }
        articleId.clear();

    }

    public void handlePayment(ActionEvent actionEvent) {
        PaymentAtRegisterEvent pare = new PaymentAtRegisterEvent();
        pare.setCustomerId(customerId.getText());
        pare.setShoppingList(localShoppingList);
        pare.setShoppingTotal(new Double(totalField.getText()).doubleValue());

        EventHubSend.sendMessage(pare);

        localShoppingList.clear();
        customerId.clear();
        shoppingList.getItems().clear();
        totalField.clear();
        total = 0;
    }
}
