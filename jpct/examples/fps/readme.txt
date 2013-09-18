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
PGUP/PGDWN  : Looking up/down
ESC         : Exit
x           : Switch between Software/LWJGL mode (may have some problems in fullscreen mode and may crash on some machines when switching from OpenGL to software!)
w	    : enable/disable wireframe mode



Final words:

It's possible to fall of the level or into a pit.

Fullscreen works, but switching between LWJGL and Java2D fullscreen seems to cause some troubles
on the Java2D side...anyway...

When using the server-VM, the test runs much faster but sometimes takes ages to load the textures...and sometimes not. Don't know what happens there behind the scenes...
