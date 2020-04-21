package folderSizes;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chris
 */
public class Controller implements Initializable 
{
    @FXML
    private TreeView<String> treeView;
    @FXML
    private ChoiceBox<SortOption> box;
    
    private FileTree fileTree;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        box.setItems(FXCollections.observableArrayList(SortOption.values()));
        box.setValue(SortOption.DEFAULT);
        box.setOnAction(this::onDropMenu);
        try
        {
            if (Loader.getParam() != null)
                fileTree = new FileTree(Paths.get(Loader.getParam()));
            else
                fileTree = new FileTree(new DirectoryChooser().showDialog(new Popup()).toPath());
//                fileTree = new FileTree(Paths.get("C:\\Users\\Chris\\Documents")); // Default for testing
        }
        catch (IOException e) 
        { 
            Alert error = new Alert(AlertType.ERROR);
            error.setContentText("Error reading " + e.getMessage());
            error.setHeaderText("");
            error.showAndWait();
        }
        treeView.setRoot(fileTree.toTreeItem());
        treeView.getTreeItem(0).setExpanded(true);
    }   
    
    private void onDropMenu(ActionEvent e)
    {
        fileTree.sortBy(box.getValue());
        treeView.setRoot(fileTree.toTreeItem());
        treeView.getTreeItem(0).setExpanded(true);
    }

    @FXML
    private void onKey(KeyEvent event)
    {
        Stage stage = (Stage) box.getScene().getWindow();
        if (event.getCode().equals(KeyCode.ESCAPE))
            stage.close();
    }
    
    
}
