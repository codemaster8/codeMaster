Desktop Info
by Glenn Delahoy
(C) Copyright 2005-2014
All rights reserved

Description
-----------
This little application displays real-time system information on your desktop.
Looks like wallpaper but stays resident in memory. Perfect for quick
identification and walk-by monitoring of production or test server farms.
Everything is customisable.

License Agreement
-----------------
This software is distributed free of charge. It may be used as many times as you 
like, for as long as you like, in a domestic or corporate environment. You may
copy and distribute copies of this program provided that you keep all original
documentation including this readme.txt file with copyright notice and
disclaimer of warranty intact. You may not charge money or fees for the software
product to anyone except to cover distribution costs.

Warranty
--------
This program is provided "as is" without warranties of any kind, either
expressed or implied, including, but not limited to, the implied warranties of
merchantability and fitness for a particular purpose. The entire risk as to the
quality and performance of the program is with you. Should the program prove
defective, you assume the cost of all necessary servicing, repair or correction.
In no event will any copyright holder be liable to you for damages, including
any general, special, incidental or consequential damages arising out of the use
or inability to use the program (including but not limited to loss of data or
data being rendered inaccurate or losses sustained by you or third parties or a
failure of the program to operate with any other programs).

Technical Support
-----------------
No guarantees whatsoever are implied that technical support will be provided or
that technical support, when provided, will be accurate. This software is
basically unsupported and supplied on an as-is basis.

Usage
-----
Just run it. You can kill it via the right click context menu or from Task
Manager. Open the desktopinfo.ini file or select Configuration from the right
click context menu and adjust each item in the items section to control
visibility, refresh times, colors and other properties. The display updates
itself automatically when you save the ini file.

Options
-------
There is no configuration program. Options are set by modifying the ini file in
a text editor such as Notepad. The included ini file shows all the available
options. Display values are relative to the primary display so if you have a
dual monitor display, you may need to specify negative values for any of these
depending on the display arrangement. Here are the rules for the position
options:

     if left is specified, it is left aligned
     if right is specified, it is right aligned
     if neither left or right, it is left
     if both are specified, it is left
     if top is specified, it is top aligned
     if bottom is specified, it is bottom aligned
     if neither top or bottom, it is top
     if both are specified, it is top
     if font size is not specified, it is 8
     if font size is less than 6, it is 8
     if width is not specified, it is half the primary monitor width
     height is always the number of items

column1width            The width of column 1. -1 is automatic, 0 will
                        effectively eliminate the column allowing the data
                        column to occupy the whole space.
centerv                 Center vertically
centerh                 Center horizontally
fontface                The name of the font.
fontsize                The size of the font.
cleartype               Enables/disables ClearType for the font.
ssfontsize              The font size in screen saver mode
formcolor               Background color
transparency            Background transparency. 0 is opaque, 100 is totally
                        transparent.
contextmenu             Enables/disables the right click context menu.
allowdrag               Enables/disables the ability to drag the form. If the
                        /f option is used the form is always draggable.
offset                  Enables/disables the display offset of the network
                        adapter, fixed disk and printer sub-items.
language                File name containing alternate display text for the
                        items.
msnstatus               Enables option to update msn status.
inimonitortime          How often to check for desktopinfo.ini changes.
log                     Write debug information to the specified log file.


Colors
------
Colors are specified as a six character bgr (reverse rgb) hexadecimal number.
The first two characters represent the level of blue, the second represent the
level of green, the third two represent the level of red. Each element has a
range of 00-ff (0-255). ff0000 is blue, 00ff00 is green, 0000ff is red. ffffff
is white (all color elements at maximum), 000000 is black (all color elements
are off).

Items
-----
The items section in the ini file controls the order and state of each info
item. Each item contains a comma delimited list of key:value pairs.

 key               value
 ---               -----
 active          : a value of 0 is off, 1 is on, 2 is on and hidden
 interval        : a value in seconds where 0 means never refresh
 color           : a bgr value as defined above
 chart           : chart type to display where 0 = no chart, 1 = bar chart,
                   2 = line chart. See below for items that have charts.
 threshold       : the value where the item text will change color
 tcolor          : the new text color when the value reaches the threshold
                   value
 style           : font style where b is bold, i is italic, u is underline
 offset          : for network items, a value of 0 is off, 1 is on
 count           : for multi items, sets the maximum number of items displayed
 shortdisplay    : abbreviates the numerical displays
 activeonly      : for network adapters, shows only active adapters
 filter          : for network adapters and fixed disks, allows filtering
 csv             : output data to a csv file
 text            : alternative column 1 description text. use this on any item,
                   required for wmi items.

