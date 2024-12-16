package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class InMemoryStorage<T> {
    protected int id = 0;
    protected final Map<Integer, T> storage = new HashMap<>();

    public T create(T item) {
        id++;
        setId(item, id);
        storage.put(id, item);
        return item;
    }

    public T update(T item) {
        int id = getId(item);
        if (!storage.containsKey(id)) {
            throw new ObjectNotFoundException("Element with id = " + id + " not found");
        }
        storage.put(id, item);
        return item;
    }

    public Optional<T> get(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Optional<T> delete(int id) {
        return Optional.ofNullable(storage.remove(id));
    }

    public Map<Integer, T> getAll() {
        return storage;
    }

    protected abstract void setId(T item, int id);

    protected abstract int getId(T item);
}
