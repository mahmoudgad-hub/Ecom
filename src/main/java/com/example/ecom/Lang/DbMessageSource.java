package com.example.ecom.Lang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DbMessageSource implements MessageSource {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        try {
//            System.out.println(locale.getLanguage());
            String lang = locale.getLanguage();
            String query = "SELECT " + (lang.equals("ar") ? "AR_MESSAGE" : "EN_MESSAGE") + " FROM ECOM_TRANSLATIONS_TAB  WHERE code = ?";
            return jdbcTemplate.queryForObject(query, new Object[]{code}, String.class);
        } catch (Exception e) {
            return defaultMessage != null ? defaultMessage : code;
        }
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) {
        return getMessage(code, args, null, locale);
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) {
        for (String code : resolvable.getCodes()) {
            String msg = getMessage(code, resolvable.getArguments(), null, locale);
            if (msg != null) return msg;
        }
        return resolvable.getDefaultMessage();
    }
}
