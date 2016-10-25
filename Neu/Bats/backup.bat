echo off
cls


color 9

REM set bakDir=\\pun-srvfile01\Healthcare\xData\Backup\Utkarsh\%Date:~-10,2%-%Date:~-7,2%-%Date:~-4,4%
set bakDir=D:\AutoBackup\%Date:~-10,2%-%Date:~-7,2%-%Date:~-4,4%
set srcDir="Z:\CLUMP"
if not exist %bakDir% mkdir %bakDir%
if not exist %bakDir%\Log_Files mkdir %bakDir%\Log_Files
set logDir="%bakDir%\Log_Files\eagleBackup_%Date:~-10,2%-%Date:~-7,2%-%Date:~-4,4%.log"

color C
cls
echo Source Directory: %srcDir%
echo Backup Directory: %bakDir%

REM #####Copy Files
echo Copying Files
xcopy %srcDir%\*.*   %bakDir%\com\ /c /s /r /d /y /i>>%logDir%


color A
echo Backup Completed...!

