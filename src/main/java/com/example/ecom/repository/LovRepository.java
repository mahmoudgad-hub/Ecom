package com.example.ecom.repository;

import com.example.ecom.dto.LovDto;
import org.apache.commons.codec.language.bm.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;

@Repository
public class LovRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<LovDto> findByType(String type) {
        Locale locale= LocaleContextHolder.getLocale();
        String labelColumn = locale.getLanguage().equals("ar") ? "LIST_NAME_AR" : "LIST_NAME_EN";
        String query = "SELECT LOOKUP_LIST_ID AS VALUE_CODE, " + labelColumn + " AS LABEL FROM ECOM_GEN_LOOKUP_LIST_TAB WHERE LOOKUP_ID = ? AND IS_DELETE_FLAG ='N' ORDER BY SEQ_NO";
       System.out.println(query);
        return jdbcTemplate.query(query, new Object[]{type}, (rs, rowNum) ->
                new LovDto(rs.getString("VALUE_CODE"), rs.getString("LABEL"))
        );
    }
}
