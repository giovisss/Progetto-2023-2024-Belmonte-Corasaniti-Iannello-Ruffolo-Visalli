import socket
import re

# Recupera l'indirizzo IP locale reale
def get_local_ip():
    # Crea una connessione a un DNS pubblico per determinare l'IP locale
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        # Connessione a un server DNS pubblico (Google DNS)
        s.connect(('8.8.8.8', 80))
        local_ip = s.getsockname()[0]
    except Exception as e:
        print(f"Errore nel recupero dell'IP: {e}")
        local_ip = '127.0.0.1'
    finally:
        s.close()
    return local_ip

# Funzione per aggiornare l'IP in un file specifico
def update_ip_in_file(file_path, ip_pattern, new_ip):
    try:
        with open(file_path, 'r+') as file:
            content = file.read()
            file.seek(0)
            updated_content = re.sub(ip_pattern, new_ip, content)
            file.write(updated_content)
            file.truncate()
        print(f"IP aggiornato nel file: {file_path}")
    except Exception as e:
        print(f"Errore nell'aggiornamento del file {file_path}: {e}")

# Esegui il recupero dell'IP e l'aggiornamento dei file
def main():
    ip_locale = get_local_ip()
    print(f"IP Locale trovato: {ip_locale}")

    # Aggiornamento del file .ENV
    update_ip_in_file('JokiBackend/.ENV', r'LOCAL_MACHINE_IP\s*=\s*\d+\.\d+\.\d+\.\d+', f'LOCAL_MACHINE_IP = {ip_locale}')

    # Aggiornamento del file global.ts
    update_ip_in_file(
        'JokiFrontend/src/app/global.ts',
        r'export const BASE_KEYCLOAK_URL\s*=\s*\'http://\d+\.\d+\.\d+\.\d+:8080\'',
        f'export const BASE_KEYCLOAK_URL = \'http://{ip_locale}:8080\''
    )

    # Aggiornamento del file ips.xml
    update_ip_in_file(
        'JokiAndroid/app/src/main/res/values/ips.xml',
        r'(\d+\.\d+\.\d+\.\d+):808\d',
        f'{ip_locale}:8080'
    )

if __name__ == "__main__":
    main()
