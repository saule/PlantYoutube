package plant.kz.aygolek.plantyoutube.DAO;

import java.util.ArrayList;
import java.util.List;

import plant.kz.aygolek.plantyoutube.DTO.PlantDTO;
import plant.kz.aygolek.plantyoutube.DTO.SpecimenDTO;

/**
 * Created by Lagrange-Support on 29/03/2018.
 */

public class SpecimenDAOStub implements ISpecimenDAO {
    @Override
    public void save(SpecimenDTO specimen) throws Exception {
        if (specimen.getCache_id() == 0 && specimen.getGuid() == 0){
            throw new Exception("A plant is not associated with this specimen. Please select a plant.");
        }
    }

    @Override
    public List<PlantDTO> search(String searchTerm) {
        List<PlantDTO> allPlants = new ArrayList<>();

        if (searchTerm.contains("Redbud")){
            //create a mockup plant
            PlantDTO plant = new PlantDTO();
            plant.setGuid(83);
            plant.setGenus("Cercis");
            plant.setSpecies("canadensis");
            plant.setCommon("Eastern Redbud");

            //create a speciemn that associate with the plant
            SpecimenDTO specimen = new SpecimenDTO();
            specimen.setLatitude("84.57");
            specimen.setLongitude("39.47");
            specimen.setLocation("cincinnati");

            List<SpecimenDTO> allSpecimens = new ArrayList<>();
            allSpecimens.add(specimen);

            //add specimen collection to the plant
            plant.setSpecimenDTO(allSpecimens);
            allPlants.add(plant);

        }
        return allPlants;
    }

    @Override
    public List<PlantDTO> search(double latitude, double longitude, double range) {
        return null;
    }
}
