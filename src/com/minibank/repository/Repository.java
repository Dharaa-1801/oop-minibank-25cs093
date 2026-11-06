package com.minibank.repository;

import com.minibank.model.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Repository<T> {
    private final ConcurrentHashMap<String, T> entities = new ConcurrentHashMap<>();

    public T save(T entity) {
        entities.put(idOf(entity), entity);
        return entity;
    }

    public T findById(String id) {
        return entities.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(entities.values());
    }

    public boolean delete(String id) {
        return entities.remove(id) != null;
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        for (T item : items) {
            save(item);
        }
    }

    private String idOf(T entity) {
        for (Field field : entity.getClass().getSuperclass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                try {
                    field.setAccessible(true);
                    return String.valueOf(field.get(entity));
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot read entity id", exception);
                }
            }
        }
        throw new IllegalArgumentException("Entity has no @Id field");
    }

    public static <N extends Number> double average(N[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("At least one value is required");
        }
        double total = 0;
        for (N value : values) {
            total += value.doubleValue();
        }
        return total / values.length;
    }
}
