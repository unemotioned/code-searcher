# Model-Searcher

## Generate Executable File

---

### Create .jar file

```power shell
java --module-path C:\javafx-sdk-21.0.8\lib `
     --add-modules javafx.controls,javafx.fxml `
-jar target/model-searcher-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

### Create Runtime Image with jlink

- `runtime` directory will be created under the project folder

```power shell
jlink --module-path "%JAVA_HOME%\jmods;C:\javafx-jmods-21.0.8" `
      --add-modules javafx.controls,javafx.fxml `
      --output runtime
```

---

### Package the App using jpackage

- A standalone app image will be created under the `dist` directory

```power shell
jpackage --type app-image --name "model-searcher" --input C:\Repository\model-searcher\target --main-jar model-searcher-1.0-SNAPSHOT-jar-with-dependencies.jar --main-class com.mcs.modelsearcher.MainApplication --runtime-image runtime --dest dist --resource-dir src\main\resources --win-console

```

---

### Happy hacking 🎉
