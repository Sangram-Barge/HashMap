public class SMap<K, V> {
	
	private static final Integer INITIAL_CAP;

	private List<Node<K, V>> bucket;
	private Integer capacity;
	private Integer size;

	public SMap() {
		bucket = new ArrayList<>();
		capacity = INITIAL_CAP;
		size = 0;
		for (i = 0; i < capacity; i++) bucket.add(null);
	}

	public void put(K key, V value) {
		Integer bucketIdx = generateHash(key);
		Node<K, V> head = bucket.get(bucketIdx);
		while (head != null) {
			if (head.value.equals(value)) return;
			head = head.next;
		}
		size++;
		Node<K, V> newHead = new Node<>(key, value);
		newHead.next = head;
		bucket.set(bucketIdx, newHead);

		double loadFactor = size / capacity;

		if (loadFactor > 0.75) {
			rehash();
		}
	}

	public V get(K key) {
		Integer bucketIdx = generateHash(key);
		Node<K, V> head = bucket.get(bucketIdx);
		while (head != null) {
			if (head.key.equals(key)) return head.value;
			head = head.next;
		}
		return null;
	}

	public void remove(K key) {
		Integer bucketIdx = generateHash(key);
		Node<K, V> head = bucket.get(bucketIdx);
		Node<K, V> prev = null;
		while (head != null) {
			if (head.key.equals(key)) {
				if (prev == null) bucket.set(bucketIdx, head.next);
				else prev.next = head.next;
				head.next = null;
				size--;
				break;
			}
			prev = head;
			head = head.next;
		}
	}

	private Integer generateHash(K key) {
		var hash = key.hashCode();
		return hash % capacity;
	}

	private void rehash() {
		List<Node<K, V>> oldBucket = bucket;
		bucket = new ArrayList<>();
		capacity = capacity * 2;
		for (int i = 0; i < capacity; i++) bucket.put(null);
		size = 0;

		for (int i = 0; i < oldBucket.size(); i++) {
			Node<K, V> node = oldBucket.get(i);
			while (node != null) {
				put(node.key, node.value);
				node = node.next;
			}
		}

	}


	private class Node<K, V> {
		K key;
		V value;

		Node<K, V> next;

		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}
}