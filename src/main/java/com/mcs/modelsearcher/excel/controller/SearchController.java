package com.mcs.modelsearcher.excel.controller;

import com.mcs.modelsearcher.excel.model.service.ExcelService;
import com.mcs.modelsearcher.excel.model.vo.Excel;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchController {
    ExcelService eServ;

    public SearchController() {
        eServ = new ExcelService();
    }

    public ArrayList<Excel> doSearch(HashMap<String, String> userInput) {
        return eServ.doSearch(userInput);
    }
}
