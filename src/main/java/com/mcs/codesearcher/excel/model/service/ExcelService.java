package com.mcs.codesearcher.excel.model.service;

import com.mcs.codesearcher.common.SqlSessionTemplate;
import com.mcs.codesearcher.excel.model.dao.ExcelDao;
import com.mcs.codesearcher.excel.model.vo.Excel;
import com.mcs.codesearcher.excel.model.vo.Hierarchy;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

public class ExcelService {

  ExcelDao dao;

  public ExcelService() {
    dao = new ExcelDao();
  }

  public int clearDataTable() {
    SqlSession session = SqlSessionTemplate.getSqlSession();
    int result = dao.clearDataTable(session);
    session.close();
    return result;
  }

  public int newDataTable(ArrayList<Excel> excelList) {
    SqlSession session = SqlSessionTemplate.getSqlSession();

    int result = -1;
    for (Excel excel : excelList) {
      result = dao.newDataTable(session, excel);
      if (result != 1) {
        break;
      }
    }

    session.close();
    return result;
  }

  public int clearHierarchyTable() {
    SqlSession session = SqlSessionTemplate.getSqlSession();
    int result = dao.clearHierarchyTable(session);
    session.close();
    return result;
  }

  public int newHierarchyTable(ArrayList<Hierarchy> hList) {
    SqlSession session = SqlSessionTemplate.getSqlSession();
    int result = -1;
    for (Hierarchy h : hList) {
      result = dao.newHierarchyTable(session, h);
      if (result != 1) {
        break;
      }
    }

    session.close();
    return result;
  }

  public List<Excel> uniSearch(ArrayList<String> keywordList) {
    SqlSession session = SqlSessionTemplate.getSqlSession();
    ArrayList<Excel> excelList = (ArrayList<Excel>) (dao.uniSearch(session, keywordList));
    session.close();
    return excelList;
  }

  public int insertToDb(Excel excel) {
    SqlSession session = SqlSessionTemplate.getSqlSession();
    int result = dao.insertToDb(session, excel);
    session.close();
    return result;
  }

  public int deleteFromDb(String insertNo) {
    SqlSession session = SqlSessionTemplate.getSqlSession();
    int result = dao.deleteFromDb(session, insertNo);
    session.close();
    return result;
  }

  public int updateFromDb(Excel excel) {
    SqlSession session = SqlSessionTemplate.getSqlSession();
    int result = dao.updateFromDb(session, excel);
    session.close();
    return result;
  }
}
