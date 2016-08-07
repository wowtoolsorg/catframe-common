package org.wowtools.common.utils;

import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 弱引用缓存
 * 
 * @author liuyu
 *
 */
public class WeakCache<K, V> {

	private ReadWriteLock rwLock = new ReentrantReadWriteLock();

	/**
	 * 弱引用key,供WeakHashMap使用
	 * 
	 * @author liuyu
	 *
	 * @param <K>
	 */
	@SuppressWarnings("hiding")
	private class WeakKey<K> {
		WeakKey(K k) {
			this.k = k;
		}

		K k;

		@Override
		public boolean equals(Object obj) {
			@SuppressWarnings("unchecked")
			WeakKey<K> other = (WeakKey<K>) obj;
			return k.equals(other.k);
		}

		@Override
		public int hashCode() {
			return k.hashCode();
		}
	}

	private WeakHashMap<WeakKey<K>, V> cache = new WeakHashMap<WeakKey<K>, V>();

	public void put(K k, V v) {
		rwLock.writeLock().lock();
		try {
			WeakKey<K> wk = new WeakKey<K>(k);
			cache.put(wk, v);
		} finally {
			rwLock.writeLock().unlock();
		}

	}

	public V get(K k) {
		rwLock.readLock().lock();
		try {
			WeakKey<K> wk = new WeakKey<K>(k);
			wk.k = k;
			return cache.get(wk);
		} finally {
			rwLock.readLock().unlock();
		}

	}
}
