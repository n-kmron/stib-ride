package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.dao.ShortcutDao;
import repository.dto.ShortcutDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ShortcutRepositoryTest {

    @Mock
    private ShortcutDao mock;

    private final ShortcutDto s1;
    private final ShortcutDto s2;
    private static final String KEY = "Domicile";

    private final List<ShortcutDto> all;

    public ShortcutRepositoryTest() {
        System.out.println("ShortcutRepositoryTest Constructor");
        //Test data
        s1 = new ShortcutDto(KEY, 8012, 8032);
        s2 = new ShortcutDto("Fake", 8022, 8042);

        all = new ArrayList<>();
        all.add(s1);
        all.add(new ShortcutDto("s3", 8022, 8072));
        all.add(new ShortcutDto("s4", 8272, 8292));
        all.add(new ShortcutDto("s5", 8794, 8784));
        all.add(new ShortcutDto("s6", 8432, 8412));
    }

    @BeforeEach
    void init() throws RepositoryException {
        Mockito.lenient().when(mock.get(s1.getKey())).thenReturn(s1);
        Mockito.lenient().when(mock.get(null)).thenThrow(RepositoryException.class);
        Mockito.lenient().when(mock.get(s2.getKey())).thenReturn(null);
        Mockito.lenient().when(mock.getAll()).thenReturn(all);
    }

    @Test
    public void testGetExist() {
        System.out.println("testGetExist");
        //Arrange
        ShortcutDto expected = s1;
        ShortcutRepository repository = new ShortcutRepository(mock);
        //Action
        ShortcutDto result = repository.get(KEY);
        //Assert
        assertEquals(expected, result);
        Mockito.verify(mock, times(1)).get(KEY);
    }

    @Test
    public void testGetNotExist() throws RepositoryException {
        System.out.println("testGetNotExist");
        //Arrange
        ShortcutRepository repository = new ShortcutRepository(mock);
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            repository.get(s2.getKey());
        });
        Mockito.verify(mock, times(1)).getAll();
    }

    @Test
    public void testGetIncorrectParameter() throws RepositoryException {
        System.out.println("testGetIncorrectParameter");
        //Arrange
        String incorrect = null;
        ShortcutRepository repository = new ShortcutRepository(mock);
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            repository.get(incorrect);
        });
        Mockito.verify(mock, never()).get(incorrect);
    }

    @Test
    public void testGetAllExist() throws Exception {
        System.out.println("testGetAllExist");
        //Arrange
        ShortcutRepository repository = new ShortcutRepository(mock);
        //Action
        List<ShortcutDto> result = repository.getAll();
        //Assert
        assertEquals(all, result);
        Mockito.verify(mock, times(1)).getAll();
    }

    @Test
    public void testContainsExist() {
        System.out.println("testContainsExist");
        //Arrange
        ShortcutRepository repository = new ShortcutRepository(mock);
        //Action
        boolean result = repository.contains(s1.getKey());
        assertTrue(result);
        Mockito.verify(mock, times(1)).getAll();
    }

    @Test
    public void testContainsNotExist() {
        System.out.println("testContainsNotExist");
        //Arrange
        ShortcutRepository repository = new ShortcutRepository(mock);
        //Action
        String key = "Fake";
        boolean result = repository.contains(key);
        assertFalse(result);
        Mockito.verify(mock, times(1)).getAll();
    }

    @Test
    public void testContainsIncorrectParameters() {
        System.out.println("testContainsIncorrectParameters");
        //Arrange
        ShortcutRepository repository = new ShortcutRepository(mock);
        //Assert
        assertThrows(IllegalArgumentException.class, () -> {
            //Action
            repository.contains(null);
        });
        Mockito.verify(mock, never()).getAll();
    }

    @Test
    public void testAddExist() throws Exception {
        System.out.println("testAddExist");
        //Arrange
        ShortcutRepository repository = new ShortcutRepository(mock);
        ShortcutDto myShortcut = new ShortcutDto("Domicile", 2222, 1111);
        //Action
        repository.add(myShortcut);
        Mockito.verify(mock, times(1)).update(any(ShortcutDto.class));
    }

    @Test
    public void testAddNotExist() throws Exception {
        System.out.println("testAddNotExist");
        //Arrange
        ShortcutRepository repository = new ShortcutRepository(mock);
        ShortcutDto myShortcut = new ShortcutDto("Test", 2222, 1111);
        //Action
        repository.add(myShortcut);
        //Assert
        Mockito.verify(mock, times(1)).insert(myShortcut);
    }

    @Test
    public void testAddIncorrectParameter() throws Exception {
        System.out.println("testAddIncorrectParameter");
        //Arrange
        ShortcutRepository repository = new ShortcutRepository(mock);
        ShortcutDto incorrect = null;
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            repository.add(incorrect);
        });
        Mockito.verify(mock, times(0)).insert(any(ShortcutDto.class));
    }

    @Test
    public void testRemoveExist() throws Exception {
        System.out.println("testRemoveExist");
        //Arrange
        ShortcutRepository repository = new ShortcutRepository(mock);
        ShortcutDto myShortcut = new ShortcutDto("Domicile", 1111, 2222);
        //Action
        repository.remove(myShortcut.getKey());
        //Assert
        Mockito.verify(mock, times(1)).delete(myShortcut.getKey());

    }

    @Test
    public void testRemoveNotExist() throws Exception {
        System.out.println("testRemoveNotExist");
        //Arrange
        ShortcutRepository repository = new ShortcutRepository(mock);
        //Assert
        assertThrows(IllegalArgumentException.class, () -> {
            //Action
            repository.remove("Fake");
        });
        Mockito.verify(mock, never()).delete(any(String.class));
    }

    @Test
    public void testRemoveIncorrectParameter() throws Exception {
        System.out.println("testRemoveIncorrectParameter");
        //Arrange
        ShortcutRepository repository = new ShortcutRepository(mock);
        String incorrect = null;
        //Assert
        assertThrows(IllegalArgumentException.class, () -> {
            //Action
            repository.remove(incorrect);
        });
        Mockito.verify(mock, never()).delete(incorrect);
    }



}