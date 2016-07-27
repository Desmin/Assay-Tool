package core.ui.controllers;

import core.drosophila.Cohort;
import core.Context;
import core.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.joda.time.DateTime;
import core.ui.AssayUI;
import core.ui.CohortCreationUI;
import core.ui.RecordDeathsUI;

import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Desmin on 6/3/2016.
 */
public class CohortController {

    @FXML
    private MenuItem closeMenuItem, createMenuItem, recordDeathsMenuItem;

    @FXML
    private ListView<Cohort> cohortsListView;

    @FXML
    private Button createButton, recordDeathsButton;

    private ObservableList<Cohort> cohorts = FXCollections.observableArrayList();

    @FXML
    private void createCohort() {
        ((Stage) createButton.getScene().getWindow()).close();
        try {
            new CohortCreationUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void recordDeaths() {
        ChoiceDialog<Cohort> cohortToRecord = new ChoiceDialog<>(null, cohorts);
        cohortToRecord.setTitle("Select a Cohort");
        cohortToRecord.setContentText("Which cohort will you be recording deaths for?");
        Optional<Cohort> selectedCohort = cohortToRecord.showAndWait();
        if (selectedCohort.isPresent()) {
            Context.getInstance().setSelectedCohort(selectedCohort.get());
            ((Stage) createButton.getScene().getWindow()).close();
            try {
                new RecordDeathsUI().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void closeUI() {
        ((Stage) createButton.getScene().getWindow()).close();
        try {
            new AssayUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        int assay = Context.getInstance().getSelectedAssay().getAssayID();
        ResultSet result = Context.getInstance().executeSQLStatement(String.format("select * from cohort where assayID = %1$d", assay));
        try {
            while (result.next()) {
                int cohortID = result.getInt("cohortID");
                int assayID = result.getInt("assayID");
                DateTime creationDate = Util.timeStringToDateTime(result.getString("creationDate"));
                DateTime completionDate = new DateTime();
                String compDate = result.getString("completionDate");
                if (Objects.nonNull(compDate)) {
                    completionDate = Util.timeStringToDateTime(compDate);
                }
                Cohort c = new Cohort();
                c.setCohortID(cohortID);
                c.setCreationDate(creationDate);
                c.setCompletionDate(completionDate);
                cohorts.add(c);
            }
            cohortsListView.setItems(cohorts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        createMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        recordDeathsMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
    }
}
