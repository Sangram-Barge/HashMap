/* 

1. key : value
2. generic <K, V>
3. load factor : 
4. operations put(key, value), get(key), remove(key)
5. initial capacity 5, double the capacity when load factor reached
6. Entry
*/

import java.util.ArrayList;
import java.util.List;

public class MyMap<K, V> {

	private List<Entry<K, V>> bucket;
	private Integer size;
	private Integer capacity;

	private static Integer INITIAL_CAP = 5;

	public MyMap(){
		bucket = new ArrayList<>();
		size = 0;
		capacity = INITIAL_CAP;
		for (int i = 0; i < bucket.size(); i++) bucket.set(i, null);
	};

	public MyMap(Integer capacity){
		bucket = new ArrayList<>();
		size = 0;
		this.capacity = capacity;
		for (int i = 0; i < capacity; i++) bucket.set(i, null);
	};

	public void put(K key, V val) {
		int bucketIdx = generateIndex(key);
		Entry<K, V> entry = bucket.get(bucketIdx);
		while(entry != null) {
			if (entry.val.equals(val)) return;
			entry = entry.next;
		}

		Entry<K, V> newEntry = new Entry<>(key, val);
		newEntry.next = bucket.get(bucketIdx);

		bucket.set(bucketIdx, newEntry);
		size++;

		if ((size / capacity) > 0.75) rehash();
	}

	private void rehash() {
		List<Entry<K, V>> oldBucket = bucket;
		bucket = new ArrayList<>();
		capacity = capacity * 2;
		for (int i = 0; i < capacity; i++) bucket.set(i, null);
		size = 0;

		for (int i = 0; i < bucket.size(); i++) {
			Entry<K, V> entry = oldBucket.get(i);
			while (entry != null) {
				put(entry.key, entry.val);
				entry = entry.next;
			}
		}
	}

	public V get(K key) {
		int bucketIdx = generateIndex(key);
		Entry<K, V> entry = bucket.get(bucketIdx);
		while (entry != null) {
			if (entry.key.equals(key)) return entry.val;
			entry = entry.next;
		}
		return null;
	}

	public void delete(K key) {
		int bucketIdx = generateIndex(key);
		Entry<K, V> entry = bucket.get(bucketIdx);
		Entry<K, V> prev = null;
		while (entry != null) {
			if (entry.key.equals(key)) {
				if (prev == null) bucket.set(bucketIdx, null);
				else prev.next = entry.next;
				entry.next = null;
				size--;
				break;
			}
			prev = entry;
			entry = entry.next;
		}
	}

	private int generateIndex(K key) {
		Integer hash = key.hashCode();
		return hash % capacity;
	}


	class Entry<K, V> {
		K key;
		V val;

		Entry<K, V> next;

		public Entry(K key, V val) {
			this.key = key;
			this.val = val;
		}
	}
}