An example is:

DATETIME=active:1,interval:1,color:FFFFFF

where the datetime item is active, refreshed every second and the color is
white.

CPU=active:1,interval:2,color:0000FF,chart:1,style:b

is active, refreshed every two seconds, the color is red, chart number 1 is
displayed and the font style is bold.

If an item does not have a chart, the chart value is ignored. If you don't
specify an item at all, it will not display.

Some items such as network adapter, fixed disk and printer control all items
of that class. For example if you switch on fixed disk, all detected fixed
disks are displayed including most usb disks.

Charts
------
Where an item has two values such as NETPACKETSRATE and DISKIO, the bar chart
value will be the sum of the two values. On line charts the first value
takes the color configured for that item, the second value takes a standard
brown/orange/tan kind of color. Any item that shows a value that can move up
and down such as CPU, CPUTEMP, BATTERY, DISKIO etc will have both bar and line
charts available. Check the ini file. Try adding a chart if it's not there on
the item.

Thresholds
----------
Items that display values may be configured to change color when that item
reaches a given value. It could be an absolute value such as PAGEFAULTS,
DISKQUEUE, it could be a percent such as CPU, PHYSICALRAM or a rate such as
NETPACKETSRATE, DISKIO.

Numbers
-------
Several of the entries show two numbers. For example, the physical ram might
show something like 528MB / 1024MB (51% used).  The first number is the amount
used, the second is the total. So in this case it shows 528MB used out of a
total of 1024MB which equates to 51% used. The same is true for virtual memory,
page file and fixed disks.

You can add the option SHORTDISPLAY to the items PHYSICALRAM, VIRTUALMEMORY,
PAGEFILE and FIXEDDISK.  This will abbreviate the standard display. For example,
the PHYSICALRAM item is normally "2052MB / 4061MB (50% used)". The abbreviated
version is "2052 / 4061MB (50%)". 

Filters
-------
The filter option will be added to selected items as required.

    NETWORKADAPTER Defines which adapters will be displayed. It should be the
                   last option on the line and consists of the keyword "filter:"
                   followed by include options prefixed with a + (plus sign) and
                   exclude options prefixed with a - (minus sign).

                   NETWORKADAPTER=active:1,interval:30,filter:+PCI+wireless-virtual

                   This example will show all adapters that contain the keywords
                   "PCI" and "wireless" and will exclude adapters that contain
                   the keyword "virtual".

    FIXEDDISK      Defines which drives will be displayed. To show only drive C:
                   add "filter:C:". To show drives C: and E: add "filter:C:E:".
                   It's case sensitive.

                  FIXEDDISK=active:1,interval:10,color:FF9955,filter:C:E:

File Monitors
-------------
Desktop Info can monitor files and folders for changes to size, write time or
version number. Add one or more items called FILE anywhere in the items section.
In addition to the normal item values, add the following values:

   text     Display name of the item
   type     Monitor type (text, size, time, version)
   file     File or folder name

The text type will display the first line of the given text file when the last
write time changes. This might be useful for monitoring semaphore or progress
files created by batch processes. The size type will display the file size when
the size changes. The time type will display the last write and access times
when either changes. The version type will display the file version resource
string when the file changes.

This monitor will work for local and network files and folders. If the file
or directory does not exist, the display will show "<N/A>". When monitoring
a directory make sure there is no trailing backslash.

This example displays the contents of the given file whenever it changes:

FILE=active:1,interval:10,color:ff5555,type:text,text:Status,file:c:\temp\semaphore.txt

This example shows the executable version number whenever it changes:

FILE=active:1,interval:10,color:ff5555,type:version,text:Desktop Info,file:d:\development\desktopinfo\desktopinfo.exe

