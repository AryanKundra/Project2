package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * - You must implement a generic chaining hashtable. You may not
 *   restrict the size of the input domain (i.e., it must accept
 *   any key) or the number of inputs (i.e., it must grow as necessary).
 *
 * - ChainingHashTable should rehash as appropriate (use load factor as shown in lecture!).
 *
 * - ChainingHashTable must resize its capacity into prime numbers via given PRIME_SIZES list.
 *   Past this, it should continue to resize using some other mechanism (primes not necessary).
 *
 * - When implementing your iterator, you should NOT copy every item to another
 *   dictionary/list and return that dictionary/list's iterator.
 */
public class ChainingHashTable<K, V> extends DeletelessDictionary<K, V> {
    private Supplier<Dictionary<K, V>> newChainSupplier;
    private double loadFactor;
    private Dictionary<K,V>[] array;
    private int primeIndex;
    private double itemCount;
    private int counter;

    static final int[] PRIME_SIZES =
            {11, 23, 47, 97, 193, 389, 773, 1549, 3089, 6173, 12347, 24697, 49393, 98779, 197573, 395147};

    public int size(){
        return counter;
    }

    public ChainingHashTable(Supplier<Dictionary<K, V>> newChainSupplier) {
        this.newChainSupplier = newChainSupplier;
        loadFactor = 0.0;
        array = new Dictionary[7];
        for(int i = 0; i < 7; i++){
            array[i] = newChainSupplier.get();
        }
        primeIndex = 0;
        itemCount = 0.0;
        counter = 0;
    }



    @Override
    public V insert(K key, V value) {
        if(loadFactor >= 1){
            this.array = resize(array);
        }
        int index = Math.abs(key.hashCode() % array.length);
        if (index >= 0){
            if (array[index] == null){
                array[index] = newChainSupplier.get();
            }
            V returnVal = null;
            if(this.find(key) == null){
                counter++;
            } else{
                returnVal = this.find(key);
            }
            array[index].insert(key,value);
            loadFactor = (++itemCount) / array.length;
            return returnVal;
        } else{
            return null;
        }
    }

    @Override
    public V find(K key) {
        int index = Math.abs(key.hashCode() % array.length);
        if(index >= 0){
            if(array[index] == null){
                array[index] = newChainSupplier.get();
                return null;
            }
            return array[index].find(key);
        } else {
            return null;
        }
    }
    private Dictionary<K,V>[] resize(Dictionary<K,V> arrayToResize[]){
        Dictionary<K,V>[] resizedArray;
        if (primeIndex > 15){
            resizedArray = new Dictionary[arrayToResize.length * 2];

        } else {
            resizedArray = new Dictionary[PRIME_SIZES[primeIndex]];
        }
        for(int i = 0; i < arrayToResize.length; i++){
            if(arrayToResize[i] != null){
                for(Item<K, V> item : arrayToResize[i]){
                    int index = Math.abs(item.key.hashCode() % resizedArray.length);
                    if(index >= 0){
                        if(resizedArray[index] == null){
                            resizedArray[index] = newChainSupplier.get();
                        }
                        resizedArray[index].insert(item.key, item.value);
                    } else {
                        return new Dictionary[0];
                    }
                }
            }
        }
        primeIndex++;
        return resizedArray;
    }
    @Override
    public Iterator<Item<K, V>> iterator() {
        if(array[0] == null){
            array[0] = newChainSupplier.get();
        }

        Iterator<Item<K,V>> it = new Iterator<Item<K,V>>() {
            private int iteratorIndex = 0;
            Iterator<Item<K,V>> currentIterator = array[0].iterator();

            public boolean hasNext(){
                if(iteratorIndex < array.length && !currentIterator.hasNext()){
                    if(array[iteratorIndex + 1] == null){
                        iteratorIndex++;

                        while(array[iteratorIndex] == null){
                            iteratorIndex++;
                            if(iteratorIndex >= array.length){
                                return false;
                            }
                        }
                    } else {
                        iteratorIndex++;
                    }
                    if(iteratorIndex < array.length){
                        currentIterator = array[iteratorIndex].iterator();
                    }
                }
                if(iteratorIndex >= array.length){
                    return false;
                } else {
                    return currentIterator.hasNext();
                }
            }

            public Item<K, V> next(){
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                return currentIterator.next();
            }
        };
        return it;
    }


    /**
     * Temporary fix so that you can debug on IntelliJ properly despite a broken iterator
     * Remove to see proper String representation (inherited from Dictionary)
     */
    @Override
    public String toString() {
        return "ChainingHashTable String representation goes here.";
    }
}
