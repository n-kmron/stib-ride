package repository;

import repository.dao.ShortcutDao;
import repository.dto.ShortcutDto;

import java.util.List;

/**
 * Repository for a Shortcut (the key is the name of the Shortcut)
 */
public class ShortcutRepository implements Repository<String, ShortcutDto>{

    private final ShortcutDao dao;

    public ShortcutRepository() {
        this.dao = ShortcutDao.getInstance();
    }

    public ShortcutRepository(ShortcutDao dao) {
        this.dao = dao;
    }

    public void add(ShortcutDto item) throws RepositoryException {
        if(item == null) {
            throw new RepositoryException("Shortcut Repository (add) - No key given as parameter");
        }
        if(contains(item.getKey())) {
            dao.update(item);
        } else {
            dao.insert(item);
        }
    }

    public void remove(String key) throws RepositoryException {
        if(key == null || !contains(key)) {
            throw new IllegalArgumentException("Shortcut Repository (remove) - The shortcut does not exist");
        }
        dao.delete(key);
    }

    @Override
    public ShortcutDto get(String key) throws RepositoryException {
        if(key == null) throw new RepositoryException("Shortcut Repository (get) - No key given as parameter");
        if(!contains(key)) throw new RepositoryException("Shortcut repository (get) - Item to grab does not exist");
        return dao.get(key);
    }

    @Override
    public List<ShortcutDto> getAll() throws RepositoryException {
        return dao.getAll();
    }

    @Override
    public boolean contains(String key) {
        if(key == null) {
            throw new IllegalArgumentException("Shortcut repository (contains) - Invalid argument (key is null)");
        }
        List<ShortcutDto> Shortcuts = dao.getAll();
        for(ShortcutDto shortcut : Shortcuts) {
            if(shortcut.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }


    
}
