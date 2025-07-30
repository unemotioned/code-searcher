package com.mcs.modelsearcher.excel.model.dao;

import com.mcs.modelsearcher.excel.model.vo.Excel;
import com.mcs.modelsearcher.excel.model.vo.Hierarchy;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExcelDao {
    public int clearDataTable(SqlSession session) {
        return session.delete("excel.clearDataTable");
    }

    public int newDataTable(SqlSession session, Excel excel) {
        return session.insert("excel.newDataTable", excel);
    }

    public int clearHierarchyTable(SqlSession session) {
        return session.delete("excel.clearHierarchyTable");
    }

    public int newHierarchyTable(SqlSession session, Hierarchy h) {
        return session.insert("excel.newHierarchyTable", h);
    }

    public List<Excel> uniSearch(SqlSession session, ArrayList<String> keywordList) {
        return session.selectList("excel.uniSearch", keywordList);
    }

    public int insertToDb(SqlSession session, Excel record) {
        return session.insert("excel.insertToDb", record);
    }

    public int deleteFromDb(SqlSession session, String insertNo) {
        return session.delete("excel.deleteFromDb", insertNo);
    }
}
