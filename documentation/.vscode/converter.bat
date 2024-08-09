@echo off
setlocal enabledelayedexpansion

set "input_folder=%2\docs\docs\assets"
set "output_folder=%2\docs\docs\assets\%1"

rem Create the output folder if it doesn't exist
if not exist "%output_folder%" mkdir "%output_folder%"

rem Loop through all .webm files in the input folder
for %%f in ("%input_folder%\*.webm") do (
    set "input_file=%%f"
    set "output_file=%output_folder%\%%~nf.webm"

    rem Check the size of the original file
    set "original_size="
    for %%I in ("!input_file!") do set "original_size=%%~zI"

    rem Only compress if the original file is larger than 2MB
    if !original_size! gtr 2000000 (
        echo Compressing !input_file!
        
        rem Compress the video
        ffmpeg -i "!input_file!" -c:v libvpx-vp9 -b:v 500k -c:a libvorbis "!output_file!"

        rem Check the size of the compressed file
        set "filesize="
        for %%I in ("!output_file!") do set "filesize=%%~zI"
        
        rem If the file is still larger than 2MB, compress again with a lower bitrate
        if !filesize! gtr 2000000 (
            ffmpeg -i "!output_file!" -c:v libvpx-vp9 -b:v 250k -c:a libvorbis "!output_file!"
        )

        rem Final size check
        for %%I in ("!output_file!") do set "filesize=%%~zI"
        if !filesize! gtr 2000000 (
            echo WARNING: !output_file! is still larger than 2MB after compression
        ) else (
            rem Delete the original file after successful compression
            del "!input_file!"
        )
    ) else (
        echo Skipping !input_file! - already less than 2MB
    )
)

echo Compression complete.
exit 0
