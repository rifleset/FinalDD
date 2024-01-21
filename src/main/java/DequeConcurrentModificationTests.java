import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class DequeConcurrentModificationTests {

    @Test(expected = ConcurrentModificationException.class)
    public void testConcurrentModificationExceptionForward() {
        DeQueue<Integer> q = new CircularArrayDeQueue<>();
        // Populate the deque
        for (int i = 0; i < 10; i++) {
            q.pushLast(i);
        }

        Iterator<Integer> iterator = q.iterator();
        while (iterator.hasNext()) {
            Integer item = iterator.next();
            if (item.equals(5)) {
                q.pushLast(99); // Modify the deque during iteration
            }
        }
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testConcurrentModificationExceptionBackward() {
        DeQueue<Integer> q = new CircularArrayDeQueue<>();
        // Populate the deque
        for (int i = 0; i < 10; i++) {
            q.pushLast(i);
        }

        Iterator<Integer> iterator = q.descendingIterator();
        while (iterator.hasNext()) {
            Integer item = iterator.next();
            if (item.equals(5)) {
                q.pushLast(99); // Modify the deque during iteration
            }
        }
    }
}
