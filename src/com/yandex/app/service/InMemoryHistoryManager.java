package com.yandex.app.service;

import com.yandex.app.model.Node;
import com.yandex.app.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public void addTaskInHistory(Task task) {
        // добаляет новую запись в хешмап
        if (task != null) {
            int id = task.getId();
            if (!historyMap.isEmpty()) {
                if (historyMap.containsKey(id)) {
                    removeTaskFromHistory(id);
                }
            }
            historyMap.put(id, linkLast(task));
        }
    }

    @Override
    public List<Task> getHistory() {
        //собирает все задачи в обычный ArrayList
        List<Task> list = new ArrayList<>();
        Node<Task> newNode = head;
        while (newNode != null) {
            list.add(newNode.getData());
            newNode = newNode.getNext();
        }
        return list;
    }

    @Override
    public void removeTaskFromHistory(int id) {
        //удаляет задачу из приложения и истории
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }

    private void removeNode(Node<Task> node) {
        //удаляет узел из связанного списка
        if (node.equals(head)) {
            Node<Task> newHead = head.getNext();
            newHead.setPrev(null);
            head = newHead;
        } else if (node.equals(tail)) {
            Node<Task> newTail = tail.getPrev();
            newTail.setNext(null);
            tail = newTail;
        } else {
            Node<Task> tempNext = node.getNext();
            Node<Task> tempPrev = node.getPrev();
            tempNext.setPrev(tempPrev);
            tempPrev.setNext(tempNext);
        }
    }

    private Node<Task> linkLast(Task task) {
        //добавляет задачу в конец двусвязанного списка
        if (head == null) {
            head = new Node<>(task, null, null);
            return head;
        } else if (tail == null) {
            tail = new Node<>(task, null, head);
            head.setNext(tail);
            return tail;
        } else {
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(task, null, oldTail);
            oldTail.setNext(newNode);
            tail = newNode;
            return newNode;
        }
    }
}