Registry Monitors
-----------------
Desktop Info can monitor registry keys or values for changes. If you specify a
key, the date/time of the last detected change is displayed. If you specify a
value, the value and date/time of the last detected change is displayed. If you
specify a key you can also choose to monitor just that key or the entire tree
starting at that key.

Add one or more items called REGISTRY anywhere in the items section. In addition
to the normal item values, add the following values:

   text         Display name of the item
   tree         1 to monitor the tree, 0 to monitor only the key specified
   key          The key or key and value to monitor
   shortdisplay 0 to show date/time of change, 1 to show just the new value

The following example will monitor a value in the Run key.

REGISTRY=active:1,color:ff5555,tree:0,text:Run Key Test,key:HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Run\Test

A value will only be displayed when monitoring a value. If shortdisplay is off,
the value and date/time is displayed, if shortdisplay is on, only the value is
displayed. If monitoring a key including a whole tree, a value is never
displayed. If shortdisplay is off, the date/time is displayed, if shortdisplay
is on, nothing is displayed.

Event Log Monitors
------------------
Desktop Info can monitor event logs for changes. Add one or more items called
EVENTLOG anywhere in the items section. In addition to the normal item values,
add the following values:

   text     Display name of the item
   log      The name of the event log to monitor

The following example will monitor the system event log:

EVENTLOG=active:1,interval:10,color:ff5555,text:System Events,log:System

Language File
-------------
The ini file [options] section may contain a "language" entry which points to an
additional configuration file in the application directory. It contains a
duplicate list of the items found in the desktopinfo.ini file. Each item is a
name=value pair where the name matches the id in the desktopinfo.ini file and
the value is any string you want to use on the display for that item. The
NETWORKADAPTER, FIXEDDISK and PRINTER items may contain %1 which will be
replaced by each item number. Don't forget the "[items]" at the top of the file.

[items]
TIMEZONE=Slide Zone
HOST=Machine Name
USER=Current User
FIXEDDISK=Hard Disk %1

Msn Status / Personal Message
-----------------------------
The ini file [options] section may contain a "msnstatus" entry which enables
the option to update your Live Messenger status / personal message with any
current alarms.

To enable msnstatus, set the value to some number above 10 indicating how often
to update the status in seconds. Alarms are buffered and the oldest will be sent
next. That is, first in first out. If there are no alarms the message is removed
and your Messenger status / personal message will revert to its normal message.
Don't set the value to less than 10 seconds as it will probably be ignored.

Your Live Messenger must be connected and configured to "Show what I'm
listening to". This has been tested on Windows Live Messenger 2009
(14.0.8089.726) and Miranda v0.8.13 but should work with any messenger that
implements the MsnMsgrUIManager interface. Messenger insists on showing the
music icon; still working on that.

Show Desktop
------------
The Show Desktop button on the Windows Quick Launch toolbar makes Desktop Info
invisible, there's nothing I can do about that.  Instead, add a Desktop Info
icon to your Quick Launch toolbar. When you run it, Desktop Info will look to
see if it's running already. If it is, it will attempt to minimise enough
applications to make itself visible. This makes it sort of an alternative to
Show Desktop.  The obscuring windows may choose to ignore the minimise
message or do other bizarre things which are outside of my control.

Command Line Options
--------------------
The following command line options may be used:

/a   Show all items regardless of ini file settings.

/f   Show a visible form. This tells Desktop Info to appear like a regular
     application allowing you to move it to the background or foreground and
     drag it around like any other application.

/ini Specify the full path and file name to an alternative desktopinfo.ini file.
     desktopinfo.exe /ini=mynewinifile.ini     Don't forget the equals sign.
    
Context Menu
------------
To get the context menu to appear in transparent mode, you have to click on a
chart or part of the text which may take a few tries (this is not necessarily
true in Windows 7). The context menu can be disabled by setting the
'contextmenu' item in the 'options' section to '0'.

Show Desktop      In default mode this minimises enough applications to make
                  Desktop Info visible. See above.
Refresh           This forces a refresh of the display and all items.
Configuration     Opens the desktopinfo.ini file with the default text editor.
                  When you edit and save this file, Desktop Info will detect the
                  change and load the new configuration.
Show Hidden Items Toggle the visibility of hidden items.
Read Me           Opens the readme.txt file with the default text editor.
Quit              Closes the Desktop Info application.

