DESCRIPTION = "Video player and streamer - davinci edition"
HOMEPAGE = "http://www.videolan.org"
SECTION = "multimedia"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "\
    git://git@github.com/RPi-Distro/vlc;protocol=https;branch=bookworm-rpt \
    file://0001-configure-fix-linking-on-RISC-V-ISA.patch \
    file://0002-Do-not-generate-cache-during-build.patch \
    file://0003-rpi.patch \
    file://0004-chain.patch \
    file://0005-vlc.patch \
    file://0006-caca.patch \
    file://0007-qt-wayland.patch \
    file://0008-qt-fullscreen.patch \
    file://0009-http-port-fix.patch \
    file://2001-fix-luaL-checkint.patch \
    file://2002-use-vorbisidec.patch \
    file://3001-configure.ac-setup-for-OE-usage.patch \
    file://3002-codec-omxil_core-replace-opt-vc-path-with-usr-lib.patch \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '', 'file://3003-remove-xorg-related-link-libs.patch', d)} \
    file://3004-exclude-opengl-vout-plugin-remove-egl.patch \
    file://3005-fix-rest-of-minor-build-errors.patch \
    "

SRCREV = "636141a3506e8de95683e3b0eb571bf9a9c19b96"

S = "${WORKDIR}/git"

PROVIDES = "vlc"
RPROVIDES:${PN} = "${PROVIDES}"
DEPENDS = "coreutils-native fribidi libtool libgcrypt libgcrypt-native \
           dbus libxml2 gnutls tremor faad2 ffmpeg flac alsa-lib libidn \
           jpeg xz libmodplug libmtp libopus orc libsamplerate0 \
           avahi libusb1 schroedinger taglib tiff bison flex"

inherit autotools gettext pkgconfig mime-xdg

export BUILDCC = "${BUILD_CC} -std=c11"
EXTRA_OECONF = "\
    --enable-run-as-root \
    --enable-xvideo \
    --disable-lua \
    --disable-screen \
    --disable-caca \
    --enable-vlm \
    --enable-tremor \
    --disable-aa \
    --disable-faad \
    --enable-dbus \
    --without-contrib \
    --without-kde-solid \
    --enable-realrtsp \
    --disable-libtar \
    --enable-avcodec \
    --disable-css \
    --disable-taglib \
    "

PACKAGECONFIG ?= "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'vc4graphics', '', 'mmal', d)} \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'x11', 'notify', '', d)} \
    live555 dv1394 fontconfig fluidsynth freetype png udev \
    x264 alsa harfbuzz jack neon fribidi dvbpsi v4l2 \
    "

# Have to disable mmal-avcodec as libavcodec/rpi_zc source
# seems to not have been pushed in recent patches.
EXTRA_OECONF += "--disable-mmal-avcodec"

