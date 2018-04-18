package plant.kz.aygolek.plantyoutube.DTO;

import java.util.List;

/**
 * Created by Lagrange-Support on 26/03/2018.
 */

public class PlantDTO {
    private long cacheID;
    public int guid;//global identifier
    private String genus;
    private String species;
    private String cultivar;
    private String common;

    private List<SpecimenDTO> specimenDTO;

    public List<SpecimenDTO> getSpecimenDTO() {
        return specimenDTO;
    }

    public void setSpecimenDTO(List<SpecimenDTO> specimenDTO) {
        this.specimenDTO = specimenDTO;
    }
    public long getCacheID() {
        return cacheID;
    }

    public void setCacheID(long cacheID) {
        this.cacheID = cacheID;
    }


    public int getGuid() {
        return guid;
    }

    public void setGuid(int guid) {
        this.guid = guid;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getCultivar() {
        return cultivar;
    }

    public void setCultivar(String cultivar) {
        this.cultivar = cultivar;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public String toString(){
        return genus+" "+species+" "+cultivar+" "+common;
    }
}