Screen Saver
------------
Desktop Info can be used as a screen saver. Make a copy of DesktopInfo.exe and
rename it to DesktopInfo.scr. Right click on this file and select Install. In
the ini file, the "ssfontsize" option controls the font size when running as a
screen saver. The size and position is preset. You can run the exe and the
screen saver at the same time.

Item Visibility
---------------
The visibility of each item is controlled by the 'active' value. If the active
value of an item is set to '1' the item will be visible. If the active value is
set to '2' the item will be active (ie collecting data) but not visible.  You
can then make it visible by selecting the right click menu 'Show Hidden Items'
option. Child items such as IPADDRESS, MACADDRESS will be set hidden if the
parent item is hidden.

Data Logging
------------
You can log individual items by adding 'csv:filename'. For example,
"csv:c:\cpu.log".  This outputs the data to a csv formatted file where the cells
are enclosed in quotes and separated by commas and the first row contains column
identifiers. The limitations are: the data is the display formatted data. You
should be able to make use of functions and macros to massage the data.

Fixed Text
----------
The TEXT item allows you to put any fixed text on the display. Useful for
any kind of internal identification or other static information such as a
machine's context, use, operator, tech support info. The value in the 'text'
property goes on the left column, the value in the 'key' field goes in the right
column.

Cpu Temperature
---------------
There are two ways of getting the cpu temperature:

      WMI
      ---
      The CPUTEMP item reads the root\wmi MSAcpi_ThermalZoneTemperature class.
      The success of this approach will vary. On Windows 7 and presumably Vista
      and Windows 8, it only works if Desktop Info is run as Administrator. The
      motherboard must have a sensor and the motherboard drivers have to install
      the correct wmi counter. So far out of all the computers i've tested only
      my Asus laptop shows the cpu temperature!

      CORE TEMP
      ---------
      The CORETEMP item reads the shared memory area of Core Temp, a nice little
      third party tool dedicated to reading cpu information. This tool is light
      on resources and can be run and hidden during Windows logon. Desktop Info
      does not need to be run as administrator using this approach. Best of all
      it's much more likely to succeed on any computer than the WMI method.
      Get Core Temp at: http://www.alcpu.com/CoreTemp/
      Hint: Pay attention during the installation process!

OEM Information
---------------
Your mileage will vary for oem information. Desktop Info reads the
OEMInformation registry key. Because of the 64 bit registry redirection done
for 32 bit applications, the information needs to exist in the Wow6432Node
branch. On my Asus, for example, the oem information exists only in the 64
branch, not the 32 bit branch. I can't bypass this redirection so it will either
work or not. OEMINFO displays Manufacturer and Model from OEMInfo.ini if
available or the OEMInformation key in the registry if available. OEMPRODUCT
displays Name and IdentifyingNumber from the WMI Win32_ComputerSystemProduct
key if available.

IP Addresses
------------
There are two ways to display an IP address. The first method is IPADDRESS which
is dependant on NETWORKADAPTER. That is, you switch on NETWORKADAPTER and
IPADDRESS then for each adapter you will have one or more IP addresses listed
in the IPADDRESS item. The second method is ALLIPADDRESS which does not depend
on NETWORKADAPTER. This allows you to switch off the adapter item and just
show the IP address. You will see all IP addresses for all active adapters.

Custom WMI Queries
------------------
You can add items to show the results of a simple WMI select query using the WMI
item.

     text         display text on the left column
     namespace    WMI namespace such as 'root\wmi' or 'root\cimv2'
     query        the wmi class and optional where clause but NOT the select
                  clause (this is implied)
     property     the property to return
     format       bps displays the number as a speed value (eg 150 Mb/s)

Example:
WMI=active:1,interval:0,color:ccffcc,text:Serial Number,namespace:root\cimv2,query:Win32_Bios,property:SerialNumber

In the where clause, strings should be quoted:
  query:Win32_PerfFormattedData_Tcpip_NetworkInterface where Name="Atheros"

The where clause can also include the 'like' keyword with wildcards:
  query:Win32_PerfFormattedData_Tcpip_NetworkInterface where Name like "%Wireless%"

WMI Explorer is a nice easy tool to explore the WMI system.
http://www.ks-soft.net/

