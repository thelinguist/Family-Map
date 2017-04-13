package bryce.familymap;

/**
 * Created by Bryce on 11/30/16.
 */

public class Event implements Comparable<Event> {
    private String eventID;
    private String personID;
    private Integer latitude;
    private Integer longitude;
    private String country;
    private String city;
    private String description;
    private String year;
    private String descendant;

    /**
     * I have this constructor because apparently sometimes the year isn't included in some events
     * from the server. So build it piece by piece.
     */
    public Event()
    {}

    /**
     * otherwise this is easier
     * @param eventID
     * @param personID
     * @param latitude
     * @param longitude
     * @param country
     * @param city
     * @param description
     * @param year
     * @param descendant
     */
    public Event(String eventID, String personID, Integer latitude, Integer longitude, String country, String city, String description, String year, String descendant) {
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.description = description;
        this.year = year;
        this.descendant = descendant;
    }

    public String getEventID() {
        return eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public String getYear() {
        return year;
    }

    public Integer getYearInt() {
        return Integer.parseInt(year);
    }

    public String getDescendant() {
        return descendant;
    }

    public void setpersonID(String personID) {
        this.personID = personID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    @Override
    public int compareTo(Event o) {
        if (description.toLowerCase().equals("birth")) {
            if (o.description.toLowerCase().equals("birth")) {
                if (year != null) {
                    if (o.year != null) {
                        return getYearInt().compareTo(o.getYearInt());
                    } else {
                        return -1;  //with data goes before
                    }
                } else if (o.year != null) {
                    return 1;
                } else return 1;
            } else return -1;
        }
        else if (description.toLowerCase().equals("death")) {
            if (o.description.toLowerCase().equals("death")) {
                if (year != null) {
                    if (o.year != null) {
                        return getYearInt().compareTo(o.getYearInt());
                    } else {
                        return -1;  //with data goes before
                    }
                } else if (o.year != null) {
                    return 1;
                } else return 1;
            } else return 1;
        }
        else {
            if (year != null) {
                if (o.year != null) {
                    if (getYearInt().compareTo(o.getYearInt()) == 0) {
                        if (description.toLowerCase().compareTo(o.description.toLowerCase()) == 0) {
                            return 1;
                        }
                        else return description.toLowerCase().compareTo(o.description.toLowerCase());
                    } else {
                        return getYearInt().compareTo(o.getYearInt());
                    }
                } else return 1;
            } else return -1;
        }
    }
}
