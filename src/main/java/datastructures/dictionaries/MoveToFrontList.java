package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.SimpleIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 1. The list is typically not sorted.
 * 2. Add new items to the front of the list.
 * 3. Whenever find or insert is called on an existing key, move it
 * to the front of the list. This means you remove the node from its
 * current position and make it the first node in the list.
 * 4. You need to implement an iterator. The iterator SHOULD NOT move
 * elements to the front.  The iterator should return elements in
 * the order they are stored in the list, starting with the first
 * element in the list. When implementing your iterator, you should
 * NOT copy every item to another dictionary/list and return that
 * dictionary/list's iterator.
 */
public class MoveToFrontList<K, V> extends DeletelessDictionary<K, V> {

  private Node start;
    private class Node {
      private Item<K, V> data;
      private Node next;
      public Node(){
          this(null, null);
      }
      public Node(Item<K, V> item){
          this(item, null);
      }
      public Node(Item<K, V> item, Node next){
          this.data = item;
          this.next = next;
      }

  }

  public MoveToFrontList(){
        this(null);
  }

  public MoveToFrontList(Item<K, V> item){
        this.start = new Node(item);
  }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null){
            throw new IllegalArgumentException();
        }
        V prev = this.find(key);
        if (prev != null){
            this.start.data.value = value;
        }
        else{
            this.start = new Node(new Item(key , value), this.start);
            this.size++;
        }
        return prev;
    }

    @Override
    public V find(K key) {
       if (key == null){
           throw new IllegalArgumentException();
       }
       Node current = this.start;
       V getVal = null;
       if (current != null && current.data != null){
           if (current.data.key.equals(key)){
               return current.data.value;
           }
           while (current.next != null && current.next.data != null && !current.next.data.key.equals(key)){
               current = current.next;
           }
           if (current.next != null && current.next.data != null){
               getVal = current.next.data.value;
               Node refer = current.next;
               current.next = refer.next;
               refer.next = this.start;
               this.start = refer;
           }
       }
       return getVal;
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new MoveToFrontListIterator();
    }
    private class MoveToFrontListIterator extends SimpleIterator<Item<K,V>> {
        private Node current;

        public MoveToFrontListIterator(){
            this.current = MoveToFrontList.this.start;
        }
        public boolean hasNext(){
            return current != null && current.next != null;
        }
        public Item<K, V> next(){
            Item<K, V> getItem = current.data;
            current = current.next;
            return getItem;
            }
        }
    }

