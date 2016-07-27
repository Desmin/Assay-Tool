package core.drosophila;

import core.Context;
import core.util.Util;
import org.joda.time.DateTime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desmin on 6/3/2016.
 */
public class Cohort {
    private DateTime creationDate, completionDate;
    private int cohortID;
    private List<Drosophila> flies = new ArrayList<>();
    private char paternalIdentifier;
    /**
     * Age of the drosophila's pateral parent.
     */
    private int ageOfPaternalParent;

    public char getPaternalIdentifier() {
        return paternalIdentifier;
    }

    public void setPaternalIdentifier(char paternalIdentifier) {
        this.paternalIdentifier = paternalIdentifier;
    }

    public int getAgeOfPaternalParent() {
        return ageOfPaternalParent;
    }

    public void setAgeOfPaternalParent(int ageOfPaternalParent) {
        this.ageOfPaternalParent = ageOfPaternalParent;
    }

    public int getCohortID() {
        return cohortID;
    }

    public void setCohortID(int cohortID) {
        this.cohortID = cohortID;
    }

    public void setFlyList(List<Drosophila> flies) {
        this.flies = flies;
    }

    public List<Drosophila> getFlies() {
        return flies;
    }

    public long getTotalFemales() {
        return flies.stream().filter(fly -> fly.getSex().isFemale()).count();
    }

    public long getTotalMales() {
        return flies.stream().filter(fly -> fly.getSex().isMale()).count();
    }

    public DateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(DateTime completionDate) {
        this.completionDate = completionDate;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        String sql = "select count(*) as c from fly where cohortID = " + getCohortID();
        int flies = 0;
        try {
            ResultSet rs = Context.getInstance().executeSQLStatement(sql);
            while (rs.next()) {
                flies = rs.getInt("c");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String str = "Cohort " + cohortID + ": created " + Util.dateTimeToString(getCreationDate());
        str += "    Flies: " + flies;
        return str;
    }
}
