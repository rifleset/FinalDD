import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class CircularArrayDequeTest {

    @Test
    public void testCircularArrayDeque() {
        DeQueue<Integer> q = new CircularArrayDeQueue<>();
        assertTrue(q.isEmpty());

        int count = 100000;
        for (int i = 0; i < count; i++) {
            q.pushLast(i); // Changed from push to pushLast
            assertTrue(q.size() == i + 1);
            assertTrue(q.first().equals(0)); // Changed to use equals for object comparison
        }

        int current = 0;
        while (!q.isEmpty()) {
            assertTrue(q.first().equals(current)); // Changed to use equals for object comparison
            assertTrue(q.popFirst().equals(current)); // Changed from pop to popFirst and equals for object comparison
            current++;
        }

        assertTrue(q.isEmpty());
    }
}
