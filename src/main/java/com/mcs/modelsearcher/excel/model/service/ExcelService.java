package com.mcs.modelsearcher.excel.model.service;

import com.mcs.modelsearcher.common.SqlSessionTemplate;
import com.mcs.modelsearcher.excel.model.dao.ExcelDao;
import com.mcs.modelsearcher.excel.model.vo.Excel;
import com.mcs.modelsearcher.excel.model.vo.Hierarchy;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExcelService {
    ExcelDao dao;

    public ExcelService() {
        super();
        dao = new ExcelDao();
    }

    public int clearDataTable() {
        SqlSession session = SqlSessionTemplate.getSqlSession();
        int result = dao.clearDataTable(session);
        session.close();
        return result;
    }

    public int newDataTable(ArrayList<Excel> excelList) {
        SqlSession session = SqlSessionTemplate.getSqlSession();

        int result = -1;
        for (Excel excel : excelList) {
            result = dao.newDataTable(session, excel);
            if (result != 1) {
                break;
            }
        }

        session.close();
        return result;
    }

    public int clearHierarchyTable() {
        SqlSession session = SqlSessionTemplate.getSqlSession();
        int result = dao.clearHierarchyTable(session);
        session.close();
        return result;
    }

    public int newHierarchyTable(ArrayList<Hierarchy> hList) {
        SqlSession session = SqlSessionTemplate.getSqlSession();
        int result = -1;
        for (Hierarchy h : hList) {
            result = dao.newHierarchyTable(session, h);
            if (result != 1) {
                break;
            }
        }

        session.close();
        return result;
    }

    public ArrayList<Excel> doSearch(HashMap<String,String> userInput) {
        SqlSession session = SqlSessionTemplate.getSqlSession();
        ArrayList<Excel> excelList = (ArrayList<Excel>) (dao.doSearch(session, userInput));
        session.close();
        return excelList;
    }

    public List<Excel> uniSearch(ArrayList<String> keywordList) {
        SqlSession session = SqlSessionTemplate.getSqlSession();
        ArrayList<Excel> excelList = (ArrayList<Excel>)  (dao.uniSearch(session, keywordList));
        session.close();
        return excelList;
    }

    public int insertRecord(Excel record) {
        SqlSession session = SqlSessionTemplate.getSqlSession();
        int result = dao.insertRecord(session, record);
        session.close();
        return result;
    }
}