Miscellaneous
-------------
The application is set to idle priority class which means it will only take up
cpu time when no other process requires it. If some other application is going
flat out 100% cpu, the Desktop Info display may not update immediately or
correctly.

Fixed disks smaller than 3GB in size will be displayed as MB. This is primarily
to avoid the rounding error of going to gigabytes.

The main CPU item shows percentage of all cpus in the system. This means it
will always show 0-100% regardless of how many cpus there are. The top process
cpu item shows the percentage per cpu. This means it may show more than 100%.

Top Process CPU can only show processes it has permissions to read. Running as
administrator or a member of the administrators group is enough for most
processes but not for some system processes. Desktop Info will attempt to
enable privileges to read system process information.

Top Process PF shows total page faults. "Page Faults/sec" shows both total and
hard page faults. The hard page faults is usually the one you're interested in.
This shows page file activity which is what kills system performance.

Some items may be denied access when retrieving data depending on the access
rights of the user running it on Vista, 7, 8 and possibly 2008.

Windows Platforms
-----------------
Desktop Info has been tested on the following Windows platforms:

XP Pro (32 bit) version 2002 service pack 2
XP Pro (32 bit) version 2002 service pack 3
XP Pro (64 bit) version 2003 service pack 2
Vista (32 bit) business service pack 1
Server 2000 (32 bit) service pack 4
Server 2003 (64 bit) service pack 2

Server 2008 (32 bit) standard service pack 1
Server 2008 (64 bit) standard service pack 1
Windows 7 Professional 64 bit
Windows 8

Disk I/O may not work on Windows 2000.

--------------------------------------------------------------

Release Notes

Version v0.10
-------------
Limited distribution test release.

Version v0.11
-------------
Change 1: Fixed the nasty flicker some systems were getting.

Change 2: Fixed the odd characters after the domain name.

Change 3: Less full refreshes means less cpu time.

Change 4: Fixed disk figures for very small drives.

Version v0.20
-------------
Change 1: Added time zone info.

Change 2: Some optimisations.

Change 3: Added refresh intervals.

Change 4: Added domain controller info.

Change 5: Added event logs.

Version v0.21
-------------
Change 1: Added terminal server session count.

Change 2: Fixed some stuff in event logs.

Version v0.22
-------------
Change 1: Added DirectX version.

Change 2: Adjusted domain controller. I can't directly test this one.

Change 3: Added network packet stats and rates.

Change 4: Added network connection count.

Change 5: Added double click refresh.

Version v0.30
-------------
Change 1: Fixed a bunch of memory issues.

Change 2: Fixed Terminal Services sessions and added session list.

Change 3: Added auto font size.

Change 4: Added 'missing ini' default values.

Change 5: Implemented proper ini file monitoring.

Change 6: Added screen info.

Change 7: Fixed display for 256 color remote desktop.

Change 8: Added files monitor options.

Version v0.31
-------------
Change 1: Added Up Time.

Change 2: Added support for cpu times for Windows 2000.

Change 3: Fixed Domain Controller

Change 4: Fixed multiples of same file watch type bug.

Change 5: Fixed memory sizes over 2GB.

Version v0.40
-------------
Change 1: Added top process cpu time.

Change 2: Added top process memory usage.

Change 3: Added file version watch type.

Change 4: Added registry watch types.

Change 5: Added cpu count.

Change 6: Additional checking for terminal sessions.

Version v0.41
-------------
Change 1: Added unread mail.

Change 2: Added formright and formbottom options.

Version v0.42
-------------
Change 1: Increased fixed disks to eight.

Change 2: Added multiple ip addresses.

Change 3: Fixed problem with missing ini file.

Change 4: Split network gateway entries to separate lines.

Change 5: Added percentages to memory and disk displays.

Version v0.50
-------------
Change 1: I think I've nailed the show desktop thing.

Version v0.51
-------------
Change 1: Fixed problem with some USB drives.

Change 2: Solved refresh problem when removable drives come
and go.

Version v0.60
-------------
Change 1: Reworked the file monitoring so it works on local and remote
files and folders. You'll need to adjust your ini file entries as noted above.

Change 2: Reworked the registry monitoring to merge the two types. You'll need
to adjust your ini file entries as noted above.

Change 3: Added the /f command line option to show a visible, moveable form.

