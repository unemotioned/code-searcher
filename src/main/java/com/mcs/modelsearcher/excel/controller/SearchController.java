package com.mcs.modelsearcher.excel.controller;

import com.mcs.modelsearcher.excel.model.service.ExcelService;
import com.mcs.modelsearcher.excel.model.vo.Excel;

import java.util.ArrayList;

public class SearchController {
    ExcelService eServ;

    public SearchController() {
        eServ = new ExcelService();
    }

    public ArrayList<Excel> selectWithPartCode(String keyword) {
        return eServ.selectWithPartCode(keyword);
    }
}
