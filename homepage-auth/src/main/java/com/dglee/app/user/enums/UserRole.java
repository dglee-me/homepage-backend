package com.dglee.app.user.enums;

import javax.persistence.AttributeConverter;

public enum UserRole implements AttributeConverter {
    ROLE_USER,
    ROLE_ADMIN;

    @Override
    public Object convertToDatabaseColumn(Object attribute) {

        // 정상적인 값이 아닐 경우, 비정상값(0) 전달
        if(!(attribute instanceof String)) {
            return 0;
        }

        switch ((String) attribute) {
            case "ROLE_ADMIN":
                return 2;
            case "ROLE_USER":
            default:
                return 1;
        }
    }

    @Override
    public Object convertToEntityAttribute(Object dbData) {
        return null;
    }
}