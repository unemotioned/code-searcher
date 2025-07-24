package com.mcs.modelsearcher.excel.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Hierarchy {
    private int parent_no;
    private int child_no;
}
