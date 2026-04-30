SUMMARY = "Installazione chiavi SSH per utente operator"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"


# SRC_URI = "file://authorized_keys"

# S = "${UNPACKDIR}"

# do_install() {
#     install -d ${D}/home/operator/.ssh
#     install -m 0600 ${WORKDIR}/authorized_keys ${D}/home/operator/.ssh/authorized_keys
#     # Cambia il proprietario (deve corrispondere all'UID dell'utente creato)
#     chown -R 1000:1000 ${D}/home/operator/.ssh
# }

# FILES:${PN} = "/home/operator/.ssh/authorized_keys"

# # Aggiungi questo alla tua ricetta esistente 'my-python-app' o creane una nuova
# do_install:append() {
#     install -d ${D}${sysconfdir}/my-app
#     echo "RPI-MODEL-B-SN-12345" > ${D}${sysconfdir}/my-app/device-id
# }
