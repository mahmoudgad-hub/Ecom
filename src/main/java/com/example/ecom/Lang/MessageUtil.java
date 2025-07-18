package com.example.ecom.Lang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageUtil {

    private final DbMessageSource dbMessageSource;

    @Autowired
    public MessageUtil(DbMessageSource dbMessageSource) {
        this.dbMessageSource = dbMessageSource;
    }

    public String get(String key) {
        return get(key, null, LocaleContextHolder.getLocale());
    }

    public String get(String key, Object[] args) {
        return get(key, args, LocaleContextHolder.getLocale());
    }

    public String get(String key, Object[] args, Locale locale) {
        try {
            return dbMessageSource.getMessage(key, args, locale);
        } catch (Exception e) {
            return "[[" + key + "]]"; // افتراضيًا ترجع اسم الكي نفسه لو مفيش ترجمة
        }
    }

    public String getOrDefault(String key, String defaultMessage) {
        try {
            return dbMessageSource.getMessage(key, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return defaultMessage;
        }
    }
}

