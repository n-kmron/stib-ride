package repository.dao;

import config.ConfigManager;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import repository.RepositoryException;
import repository.dto.StopDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class StopDaoTest {
    private final StopDto s8382;
    private final StopDto s1000;
    private static final Pair<Integer, Integer> KEY = new Pair<>(1,8382);

    private final List<StopDto> all;
    private StopDao dao;

    public StopDaoTest() {
        System.out.println("==== StopDaoTest Constructor =====");
        //Test data
        s8382 = new StopDto(1, 8382, 1, "GARE DE L'OUEST");
        s1000 = new StopDto(1, 1000, 5, "FAKE STATION");

        all = new ArrayList<>();
        all.add(s8382);
        all.add(s1000);
        all.add(new StopDto(1, 8742, 2, "BEEKKANT"));
        all.add(new StopDto(1, 8292, 3, "ETANGS NOIRS"));
        all.add(new StopDto(1, 8282, 4, "COMTE DE FLANDRE"));
        all.add(new StopDto(1, 8272, 5, "SAINTE-CATHERINE"));
        all.add(new StopDto(1, 8012, 6, "DE BROUCKERE"));
        all.add(new StopDto(1, 8022, 7, "GARE CENTRALE"));
        try {
            ConfigManager.getInstance().load();
            dao = StopDao.getInstance();
        } catch (RepositoryException ex) {
            Assertions.fail("Error connecting to test database\n", ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testSelectExist() throws Exception {
        System.out.println("testSelectExist");
        //Arrange
        StopDto expected = s8382;
        //Action
        StopDto result = dao.get(KEY);
        //Assert
        assertEquals(expected, result);
    }

    @Test
    public void testSelectNotExist() throws Exception {
        System.out.println("testSelectNotExist");
        //Action
        StopDto result = dao.get(s1000.getKey());
        //Assert
        assertNull(result);
    }

    @Test
    public void testSelectIncorrectParameter() throws Exception {
        System.out.println("testSelectIncorrectParameter");
        //Arrange
        Pair<Integer, Integer> incorrect = null;
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            dao.get(incorrect);
        });
    }


    @Test
    public void testGetAllExist() throws Exception {
        System.out.println("testGetAllExist");
        //Arrange
        List<StopDto> expected = all;
        //Action
        List<StopDto> result = dao.getAll();
        //Assert
        assertEquals(expected, result);
    }

}
