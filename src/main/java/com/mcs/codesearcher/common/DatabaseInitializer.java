package com.mcs.codesearcher.common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

  private static final String DB_DIR =
      System.getProperty("user.home") + File.separator + ".code-searcher";
  private static final String DB_PATH = DB_DIR + File.separator + "sqlite.db";
  private static final String JDBC_URL = "jdbc:sqlite:" + DB_PATH;

  private static final String SCHEMA_SQL =
      """
      drop table if exists file;
      drop table if exists hash;
      drop table if exists data;
      drop table if exists hierarchy;

      drop table if exists data_fts;
      drop trigger if exists data_ai;
      drop trigger if exists data_ad;
      drop trigger if exists data_au;


      create table file
      (
          file_path text
      );

      create table hash
      (
          sheet text,
          hash  text
      );

      create table hierarchy
      (
          parent_no text references data (insert_no),
          child_no  text references data (insert_no)
      );

      create table data
      (
          insert_no        text primary key,
          part_code        text,
          rev              text,
          apply_1          text,
          apply_2          text,
          blueprint_date   text,
          client_blueprint text,
          scan             text,
          self_blueprint   text,
          category         text,
          name             text,
          spec             text,
          maker            text,
          vendor           text,
          unit_price       integer,
          mgmt_cost        integer,
          est_price        integer,
          ref_price        integer,
          note             text
      );


      create virtual table data_fts using fts5
      (
          insert_no,
          part_code,
          rev,
          apply_1,
          apply_2,
          blueprint_date,
          client_blueprint,
          scan,
          self_blueprint,
          category,
          name,
          spec,
          maker,
          vendor,
          unit_price unindexed,
          mgmt_cost unindexed,
          est_price unindexed,
          ref_price unindexed,
          note
      );

      -- after insert
      create trigger data_ai
          after insert
          on data
      begin
          insert into data_fts (insert_no, part_code, rev, apply_1, apply_2, blueprint_date,
                                client_blueprint, scan, self_blueprint, category, name,
                                spec, maker, vendor,
                                unit_price, mgmt_cost, est_price, ref_price, note)
          values (new.insert_no, new.part_code, new.rev, new.apply_1, new.apply_2, new.blueprint_date,
                  new.client_blueprint, new.scan, new.self_blueprint, new.category, new.name,
                  new.spec, new.maker, new.vendor,
                  new.unit_price, new.mgmt_cost, new.est_price, new.ref_price, new.note);
      end;

      -- after delete
      create trigger data_ad
          after delete
          on data
      begin
          delete from data_fts where insert_no = old.insert_no;
      end;

      -- after update
      create trigger data_au
          after update
          on data
      begin
          update data_fts
          set insert_no        = new.insert_no,
              part_code        = new.part_code,
              rev              = new.rev,
              apply_1          = new.apply_1,
              apply_2          = new.apply_2,
              blueprint_date   = new.blueprint_date,
              client_blueprint = new.client_blueprint,
              scan             = new.scan,
              self_blueprint   = new.self_blueprint,
              category         = new.category,
              name             = new.name,
              spec             = new.spec,
              maker            = new.maker,
              vendor           = new.vendor,
              unit_price       = new.unit_price,
              mgmt_cost        = new.mgmt_cost,
              est_price        = new.est_price,
              ref_price        = new.ref_price,
              note             = new.note
          where insert_no = old.insert_no;
      end;
      """;

  public DatabaseInitializer() {
    try {
      initializeDatabase();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to initialize the database", e);
    }
  }

  private void initializeDatabase() throws Exception {
    Path dbDir = Paths.get(DB_DIR);
    Path dbFile = Paths.get(DB_PATH);

    // Create directory if it doesn't exist
    if (!Files.exists(dbDir)) {
      Files.createDirectories(dbDir);
    }

    // If DB file doesn't exist, create it and initialize schema
    boolean createSchema = !Files.exists(dbFile);

    try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
      if (createSchema) {
        try (Statement stmt = conn.createStatement()) {
          stmt.executeUpdate("PRAGMA foreign_keys=ON;");
          stmt.executeUpdate(SCHEMA_SQL);
          System.out.println("Database created and schema initialized at: " + DB_PATH);
        }
      } else {
        System.out.println("Database already exists at: " + DB_PATH);
      }
    }
  }

  public static String getJdbcUrl() {
    return JDBC_URL;
  }
}
