import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CircularArrayDeQueue<E> implements DeQueue<E> {
    // Default initial capacity for the deque.
    private static final int DEFAULT_CAPACITY = 8;
    // Array used to store the elements of the deque.
    private E[] array;
    // Indices for the front and rear of the deque and the current size.
    private int front, rear, size;
    // Counter for tracking structural modifications for iterator fail-fast behavior.
    private volatile int modCount;

    @SuppressWarnings("unchecked")
    public CircularArrayDeQueue() {
        // Initialize the deque with the default capacity.
        array = (E[]) new Object[DEFAULT_CAPACITY];
        // Initial positions for front, rear, and size.
        front = rear = size = 0;
        // Reset modification count.
        modCount = 0;
    }

    @Override
    public void pushFirst(E elem) {
        // Ensure enough capacity before adding a new element at the front.
        ensureCapacity();
        // Calculate the new front position in a circular manner.
        front = (front - 1 + array.length) % array.length;
        // Insert the element at the new front position.
        array[front] = elem;
        // Increment size and modification count.
        size++;
        modCount++;
    }

    @Override
    public void pushLast(E elem) {
        // Ensure enough capacity before adding a new element at the rear.
        ensureCapacity();
        // Insert the element at the current rear position.
        array[rear] = elem;
        // Calculate the new rear position in a circular manner.
        rear = (rear + 1) % array.length;
        // Increment size and modification count.
        size++;
        modCount++;
    }

    @Override
    public E popFirst() {
        // Throw an exception if the deque is empty.
        if (isEmpty()) throw new NoSuchElementException();
        // Retrieve the element from the front.
        E elem = array[front];
        // Set the slot to null to aid garbage collection.
        array[front] = null;
        // Move the front forward in a circular manner.
        front = (front + 1) % array.length;
        // Decrement size and increment modification count.
        size--;
        modCount++;
        // Potentially reduce capacity if deque is too empty.
        reduceCapacity();
        return elem;
    }

    @Override
    public E popLast() {
        // Throw an exception if the deque is empty.
        if (isEmpty()) throw new NoSuchElementException();
        // Calculate the index of the last element.
        rear = (rear - 1 + array.length) % array.length;
        // Retrieve and remove the last element.
        E elem = array[rear];
        array[rear] = null; // Set the slot to null to aid garbage collection.
        size--;
        modCount++;
        // Reduce the capacity of the deque if it's too empty.
        reduceCapacity();
        return elem;
    }

    @Override
    public E first() {
        // Check if the deque is empty and throw an exception if it is.
        if (isEmpty()) throw new NoSuchElementException();
        // Return the element at the front of the deque.
        return array[front];
    }

    @Override
    public E last() {
        // Check if the deque is empty and throw an exception if it is.
        if (isEmpty()) throw new NoSuchElementException();
        // Calculate the actual index of the last element and return it.
        return array[(rear - 1 + array.length) % array.length];
    }

    @Override
    public boolean isEmpty() {
        // The deque is empty if its size is 0.
        return size == 0;
    }

    @Override
    public int size() {
        // Return the current number of elements in the deque.
        return size;
    }

    @Override
    public void clear() {
        // Fill the array with null values to help with garbage collection.
        Arrays.fill(array, null);
        // Reset front, rear, and size to initial values.
        front = rear = size = 0;
        // Increment the modification count.
        modCount++;
    }

    @Override
    public Iterator<E> iterator() {
        // Return a new instance of the forward iterator for the deque.
        return new DequeIterator();
    }

    @Override
    public Iterator<E> descendingIterator() {
        // Return a new instance of the backward (descending) iterator for the deque.
        return new DescendingDequeIterator();
    }

    private void ensureCapacity() {
        // Check if the array is full.
        if (size == array.length) {
            // Double the array size if it's full.
            resize(array.length * 2);
        }
    }

    private void reduceCapacity() {
        // Check if the array is at most one-fourth full and larger than default capacity.
        if (size <= array.length / 4 && array.length > DEFAULT_CAPACITY) {
            // Halve the array size if the conditions are met.
            resize(array.length / 2);
        }
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        // Create a new array with the specified new capacity.
        E[] newArray = (E[]) new Object[newCapacity];
        // Copy elements from the old array to the new array in the correct order.
        for (int i = 0, j = front; i < size; i++, j = (j + 1) % array.length) {
            newArray[i] = array[j];
        }
        // Replace the old array with the new array.
        array = newArray;
        // Reset front and rear pointers.
        front = 0;
        rear = size;
        // Increment the modification count.
        modCount++;
    }

    // Inner class for a standard iterator that iterates over the deque from front to rear.
    private class DequeIterator implements Iterator<E> {
        private int current = front;  // Current position of the iterator.
        private int lastRet = -1;     // Index of last element returned; -1 if no such.
        private int expectedModCount = modCount;  // ModCount value at the time of iterator creation.

        public boolean hasNext() {
            // Returns true if the current position is not at the rear (more elements to iterate over).
            return current != rear;
        }

        public E next() {
            // Check for any modifications made to the deque after the iterator was created.
            checkForComodification();
            if (!hasNext()) throw new NoSuchElementException();
            lastRet = current;  // Set the last returned element to current.
            // Move to the next element in a circular manner and return the current element.
            current = (current + 1) % array.length;
            return array[lastRet];
        }

        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            // Check for any modifications made to the deque after the iterator was created.
            checkForComodification();
            // Remove the element at the lastRet index.
            CircularArrayDeQueue.this.removeAt(lastRet);
            if (lastRet < current) current--;
            lastRet = -1;
            expectedModCount = modCount;
        }

        final void checkForComodification() {
            // Check if the deque has been structurally modified since the iterator was created.
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    // Inner class for an iterator that iterates over the deque from rear to front.
    private class DescendingDequeIterator implements Iterator<E> {
        private int current = (rear - 1 + array.length) % array.length;  // Current position, starting from the rear.
        private int lastRet = -1;     // Index of last element returned; -1 if no such.
        private int expectedModCount = modCount;  // ModCount value at the time of iterator creation.

        public boolean hasNext() {
            // Returns true if the current position is not before the front (more elements to iterate over).
            return current != (front - 1 + array.length) % array.length;
        }

        public E next() {
            // Check for any modifications made to the deque after the iterator was created.
            checkForComodification();
            if (!hasNext()) throw new NoSuchElementException();
            lastRet = current;  // Set the last returned element to current.
            // Move to the previous element in a circular manner and return the current element.
            current = (current - 1 + array.length) % array.length;
            return array[lastRet];
        }

        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            // Check for any modifications made to the deque after the iterator was created.
            checkForComodification();
            // Remove the element at the lastRet index.
            CircularArrayDeQueue.this.removeAt(lastRet);
            lastRet = -1;
            expectedModCount = modCount;
        }

        final void checkForComodification() {
            // Check if the deque has been structurally modified since the iterator was created.
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    // Method to remove an element at a specific index.
    private void removeAt(int index) {
        if (index == front) {
            // Remove the first element if the index is at the front.
            popFirst();
        } else if (index == (rear - 1 + array.length) % array.length) {
            // Remove the last element if the index is at the rear.
            popLast();
        } else {
            // If the index is neither at the front nor at the rear, removal is not supported.
            throw new UnsupportedOperationException("Can remove only first or last element.");
        }
    }
}
