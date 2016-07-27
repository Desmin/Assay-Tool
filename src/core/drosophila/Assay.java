package core.drosophila;

import core.Context;
import core.util.Util;
import org.joda.time.DateTime;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Desmin on 6/3/2016.
 */
public class Assay {

    private int assayID;
    private DateTime creationDate;

    public int getAssayID() {
        return assayID;
    }

    public void setAssayID(int assayID) {
        this.assayID = assayID;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        String sql = "select count(*) as c from cohort where assayID = " + getAssayID();
        int cohorts = 0;
        try {
            ResultSet rs = Context.getInstance().executeSQLStatement(sql);
            while (rs.next()) {
                cohorts = rs.getInt("c");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String str = "Assay " + assayID + ": created " + Util.dateTimeToString(getCreationDate());
        str += "    Cohorts: " + cohorts;
        return str;
    }
}
