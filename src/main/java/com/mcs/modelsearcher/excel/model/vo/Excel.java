package com.mcs.modelsearcher.excel.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Excel {
    private int no;
    private String modelNo;
    private String rev;
    private String application1;
    private String application2;
    private String application3;
    private boolean bluePrint;
    private Date bluePrintDate;
    private String category;
    private String name;
    private String spec;
    private String maker;
    private String vendor;
    private int unitPrice;
    private int mgmtCost; // percentage
    private int estimatePrice;
    private String note;
}
