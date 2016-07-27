package core.ui.controllers;

import core.Context;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Desmin on 6/10/2016.
 */
public class RecordDeathsController {

    @FXML
    private Spinner<Integer> maleSpinner, femaleSpinner;

    @FXML
    private DatePicker dateOfDeath;

    @FXML
    private Button recordDeathsButton;

    @FXML
    private void recordDeaths() {

    }

    @FXML
    private void initialize() {
        int males = 0, females = 0;
        int cohortID = Context.getInstance().getSelectedCohort().getCohortID();
        String sql = "select count(*) as c from fly where cohortID = %1$s and sex = '%2$s' and dateOfDeath is null";
        ResultSet maleResult = Context.getInstance().executeSQLStatement(String.format(sql, cohortID, "Male"));
        ResultSet femaleResult = Context.getInstance().executeSQLStatement(String.format(sql, cohortID, "Female"));
        try {
            while (maleResult.next()) {
                males = maleResult.getInt("c");
            }
            while (femaleResult.next()) {
                females = femaleResult.getInt("c");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SpinnerValueFactory<Integer> maleSpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, males, 1);
        SpinnerValueFactory<Integer> femaleSpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, females, 1);
        maleSpinner.setValueFactory(maleSpinnerFactory);
        femaleSpinner.setValueFactory(femaleSpinnerFactory);
    }
}
