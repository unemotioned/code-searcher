# Code-Searcher

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
