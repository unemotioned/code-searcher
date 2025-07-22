package com.mcs.modelsearcher.common;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class SqlSessionTemplate {
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (Exception e) {
            System.out.println("Error in SqlSessionFactory: " + e.getMessage());
        }
    }

    public static SqlSession getSqlSession() {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            session.getConnection().createStatement().execute("PRAGMA foreign_keys = ON;");
        } catch (Exception e) {
            System.out.println("Failed to enable foreign keys: " + e.getMessage());
        }
        return session;
    }
}
