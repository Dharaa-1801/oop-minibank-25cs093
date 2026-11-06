package com.minibank.util;

import com.minibank.model.annotation.MaxLength;
import com.minibank.model.annotation.Positive;

import java.lang.reflect.Field;
import java.util.ArrayList;

public final class AnnotationValidator {
    private AnnotationValidator() {
    }

    public static String[] validate(Object object) {
        ArrayList<String> errors = new ArrayList<>();
        for (Field field : object.getClass().getSuperclass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(object);
                if (field.isAnnotationPresent(Positive.class)
                        && value instanceof Number number && number.longValue() <= 0) {
                    errors.add(field.getName() + ": "
                            + field.getAnnotation(Positive.class).message());
                }
                MaxLength maxLength = field.getAnnotation(MaxLength.class);
                if (maxLength != null && value instanceof String text
                        && text.length() > maxLength.value()) {
                    errors.add(field.getName() + ": exceeds " + maxLength.value() + " characters");
                }
            } catch (IllegalAccessException exception) {
                throw new IllegalStateException("Cannot validate " + field.getName(), exception);
            }
        }
        return errors.toArray(String[]::new);
    }
}
