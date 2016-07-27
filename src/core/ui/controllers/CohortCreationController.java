package core.ui.controllers;

import core.drosophila.Cohort;
import core.Context;
import core.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import core.ui.CohortUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Created by Desmin on 6/9/2016.
 */
public class CohortCreationController {

    @FXML
    private DatePicker eclosionDate;

    @FXML
    private ChoiceBox<Character> paternalIdentifierChoiceBox;

    @FXML
    private Spinner<Integer> maleSpinner, femaleSpinner, paternalAgeSpinner;

    @FXML
    private Button submitButton, cancelButton;

    @FXML
    private Label totalFliesLabel;

    private ObservableList<Character> parentalIdentifiers
            = FXCollections.observableArrayList('A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z');

    @FXML
    private void initialize() {
        SpinnerValueFactory<Integer> paternalAgeSpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150, 1);
        SpinnerValueFactory<Integer> maleSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150, 1);
        SpinnerValueFactory<Integer> femaleSpinnerFactroy = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150, 1);
        maleSpinner.setValueFactory(maleSpinnerValueFactory);
        femaleSpinner.setValueFactory(femaleSpinnerFactroy);
        paternalAgeSpinner.setValueFactory(paternalAgeSpinnerFactory);
        paternalIdentifierChoiceBox.setItems(parentalIdentifiers);
        paternalIdentifierChoiceBox.getSelectionModel().select(0);
        eclosionDate.setValue(LocalDate.now());
    }

    @FXML
    private void updateTotalFlies() {
        int total = maleSpinner.getValue() + femaleSpinner.getValue();
        totalFliesLabel.setText("Total Flies: " + total);
    }

    @FXML
    private void closeWindow() {
        ((Stage) maleSpinner.getScene().getWindow()).close();
        try {
            new CohortUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void createCohort() {
        int cohortID = 1;
        ResultSet rs = Context.getInstance().executeSQLStatement("select max(cohortID) as c from cohort");
        try {
            while (rs.next()) {
                cohortID += rs.getInt("c");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int assayID = Context.getInstance().getSelectedAssay().getAssayID();
        String date = Util.localDateToString(eclosionDate.getValue());
        Cohort cohort = new Cohort();
        cohort.setCohortID(cohortID);
        cohort.setPaternalIdentifier(paternalIdentifierChoiceBox.getValue());
        cohort.setCompletionDate(Util.timeStringToDateTime(date));
        cohort.setAgeOfPaternalParent(paternalAgeSpinner.getValue());
        String sql = "insert into cohort values (" + cohortID + ", " + assayID + ", " + cohort.getAgeOfPaternalParent()
                + ", '" + cohort.getPaternalIdentifier() + "', '" + date + "', null)";
        Context.getInstance().updateDatabase(sql);
        for (int i = 0; i < maleSpinner.getValue(); i++) {
            sql = "insert into fly values (" + cohortID + ", " + "'Male', '" + date + "', null)";
            Context.getInstance().updateDatabase(sql);
        }
        for (int i = 0; i < femaleSpinner.getValue(); i++) {
            sql = "insert into fly values (" + cohortID + ", " + "'Female', '" + date + "', null)";
            Context.getInstance().updateDatabase(sql);
        }
        closeWindow();
    }
}