Change 4: Added the /a command line option to show all items regardless of the
ini file settings.

Change 5: Added right click context menu.

Change 6: The network entries are grouped so all information for each
adapter is together.

Change 7: Tested on a variety of Windows platforms. The results are noted above.

Version v0.70
-------------
Change 1: Added Disk IO on fixed logical drives.

Change 2: Added disk queue length.

Change 3: Changed ini file format as noted above. Will continue to read the old
format while the Intervals section exists.

Change 4: Added item colors.

Change 5: Fixed bug in Top Process Cpu

Change 6: Fixed bug in cpu times.

Change 7: Added charts.

Change 8: Added thresholds.

Change 9: Added exception handlers to process enumerators.

Version v0.71
-------------
Change 1: Fixed an access violation in the disk io routines.

Change 2: Fixed refresh when disks come and go.

Version v0.80
-------------
Change 1: Added colors to the file and registry monitors.

Change 2: Added option to disable the context menu.

Change 3: Added option to toggle the indents on disks/networks.

Change 4: Modified registry monitor to optionally show values.

Change 5: Registry item names are shown as found in the ini file.

Change 6: Added event log monitor and removed redundant event logs from
the items.

Version v0.81
-------------
Change 1: Improved disk change handling.

Change 2: Fixed file monitor where file/directory does not exist or
disappears or reappears.

Change 3: Fixed divide by zero error in fixed disks.

Version v0.90
-------------
Change 1: Refactored the chart display code to be more useful and added charts
to more items. See the charts section for more information.

Change 2: Refactored form display code. Options have changed accordingly. Auto
form width, auto font size and font color are gone. You should make sure every
item has the color set. See the options section for more information.

Change 3: Rewritten all the network adapter stuff. Added change handling.
Added filtering. See the options section for more information.

Change 4: Prevent form from resetting it's position after it is dragged.

Change 5: Rewrote the bar chart.

Change 6: Fixed problem with event log monitor thread not terminating correctly.

Change 7: Added the following items: printer, printerstatus, displaycontroller,
bios, motherboard, workgroup. Printers is work in progress.

Change 8: Added language file.

Change 9: Added screen saver option.

Change 10: Added msnstatus option.

Change 11: Changed method for retrieving service pack.

Version v1.00
-------------
Change 1: Added page faults and top process page faults.

Change 2: The order of displayed items now follows the order in the ini file.

Change 3: New ini file item format. This will make it easier to read and
easier for me to add new options. See the item section above for more details.

Change 4: Fixed the process name for all known varieties of Windows. If a
process name can't be retrieved for any reason, it will display <n/a> and maybe
an error message.

Change 5: Added cpu temperature from wmi. See the miscellaneous section above.

Change 6: Fixed line chart width in screen saver mode.

Change 7: Subdued some redundant refreshes.

Change 8: Some memory usage optimisations.

Change 9: Fixed display controller and bios on Windows 7 and hopefully haven't
broken it elsewhere.

Change 10: Added oeminfo. See miscellaneous section above.

Change 11: Added item font style.

Version v1.01
-------------
Change 1: Fixed fatal crash on startup as the result of an access denied problem 
when retrieving OS info in limited access Windows account.

Version v1.10
-------------
Change 1: Added battery status.

Change 2: Added option to disable ClearType.

Change 3: Added header item. See ini file.

Change 4: Added cpu kernel time.

Change 5: Added inimonitortime to options. This is the number of seconds to
check the desktopinfo.ini file for changes.

Change 6: Added osbuild.

Change 7: Fixed a problem with reading process information for some system
processes.

Change 8: Removed DISKQUEUE item. It's included on the DISKIO item.

Change 9: Added filter option to FIXEDDISK item.

Version v1.11
-------------
Change 1: Changed the way the Windows architecture is determined.

Change 2: Fixed the transparency on 16 bit color display.

Version v1.12
-------------
Change 1: Forgot to switch off debug causing large log file, oops.

Version v1.20
-------------
Change 1: Fixed index out of bounds when battery chart type is 2.

Change 2: Added network PROXY item.

Change 3: Changed HEADER item into more general COMMENT item.

Change 4: COMMENT is ignored when calculating width of column 1 allowing it to
display over both columns.

