package plant.kz.aygolek.plantyoutube.DAO;

import java.util.List;

import plant.kz.aygolek.plantyoutube.DTO.PlantDTO;
import plant.kz.aygolek.plantyoutube.DTO.SpecimenDTO;

/**
 * Created by Lagrange-Support on 30/03/2018.
 */

public interface ISpecimenDAO {
    void save(SpecimenDTO specimenDTO) throws Exception;

    List<PlantDTO> search(String searchTerm);

    List<PlantDTO> search(double latitude, double longitude, double range);

}
