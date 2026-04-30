LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

# Installa il server OpenSSH
IMAGE_INSTALL:append = " openssh"

# Modifica la configurazione di SSH all'avvio
# Rimuove il login root e forza l'uso delle chiavi se desiderato
SERVICE_CONF_ssh = "PermitRootLogin no"

# In alternativa, usa una funzione di post-elaborazione per modificare sshd_config
ROOTFS_POSTPROCESS_COMMAND += "disable_root_user; "

disable_root_user() {
    # Disabilita login root in SSH
    sed -i 's/#PermitRootLogin.*/PermitRootLogin no/' ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config
    # Opzionale: blocca l'account root a livello di sistema
    passwd -l root ${IMAGE_ROOTFS}
}