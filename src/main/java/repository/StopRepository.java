package repository;

import javafx.util.Pair;
import repository.dao.StopDao;
import repository.dto.StopDto;
import java.util.List;

/**
 * Repository for a stop of a metro line (the key is a pair of his line and his station)
 */
public class StopRepository implements Repository<Pair<Integer, Integer>, StopDto> {

    private final StopDao dao;

    public StopRepository() {
        this.dao = StopDao.getInstance();
    }

    public StopRepository(StopDao dao) {
        this.dao = dao;
    }

    @Override
    public StopDto get(Pair<Integer, Integer> key) throws RepositoryException {
        if(key == null) throw new RepositoryException("Stop Repository (get) - No key given as parameter");
        if(!contains(key)) throw new RepositoryException("Stop repository (get) - Item to grab does not exist");
        return dao.get(key);
    }

    @Override
    public List<StopDto> getAll() throws RepositoryException {
        return dao.getAll();
    }

    @Override
    public boolean contains(Pair<Integer, Integer> key) {
        if(key == null) {
            throw new IllegalArgumentException("Stop repository (contains) - Invalid argument (key is null)");
        }
        List<StopDto> stops = dao.getAll();
        for(StopDto stop : stops) {
            if(stop.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }
}

