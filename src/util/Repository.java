package util;

import model.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public final class Repository<T> {
    private final HashMap<String, T> entities = new HashMap<>();

    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
        String id = findId(entity);
        entities.put(id, entity);
        return entity;
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        if (items == null) {
            throw new IllegalArgumentException("Items must not be null");
        }
        for (T item : items) {
            save(item);
        }
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

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values must not be empty");
        }
        double total = 0;
        for (N value : values) {
            if (value == null) {
                throw new IllegalArgumentException("Values must not contain null");
            }
            total += value.doubleValue();
        }
        return total / values.length;
    }

    private String findId(T entity) {
        for (Class<?> type = entity.getClass(); type != null; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(entity);
                        if (!(value instanceof String id) || id.isBlank()) {
                            throw new IllegalStateException(
                                    "The @Id field must contain a non-blank String");
                        }
                        return id;
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException(
                                "Cannot read @Id field " + field.getName(), exception);
                    }
                }
            }
        }
        throw new IllegalStateException(
                "No field annotated with @Id found on " + entity.getClass().getName());
    }
}
