echo off
cls


color 9

set bakDir="C:\newGit\clu-tools-soapui\src\main\groovy"
set srcDir="C:\Program Files\SmartBear\SoapUI-Pro-5.0.0\bin\scripts\com"
if not exist %bakDir% mkdir %bakDir%
set logDir=C:\Bats\Log_Files
if not exist %logDir% mkdir %logDir%
set logDir=%logDir%\%Date:~-10,2%-%Date:~-7,2%-%Date:~-4,4%_Backup.log
color C
cls

echo *********************************************************************** > %logDir%
echo Executing Replace in Repo at: %date% %time%>> %logDir%
echo Source Directory: %srcDir% >> %logDir%
echo Backup Directory: %bakDir% >> %logDir%
echo . >> %logDir%
echo . >> %logDir%
echo . >> %logDir%

REM #####Copy Files
echo Copying Files >>%logDir%
xcopy %srcDir%\*.*   %bakDir%\com\ /c /s /r /d /y /i >>%logDir% 
type %logDir%
echo Copying Completed !!!
echo *********************************************************************** >> %logDir%

color A
echo Backup Completed...!
