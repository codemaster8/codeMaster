taskkill /f /im tail.exe

set folder="C:\clu-QAAutomation\EPIC\testResults\test-epic\DocumentExtraction"

cd %folder%
for /f "eol=: delims=" %%F in ('dir /b /od') do @set "newest=%%F"

explorer.exe "%newest%"
C:\Bats\Tail-4.2.12\Tail.exe %newest%\output.log
%newest%\VerificationResults.csv
