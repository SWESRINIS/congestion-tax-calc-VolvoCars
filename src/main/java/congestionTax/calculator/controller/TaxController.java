package congestionTax.calculator.controller;

import congestionTax.calculator.exception.GlobalException;
import congestionTax.calculator.model.*;
import congestionTax.calculator.service.CongestionTaxCalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tax-calculate")
public class TaxController {

    private CongestionTaxCalculatorService congestionTaxCalculatorService;


    public TaxController(CongestionTaxCalculatorService congestionTaxCalculatorService) {
        this.congestionTaxCalculatorService = congestionTaxCalculatorService;
    }
    @Operation(summary = "Please use the format as in example 2012-12-31 13:59:59 for check in time", description = "Used to perform a congestion tax calculation based on the city, vehicle and check-in-time")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaxCalculatorRequest.class),
                                    examples = @ExampleObject(value = "{\n" +
                                            "  \"ChargesHistoryByDate\": {\n" +
                                            "    \"Date\": \"Value\"\n" +
                                            "  },\n" +
                                            "  \"taxAmount\": \n" +
                                            "    \"Total Value,\"\n" +
                                            "  \n" +
                                            "}"))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Resource Not Found!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "415",
                            description = "Unsupported Media Type",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                }
            )

    @PostMapping
    public ResponseEntity<TaxCalculatorResponse> calculateCongestionTax(@RequestBody
                                                                            TaxCalculatorRequest taxCalculatorRequest,
                                                                            @RequestHeader("city") String city)
            throws GlobalException {

        congestionTaxCalculatorService.isValidCity(city);
        congestionTaxCalculatorService.isValidVehicle(taxCalculatorRequest.getVehicle());
        TaxCalculatorResponse result = congestionTaxCalculatorService.getTax(taxCalculatorRequest.getVehicle(), taxCalculatorRequest.getCheckInTime(), city);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler({GlobalException.class})
    public final ResponseEntity<Object> handleGlobalException(GlobalException e) {
        HttpStatus httpStatus = e.getHttpStatus();
        if (null == e.getHttpStatus()) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        List<String> details = new ArrayList<>();
        details.add(e.getMessage());
        return new ResponseEntity<>(details, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        List<String> details = new ArrayList<>();
        details.add(e.getMessage());
        return new ResponseEntity<>(details, httpStatus);
    }
}