Change 5: Added underline to style option.

Change 6: Changed FORMCOLOR in the main options section to a bgr type value to
keep it consistent with other colors. 000000 means transparent in normal mode
or black background in form mode.

Change 7: Converted file monitor into a regular item. Now you can put one or
more file monitor items anywhere in the items list.

Change 8: Converted registry monitor into a regular item. Now you can put one or
more registry monitor items anywhere in the items list.

Change 9: Fixed problem with monitoring registry on 64 bit Windows.

Change 10: Converted event log monitor into a regular item. Now you can put one
or more event log monitor items anywhere in the items list.

Change 11: Added AUDIOCONTROLLER item.

Change 12: Added logging option.

Change 13: Added /ini command line option.

Change 14: Added SERIALNUMBER item.

Change 15: Added multiple IP addresses per nic.

Change 16: Added option to allow/disallow dragging the form.

Change 17: Added offset property to network items.

Change 18: Added multi core cpu item, CPUUSAGE.

Change 19: Added activeonly property to NETWORKADAPTER.

Change 20: Added count property to control maximum number of multi items
displayed (CPUUSAGE, NETWORKADAPTER, FIXEDDISK, PRINTER.

Version v1.30
-------------

Change 1: Fixed critical error where USB drive is ejected but not
removed.

Change 2: Added SHORTDISPLAY option to PHYSICALRAM, VIRTUALMEMORY,
PAGEFILE and FIXEDDISK items.

Change 3: Added ENVVAR item.

Change 4: Added DEFAULTPRINTER item.

Change 5: Added SHORTDISPLAY option to the registry monitor.

Change 6: Added LOGONSESSION item.

Change 7: Moved network adapter filter to the item configuration and is
much more flexible with include and exclude options.

Version v1.40
-------------

Change 1: Added ALLIPADDRESS. This is a stand alone item to display all
active IP addresses without depending on the NETWORKADAPTER item.

Change 2: Fixed critical error where a network adapter has more than one
ip address sometimes causes an access violation.

Change 3: Improved response for changing network adapters, fixed disks and
screen resolution.

Change 4: Added hidden items. Set active to 2. Right click and select Show
Hidden Items. See above.

Change 5: Added OEMPRODUCT item. See above.

Change 6: Added SUBNETMASK item.

Change 7: Added csv option to items. See above.

Change 8: Fixed disk filter got lost during last version.

Change 9: Apparently fixed an obscure wmi bug.

Change 10: Fixed CPUTEMP charts.

Change 11: Added SERVICESTATE.

Change 12: Added TEXT.

Change 13: Added hook to Core Temp temperature reader.

Change 14: Added custom WMI item. See above for details. There's some examples
in the ini file. These may duplicate existing items. I'll leave the old items in
for this release and if all goes well they'll be removed next release.

Change 15: Adjusted screen saver display.

Version v1.50
-------------
Change 1: Fixed wmi line chart resetting when switching between hidden and not
hidden.

Change 2: Added volume to FIXEDDISK.

Change 3: There was a test item called PROCESSMEM which was hard wired to
DesktopInfo.exe. I've modified this so you can point it to any process name. The
result will be the sum of all processes by that name. 'ws' is Working Set, 'pf'
is Page File Usage or Commit Size.

Change 4: Added 'column1width' to global options.

Change 5: Added format option to WMI item.

Change 6: Added centerv and centerh to options.

Change 7: Removed following items as they can now be done with WMI: SERVICESTATE,
AUDIOCONTROLLER, SERIALNUMBER, WORKGROUP. The ini file has been updated as
appropriate.

Change 8: I don't know what I was thinking with LOGONSESSION.

Change 9: Added a global exception handler so maybe we won't see any more of
those cascading error messages.

Change 10: Added option to adjust background transparency.

Change 11: Cleaned up the display code and fixed a few little annoyances.

Change 12: Fixed IEVERSION to include svcversion updates.

Version v1.51
-------------

Change 1: Fixed charts updating every second even though data is not updated.

Change 2: Fixed regression bug with COMMENT item.

Change 3: Fixed refresh ugliness and missing top lines.

--------------------------------------------------------------

Comments/suggestions to: support@delahoy.com
http://www.glenn.delahoy.com
