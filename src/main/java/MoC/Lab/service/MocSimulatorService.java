package MoC.Lab.service;

import MoC.Lab.dto.MocRequest;
import MoC.Lab.dto.MocResponse;
import MoC.Lab.dto.MocResponse.StepResult;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MocSimulatorService {

    private static final String SELECT_APDU =
            "00 A4 04 0C 10 A0 00 00 00 77 03 0C 60 00 00 00 FE 00 00 05 00";

    private static final String GET_BIOMETRICS_APDU =
            "00 CB 3F FF 02 7F 61 00";

    public MocResponse execute(MocRequest request) {

        if (request == null || request.mode() == null) {
            throw new IllegalArgumentException(
                    "Debes seleccionar una modalidad."
            );
        }

        String mode = request.mode().toUpperCase();

        if (!List.of(
                "CONTACT_NO_PACE",
                "CONTACT_PACE",
                "NFC_PACE"
        ).contains(mode)) {
            throw new IllegalArgumentException(
                    "Modalidad no válida: " + mode
            );
        }

        List<StepResult> steps = new ArrayList<>();

        steps.add(new StepResult(
                "1. Inicialización",
                "INICIO",
                "OK",
                "SUCCESS",
                "Se inicia la simulación MoC."
        ));

        if (mode.equals("CONTACT_PACE")
                || mode.equals("NFC_PACE")) {

            steps.add(new StepResult(
                    "2. Autenticación PACE",
                    "SIMULACIÓN PACE",
                    "OK",
                    "SIMULATED",
                    "Se representa el paso PACE de forma conceptual. "
                            + "No se ejecuta criptografía PACE real."
            ));
        }

        steps.add(new StepResult(
                "3. Selección de aplicación",
                SELECT_APDU,
                "90 00",
                "SUCCESS",
                "Respuesta simulada: aplicación seleccionada."
        ));

        steps.add(new StepResult(
                "4. Consulta de datos biométricos",
                GET_BIOMETRICS_APDU,
                "7F 61 ... 90 00",
                "SUCCESS",
                "Respuesta simulada: estructura biométrica disponible."
        ));

        boolean hit = request.match();

        String verifyResponse = hit ? "90 00" : "63 00";
        String result = hit ? "HIT" : "NO HIT";

        steps.add(new StepResult(
                "5. Cotejo biométrico",
                "00 21 00 06 [DATOS DE PRUEBA]",
                verifyResponse,
                hit ? "SUCCESS" : "NO_MATCH",
                hit
                        ? "La simulación representa una coincidencia biométrica."
                        : "La simulación representa una ausencia de coincidencia."
        ));

        String explanation = hit
                ? "HIT: el cotejo simulado indica coincidencia."
                : "NO HIT: el cotejo simulado indica que no hay coincidencia.";

        return new MocResponse(
                mode,
                result,
                explanation,
                steps
        );
    }
}
