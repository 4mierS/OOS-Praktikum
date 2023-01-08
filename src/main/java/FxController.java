import bank.PrivateBank;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.NumericValueInvalidException;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class FxController {

//    public static void ExceptionAlert(int i) {
//
//        if (i == 1) {
//            System.out.println("Account already exists");
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Account already exists");
//            alert.setContentText("Please choose another name");
//            alert.showAndWait();
//        } else if (i == 2) {
//            System.out.println("Account does not exist");
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Account does not exist");
//            alert.setContentText("Please choose an existing account");
//            alert.showAndWait();
//        } else if (i == 3) {
//            System.out.println("Numeric value invalid");
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Numeric value invalid");
//            alert.setContentText("Please enter a valid number");
//            alert.showAndWait();
//        } else {
//            System.out.println("Unknown error");
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Unknown error");
//            alert.setContentText("Please try again");
//            alert.showAndWait();
//        }
//    }

    PrivateBank bank = new PrivateBank("Meine Bank", 0.2, 0.2);
    @FXML
    TextField testField;
    @FXML
    Button testButton;
    @FXML
    MenuItem NewAccount;

    @FXML
    ListView accountsList;

    public List<String> fetchAllAccounts() {
        return bank.getAllAccounts();
    }

    private void changeToAccountView(ActionEvent event, String accountName) throws IOException {
        System.out.println("changeToAccountView");
        System.out.println(accountName);

        Stage primaryStage = (Stage) accountsList.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountView.fxml"));
        Parent accountView = loader.load();

        AccountViewController controller = loader.getController();
        try {
            controller.setAccount(accountName, bank);
        } catch (Exception e) {
            FxApplication.showError(e);
            e.printStackTrace();
        }

        Scene accountViewScene = new Scene(accountView);
        primaryStage.setScene(accountViewScene);
    }


    public void onSelect(ActionEvent event, String accountName) throws IOException {
        System.out.println("Ausgewählt");
        changeToAccountView(event, accountName);
    }

    public void onDelete(ActionEvent event, String accountName) throws AccountDoesNotExistException, IOException {
        System.out.println("Loesche: " + accountName);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete account");
        alert.setHeaderText("Delete account");
        alert.setContentText("Are you sure you want to delete this account?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            bank.deleteAccount(accountName);
            accountsList.getItems().remove(accountName);
        } else {
            // ... user chose CANCEL or closed the dialog
        }

    }

    @FXML
    public void initialize() {
        try {
            bank.setDirectoryName("test2");
            bank.readAccounts();
            accountsList.getItems().addAll(fetchAllAccounts());


            // Binding Cells to ContextMenu
            accountsList.setCellFactory(lv -> {

                ListCell<String> cell = new ListCell<>();
                ContextMenu contextMenu = new ContextMenu();

                MenuItem selectItem = new MenuItem();
                selectItem.textProperty().bind(Bindings.format("Auswahlen \"%s\"", cell.itemProperty()));
                selectItem.setOnAction(event -> {
                    try {
                        onSelect(event, cell.getItem());
                    } catch (IOException e) {
                        FxApplication.showError(e);
                        throw new RuntimeException(e);
                    }
                });

                MenuItem deleteItem = new MenuItem();
                deleteItem.textProperty().bind(Bindings.format("Loeschen \"%s\"", cell.itemProperty()));
                deleteItem.setOnAction(event -> {
                    try {
                        onDelete(event, cell.getItem());
                    } catch (AccountDoesNotExistException | IOException e) {
                        throw new RuntimeException(e);
                    }
                });


                contextMenu.getItems().addAll(selectItem, deleteItem);

                cell.textProperty().bind(cell.itemProperty());
                cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                        cell.setContextMenu(null);
                    } else {
                        cell.setContextMenu(contextMenu);
                    }
                });
                return cell;
            });

        } catch (Exception e) {
            FxApplication.showError(e);
            e.printStackTrace();
        }
    }

    public FxController() throws NumericValueInvalidException {
    }

    @FXML
    private Pane CreateAccountPane;

    @FXML
    public void CreateNewAccount(ActionEvent event) throws IOException {

//        CreateAccountPane.visibleProperty().set(true);

        //ToDo: Show the dialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create new account");
        dialog.setHeaderText("Create new account");
        dialog.setContentText("Please enter the name of the new account:");


        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            try {
                if (Objects.equals(dialog.getEditor().getText(), "")) {
                    dialog.getEditor().setText("New Account");
                }else{
                bank.createAccount(name);
                accountsList.getItems().add(name);
                }
            } catch (AccountAlreadyExistsException e) {
                FxApplication.showError(e);
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }


    @FXML Button CreateAccountButton;
    @FXML Button CreateAccountCancel;

    @FXML
    public void CAB() throws AccountAlreadyExistsException {
        try {
            System.out.println("Created Account");
            bank.createAccount(NT());
            NameText.clear();
            DText.clear();
            MText.clear();
            YearText.clear();
            TransactionsText.clear();
            accountsList.getItems().clear();

            accountsList.getItems().addAll(bank.getAllAccounts());
            System.out.println(bank.getAllAccounts());
        }catch (AccountAlreadyExistsException e){
            FxApplication.showError(e);
        }

    }

    @FXML
    public void CAC(ActionEvent event) throws IOException {
        CreateAccountPane.visibleProperty().set(false);
        NameText.clear();
        DText.clear();
        MText.clear();
        YearText.clear();
        TransactionsText.clear();
        System.out.println("Creating Account canceled");
    }

    @FXML TextField NameText;
    @FXML TextField DText;
    @FXML TextField MText;
    @FXML TextField YearText;
    @FXML TextField TransactionsText;

    @FXML
    public String NT(){
        System.out.println(NameText.getText());
        return NameText.getText();
    }



}