
package MoC.controller;

import MoC.Lab.dto.MocRequest;
import MoC.Lab.dto.MocResponse;
import MoC.Lab.service.MocSimulatorService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moc-lab")
public class MocController {

    private final MocSimulatorService simulatorService;

    public MocController(MocSimulatorService simulatorService) {
        this.simulatorService = simulatorService;
    }

    @PostMapping("/execute")
    public MocResponse execute(@RequestBody MocRequest request) {
        return simulatorService.execute(request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            IllegalArgumentException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }

    public record ErrorResponse(String message) {
    }
}
