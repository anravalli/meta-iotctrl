SUMMARY = "Aggiunge alias globali per la shell Bash"
SECTION = "base"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://extra-aliases.sh"

S = "${UNPACKDIR}"

do_install() {
    install -d ${D}${sysconfdir}/profile.d
    install -m 0755 extra-aliases.sh ${D}${sysconfdir}/profile.d/extra-aliases.sh
}

FILES:${PN} += "${sysconfdir}/profile.d/extra-aliases.sh"

RDEPENDS:${PN} = "bash"