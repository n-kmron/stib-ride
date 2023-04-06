package repository.dao;

import config.ConfigManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import repository.RepositoryException;
import repository.dto.ShortcutDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShortcutDaoTest {

    private final ShortcutDto s1;
    private final ShortcutDto s2;
    private static final String KEY = "Domicile";

    private ShortcutDao dao;
    private final List<ShortcutDto> all;

    public ShortcutDaoTest() {
        System.out.println("ShortcutRepositoryTest Constructor");
        //Test data
        s1 = new ShortcutDto(KEY, "DE BROUCKERE", "PARC");
        s2 = new ShortcutDto("Fake", "GARE CENTRALE", "ART-LOI");

        all = new ArrayList<>();
        all.add(s1);
        all.add(new ShortcutDto("s3", "GARE CENTRALE", "MERODE"));
        all.add(new ShortcutDto("s4", "SAINTE-CATHERINE", "ETANGS NOIRS"));
        all.add(new ShortcutDto("s5", "BOCKSTAEL", "PANNENHUIS"));
        all.add(new ShortcutDto("s6", "ROGIER", "MADOU"));
        try {
            ConfigManager.getInstance().load();
            dao = ShortcutDao.getInstance();
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
        ShortcutDto result = dao.get(KEY);
        //Assert
        assertEquals(s1, result);
    }

    @Test
    public void testSelectNotExist() {
        System.out.println("testSelectNotExist");
        //Action
        ShortcutDto result = dao.get(s2.getKey());
        //Assert
        assertNull(result);
    }

    @Test
    public void testSelectIncorrectParameter() throws Exception {
        System.out.println("testSelectIncorrectParameter");
        //Arrange
        String incorrect = null;
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
        List<ShortcutDto> expected = all;
        //Action
        List<ShortcutDto> result = dao.getAll();
        //Assert
        assertEquals(expected, result);
    }

    @Test
    public void testInsertExist() throws Exception {
        System.out.println("testInsertExist");
        //Arrange
        ShortcutDto expected = new ShortcutDto(KEY, "DE BROUCKERE", "PARC");
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            dao.insert(expected);
        });
    }

    @Test
    public void testInsertNotExist() {
        System.out.println("testInsertNotExist");
        //Arrange
        ShortcutDto expected = new ShortcutDto("NewOne", "MERODE", "PLOPSA");
        //Action
        dao.insert(expected);
        ShortcutDto result = dao.get("NewOne");
        //Assert
        assertEquals(expected, result);
    }

    @Test
    public void testInsertIncorrectParameter() throws Exception{
        System.out.println("testInsertIncorrectParameter");
        //Arrange
        ShortcutDto incorrect = null;
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            dao.insert(incorrect);
        });
    }


    @Test
    public void testUpdateExist() {
        System.out.println("testUpdateExist");
        //Arrange
        ShortcutDto expected = new ShortcutDto("NewOne", "MERODE", "PLOPSA");
        //Action
        expected.setSource("Edited");
        dao.update(expected);
        ShortcutDto result = dao.get("NewOne");
        //Assert
        assertEquals(expected, result);
    }

    @Test
    public void testUpdateNotExist() throws Exception {
        System.out.println("testUpdateNotExist");
        //Arrange
        ShortcutDto expected = new ShortcutDto("Fake", "IPPO", "MENTALITY");
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            dao.update(expected);
        });
    }

    @Test
    public void testUpdateIncorrectParameter() throws Exception{
        System.out.println("testUpdateIncorrectParameter");
        //Arrange
        ShortcutDto incorrect = null;
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            dao.update(incorrect);
        });
    }

    @Test
    public void testDeleteExist() {
        System.out.println("testDeleteExist");
        //Arrange
        //Action
        dao.delete("s3");
        ShortcutDto result = dao.get("s3");
        //Assert
        assertNull(result);
    }

    @Test
    public void testDeleteNotExist() throws Exception {
        System.out.println("testDeleteNotExist");
        //Arrange
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            dao.delete("Fake");
        });
    }

    @Test
    public void testDeleteIncorrectParameter() throws Exception{
        System.out.println("testUpdateIncorrectParameter");
        //Arrange
        String incorrect = null;
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            dao.delete(incorrect);
        });
    }


}