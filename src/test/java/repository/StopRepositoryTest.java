package repository;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.dao.StopDao;
import repository.dto.StopDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class StopRepositoryTest {

    @Mock
    private StopDao mock;

    private final StopDto s8382;
    private final StopDto s1000;
    private static final Pair<Integer, Integer> KEY = new Pair<>(1,8382);

    private final List<StopDto> all;

    public StopRepositoryTest() {
        System.out.println("StopRepositoryTest Constructor");
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
    }

    @BeforeEach
    void init() throws RepositoryException {
        Mockito.lenient().when(mock.get(s8382.getKey())).thenReturn(s8382);
        Mockito.lenient().when(mock.get(null)).thenThrow(RepositoryException.class);
        Mockito.lenient().when(mock.get(s1000.getKey())).thenReturn(null);
        Mockito.lenient().when(mock.getAll()).thenReturn(all);
    }

    @Test
    public void testGetExist() {
        System.out.println("testGetExist");
        //Arrange
        StopRepository repository = new StopRepository(mock);
        //Action
        StopDto result = repository.get(KEY);
        //Assert
        assertEquals(s8382, result);
        Mockito.verify(mock, times(1)).get(KEY);
    }

    @Test
    public void testGetNotExist() throws Exception {
        System.out.println("testGetNotExist");
        //Arrange
        StopRepository repository = new StopRepository(mock);
        //Action
        StopDto result = repository.get(s1000.getKey());
        //Assert
        assertNull(result);
        Mockito.verify(mock, times(1)).get(s1000.getKey());
    }

    @Test
    public void testGetIncorrectParameter() throws Exception {
        System.out.println("testGetIncorrectParameter");
        //Arrange
        Pair<Integer, Integer> incorrect = null;
        StopRepository repository = new StopRepository(mock);
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
        StopRepository repository = new StopRepository(mock);
        //Action
        List<StopDto> result = repository.getAll();
        //Assert
        assertEquals(all, result);
        Mockito.verify(mock, times(1)).getAll();
    }

    @Test
    public void testContainsExist() {
        System.out.println("testContainsExist");
        //Arrange
        StopRepository repository = new StopRepository(mock);
        //Action
        boolean result = repository.contains(s8382.getKey());
        assertTrue(result);
        Mockito.verify(mock, times(1)).getAll();
    }

    @Test
    public void testContainsNotExist() {
        System.out.println("testContainsNotExist");
        //Arrange
        StopRepository repository = new StopRepository(mock);
        //Action
        Pair<Integer, Integer> key = new Pair<>(1,1);
        boolean result = repository.contains(key);
        assertFalse(result);
        Mockito.verify(mock, times(1)).getAll();
    }

    @Test
    public void testContainsIncorrectParameters() {
        System.out.println("testContainsIncorrectParameters");
        //Arrange
        StopRepository repository = new StopRepository(mock);
        //Assert
        assertThrows(IllegalArgumentException.class, () -> {
            //Action
            repository.contains(null);
        });
        Mockito.verify(mock, never()).getAll();
    }
}
