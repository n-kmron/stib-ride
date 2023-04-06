package repository.dao;

import javafx.util.Pair;
import repository.RepositoryException;
import repository.dto.StopDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for a stop of a metro line
 * The access is via a database managed by a DBManager
 */
public class StopDao implements Dao<Pair<Integer,Integer>, StopDto> {

    private Connection connexion;

    private StopDao() throws RepositoryException {
        connexion = DBManager.getInstance().getConnection();
    }

    public static StopDao getInstance() throws RepositoryException {
        return StopDaoHolder.getInstance();
    }


    @Override
    public StopDto get(Pair<Integer,Integer> key) {
        if(key == null) throw new RepositoryException("Stop Dao (get) - No key given as a parameter");
        String sql = "SELECT * FROM STOPS JOIN STATIONS ON id_station = id WHERE id_line=? and id_station=?";
        StopDto dto = null;
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setInt(1, key.getKey());
            pstmt.setInt(2, key.getValue());
            ResultSet result = pstmt.executeQuery();
            int count = 0;
            while (result.next()) {
                int line = result.getInt(1);
                int station = result.getInt(2);
                int order = result.getInt(3);
                String name = result.getString(5);
                dto =  new StopDto(line, station, order, name);
                count++;
            }
            if(count > 1) {
                throw new RepositoryException("Stop Dao (get) - Many records for a same key : " + key);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
        return dto;
    }


    @Override
    public List<StopDto> getAll() {
        List<StopDto> stops = new ArrayList<>();
        String sql = "SELECT * FROM STOPS JOIN STATIONS ON id_station = id ORDER BY id_line, id_order";
        try {
            Statement stmt = connexion.createStatement();
            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                int line = result.getInt(1);
                int station = result.getInt(2);
                int order = result.getInt(3);
                String name = result.getString(5);
                stops.add(new StopDto(line, station, order, name));
            }
        } catch (SQLException ex) {
            throw new RepositoryException(ex.getMessage());
        }
        return stops;
    }

    /**
     * Singleton pattern for this Dao
     */
    private static class StopDaoHolder {
        private static StopDao getInstance() throws RepositoryException {
            return new StopDao();
        }
    }

}

