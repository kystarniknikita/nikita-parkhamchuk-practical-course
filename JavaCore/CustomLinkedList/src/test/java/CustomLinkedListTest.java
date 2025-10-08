import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CustomLinkedListTest {
    private CustomLinkedList<Integer> customLinkedList;

    @BeforeEach
    void setUp(){
        customLinkedList = new CustomLinkedList<>();
    }

    @Test
    @DisplayName("Test add with wrong index")
    void whenGiveWrongIndex_thenThrowException(){
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> customLinkedList.add(-1, 1));
        Assertions.assertThrows(IndexOutOfBoundsException.class,() -> customLinkedList.add(1, 1) );
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> customLinkedList.remove(-1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> customLinkedList.remove(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> customLinkedList.get(-1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> customLinkedList.get(1));
    }

    @Test
    @DisplayName("Test addFirst in list")
    void testAddFirst(){
        customLinkedList.addFirst(1);
        customLinkedList.addFirst(2);
        Assertions.assertEquals(2, customLinkedList.size());
        Assertions.assertEquals(2, customLinkedList.get(0));
    }

    @Test
    @DisplayName("Test addLast in list")
    void testAddLast(){
        customLinkedList.addLast(1);
        customLinkedList.addLast(2);
        Assertions.assertEquals(2, customLinkedList.size());
        Assertions.assertEquals(2, customLinkedList.get(1));
    }

    @Test
    @DisplayName("Test getFirst in list")
    void testGetFirst(){
        customLinkedList.addFirst(1);
        customLinkedList.addFirst(2);
        Assertions.assertEquals(2, customLinkedList.getFirst());
    }

    @Test
    @DisplayName("Test getLast in list")
    void testGetLast(){
        customLinkedList.addFirst(1);
        customLinkedList.addFirst(2);
        Assertions.assertEquals(1, customLinkedList.getLast());
    }

    @Test
    @DisplayName("Test size is always correct")
    void testSize(){
        customLinkedList.addFirst(1);
        Assertions.assertEquals(1, customLinkedList.size());
        customLinkedList.removeLast();
        Assertions.assertEquals(0, customLinkedList.size());
    }
}
