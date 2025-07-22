package com.mcs.modelsearcher.excel.model.service;

import com.mcs.modelsearcher.common.SqlSessionTemplate;
import com.mcs.modelsearcher.excel.model.dao.ExcelDao;
import org.apache.ibatis.session.SqlSession;

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
}
