package com.mcs.modelsearcher.file.model.service;

import com.mcs.modelsearcher.common.SqlSessionTemplate;
import com.mcs.modelsearcher.file.model.dao.FileDao;
import org.apache.ibatis.session.SqlSession;

public class FileService {
    private final FileDao dao;

    public FileService() {
        super();
        dao = new FileDao();
    }

    public int insertPath(String path) {
        SqlSession session = SqlSessionTemplate.getSqlSession();
        int result = dao.insertPath(session, path);
        session.close();
        return result;
    }
}
