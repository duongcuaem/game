package com.game.lyn.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

public final class BeanUtilsHelper {

    private BeanUtilsHelper() {
        // Ngăn tạo instance cho lớp tiện ích
    }

    /**
     * Phương thức lấy danh sách các thuộc tính có giá trị null của đối tượng.
     * @param source đối tượng cần kiểm tra
     * @return mảng các tên thuộc tính có giá trị null
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        Set<String> emptyNames = new HashSet<>();

        for (java.beans.PropertyDescriptor pd : src.getPropertyDescriptors()) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames.toArray(new String[0]);
    }
}
