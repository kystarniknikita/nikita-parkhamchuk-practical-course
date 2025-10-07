import java.text.MessageFormat;
import java.util.NoSuchElementException;

public class CustomLinkedList<E> {
    private Node<E> head = null;
    private Node<E> tail = null;
    private int size = 0;

    public int size() {
        return size;
    }

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> prev;

        Node(E value) {
            this.value = value;
        }
    }

    public void addFirst(E el){
        Node<E> newNode = new Node<>(el);
        if (head == null){
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public void addLast(E el){
        Node<E> newNode = new Node<>(el);
        if (tail == null){
            tail = head = newNode;
        }else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public void add(int index, E el){
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(MessageFormat.format("Index: {0}, size: {1}", index, size));
        }
        if (index == 0){
            addFirst(el);
            return;
        }
        if (index == size){
            addLast(el);
            return;
        }
        Node<E> current = head;
        for (int i = 0; i < index; i++){
            current = current.next;
        }
        Node<E> newNode = new Node<>(el);
        Node<E> prevNode = current.prev;

        newNode.next = current;
        newNode.prev = prevNode;
        prevNode.next = newNode;
        current.prev = newNode;

        size++;
    }

    public E getFirst(){
        if (head == null){
            throw new NoSuchElementException();
        }
        return head.value;
    }

    public E getLast(){
        if (tail == null){
            throw new NoSuchElementException();
        }
        return tail.value;
    }

    public E get(int index){
        checkIndex(index);
        Node<E> current = head;
        for (int i = 0; i < index; i++){
            current = current.next;
        }
        return current.value;
    }

    public E removeFirst(){
        if (head == null){
            throw new NoSuchElementException();
        }
        E value = head.value;
        if (head == tail){
            head = tail = null;
        }else {
            head = head.next;
            head.prev = null;
        }
        size--;
        return value;
    }

    public E removeLast(){
        if (tail == null){
            throw new NoSuchElementException();
        }
        E value = tail.value;
        if (head == tail){
            head = tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return value;
    }

    public E remove(int index){
        checkIndex(index);
        if (index == 0){
            return removeFirst();
        }
        if (index == size - 1){
            return removeLast();
        }

        Node<E> current = head;
        for (int i = 0; i < index; i++){
            current = current.next;
        }

        Node<E> nodeToRemove = current;
        Node<E> prevNode = nodeToRemove.prev;
        Node<E> nextNode = nodeToRemove.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;

        size--;
        return nodeToRemove.value;
    }

    private void checkIndex(int index){
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException(MessageFormat.format("Index: {0}, size: {1}", index, size));
        }
    }
}
