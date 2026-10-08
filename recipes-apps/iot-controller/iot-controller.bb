inherit systemd useradd

SUMMARY = "A simple application to demonstrate IoT remote device control"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

python do_display_banner() {
    bb.plain("***********************************************");
    bb.plain("*                                             *");
    bb.plain("*           Building iot-device               *");
    bb.plain("*                                             *");
    bb.plain("***********************************************");
}

addtask display_banner before do_build

# Sorgenti: il file locale nella cartella 'files'
SRC_URI = "file://iot-device.py \
    file://iot-controller.service \
    file://90-gpio.rules \
    "

SYSTEMD_SERVICE:${PN} = "iot-controller.service"
SYSTEMD_AUTO_ENABLE = "enable"

FILES:${PN} += "${systemd_unitdir}/system/iot-controller.service"

RDEPENDS:${PN} = " \
    python3-core \
    python3-paho-mqtt \
    python3-gpiod \
"
S = "${UNPACKDIR}"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "--system gpio"


do_install() {
    # Crea la directory di destinazione sulla Raspberry (/usr/bin)
    install -d ${D}${bindir}
    # Installa lo script e rendilo eseguibile
    install -m 0755 ${S}/iot-device.py ${D}${bindir}/iot-device.py
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${S}/iot-controller.service ${D}/${systemd_unitdir}/system

    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 90-gpio.rules ${D}${sysconfdir}/udev/rules.d/
}
