package com.mcs.modelsearcher.excel.model.dao;

import org.apache.ibatis.session.SqlSession;

public class ExcelDao {
    public int deleteDataTable(SqlSession session) {
        return session.delete("excel.deleteDataTable");
    }
}
