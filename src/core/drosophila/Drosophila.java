package core.drosophila;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Objects;

/**
 * Defines the attributes we're interested in tracking.
 * <p>
 * Created by Desmin Little on 5/28/2016.
 */
public class Drosophila {

    /**
     * DateTime objects to represent that dates that the drosophila were born and when they died.
     */
    private DateTime dateOfEclosion, dateOfDeath;
    /**
     * The sex of the drosophila.
     */
    private Sex sex;

    public DateTime getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(DateTime dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public DateTime getDateOfEclosion() {
        return dateOfEclosion;
    }

    public void setDateOfEclosion(DateTime dateOfEclosion) {
        this.dateOfEclosion = dateOfEclosion;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    /**
     * Calculates the total days this drosophila has lived.
     *
     * @return The total days this drosophila has lived.
     */
    private int getTotalDaysLived() {
        Objects.requireNonNull(dateOfEclosion);
        Objects.requireNonNull(dateOfDeath);
        return new Duration(dateOfEclosion, dateOfDeath).toStandardDays().getDays();
    }

    /**
     * An enum to represent the sex a drosophila can be.
     */
    public enum Sex {
        MALE, FEMALE;

        public boolean isMale() {
            return this.equals(MALE);
        }

        public boolean isFemale() {
            return this.equals(FEMALE);
        }
    }
}
