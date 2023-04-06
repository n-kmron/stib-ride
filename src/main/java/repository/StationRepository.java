package repository;

import repository.dao.StationDao;
import repository.dto.StationDto;
import java.util.List;

/**
 * Repository for a Station of a metro line (the key is the id of the station)
 */
public class StationRepository implements Repository<Integer, StationDto> {

    private final StationDao dao;

    public StationRepository() {
        this.dao = StationDao.getInstance();
    }

    public StationRepository(StationDao dao) {
        this.dao = dao;
    }

    @Override
    public StationDto get(Integer key) throws RepositoryException {
        if(key == null) throw new RepositoryException("Station Repository (get) - No key given as parameter");
        if(!contains(key)) throw new RepositoryException("Station repository (get) - Item to grab does not exist");
        return dao.get(key);
    }

    @Override
    public List<StationDto> getAll() throws RepositoryException {
        return dao.getAll();
    }

    @Override
    public boolean contains(Integer key) {
        if(key == null) {
            throw new IllegalArgumentException("Station repository (contains) - Invalid argument (key is null)");
        }
        List<StationDto> stations = dao.getAll();
        for(StationDto station : stations) {
            if(station.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }
}

