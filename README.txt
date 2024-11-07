#comando container docker per database e keycloak
docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -e KC_DB=postgres -e KC_DB_URL_HOST=magora.it -e KC_DB_URL_PORT=5432 -e KC_DB_URL_DATABASE=ea -e KC_DB_USERNAME=giovanni -e KC_DB_PASSWORD=JSGgdhf5DR84AH38enu37 quay.io/keycloak/keycloak:24.0.4 start-dev

#credenziali DB postgres (porta: 5432)
host: magora.it
username: giovanni
password: JSGgdhf5DR84AH38enu37
url: jdbc:postgresql://magora.it:5432/postgres



abbiamo installato daisyUI per componenti utilizzati nel progetto (sitoweb)

abbiamo aggiunto uno script python che serve per settare automaticamente gli IP invece del localhost che su android creava problemi, se non funziona bisogna cambiarli a mano


credenziali di utenti già creati:

username: aaa
password: aaa
ruolo: user

username: noemiprova
password: nnn
ruolo: user

username: bbb
password: bbb
ruolo: admin
