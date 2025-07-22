package com.mcs.modelsearcher.excel.model.service;

import com.mcs.modelsearcher.excel.model.dao.ExcelDao;

public class ExcelService {
    ExcelDao dao;

    public ExcelService() {
        super();
        dao = new ExcelDao();
    }
}
