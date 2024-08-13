package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.PriorityWorkList;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.NoSuchElementException;


/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
public class MinFourHeapComparable<E extends Comparable<E>> extends PriorityWorkList<E> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;
    private int size;
    private Comparator<E> comparator;

    public MinFourHeapComparable() {
        this(Comparator.naturalOrder());
    }
    public MinFourHeapComparable(Comparator<E> comparator) {
        this.data = (E[]) Array.newInstance(Comparable.class, 10);
        this.size = 0;
        this.comparator = comparator;
    }

    @Override
    public boolean hasWork() {
        return size > 0;
    }

    @Override
    public void add(E work) {
        if (this.size == this.data.length){
            this.increaseArray();
        }
        this.data[this.size] = work;

        if (this.size > 0) {
            this.percoUp(this.parent(this.size), this.size);
        }
        this.size++;
    }

    @Override
    public E peek() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return data[0];
    }

    @Override
    public E next() {
        if (size == 0){
            throw new NoSuchElementException();
        }
        E min = data[0];
        data[0] = data[size - 1];
        size--;
        percoDown(0);
        return min;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {

            for (int i = 0; i < size; i++) {
                data[i] = null;
            }
            this.size = 0;
        }

    private void increaseArray(){
        E[] newData = (E[]) new Comparable[data.length * 2];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    private int parent(int index){
        return (index - 1) / 4;
    }
    private void percoUp(int startIndex, int currentIndex){
        while (startIndex >= 0 && comparator.compare(data[currentIndex], data[startIndex]) < 0){
            swap(currentIndex, startIndex);
            currentIndex = startIndex;
            startIndex = parent(startIndex);
        }
    }

    private void percoDown(int index){
        int smallest = index;
        for (int i = 1; i <= 4; i++){
            int child = 4 * index + i;
            if (child < size && comparator.compare(data[child], data[smallest]) < 0){
                smallest = child;
            }
        }

        if (smallest != index){
            swap(index, smallest);
            percoDown(smallest);
        }
    }

    private void swap(int i, int k){
        E temp = data[i];
        data[i] = data[k];
        data[k] = temp;
    }
}
