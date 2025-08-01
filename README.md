# Model-Searcher

## Generate .executable File

### Create .jar file

```power shell
java --module-path C:\javafx-sdk-21.0.8\lib --add-modules javafx.controls,javafx.fxml -jar target/model-searcher-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

### Runtime Image with Jlink

```power shell
jlink --module-path "%JAVA_HOME%\jmods;C:\javafx-jmods-21.0.8" --add-modules javafx.controls,javafx.fxml --output runtime
```

---

### Full thing with Jpackage

```power shell
jpackage --type app-image --name "model-searcher" --input C:\Repository\model-searcher\target --main-jar model-searcher-1.0-SNAPSHOT-jar-with-dependencies.jar --main-class com.mcs.modelsearcher.MainApplication --runtime-image runtime --dest dist --resource-dir src\main\resources --win-console
```

---

### Happy hacking 🎉

---

## Create DataBase

```sql
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
```

---

#### Happy Hacking 🎉
