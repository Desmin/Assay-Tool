package core.ui.controllers;

import core.drosophila.Assay;
import core.Context;
import core.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.joda.time.DateTime;
import core.ui.CohortUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Desmin on 6/3/2016.
 */
public class AssayController {

    @FXML
    private MenuItem closeMenuItem, createMenuItem, deleteMenuItem;
    @FXML
    private Button createButton, deleteButton;
    @FXML
    private ListView<Assay> assayListView;

    private ObservableList<Assay> assays = FXCollections.observableArrayList();

    @FXML
    private void createAssay() {
        Assay newAssay = new Assay();
        int id = 1;
        String sql = "select max(assayID) as id from assay";
        ResultSet rs = Context.getInstance().executeSQLStatement(sql);
        try {
            while (rs.next()) {
                id += rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DateTime time = new DateTime(System.currentTimeMillis());
        newAssay.setAssayID(id);
        newAssay.setCreationDate(time);
        sql = String.format("insert into assay values (%1$d, '%2$s')", id, Util.dateTimeToSQLString(time));
        Context.getInstance().updateDatabase(sql);
        assays.add(newAssay);
    }

    @FXML
    private void close() {
        Context.getInstance().stopServices();
        System.exit(0);
    }

    @FXML
    private void deleteAssay() {
        Assay toDelete = null;
        ChoiceDialog<Assay> deleteDialog = new ChoiceDialog<>(null, assays);
        deleteDialog.setTitle("Assay Deletion");
        deleteDialog.setContentText("Select an assay to delete:");
        Optional<Assay> choiceResult = deleteDialog.showAndWait();
        if (choiceResult.isPresent()) {
            toDelete = choiceResult.get();
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Confirm Deletion");
            alert.setContentText("Are you sure you wish to delete: " + toDelete.toString());
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> alertResult = alert.showAndWait();
            if (alertResult.get() == ButtonType.YES) {
                Context.getInstance().updateDatabase(String.format("delete from assay where assayID = %1$d", toDelete.getAssayID()));
                assays.removeAll(toDelete);
            }
        }
    }

    @FXML
    private void openAssay() {
        if (Objects.isNull(assayListView.getSelectionModel().getSelectedItem()))
            return;
        Context.getInstance().setSelectedAssay(assayListView.getSelectionModel().getSelectedItem());
        ((Stage) assayListView.getScene().getWindow()).close();
        try {
            new CohortUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        ResultSet result = Context.getInstance().executeSQLStatement("select * from assay");
        try {
            while (result.next()) {
                int assayID = result.getInt("assayID");
                String date = result.getString("creationDate");
                DateTime dateTime = Util.timeStringToDateTime(date);
                Assay a = new Assay();
                a.setAssayID(assayID);
                a.setCreationDate(dateTime);
                assays.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assayListView.setItems(assays);
        closeMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        createMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        deleteMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
    }
}
