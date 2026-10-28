@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a key stroke before ending
@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM     set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM MAVEN_SKIP_RC - flag to indicate skipping ofomavenrc files
@REM ----------------------------------------------------------------------------

@REM Begin all @rem decisions with double colon ::


@echo off
@REM set title of command prompt window
title %0
@REM enable echoing by setting MAVEN_BATCH_ECHO to 'on'
@if "%MAVEN_BATCH_ECHO%" == "on"  echo %MAVEN_BATCH_ECHO%

@REM set %HOME% to equivalent of $HOME
if "%HOME%" == "" (set "HOME=%USERPROFILE%")

@REM Execute a user defined script before this one
if not "%MAVEN_SKIP_RC%" == "" goto skipRcPre
@REM check for pre script, once with legacy name, once with meeting standards
if exist "%USERPROFILE%\.mavenrc_pre.bat" call "%USERPROFILE%\.mavenrc_pre.bat"
if exist "%USERPROFILE%\mavenrc_pre.bat" call "%USERPROFILE%\mavenrc_pre.bat"
:skipRcPre

@REM Begin _top
if not "%NETBEANS_PROFILER_DETACHED%" == "" goto envClean
set ERROR_CODE=0

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo Error: JAVA_HOME not found in your environment. >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto init

echo.
echo Error: JAVA_HOME is set to an invalid directory. >&2
echo JAVA_HOME = "%JAVA_HOME%" >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:init

@REM Find the project base directory, i.e. the directory that contains the folder ".mvn".
@REM Fallback to current directory if not found.

set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
if not "%MAVEN_PROJECTBASEDIR%" == "" goto endDetectBaseDir

set EXEC_DIR=%CD%
set WNW_DIR=%~dp0
set MAVEN_PROJECTBASEDIR=%WNW_DIR%
:endDetectBaseDir

IF NOT EXIST "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" (
    echo "maven-wrapper.jar missing. Downloading it..."
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('https://repo.maven.apache.org/maven2/wrapper/maven-wrapper/3.1.0/maven-wrapper-3.1.0.jar', '%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar')"
)

@REM Provide a "standard" structured location for the intermediate
@REM execution debug java process.
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set MAVEN_CONFIG=%MAVEN_PROJECTBASEDIR%\.mvn

%WRAPPER_JAR%

@REM Setup the command line
set CLASSPATH=%WRAPPER_JAR%

@REM Start Maven
"%JAVA_HOME%\bin\java.exe" %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% -classpath %CLASSPATH% %WRAPPER_LAUNCHER% %*

if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNew
@REM otherwise environment variables are clean
goto postRc

:endNew
@REM clear local environment variables

:postRc
@REM check for post script, once with legacy name, once with meeting standards
if not "%MAVEN_SKIP_RC%" == "" goto skipRcPost
if exist "%USERPROFILE%\.mavenrc_post.bat" call "%USERPROFILE%\.mavenrc_post.bat"
if exist "%USERPROFILE%\mavenrc_post.bat" call "%USERPROFILE%\mavenrc_post.bat"
:skipRcPost

@REM pause the script if MAVEN_BATCH_PAUSE is set to 'on'
if "%MAVEN_BATCH_PAUSE%" == "on" pause

if "%NETBEANS_PROFILER_DETACHED%" == "" exit /B %ERROR_CODE%
