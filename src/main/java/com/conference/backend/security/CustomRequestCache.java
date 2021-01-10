package com.conference.backend.security;


import java.util.*;

// Reference queue data structure:
// https://github.com/aokolnychyi/data-structures-min/blob/master/src/main/java/com/aokolnychyi/ds/queue/Queue.java

/**
 * Request cache to save internal requests.
 *
 * @param <T> the type of request needed.
 */
//@SuppressWarnings("unchecked")
public class CustomRequestCache<T> {
    private int capacity;
    private List<T> elements;
    private int headIndex;
    private int tailIndex;

    /**
     * Creates a CustomRequestCache with a generic type given the param
     * @param capacity the maximum size of the cache
     */
    public CustomRequestCache(int capacity) {
        this.capacity = capacity;
        this.elements = new ArrayList<>(Collections.nCopies(capacity, null));
        this.headIndex = -1;
        this.tailIndex = -1;
    }

    /**
     * Get the most prioritized request (priority is determined by the time it was requested)
     * @return the earliest request {@code T}
     */
    public T pullRequest() {
        if (size() == 0) throw new NoSuchElementException("Cannot dequeue from an empty cache.");
        T head = elements.get(headIndex);
        elements.set(headIndex, null);
        if (headIndex == tailIndex) {
            headIndex = -1;
            tailIndex = -1;
        } else {
            headIndex = (headIndex + 1) % capacity;
        }
        return head;
    }

    /**
     * Save the request {@code T} the client requests
     * @param request the request to be added
     */
    public void saveRequest(T request) {
        if (size() == capacity) throw new RuntimeException("Cannot insert into a full cache.");
        if (tailIndex == capacity - 1) {
            tailIndex = 0;
        } else {
            tailIndex = (tailIndex + 1) % capacity;
        }
        elements.set(tailIndex, request);
        // handle insertions to an empty queue
        if (headIndex == -1) headIndex = 0;
    }

    /**
     * See the request without removing
     * @return the earliest request
     */
    public Optional<T> peek() {
        return headIndex == -1 ? Optional.empty() : Optional.of(elements.get(headIndex));
    }

    /**
     * The size of the current queue
     * @return an {@code int} representing the size
     */
    public int size() {
        final int size;
        if (headIndex == -1 && tailIndex == -1) {
            size = 0;
        } else if (headIndex < tailIndex) {
            size = tailIndex - headIndex + 1;
        } else {
            size = capacity - headIndex + tailIndex + 1;
        }
        return size;
    }

    /**
     * To check if cache is empty
     * @return true if the cache is empty
     */
    public boolean isEmpty() {
        return size() == 0;
    }
}
