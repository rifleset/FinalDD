import org.junit.Before;
import org.junit.Test;
import java.util.Iterator;
import java.util.NoSuchElementException;
import static org.junit.Assert.*;

public class DequeIteratorBehaviorTests {

    private CircularArrayDeQueue<Integer> deque;

    @Before
    public void setUp() {
        deque = new CircularArrayDeQueue<>();
    }

    @Test
    public void testIteratorOnEmptyDeque() {
        Iterator<Integer> it = deque.iterator();
        assertFalse("Iterator should not have next on empty deque", it.hasNext());
    }

    @Test
    public void testDescendingIteratorOnEmptyDeque() {
        Iterator<Integer> it = deque.descendingIterator();
        assertFalse("Descending iterator should not have next on empty deque", it.hasNext());
    }

    @Test
    public void testSingleElementIteration() {
        deque.pushLast(1);
        Iterator<Integer> it = deque.iterator();
        assertTrue("Iterator should have next", it.hasNext());
        assertEquals("Iterator next should return the correct element", Integer.valueOf(1), it.next());
        assertFalse("Iterator should have no next after one element", it.hasNext());
    }

    @Test
    public void testSingleElementDescendingIteration() {
        deque.pushLast(1);
        Iterator<Integer> it = deque.descendingIterator();
        assertTrue("Descending iterator should have next", it.hasNext());
        assertEquals("Descending iterator next should return the correct element", Integer.valueOf(1), it.next());
        assertFalse("Descending iterator should have no next after one element", it.hasNext());
    }

    @Test
    public void testMultipleElementsIteration() {
        for (int i = 0; i < 5; i++) {
            deque.pushLast(i);
        }

        int count = 0;
        Iterator<Integer> it = deque.iterator();
        while (it.hasNext()) {
            assertEquals("Iterator should return elements in correct order", Integer.valueOf(count), it.next());
            count++;
        }
        assertEquals("Iterator should iterate over all elements", 5, count);
    }

    @Test
    public void testMultipleElementsDescendingIteration() {
        for (int i = 0; i < 5; i++) {
            deque.pushLast(i);
        }

        int count = 4;
        Iterator<Integer> it = deque.descendingIterator();
        while (it.hasNext()) {
            assertEquals("Descending iterator should return elements in reverse order", Integer.valueOf(count), it.next());
            count--;
        }
        assertEquals("Descending iterator should iterate over all elements", -1, count);
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorPastEnd() {
        deque.pushLast(1);
        Iterator<Integer> it = deque.iterator();
        it.next(); // Should return the first element
        it.next(); // Should throw NoSuchElementException
    }

    @Test(expected = NoSuchElementException.class)
    public void testDescendingIteratorPastEnd() {
        deque.pushLast(1);
        Iterator<Integer> it = deque.descendingIterator();
        it.next(); // Should return the first element
        it.next(); // Should throw NoSuchElementException
    }
}
