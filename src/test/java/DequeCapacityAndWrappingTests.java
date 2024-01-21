import org.junit.Test;
import static org.junit.Assert.*;

public class DequeCapacityAndWrappingTests {

    @Test
    public void testAddRemoveAtCapacity() {
        DeQueue<Integer> q = new CircularArrayDeQueue<>();
        int initialCapacity = 8;

        // Fill the deque to capacity
        for (int i = 0; i < initialCapacity; i++) {
            q.pushLast(i);
        }
        assertEquals(initialCapacity, q.size());

        // Add one more element to trigger resize
        q.pushLast(initialCapacity);
        assertEquals(initialCapacity + 1, q.size());

        // Check if elements are in correct order after resize
        for (int i = 0; i <= initialCapacity; i++) {
            assertEquals(Integer.valueOf(i), q.popFirst());
        }
        assertTrue(q.isEmpty());
    }

    @Test
    public void testWrappingBehavior() {
        DeQueue<Integer> q = new CircularArrayDeQueue<>();
        int initialCapacity = 8; // Assuming DEFAULT_CAPACITY is 8, should match with the actual default capacity

        // Fill the deque to capacity
        for (int i = 0; i < initialCapacity; i++) {
            q.pushLast(i);
        }

        // Remove and then add elements to cause wrapping
        for (int i = 0; i < initialCapacity; i++) {
            assertEquals(Integer.valueOf(i), q.popFirst());
            q.pushLast(i + initialCapacity);
        }

        // Check if elements are in correct order after wrapping
        for (int i = 0; i < initialCapacity; i++) {
            assertEquals(Integer.valueOf(i + initialCapacity), q.popFirst());
        }
        assertTrue(q.isEmpty());
    }
}

