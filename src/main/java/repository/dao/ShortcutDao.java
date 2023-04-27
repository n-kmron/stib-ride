package repository.dao;

import repository.RepositoryException;
import repository.dto.ShortcutDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for a shortcut
 * The access is via a database managed by a DBManager
 */
public class ShortcutDao implements Dao<String, ShortcutDto>{

    private Connection connexion;

    private ShortcutDao() throws RepositoryException {
        connexion = DBManager.getInstance().getConnection();
    }

    public static ShortcutDao getInstance() throws RepositoryException {
        return ShortcutDao.ShortcutDaoHolder.getInstance();
    }


    @Override
    public ShortcutDto get(String key) {
        if(key == null) throw new RepositoryException("Shortcut Dao (get) - No key given as a parameter");
        String sql = "SELECT * FROM SHORTCUTS WHERE name=?";
        ShortcutDto dto = null;
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setString(1, key);
            ResultSet result = pstmt.executeQuery();
            int count = 0;
            while (result.next()) {
                String name = result.getString(1);
                int source = result.getInt(2);
                int destination = result.getInt(3);
                dto =  new ShortcutDto(name, source, destination);
                count++;
            }
            if(count > 1) {
                throw new RepositoryException("Shortcut Dao (get) - Plusieurs records pour une même clé : " + key);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return dto;
    }


    @Override
    public List<ShortcutDto> getAll() {
        List<ShortcutDto> shortcuts = new ArrayList<>();
        String sql = "SELECT * FROM SHORTCUTS;";
        try {
            Statement stmt = connexion.createStatement();
            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                String name = result.getString(1);
                int source = result.getInt(2);
                int destination = result.getInt(3);
                shortcuts.add(new ShortcutDto(name, source, destination));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RepositoryException(ex.getMessage());
        }
        return shortcuts;
    }

    public void insert(ShortcutDto item) {
        if (item == null) {
            throw new RepositoryException("Shortcut Dao (insert) - No shortcut given as a parameter");
        }
        if(get(item.getKey()) != null && get(item.getKey()).equals(item)) {
            throw new RepositoryException("Shortcut Dao (insert) - Shortcut already exists");
        }
        Integer id = 0;
        String sql = "INSERT INTO SHORTCUTS(name,source,destination) values(?, ?, ?)";
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setString(1, item.getKey());
            pstmt.setInt(2, item.getSource());
            pstmt.setInt(3, item.getDestination());
            pstmt.executeUpdate();

            ResultSet result = pstmt.getGeneratedKeys();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException(e.getMessage());
        }
    }

    public void delete(String key) {
        if(key == null) throw new RepositoryException("Shortcut Dao (delete) - No shortcut given as a parameter");
        if(get(key) == null) {
            throw new RepositoryException("Shortcut Dao (delete) - Shortcut does not exists");
        }
        String sql = "DELETE FROM SHORTCUTS WHERE name=?";
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setString(1, key);
            int count = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public void update(ShortcutDto item) {
        if(item == null) throw new RepositoryException("Shortcut Dao (update) - No shortcut given as a parameter");
        if(get(item.getKey()) == null) {
            throw new RepositoryException("Shortcut Dao (update) - Shortcut does not exists");
        }
        String sql = "UPDATE SHORTCUTS SET source=?,destination=? where name=?";
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setInt(1, item.getSource());
            pstmt.setInt(2, item.getDestination());
            pstmt.setString(3, item.getKey());
            int count = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }


    /**
     * Singleton pattern for this Dao
     */
    private static class ShortcutDaoHolder {
        private static ShortcutDao getInstance() throws RepositoryException {
            return new ShortcutDao();
        }
    }
}
