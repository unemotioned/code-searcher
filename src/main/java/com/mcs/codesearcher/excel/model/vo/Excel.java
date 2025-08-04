package com.mcs.codesearcher.excel.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Excel {

  private String insertNo;
  private String partCode;
  private String rev;
  private String apply1;
  private String apply2;
  private String blueprintDate;
  private String clientBlueprint;
  private String scan;
  private String selfBlueprint;
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

  // Case for editing the insertNo
  private String originalInsertNo;
}
