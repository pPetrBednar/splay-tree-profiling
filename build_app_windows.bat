@ECHO OFF

rem ------ ENVIRONMENT --------------------------------------------------------
rem The script depends on various environment variables to exist in order to
rem run properly. The java version we want to use, the location of the java
rem binaries (java home), and the project version as defined inside the pom.xml
rem file, e.g. 1.0-SNAPSHOT.
rem
rem PROJECT_VERSION: version used in pom.xml, e.g. 1.0-SNAPSHOT
rem APP_VERSION: the application version, e.g. 1.0.0, shown in "about" dialog

set JAVA_VERSION=19
set MAIN_JAR=STP-%PROJECT_VERSION%.jar

rem Set desired installer type: "app-image" "msi" "exe".
set INSTALLER_TYPE=msi

echo Java home: %JAVA_HOME%
echo Project version: %PROJECT_VERSION%
echo App version: %APP_VERSION%
echo Main JAR file: %MAIN_JAR%

rem ------ SETUP DIRECTORIES AND FILES ----------------------------------------
rem Remove previously generated java runtime and installers. Copy all required
rem jar files into the input/libs folder.

IF EXIST target\java-runtime rmdir /S /Q  .\target\java-runtime
IF EXIST target\installer rmdir /S /Q target\installer

echo Copying dependencies
xcopy /S /Q target\lib\* target\installer\input\lib\

echo Copying libraries
xcopy /S /Q app\lib\* target\installer\input\lib\

echo Copying resources
xcopy /S /Q app\res\* target\installer\input\res\

echo Copying main jar
copy target\%MAIN_JAR% target\installer\input\lib\

rem ------ REQUIRED MODULES ---------------------------------------------------
rem Use jlink to detect all modules that are required to run the application.
rem Starting point for the jdep analysis is the set of jars being used by the
rem application.

echo Detecting required modules

"%JAVA_HOME%\bin\jdeps" ^
  -q ^
  --multi-release %JAVA_VERSION% ^
  --ignore-missing-deps ^
  --class-path "target\installer\input\lib\*" ^
  --print-module-deps target\classes\io\github\ppetrbednar\stp\App.class > temp.txt

set /p detected_modules=<temp.txt

echo Detected modules: %detected_modules%

rem ------ MANUAL MODULES -----------------------------------------------------
rem jdk.crypto.ec has to be added manually bound via --bind-services or
rem otherwise HTTPS does not work.
rem
rem See: https://bugs.openjdk.java.net/browse/JDK-8221674
rem
rem In addition we need jdk.localedata if the application is localized.
rem This can be reduced to the actually needed locales via a jlink parameter,
rem e.g., --include-locales=en,de.
rem
rem Don't forget the leading ','!

set manual_modules=,jdk.crypto.ec,jdk.localedata
echo Manual modules: %manual_modules%

rem ------ RUNTIME IMAGE ------------------------------------------------------
rem Use the jlink tool to create a runtime image for our application. We are
rem doing this in a separate step instead of letting jlink do the work as part
rem of the jpackage tool. This approach allows for finer configuration and also
rem works with dependencies that are not fully modularized, yet.

echo Creating java runtime image

call "%JAVA_HOME%\bin\jlink" ^
  --strip-native-commands ^
  --no-header-files ^
  --no-man-pages ^
  --compress=2 ^
  --strip-debug ^
  --add-modules %detected_modules%%manual_modules% ^
  --include-locales=en ^
  --output target/java-runtime


rem ------ PACKAGING ----------------------------------------------------------
rem In the end we will find the package inside the target/installer directory.

echo Creating installer of type %INSTALLER_TYPE%

call "%JAVA_HOME%\bin\jpackage" ^
  --type %INSTALLER_TYPE% ^
  --dest target/installer ^
  --input target/installer/input ^
  --name STP ^
  --main-class io.github.ppetrbednar.stp.AppLauncher ^
  --main-jar lib/%MAIN_JAR% ^
  --java-options -Xmx2048m ^
  --runtime-image target/java-runtime ^
  --icon src/main/resources/io/github/ppetrbednar/stp/logo/windows/icon.ico ^
  --app-version %APP_VERSION% ^
  --vendor "Petrexis" ^
  --copyright "Copyright © 2022 Petrexis" ^
  --win-dir-chooser ^
  --win-shortcut ^
  --win-per-user-install ^
  --win-menu
