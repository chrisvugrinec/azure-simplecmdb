package cmdb;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.simple.JSONObject;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable {


    private static Data data = new Data();
    private static final Logger LOGGER = Logger.getLogger( Data.class.getName() );

    private ArrayList<VM> selectedVMs;

    static {
        data.collect();
    }

    private Stage stage;
    private Scene scene;
    private Parent parent;

    @FXML private ListView<String> listView;

    @FXML
    private ComboBox<String> subscriptionCombo;

    @FXML private TableView<VM> tableView;
    @FXML private TableColumn<VM, String> resourceGroup;
    @FXML private TableColumn<VM, String> virtNet;
    @FXML private TableColumn<VM, String> subNet;
    @FXML private TableColumn<VM, String> virtMachine;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //  Populating the subscription list
        for(String sub : data.getSubscriptions()){
            subscriptionCombo.getItems().add(sub);
        }

        resourceGroup.setCellValueFactory(new PropertyValueFactory<VM, String>("resourcegroup"));
        virtNet.setCellValueFactory(new PropertyValueFactory<VM, String>("vnet"));
        subNet.setCellValueFactory(new PropertyValueFactory<VM, String>("subnet"));
        virtMachine.setCellValueFactory(new PropertyValueFactory<VM, String>("vm"));

        //  Links the action to the trigger (selecting a subscription)
        subscriptionCombo.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String subscription) {
                LOGGER.log(Level.INFO, "selected subscription is: {0}", subscription);
                JSONObject selectedSubscription = data.getSubscriptionObject(subscription);
                //  Populate the table with selected VM's
                tableView.getItems().setAll(data.getSelectedVMs(subscription));
            }
        });
    }
}
