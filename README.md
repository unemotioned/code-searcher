# Code Searcher

A [JavaFX](https://openjfx.io/) desktop application that imports a predefined
Excel workbook into a [SQLite](https://sqlite.org/) database for fast searching
and editing.

---

## Table of Contents

- [설명서](#설명서)
- [동작 방식](#동작-방식)
- [Prerequisite](#prerequisite)
- [IntelliJ Settings](#intellij-settings)
- [Deployment](#deployment)
  - [Build JAR](#build-jar)
  - [Run Built JAR](#run-built-jar)
  - [Runtime with jlink](#runtime-with-jlink)
  - [Package with jpackage](#package-with-jpackage)
- [TODO](#todo)

---

## 설명서

1. 프로그램 실행

2. 서버에서 자재관리대장 파일 선택

3. 통합 검색창에서 키워드로 검색
   - 모든 행의 모든 셀에 대해서 검색
   - 띄어쓰기를 이용해 키워드 구분
   - 엔터를 누르지 않아도 자동으로 검색
   - 날짜 검색할때는 `-` 사용 (예: `2025-08-08`)

4. 검색 결과에서 셀 **더블클릭** &rarr; 클립보드에 자동으로 **복사됨**

> [!WARNING]
> 마이크로소프트 엑셀로 열려있으면 편집 불가

### 자제 삽입 버튼

- **등록 no**: 엑셀파일에서 가장 마지막의 번호 + 1
- **Rev.**: 기본값 000
- **고객도면 작성일**: 6자리 년월일로 입력
- **고객도면, 스캔, 자체도면**: 숫자 `0`을 입력하면 엑셀에 `○`로 입력됨

### 수정, 삭제

- 검색결과의 우클릭 메뉴에서 선택

### 다시 읽기

- 엑셀파일의 정보와 검색결과가 일치하지 않을때 DB를 초기화하고 다시 읽어옴

---

## 동작 방식

- 엑셀 파일을 읽음 전체 데이터를 HASH 하여서 현재 상태를 이전 값과 비교

- HASH 값이 다르면 프로그램 자체 데이터베이스 `(경로: C:/사용자/{사용자이름}/.code-searcher/sqlite.db)`를
  전부 지우고 다시 생성

- 검색 및 편집 작업 수행

### Tip

- 검색창에서 더블클릭 하면 키워드 하나 선택, 트리플 클릭하면 모든 키워드 선택됨

- 쉬프트키를 누르고서 마우스 스크롤을 하면 화면이 좌우로 이동 ("비고" 칸 보기 편함)

---

## Prerequisite

Download and install the following programs

- [Amazon Corretto 21](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html#amazon-corretto-yum-urls)
- [JavaFX](https://gluonhq.com/products/javafx/#downloads)
- [Scene Builder](https://gluonhq.com/products/scene-builder/#download)
- [Apache Maven](https://maven.apache.org/download.cgi)

To set up Maven **Environment Variable** on Windows follow instructions from:

[unemotioned/spring-boot-template](https://github.com/unemotioned/spring-boot-template#apache-maven-preparation)

> [!IMPORTANT]
> Major versions of **JDK** and **JavaFX** must match.

---

## IntelliJ Settings

### Project Structure

- Project > **SDK**: `21`

- Libraries
  1. Remove every default libraries
  2. Add new `Java` project libraries
  3. Select every **Jar** files under `C:/javafx-sdk-21.0.11/lib` directory

### Edit Configurations

Add new `Application`

- **JDK**: `java 21`

- **Program Arguments**:

```text
-Dfile.encoding=UTF-8
```

- **Modify options** > `Add VM options`
  - **VM options**:

```text
--module-path "C:\javafx-sdk-21.0.11\lib" --add-modules javafx.controls,javafx.fxml
```

### Maven

Settings > Build, Execution, Deployment > **Maven** > Check **Override** of
`User Settings file:` > Select `C:\apache-maven-3.9.16\conf\settings.xml`

### Lombok

Settings > Build, Execution, Deployment > Compiler > **Annotation Processors** >
Check `Enable annotation processing`

---

## Deployment

`jlink`, `jpackage` is included in JDK.

### Build JAR

Build program with **Maven**. Creates `target` directory with **JAR** files

```sh
mvn clean package
```

### Run Built JAR

Check built **JAR** file runs properly.

```sh
java ^
  --module-path C:\javafx-sdk-21.0.11\lib ^
  --add-modules javafx.controls,javafx.fxml ^
  -jar target/code-searcher-1.0.5-jar-with-dependencies.jar
```

### Runtime with jlink

Create `Java runtime image` which includes necessary **Java modules** and
**JavaFX**. Creates `runtime` directory.

```sh
jlink ^
  --module-path "%JAVA_HOME%\jmods;C:\javafx-jmods-21.0.11" ^
  --add-modules javafx.controls,javafx.fxml,java.sql ^
  --output runtime
```

### Package with jpackage

Create Windows executable **Application Image**. Creates `dist` directory.

```sh
jpackage ^
  --type app-image ^
  --name "code-searcher" ^
  --input C:\dev\code-searcher\target ^
  --main-jar code-searcher-1.0.5-jar-with-dependencies.jar ^
  --main-class com.mcs.codesearcher.CodeSearcher ^
  --runtime-image runtime ^
  --dest dist ^
  --icon C:\dev\code-searcher\src\main\resources\icon\code-searcher.ico
```

> [!NOTE]
> Now you can distribute `dist` folder to other machines and use **Code Searcher** program.

---

## TODO

- [ ] Check DB integrity by last modified date instead of hashing entire file
- [ ] After insert or edit update db's last modified data
- [ ] Allow search of rows without insert_no
- [ ] When inserting new data must account for rows without insert_no
- [ ] Update JDK and JavaFX to version 25
