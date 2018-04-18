package plant.kz.aygolek.plantyoutube.DTO;

/**
 * Created by Lagrange-Support on 30/03/2018.
 */

public class SpecimenDTO {
    private long cache_id;
    private int guid;
    private String location;
    private String latitude;
    private String longitude;
    private String description;
    private String picture_URI;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }



    public long getCache_id() {
        return cache_id;
    }

    public void setCache_id(long cache_id) {
        this.cache_id = cache_id;
    }

    public int getGuid() {
        return guid;
    }

    public void setGuid(int guid) {
        this.guid = guid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture_URI() {
        return picture_URI;
    }

    public void setPicture_URI(String picture_URI) {
        this.picture_URI = picture_URI;
    }

    public String toString() {
        return location+" "+latitude+" "+longitude+" "+description+" "+picture_URI;
    }
}
