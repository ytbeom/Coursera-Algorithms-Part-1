/* *****************************************************************************
 *  Name: Taekbeom Yoo
 *  Date: 2020.05.24
 *  Description: RandomizedQueue
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private Item[] queue;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
        size = 0;

    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (size == queue.length)
            resize(2 * queue.length);
        queue[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0)
            throw new NoSuchElementException();
        int idx = StdRandom.uniform(0, size);
        Item item = queue[idx];
        queue[idx] = queue[--size];
        queue[size] = null;
        if (size > 0 && size < queue.length / 4)
            resize(queue.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0)
            throw new NoSuchElementException();
        return queue[StdRandom.uniform(0, size)];
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++)
            copy[i] = queue[i];
        queue = copy;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int count = 0;

        public boolean hasNext() {
            return count < size;
        }

        public Item next() {
            if (count == size)
                throw new NoSuchElementException();
            int idx = StdRandom.uniform(count, size);
            Item temp = queue[idx];
            queue[idx] = queue[count];
            queue[count++] = temp;

            return queue[idx];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        rq.enqueue("a");
        rq.enqueue("b");
        rq.enqueue("c");
        rq.enqueue("d");
        rq.enqueue("e");
        rq.enqueue("f");
        rq.enqueue("g");
        rq.dequeue();
        Iterator<String> iter1 = rq.iterator();
        Iterator<String> iter2 = rq.iterator();
        while (iter1.hasNext()) {
            System.out.print(iter1.next() + ",");
        }
        System.out.println();
        while (iter2.hasNext()) {
            System.out.print(iter2.next() + ",");
        }
        System.out.println();
    }
}
