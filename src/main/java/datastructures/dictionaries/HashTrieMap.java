package datastructures.dictionaries;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.trie.TrieMap;
import cse332.types.BString;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * See cse332/interfaces/trie/TrieMap.java
 * and cse332/interfaces/misc/Dictionary.java
 * for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {
    public class HashTrieNode extends TrieNode<Map<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            this.pointers = new HashMap<A, HashTrieNode>();
            this.value = value;
        }

        @Override
        public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
            return pointers.entrySet().iterator();
        }
    }

    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode();
    }

    @Override
    public V insert(K key, V value) {
        if (key == null) {
            throw new NotYetImplementedException();
        }
        HashTrieNode currentNode = (HashTrieNode) this.root;
        for (A symbol: key){
            if (!currentNode.pointers.containsKey(symbol)) {
                currentNode.pointers.put(symbol, new HashTrieNode());
            }
            currentNode = currentNode.pointers.get(symbol);

        }
        V oldValue = currentNode.value;
        currentNode.value = value;
        size++;
        return oldValue;
    }

    @Override
    public V find(K key) {
        HashTrieNode currentNode = (HashTrieNode) this.root;
        for (A symbol : key) {
            if (!currentNode.pointers.containsKey(symbol)) {
                return null;

            }
            currentNode = currentNode.pointers.get(symbol);
        }
        return currentNode.value;
    }

    @Override
    public boolean findPrefix(K key) {
        HashTrieNode currentNode = (HashTrieNode) this.root;
        for (A symbol : key) {
            if (!currentNode.pointers.containsKey(symbol)) {
                return false;
            }
            currentNode = currentNode.pointers.get(symbol);
        }
        return true;
    }
    public interface BString<A extends Comparable<A>> {
        int size();
        A get(int index);
    }
    private boolean deleteHelper(HashTrieNode currentNode,  Iterator<A> iterator, K key, int depth) {
        if (currentNode == null){
            return false;
        }
        if (!iterator.hasNext()) {
            currentNode.value = null;
        }else{
            A symbol = iterator.next();
            if (deleteHelper(currentNode.pointers.get(symbol), iterator, key, depth + 1)) {
                currentNode.pointers.remove(symbol);
            }
        }
        return currentNode.value == null && currentNode.pointers.isEmpty();
    }

    @Override
    public void delete(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        Iterator<A> iterator = key.iterator();
        deleteHelper((HashTrieNode) this.root, iterator, key, 0);
    }


    @Override
    public void clear() {
        this.root = new HashTrieNode();
    }
}


