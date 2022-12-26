package ru.berezovskaya_rgr.list;

public interface IList {
    void add(Object data);
    Object get(int index);
    void remove(int index);
    int size();
    void add(Object data, int index);
    void forEach(Action a);
    void sort(Comparator comparator);
}
