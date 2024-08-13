package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */
public class CircularArrayFIFOQueue<E extends Comparable<E>> extends FixedSizeFIFOWorkList<E> {
    private E[] array;
    private int front;
    private int end;
    private int size;

    private int capacity;

    private boolean isEmpty(){
        return size() == 0;
    }

    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        this.capacity = capacity;
        this.array = (E[]) Array.newInstance(Comparable.class, capacity);
        this.front = 0;
        this.end = 0;

    }

    @Override
    public void add(E work) {
        if (isFull()) {
            throw new IllegalStateException();
        }
        array[end] = work;
        end = (end + 1) % capacity;
        size++;
    }


    @Override
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return array[front];
    }

    @Override
    public E peek(int i) {
        if (i < 0 || i >= capacity()) {
            throw new IndexOutOfBoundsException();
        }
        int index = (front + i) % capacity;
        return array[index];
    }
    @Override
    public E next() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E result = array[front];
        array[front] = null;
        front = (front + 1) % capacity;
        size--;
        return result;
    }

    @Override
    public void update(int i, E value) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();

        }
        int index = (front+i)%capacity;
        array[index] = value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++){
            array[i] = null;
        }
        front = 0;
        end = 0;
        size = 0;

    }

    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
       int shortLen = Math.min(this.size(), other.size());
       int compare = 0;
       for (int i = 0; i < shortLen; i++){
           compare = this.peek(i).compareTo(other.peek(i));
           if (compare != 0){
               return compare;
           }
       }
       return this.size() - other.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        // You will finish implementing this method in project 2. Leave this method unchanged for project 1.
        if (this == obj) {
            return true;
        } else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        } else {

             FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;
             if (other.size() != this.size()){
                 return false;
             }
             else {
                 return (this.compareTo(other) == 0);
             }
        }
    }


    @Override
    public int hashCode() {
        int hashCodeBuildUp = 0;
        for (int i = 0; i < this.size; i++){
            E element = array[(front + i) % capacity]; // Access elements in the correct order
            hashCodeBuildUp = 31 * hashCodeBuildUp + (element == null ? 0 : element.hashCode());
        }
        return hashCodeBuildUp;
    }
}