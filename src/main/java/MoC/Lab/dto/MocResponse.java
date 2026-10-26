package MoC.Lab.dto;

import java.util.List;

public record MocResponse(
        String mode,
        String result,
        String explanation,
        List<StepResult> steps
) {
    public record StepResult(
            String name,
            String command,
            String response,
            String status,
            String detail
    ) {
    }
}
