package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.dao.StationDao;
import repository.dto.StationDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class StationRepositoryTest {
    @Mock
    private StationDao mock;

    private final StationDto s8012;
    private final StationDto s1000;
    private static final Integer KEY = 8012; //de brouckère

    private final List<StationDto> all;

    public StationRepositoryTest() {
        System.out.println("StationRepositoryTest Constructor");
        //Test data
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
    }

    @BeforeEach
    void init() throws RepositoryException {
        Mockito.lenient().when(mock.get(s8012.getKey())).thenReturn(s8012);
        Mockito.lenient().when(mock.get(null)).thenThrow(RepositoryException.class);
        Mockito.lenient().when(mock.get(s1000.getKey())).thenReturn(null);
        Mockito.lenient().when(mock.getAll()).thenReturn(all);
    }

    @Test
    public void testGetExist() {
        System.out.println("testGetExist");
        //Arrange
        StationDto expected = s8012;
        StationRepository repository = new StationRepository(mock);
        //Action
        StationDto result = repository.get(KEY);
        //Assert
        assertEquals(expected, result);
        Mockito.verify(mock, times(1)).get(KEY);
    }

    @Test
    public void testGetNotExist() throws RepositoryException {
        System.out.println("testGetNotExist");
        //Arrange
        StationRepository repository = new StationRepository(mock);
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            repository.get(s1000.getKey());
        });
        Mockito.verify(mock, times(1)).getAll();
    }

    @Test
    public void testGetIncorrectParameter() throws RepositoryException {
        System.out.println("testGetIncorrectParameter");
        //Arrange
        Integer incorrect = null;
        StationRepository repository = new StationRepository(mock);
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
        StationRepository repository = new StationRepository(mock);
        //Action
        List<StationDto> result = repository.getAll();
        //Assert
        assertEquals(all, result);
        Mockito.verify(mock, times(1)).getAll();
    }

    @Test
    public void testContainsExist() {
        System.out.println("testContainsExist");
        //Arrange
        StationRepository repository = new StationRepository(mock);
        //Action
        boolean result = repository.contains(s8012.getKey());
        assertTrue(result);
        Mockito.verify(mock, times(1)).getAll();
    }

    @Test
    public void testContainsNotExist() {
        System.out.println("testContainsNotExist");
        //Arrange
        StationRepository repository = new StationRepository(mock);
        //Action
        Integer key = 1000;
        boolean result = repository.contains(key);
        assertFalse(result);
        Mockito.verify(mock, times(1)).getAll();
    }

    @Test
    public void testContainsIncorrectParameters() {
        System.out.println("testContainsIncorrectParameters");
        //Arrange
        StationRepository repository = new StationRepository(mock);
        //Assert
        assertThrows(IllegalArgumentException.class, () -> {
            //Action
            repository.contains(null);
        });
        Mockito.verify(mock, never()).getAll();
    }
}