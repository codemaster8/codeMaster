
SET /P AREYOUSURE=Do you want to reset ?  [y/n]
IF /I "%AREYOUSURE%" NEQ "Y" GOTO exiting

cd C:\DEVELOP
git status
git clean -fd
git reset --hard
git clean -fd
git pull -a
git checkout develop
git pull -a
:exiting