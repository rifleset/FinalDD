import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class DequeEmptyStateTests {

    @Test(expected = NoSuchElementException.class)
    public void testPopFirstOnEmptyDeque() {
        DeQueue<Integer> q = new CircularArrayDeQueue<>();
        assertTrue(q.isEmpty());
        q.popFirst(); // This should throw NoSuchElementException.
    }

    @Test(expected = NoSuchElementException.class)
    public void testPopLastOnEmptyDeque() {
        DeQueue<Integer> q = new CircularArrayDeQueue<>();
        assertTrue(q.isEmpty());
        q.popLast(); // This should throw NoSuchElementException.
    }

    @Test(expected = NoSuchElementException.class)
    public void testFirstOnEmptyDeque() {
        DeQueue<Integer> q = new CircularArrayDeQueue<>();
        assertTrue(q.isEmpty());
        q.first(); // This should throw NoSuchElementException.
    }

    @Test(expected = NoSuchElementException.class)
    public void testLastOnEmptyDeque() {
        DeQueue<Integer> q = new CircularArrayDeQueue<>();
        assertTrue(q.isEmpty());
        q.last(); // This should throw NoSuchElementException.
    }
}
