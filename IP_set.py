import socket
import re

def get_local_ip():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        s.connect(('8.8.8.8', 80))
        local_ip = s.getsockname()[0]
    except Exception as e:
        print(f"Errore nel recupero dell'IP: {e}")
        local_ip = '127.0.0.1'
    finally:
        s.close()
    return local_ip

def update_ip_in_file(file_path, ip_pattern, new_ip):
    try:
        with open(file_path, 'r+') as file:
            content = file.read()
            file.seek(0)
            # Sostituisce solo l'IP, lasciando intatta la porta
            updated_content = re.sub(ip_pattern, fr'{new_ip}:\2' if r'(\d+)' in ip_pattern else new_ip, content)
            file.write(updated_content)
            file.truncate()
        print(f"IP aggiornato nel file: {file_path}")
    except Exception as e:
        print(f"Errore nell'aggiornamento del file {file_path}: {e}")

def main():
    ip_locale = get_local_ip()
    print(f"IP Locale trovato: {ip_locale}")
    # Aggiornamento del file .ENV
    update_ip_in_file(
        'JokiBackend/.ENV',
        r'LOCAL_MACHINE_IP\s*=\s*(\d+\.\d+\.\d+\.\d+)',
        f'LOCAL_MACHINE_IP = {ip_locale}'
    )
    # Aggiornamento del file global.ts
    update_ip_in_file(
        'JokiFrontend/src/app/global.ts',
        r'http://(\d+\.\d+\.\d+\.\d+):(\d+)',
        f'http://{ip_locale}'
    )
    # Aggiornamento del file ips.xml
    update_ip_in_file(
        'JokiAndroid/app/src/main/res/values/ips.xml',
        r'(\d+\.\d+\.\d+\.\d+):(\d+)',
        ip_locale
    )

if __name__ == "__main__":
    main()