package com.mcs.modelsearcher.excel.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Excel {
    private int insertNo;
    private String modelNo;
    private String rev;
    private String apply1;
    private String apply2;
    private String apply3;
    private boolean bluePrint;
    private Date bluePrintDate;
    private String category;
    private String name;
    private String spec;
    private String maker;
    private String vendor;
    private int unitPrice;
    private int mgmtCost; // percentage
    private int estPrice;
    private String note;
}
