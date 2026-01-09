package com.vetsync.backend.global.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OptionMapValidator implements ConstraintValidator<ValidOptionMap, Map<String, String>> {

    @Override
    public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {
        // null 여부는 @NotNull이 별도로 처리하므로 여기서는 허용
        if (value == null) return true;
        List<Integer> keys = new ArrayList<>();

        for (Map.Entry<String, String> entry : value.entrySet()) {
            String key = entry.getKey();
            String label = entry.getValue();

            if (key == null || !key.matches("\\d+")) {
                return false;
            }
            if (label == null || label.isBlank()) {
                return false;
            }
            keys.add(Integer.parseInt(key));

        }
        keys.sort(Integer::compareTo);
        for (int i = 0; i < keys.size(); i++) {
            int expected = i + 1;
            if (keys.get(i) != expected) {
                return false;
            }
        }
        return true;
    }
}
