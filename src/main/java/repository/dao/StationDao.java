package repository.dao;

import model.Language;
import repository.RepositoryException;
import repository.dto.StationDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for a station of a metro line
 * The access is via a database managed by a DBManager
 */
public class StationDao implements Dao<Integer, StationDto> {

    private final Connection connexion;

    private final Language language;

    private StationDao(Language language) throws RepositoryException {
        connexion = DBManager.getInstance().getConnection();
        this.language = language;
    }

    public static StationDao getInstance(Language language) throws RepositoryException {
        return StationDaoHolder.getInstance(language);
    }


    @Override
    public StationDto get(Integer key) {
        if(key == null) throw new RepositoryException("Station Dao (get) - No key given as a parameter");
        String sql = "SELECT * FROM STATIONS WHERE id=?";
        if(this.language == Language.NL) {
            sql = "SELECT * FROM STATIONS_NL WHERE id=?";
        }
        StationDto dto = null;
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setInt(1, key);
            ResultSet result = pstmt.executeQuery();
            int count = 0;
            while (result.next()) {
                int id = result.getInt(1);
                String name = result.getString(2);
                dto =  new StationDto(id, name);
                count++;
            }
            if(count > 1) {
                throw new RepositoryException("Station Dao (get) - Many records for a same key : " + key);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return dto;
    }


    @Override
    public List<StationDto> getAll() {
        List<StationDto> stations = new ArrayList<>();
        String sql = "SELECT * FROM STATIONS;";
        if(language == Language.NL) {
            sql = "SELECT * FROM STATIONS_NL;";
        }
        try {
            Statement stmt = connexion.createStatement();
            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                int id = result.getInt(1);
                String name = result.getString(2);
                stations.add(new StationDto(id, name));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RepositoryException(ex.getMessage());
        }
        return stations;
    }

    /**
     * Singleton pattern for this Dao
     */
    private static class StationDaoHolder {
        private static StationDao getInstance(Language language) throws RepositoryException {
            return new StationDao(language);
        }
    }

}

