# Code-Searcher

## 설명서

1. 프로그램 실행
2. 서버에서 자재관리대장 파일 선택

3. 통합 검색창에서 키워드로 검색
   - 모든 행의 모든 셀에 대해서 검색
   - 띄어쓰기를 이용해 키워드 구분
   - 엔터를 누르지 않아도 자동으로 검색
   - 날짜 검색할때는 "-" 사용

4. 검색 결과에서 셀 더블클릭 -> 클립보드에 자동으로 복사됨

### 엑셀파일 수정하기 전 다른 프로그램에서 열려 있지 않은지 확인

- 마이크로소프트 엑셀로 열려있으면 편집 불가

1. 자제 삽입 버튼
   - 등록 no: 엑셀파일에서 가장 마지막의 번호 + 1
   - Rev.: 기본값 000
   - 고객도면 작성일: 6자리 년월일로 입력
   - 고객도면, 스캔, 자체도면: 숫자 "0"을 입력하면 엑셀에 "○"로 입력됨

2. 수정, 삭제
   - 검색결과의 우클릭 메뉴에서 선택

3. 다시 읽기
   - 엑셀파일의 정보와 검색결과가 일치하지 않을때
     DB를 초기화하고 다시 읽어옴

---

## 동작 방식

- 엑셀 파일을 읽음
  전체 데이터를 HASH 하여서 현재 상태를 이전 값과 비교

- HASH 값이 다르면 프로그램 자체 데이터베이스(경로: C:/사용자/{사용자이름}/./code-searcher/sqlite.db)를
  전부 지우고 다시 생성

- 검색 및 편집 작업 수행

### Tip

- 검색창에서 더블클릭 하면 키워드 하나 선택,
  트리플 클릭하면 모든 키워드 선택됨

- 쉬프트키를 누르고서 마우스 스크롤을 하면 화면이 좌우로 이동("비고" 칸 보기 편함)

---

## Generate Executable

### Create .jar file

```bash
java
--module-path C: \javafx-sdk-21.0.8\lib ^
--add-modules javafx.controls, javafx.fxml -jar target/code-searcher-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

### Create Runtime Image with jlink

- `runtime` directory will be created under the project folder

```bash
jlink
--module-path "%JAVA_HOME%\jmods;C:\javafx-jmods-21.0.8" ^
--add-modules javafx.controls,javafx.fxml,java.sql ^
--output runtime
```

---

### Package the App using jpackage

- A standalone app image will be created under the `dist` directory

```bash
jpackage
--type app-image ^
--name "code-searcher" ^
--input C:\Repository\code-searcher\target ^
--main-jar code-searcher-1.0-SNAPSHOT-jar-with-dependencies.jar ^
--main-class com.mcs.codesearcher.CodeSearcher ^
--runtime-image runtime ^
--dest dist ^
--icon C:\Repository\code-searcher\src\main\resources\icon\code-searcher.ico ^
--type app-image
```

- Add this to end of jpackage command to open terminal with the App

```bash
--win-console
```

---

```bash
java --module-path C:\javafx-sdk-21.0.8\lib --add-modules javafx.controls,javafx.fxml -jar target/code-searcher-1.0-SNAPSHOT-jar-with-dependencies.jar
```

```bash
jlink --module-path "%JAVA_HOME%\jmods;C:\javafx-jmods-21.0.8" --add-modules javafx.controls,javafx.fxml,java.sql --output runtime
```

```bash
jpackage --type app-image --name "code-searcher" --input C:\Repository\code-searcher\target --main-jar code-searcher-1.0-SNAPSHOT-jar-with-dependencies.jar --main-class com.mcs.codesearcher.CodeSearcher --runtime-image runtime --dest dist --icon C:\Repository\code-searcher\src\main\resources\icon\code-searcher.ico --type app-image
```

---

#### Happy hacking 🎉
