package com.mcs.modelsearcher.excel.model.service;

import com.mcs.modelsearcher.common.SqlSessionTemplate;
import com.mcs.modelsearcher.excel.model.dao.ExcelDao;
import com.mcs.modelsearcher.excel.model.vo.Excel;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;

public class ExcelService {
    ExcelDao dao;

    public ExcelService() {
        super();
        dao = new ExcelDao();
    }

    public int deleteDataTable() {
        SqlSession session = SqlSessionTemplate.getSqlSession();
        int result = dao.deleteDataTable(session);
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
}
