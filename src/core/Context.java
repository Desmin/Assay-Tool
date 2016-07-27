package core;

import core.drosophila.Assay;
import core.drosophila.Cohort;

import java.sql.*;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Desmin on 6/5/2016.
 */
public class Context {

    private static final String SQL_DATABASE_ADDRESS = "jdbc:mysql://localhost:3306/fly database";
    private static final String DATABASE_USERNAME = "flybase";
    private static final String DATABASE_PASSWORD = "how2";
    private static Context instance = new Context();
    private Connection databaseConnection;
    private Logger logger = Logger.getLogger("Context");
    private Cohort selectedCohort;
    private Assay selectedAssay;

    public static Context getInstance() {
        return instance;
    }

    public Cohort getSelectedCohort() {
        return selectedCohort;
    }

    public void setSelectedCohort(Cohort selectedCohort) {
        this.selectedCohort = selectedCohort;
    }

    public Assay getSelectedAssay() {
        return selectedAssay;
    }

    public void setSelectedAssay(Assay selectedAssay) {
        this.selectedAssay = selectedAssay;
    }

    public boolean startServices() throws Exception {
        if (Objects.nonNull(databaseConnection))
            return true;
        logger.log(Level.INFO, "Starting services...");
        if (!startSQLService())
            return false;
        logger.log(Level.INFO, "Services started!");
        return true;
    }

    public void stopServices() {
        logger.log(Level.INFO, "Stopping services...");
        try {
            databaseConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "Services stopped.");
    }

    private boolean startSQLService() {
        try {
            logger.log(Level.INFO, "Creating database connection...");
            Class.forName("com.mysql.jdbc.Driver");
            databaseConnection = DriverManager.getConnection(SQL_DATABASE_ADDRESS, DATABASE_USERNAME, DATABASE_PASSWORD);
            logger.log(Level.INFO, "Connection to database achieved!");
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Issue connecting to database!", e);
            return false;
        }
    }

    public ResultSet executeSQLStatement(String query) {
        ResultSet set = null;
        if (Objects.isNull(databaseConnection))
            if (!startSQLService())
                return null;
        Statement sqlStatement = null;
        try {
            sqlStatement = databaseConnection.createStatement();
            set = sqlStatement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return set;
    }

    public void updateDatabase(String query) {
        if (Objects.isNull(databaseConnection))
            if (!startSQLService())
                return;
        Statement sqlStatement = null;
        try {
            sqlStatement = databaseConnection.createStatement();
            sqlStatement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
