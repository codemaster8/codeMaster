
@ECHO OFF

color 0b
cls
cd C:\REPO\qa\testcases\
echo ################################################################################
echo,
echo,
git status
echo,
echo,
SET /P AREYOUSURE=Do you want to reset ?  [y/n]
IF /I "%AREYOUSURE%" NEQ "Y" GOTO Commit

git clean -fd
git reset --hard
cls
echo,
echo,
echo,
echo ################################################################################
echo,
echo     Press any key to make changes as needed in the repository
echo        This will open the repository for modifications
echo,
echo ################################################################################
pause > NUL
explorer C:\REPO\qa\testcases
cls
echo,
echo,
echo,

echo ################################################################################
echo,
echo      Modify all files as needed now and Press any Key to continue to Commit
echo,
echo ################################################################################
pause > NUL
:Commit
cls
echo MAKE SURE MODIFIED CHANGES ARE AS EXPECTED !!!
echo ################################################################################
git diff
git status
echo,

echo ################################################################################
echo,
echo,
pause
echo,
echo,
echo Adding All changes  
git add -A
git status
echo,
echo,
echo Press any Key to proceed and push all changes
set /p cMsg=Pushing changes enter commit message:
echo,
echo,
git commit -m"%cMsg%"
git push

echo Git push complete !!! 
sleep 1
echo Git push complete !!! 
sleep 1
echo Yeppie Git push complete !!! 
sleep 1
cls
echo,
echo,
echo,
echo ################################################################################
echo,
echo,
echo Latest Commit ID : 
git rev-parse HEAD
echo,
echo,
echo ################################################################################
pause
