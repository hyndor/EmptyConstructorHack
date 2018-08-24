package ru.hyndo.emptyconstructorhack;

public interface ClassDefiner {

    Class<?> defineClass(ClassLoader loader, String className, byte[] data);

}
