# Set strict mode for better error handling
Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

# Set paths
$javafxLib = "C:\javafx-sdk-21.0.8\lib"
$javafxJmods = "C:\javafx-jmods-21.0.8"
$projectDir = "C:\Repository\model-searcher"
$targetDir = "$projectDir\target"
$runtimeDir = "$projectDir\runtime"
$distDir = "$projectDir\dist"
$resourcesDir = "$projectDir\src\main\resources"
$mainJar = "model-searcher-1.0-SNAPSHOT-jar-with-dependencies.jar"
$mainClass = "com.mcs.modelsearcher.ModelSearcher"

# Step 1: Build JAR using Maven
Write-Host "📦 Building JAR with Maven..."
cd $projectDir
mvn clean package

# Step 2: Create custom Java runtime with jlink
Write-Host "🔧 Creating runtime image with jlink..."
if (Test-Path $runtimeDir) { Remove-Item -Recurse -Force $runtimeDir }
jlink --module-path "$Env:JAVA_HOME\jmods;$javafxJmods" `
      --add-modules javafx.controls,javafx.fxml `
      --output $runtimeDir

# Step 3: Package with jpackage
Write-Host "📦 Packaging app with jpackage..."
if (Test-Path $distDir) { Remove-Item -Recurse -Force $distDir }
jpackage --type app-image `
         --name "model-searcher" `
         --input $targetDir `
         --main-jar $mainJar `
         --main-class $mainClass `
         --runtime-image $runtimeDir `
         --dest $distDir `
         --resource-dir $resourcesDir `
         --win-console

Write-Host "✅ Done! Check the 'dist' folder."
