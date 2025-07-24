package com.mcs.modelsearcher.excel.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Excel {
    private String insertNo;
    private String partCode;
    private String rev;
    private String apply1;
    private String apply2;
    private Date blueprintDate;
    private boolean clientBlueprint;
    private boolean scan;
    private boolean blueprint;
    private String category;
    private String name;
    private String spec;
    private String maker;
    private String vendor;
    private int unitPrice;
    private int mgmtCost;
    private int estPrice;
    private int refPrice; // 을지단가
    private String note;
}