PACKAGECONFIG[mmal] = "--enable-omxil --enable-omxil-vout --enable-rpi-omxil --enable-mmal,,userland"
PACKAGECONFIG[x264] = "--enable-x264,--disable-x264,x264"
PACKAGECONFIG[mad] = "--enable-mad,--disable-mad,libmad"
PACKAGECONFIG[a52] = "--enable-a52,--disable-a52,liba52"
PACKAGECONFIG[jack] = "--enable-jack,--disable-jack,jack"
PACKAGECONFIG[live555] = "--enable-live555 LIVE555_PREFIX=${STAGING_DIR_HOST}${prefix},--disable-live555,live555"
PACKAGECONFIG[libass] = "--enable-libass,--disable-libass,libass"
PACKAGECONFIG[postproc] = "--enable-postproc,--disable-postproc,libpostproc"
PACKAGECONFIG[libva] = "--enable-libva,--disable-libva,libva"
#PACKAGECONFIG[opencv] = "--enable-opencv,--disable-opencv,opencv"
PACKAGECONFIG[speex] = "--enable-speex,--disable-speex,speex"
PACKAGECONFIG[gstreamer] = "--enable-gst-decode,--disable-gst-decode,gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad"
PACKAGECONFIG[vpx] = "--enable-vpx,--disable-vpx, libvpx"
#PACKAGECONFIG[freerdp] = "--enable-freerdp,--disable-freerdp, freerdp"
PACKAGECONFIG[dvbpsi] = "--enable-dvbpsi,--disable-dvbpsi, libdvbpsi"
#PACKAGECONFIG[samba] = "--enable-smbclient,--disable-smbclient, samba"
PACKAGECONFIG[upnp] = "--enable-upnp,--disable-upnp,libupnp"
PACKAGECONFIG[dvdnav] = "--enable-dvdnav,--disable-dvdnav,libdvdnav libdvdcss"
PACKAGECONFIG[sftp] = "--enable-sftp,--disable-sftp,libssh2"
PACKAGECONFIG[vorbis] = "--enable-vorbis,--disable-vorbis,libvorbis libogg"
PACKAGECONFIG[ogg] = "--enable-ogg,--disable-ogg,libvorbis libogg"
PACKAGECONFIG[dc1394] = "--enable-dc1394,--disable-dc1394,libdc1394"
PACKAGECONFIG[dv1394] = "--enable-dv1394,--disable-dv1394,libraw1394 libavc1394"
PACKAGECONFIG[svg] = "--enable-svg,--disable-svg,librsvg"
PACKAGECONFIG[svgdec] = "--enable-svgdec,--disable-svgdec,librsvg cairo"
PACKAGECONFIG[notify] = "--enable-notify,--disable-notify, libnotify gtk+3"
PACKAGECONFIG[fontconfig] = "--enable-fontconfig,--disable-fontconfig, fontconfig"
PACKAGECONFIG[freetype] = "--enable-freetype,--disable-freetype, freetype"
#PACKAGECONFIG[dvdread] = "--enable-dvdread,--disable-dvdread, libdvdread libdvdcss"
PACKAGECONFIG[vnc] = "--enable-vnc,--disable-vnc, libvncserver"
PACKAGECONFIG[x11] = "--with-x --enable-xcb,--without-x --disable-xcb, xcb-util-keysyms libxpm libxinerama"
PACKAGECONFIG[png] = "--enable-png,--disable-png,libpng"
#PACKAGECONFIG[vdpau] = "--enable-vdpau,--disable-vdpau,libvdpau"
#PACKAGECONFIG[wayland] = "--enable-wayland,--disable-wayland,wayland wayland-native"
PACKAGECONFIG[gles2] = "--enable-gles2,--disable-gles2,virtual/libgles2"
#PACKAGECONFIG[dca] = "--enable-dca,--disable-dca,libdca"
PACKAGECONFIG[fribidi] = "--enable-fribidi,,fribidi"
PACKAGECONFIG[gnutls] = "--enable-gnutls,,gnutls"
PACKAGECONFIG[fluidsynth] = "--enable-fluidsynth,,fluidsynth"
PACKAGECONFIG[harfbuzz] = "--enable-harfbuzz,--disable-harfbuzz,harfbuzz"
PACKAGECONFIG[udev] = "--enable-udev,--disable-udev,udev"
PACKAGECONFIG[neon] = "--enable-neon,--disable-neon,"
PACKAGECONFIG[opus] = "--enable-opus,--disable-opus,libopus libogg"
PACKAGECONFIG[ncurses] = "--enable-ncurses,--disable-ncurses,ncurses"
PACKAGECONFIG[alsa] = "--enable-alsa,--disable-alsa,alsa-lib"
PACKAGECONFIG[pulseaudio] = "--enable-pulse,--disable-pulse,pulseaudio"
PACKAGECONFIG[sdl-image] = "--enable-sdl-image,,libsdl-image"
PACKAGECONFIG[v4l2] = "--enable-v4l2,,v4l-utils"

TARGET_CFLAGS:append = " -I${STAGING_INCDIR}/drm"
TARGET_CFLAGS:append = " -I${RECIPE_SYSROOT}/usr/include"
TARGET_LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', '-lGLESv2', '', d)}"

# Ensures the --enable-mmal-avcodec flag is available for usage
do_configure:prepend() {
    olddir=`pwd`
    cd ${S}
    ./bootstrap
    cd $olddir
}

# This recipe packages vlc as a library as well, so qt4 dependencies
# can be avoided when only the library is installed.
PACKAGES =+ "libvlc"

LEAD_SONAME_libvlc = "libvlc.so.5"
FILES:libvlc = "${libdir}/lib*.so.*"

FILES:${PN} += "\
    ${bindir}/vlc \
    ${libdir}/vlc \
    ${datadir}/applications \
    ${datadir}/vlc \
    ${datadir}/icons \
    ${datadir}/metainfo/vlc.appdata.xml \
    "

FILES:${PN}-dbg += "\
    ${libdir}/vlc/*/.debug \
    ${libdir}/vlc/plugins/*/.debug \
    "

FILES:${PN}-staticdev += "\
    ${libdir}/vlc/plugins/*/*.a \
    ${libdir}/vlc/libcompat.a \
    "

# Only enable it for rpi class of machines
COMPATIBLE_HOST = "null"
COMPATIBLE_HOST:rpi = "(.*)"

INSANE_SKIP:${PN} = "dev-so"
