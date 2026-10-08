LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

# Abilita le funzionalità per la gestione degli utenti
INHERIT += "extrausers"

# Crea l'utente 'operator', imposta la shell e una password (es: 'password123')
# Nota: La password deve essere criptata. Puoi generarla con: openssl passwd -6 password123
EXTRA_USERS_PARAMS = " \
    useradd -p '\$6\$rounds=4096\$salt\$hashedpassword...' iotops; \
    usermod -a -G video,audio,iotops; \
"