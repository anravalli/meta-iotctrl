SUMMARY = "Prepare SDCARD image with full lenght /userdata partition"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"


make_usrdata_dir() {
    mkdir -p ${IMAGE_ROOTFS}/usrdata
}

add_data_to_fstab() {
    echo "/dev/mmcblk0p3  /usrdata  ext4  defaults,noatime  0  2" >> ${IMAGE_ROOTFS}${sysconfdir}/fstab
}

ROOTFS_POSTPROCESS_COMMAND += " add_data_to_fstab; make_usrdata_dir; "

SRC_URI = " \
    file://fb-data-resize.sh \
    file://fb-data-resize.service \
"

S = "${UNPACKDIR}"

# Dipendenze fondamentali: parted per la tabella e resize2fs per il filesystem
RDEPENDS:${PN} = "parted e2fsprogs-resize2fs bash"

inherit systemd

SYSTEMD_SERVICE:${PN} = "fb-data-resize.service"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 fb-data-resize.sh ${D}${bindir}/fb-data-resize.sh

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 fb-data-resize.service ${D}${systemd_system_unitdir}/fb-data-resize.service
}