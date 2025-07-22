package com.mcs.modelsearcher.excel.model.dao;

import com.mcs.modelsearcher.excel.model.vo.Excel;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;

public class ExcelDao {
    public int deleteDataTable(SqlSession session) {
        return session.delete("excel.deleteDataTable");
    }

    public int newDataTable(SqlSession session, Excel excel) {
        return session.insert("excel.newDataTable", excel);
    }
}
