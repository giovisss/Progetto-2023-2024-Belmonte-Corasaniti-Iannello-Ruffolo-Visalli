package unical.enterprise.jokibackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableCaching
public class JokiBackendApplication {
    public static void main(String[] args) {
        callPythonScript();
        SpringApplication.run(JokiBackendApplication.class, args);
    }

    private static void callPythonScript() {
        try {
            // Percorso relativo dello script Python
            String pythonScriptPath = "IP_set.py";

            // Ottieni la directory di lavoro corrente
            String currentDir = System.getProperty("user.dir");
            System.out.println("Directory di lavoro corrente: " + currentDir);

            // Naviga alla directory superiore
            File parentDir = new File(currentDir).getParentFile();
            if (parentDir != null) {
                System.out.println("Directory superiore: " + parentDir.getAbsolutePath());

                // Costruisci il percorso assoluto dello script Python
                File scriptFile = new File(parentDir, pythonScriptPath).getCanonicalFile();
                // System.out.println("Percorso assoluto dello script Python: " + scriptFile.getAbsolutePath());

                // Verifica se lo script esiste
                if (!scriptFile.exists()) {
                    System.out.println("Errore: Impossibile trovare lo script Python nel percorso specificato.");
                    return;
                }

                // Costruisci il comando per eseguire lo script Python
                ProcessBuilder processBuilder = new ProcessBuilder("python", scriptFile.getAbsolutePath());

                // Imposta la directory di lavoro del processo alla directory superiore
                processBuilder.directory(parentDir);

                Process process = processBuilder.start();

                // Leggi l'output dello script Python
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                // Attendi che il processo termini
                int exitCode = process.waitFor();
                System.out.println("Script Python terminato con codice: " + exitCode);

            } else {
                System.out.println("Errore: Impossibile ottenere la directory superiore.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
