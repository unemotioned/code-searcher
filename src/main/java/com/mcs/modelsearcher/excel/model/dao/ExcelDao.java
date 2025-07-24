package com.mcs.modelsearcher.excel.model.dao;

import com.mcs.modelsearcher.excel.model.vo.Excel;
import com.mcs.modelsearcher.excel.model.vo.Hierarchy;
import org.apache.ibatis.session.SqlSession;

public class ExcelDao {
    public int deleteDataTable(SqlSession session) {
        return session.delete("excel.deleteDataTable");
    }

    public int newDataTable(SqlSession session, Excel excel) {
        return session.insert("excel.newDataTable", excel);
    }

    public int deleteHierarchyTable(SqlSession session) {
        return session.delete("excel.deleteHierarchyTable");
    }

    public int newHierarchyTable(SqlSession session, Hierarchy h) {
        return session.insert("excel.newHierarchyTable", h);
    }
}
