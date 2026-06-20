@rem Maven Wrapper script for Windows

@if "%MAVEN_HOME%"=="" set MAVEN_HOME=%~dp0\.mvn\wrapper
@if "%MAVEN_JAVA_EXE%"=="" set MAVEN_JAVA_EXE=java.exe

"%MAVEN_JAVA_EXE%" %MAVEN_OPTS% -classpath "%MAVEN_HOME%\maven-wrapper.jar" "-Dmaven.home=%MAVEN_HOME%" org.apache.maven.wrapper.MavenWrapperMain %*
