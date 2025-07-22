package com.mcs.modelsearcher.excel.controller;

import com.mcs.modelsearcher.excel.model.service.ExcelService;

public class ExcelController {

    ExcelService service;

    public ExcelController() {
        super();
        service = new ExcelService();
    }
}
