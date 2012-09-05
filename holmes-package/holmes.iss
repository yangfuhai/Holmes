; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "Holmes UPnP"
#define MyAppVersion "0.2.1"
#define MyAppPublisher "Cedric Cheneau"
#define MyAppURL "https://github.com/ccheneau/Holmes"
#define MyAppExeName "startup.bat"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{338670D7-052E-4682-829C-33329C828C34}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={pf}\{#MyAppName}
DefaultGroupName={#MyAppName}
LicenseFile=target\holmes-{#MyAppVersion}\license.txt
OutputBaseFilename=holmes-{#MyAppVersion}-setup
OutputDir=target
Compression=lzma
SolidCompression=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"
Name: "french"; MessagesFile: "compiler:Languages\French.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "target\holmes-{#MyAppVersion}\bin\startup.bat"; DestDir: "{app}\bin"; Flags: ignoreversion
Source: "target\holmes-{#MyAppVersion}\holmes.ico"; DestDir: "{app}"; Flags: ignoreversion
Source: "target\holmes-{#MyAppVersion}\license.txt"; DestDir: "{app}"; Flags: ignoreversion
Source: "target\holmes-{#MyAppVersion}\Readme.txt"; DestDir: "{app}"; Flags: ignoreversion
Source: "target\holmes-{#MyAppVersion}\site\*"; DestDir: "{app}\site"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "target\holmes-{#MyAppVersion}\lib\*"; DestDir: "{app}\lib"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "target\holmes-{#MyAppVersion}\conf\*"; DestDir: "{app}\conf"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Dirs]
Name: "{app}\log"

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\bin\{#MyAppExeName}"; IconFilename: "{app}\holmes.ico"
Name: "{commondesktop}\{#MyAppName}"; Filename: "{app}\bin\{#MyAppExeName}"; Tasks: desktopicon; IconFilename: "{app}\holmes.ico"

[Run]
Filename: "{app}\bin\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: shellexec postinstall skipifsilent

