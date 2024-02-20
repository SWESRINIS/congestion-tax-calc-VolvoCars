package congestionTax.calculator.controller;

import congestionTax.calculator.exception.GlobalException;
import congestionTax.calculator.model.TaxCalculatorRequest;
import congestionTax.calculator.model.TaxCalculatorResponse;
import congestionTax.calculator.model.Vehicle;
import congestionTax.calculator.service.CongestionTaxCalculatorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TaxControllerTest {

    @InjectMocks
    private TaxController taxController;

    @Mock
    private CongestionTaxCalculatorService congestionTaxCalculatorService;

    @Test
    public void when_calculate_tax_called_with_valid_input_then_return_success_response() throws Exception {

        TaxCalculatorRequest request = constructRequest("Car");
        TaxCalculatorResponse taxCalculatorResponse = TaxCalculatorResponse.builder().taxAmount(new BigDecimal(10)).build();
        Mockito.when(congestionTaxCalculatorService.getTax(request.getVehicle(), request.getCheckInTime(), "Gothenburg")).thenReturn(taxCalculatorResponse);
        ResponseEntity<TaxCalculatorResponse> response = taxController.calculateCongestionTax(request, "Gothenburg");
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTaxAmount()).isEqualTo(new BigDecimal(10));
    }

    @Test(expected = GlobalException.class)
    public void when_calculate_tax_called_with_invalid_vehicle_type_then_return_error_response() throws Exception {

        TaxCalculatorRequest request = constructRequest("Cycle");
        Mockito.doThrow(new GlobalException("Invalid Vehicle")).when(congestionTaxCalculatorService).isValidVehicle(request.getVehicle());
        taxController.calculateCongestionTax(request, "Gothenburg");
    }

    private TaxCalculatorRequest constructRequest(String vehicleType) {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(vehicleType);
        List<Date> dateList = new ArrayList<>();
        dateList.add(new Date());
        TaxCalculatorRequest request = new TaxCalculatorRequest();
        request.setVehicle(vehicle);
        request.setCheckInTime(dateList);
        return request;
    }
}