package ru.berezovskaya_rgr.list.builder;

import ru.berezovskaya_rgr.list.Comparator;

public interface UserTypeBuilder {
    String typeName();

    Object create();

    Comparator getComparator();

    Object createFromString(String s);

    String toString(Object object);
}
