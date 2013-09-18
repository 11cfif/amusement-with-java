Batchfiles:

run_java.bat  : Run the example on Windows
run_java.sh   : Run the example on Linux


Commandline parameters:

width=xxx  : The width of the framebuffer
height=xxx : The height of the framebuffer
fullscreen : Force the engine to use fullscreen modes
16bit      : Try to get a videomode with 16bit color (instead of 32)
zbuffer=xx : Use xx for the depth of the ZBuffer (OpenGL only)
trilinear  : Use trilinear filtering (OpenGL only)
mipmap     : Use mipmap (overwritten by trilinear/OpenGL only)
refresh=xx : Try to use a video with a refresh-rate of xx (OpenGL only, ignored in windowed mode)


Controls:

cursor-keys : Moving around
SPACE       : Fire bullet
ESC         : Exit
x           : Switch between Software/LWJGL mode (may have some problems in fullscreen mode and may crash on some machines when switching from OpenGL to software!)
w	    : enable/disable wireframe mode

