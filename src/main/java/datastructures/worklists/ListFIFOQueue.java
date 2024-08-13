package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FIFOWorkList.java
 * for method specifications.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {
    private Node<E> front;
    private Node<E> end;
    private int size;

    private static class Node<E> {
        E data;

        Node<E> next;

        public Node(E data){
            this.data = data;
            this.next = null;
        }
    }


    public ListFIFOQueue() {

        front = null;
        end = null;
        size = 0;
    }

    @Override
    public void add(E work) {
        Node<E> newNode = new Node<>(work);
        if (isEmpty()) {
            front = end = newNode;
        }else {
            end.next = newNode;
            end = newNode;
        }
        size++;
    }

    @Override
    public E peek() {
        if (isEmpty()){
            throw new NoSuchElementException("Queue is empty");
        }
        return front.data;
    }

    @Override
    public E next() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");

        }
        E result = front.data;
        front = front.next;
        size--;
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        front = null;
        end = null;
        size = 0;
    }


    private boolean isEmpty() {
        return size == 0;
    }
}