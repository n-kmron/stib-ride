package repository.dao;

import config.ConfigManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import repository.RepositoryException;
import repository.dto.StationDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StationDaoTest {
    private final StationDto s8012;
    private final StationDto s1000;
    private static final Integer KEY = 8012;

    private final List<StationDto> all;
    private StationDao dao;

    public StationDaoTest() {
        System.out.println("==== StudentDaoTest Constructor =====");
        s8012 = new StationDto(KEY, "De Brouckere");
        s1000 = new StationDto(1000, "Mediacite");

        all = new ArrayList<>();
        all.add(s8012);
        all.add(new StationDto(8022, "Gare centrale"));
        all.add(new StationDto(8272, "Sainte-Catherine"));
        all.add(new StationDto(8282, "Comte de flandre"));
        all.add(new StationDto(8292, "Etangs noirs"));
        all.add(new StationDto(8382, "Gare de l'ouest"));
        all.add(new StationDto(8742, "Beekkant"));
        try {
            ConfigManager.getInstance().load();
            dao = StationDao.getInstance();
        } catch (RepositoryException ex) {
            Assertions.fail("Error connecting to test database\n", ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testSelectExist() throws Exception {
        System.out.println("testSelectExist");
        //Action
        StationDto result = dao.get(KEY);
        //Assert
        assertEquals(s8012, result);
    }

    @Test
    public void testSelectNotExist() {
        System.out.println("testSelectNotExist");
        //Action
        StationDto result = dao.get(s1000.getKey());
        //Assert
        assertNull(result);
    }

    @Test
    public void testSelectIncorrectParameter() throws Exception {
        System.out.println("testSelectIncorrectParameter");
        //Arrange
        Integer incorrect = null;
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            dao.get(incorrect);
        });
    }


    @Test
    public void testGetAllExist() {
        System.out.println("testGetAllExist");
        //Arrange
        List<StationDto> expected = all;
        //Action
        List<StationDto> result = dao.getAll();
        //Assert
        assertEquals(expected, result);
    }
}