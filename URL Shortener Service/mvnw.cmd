@echo off
setlocal

set "MAVEN_PROJECTBASEDIR=%~dp0"
set "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%"

set "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set "WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties"
set "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain"

if not exist "%WRAPPER_JAR%" (
  echo Maven wrapper jar missing: "%WRAPPER_JAR%"
  exit /b 1
)

if not exist "%WRAPPER_PROPERTIES%" (
  echo Maven wrapper properties missing: "%WRAPPER_PROPERTIES%"
  exit /b 1
)

set "JAVA_EXE=java"
if defined JAVA_HOME set "JAVA_EXE=%JAVA_HOME%\bin\java"

"%JAVA_EXE%" %MAVEN_OPTS% ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  -classpath "%WRAPPER_JAR%" ^
  %WRAPPER_LAUNCHER% %*

endlocal
