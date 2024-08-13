package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.LIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 */
public class ArrayStack<E> extends LIFOWorkList<E> {
    private static final int cap = 10;
    private Object[] array;
    private int size;
    private boolean isEmpty() {
        return size == 0;
    }
    private void confirmCapacity() {
        if (size == array.length) {
            int newCapacity = array.length * 2;
            Object[] newArray = new Object[newCapacity];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    public ArrayStack() {

        array = new Object[cap];
        size = 0;

    }

    @Override
    public void add(E work) {
        confirmCapacity();
        array[size++] = work;


    }

    @Override
    public E peek() {
        if (isEmpty()){
            throw new NoSuchElementException("Stack is empty");
        }
        return (E) array[size - 1];

    }

    @Override
    public E next() {
        if (isEmpty()){
            throw new NoSuchElementException("Stack is empty");
        }
        E result = (E) array[--size];
        array[size] = null;
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++){
            array[i] = null;
        }
        size = 0;
    }
}