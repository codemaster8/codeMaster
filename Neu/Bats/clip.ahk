
#Persistent
return

OnClipboardChange:

;Kill Excel Remains
Process,Close,Excel.exe

StringSplit, array, clipboard,">"

DirectoryCheckVar = %array2%
IfNotExist, %DirectoryCheckVar%
{
    ToolTip %DirectoryCheckVar%    >>Not Preset 
	Sleep 3000
}
else
{
    ToolTip %DirectoryCheckVar% Exists Opening !!!
	Sleep 1500
	Run, %DirectoryCheckVar%\VerificationResults.csv
}
ToolTip,  

Winactivate, Microsoft Excel - VerificationResults.csv
WinWaitActive, Microsoft Excel - VerificationResults.csv, 

SendInput, {Shift Down}{Ctrl Down}{D Down}
SendInput, {Shift Up}{Ctrl Up}{D Up}


return
