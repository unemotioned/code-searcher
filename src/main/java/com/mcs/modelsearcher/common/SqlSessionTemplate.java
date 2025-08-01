package com.mcs.modelsearcher.common;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

public class SqlSessionTemplate {

  private static SqlSessionFactory sqlSessionFactory;

  static {
    try {
      // Get the user's home directory
      String homeDir = System.getProperty("user.home");
      String dbPath = homeDir + "/model-searcher/sqlite.db";
      String jdbcUrl = "jdbc:sqlite:" + dbPath;

      // Set up SQLite datasource
      UnpooledDataSource dataSource = new UnpooledDataSource();
      dataSource.setDriver("org.sqlite.JDBC");
      dataSource.setUrl(jdbcUrl);

      // Set up MyBatis environment programmatically
      Environment environment = new Environment("dev", new JdbcTransactionFactory(), dataSource);
      Configuration configuration = new Configuration(environment);

      sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    } catch (Exception e) {
      System.out.println("SqlSessionTemplate.SqlSessionFactory: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public static SqlSession getSqlSession() {
    SqlSession session = sqlSessionFactory.openSession(true);
    try (Connection conn = session.getConnection();
         Statement stmt = conn.createStatement()) {
      stmt.execute("PRAGMA foreign_keys = ON;");
    } catch (Exception e) {
      System.out.println("SqlSessionFactory.getSqlSession - while enabling foreign keys: " + e.getMessage());
    }
    return session;
  }
}