package edu.example.labs;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class LabWorkers {
  @JobWorker(type = "loan-validate")
  public Map<String, Object> loanValidate(@Variable String applicantRef,
      @Variable Double monthlyIncome, @Variable Double requestedAmount,
      @Variable Boolean failWorker) {
    if (Boolean.TRUE.equals(failWorker)) throw new IllegalStateException("Synthetic loan worker failure");
    boolean ok = applicantRef != null && !applicantRef.isBlank()
        && monthlyIncome != null && monthlyIncome > 0
        && requestedAmount != null && requestedAmount > 0;
    return Map.of("validationOk", ok);
  }

  @JobWorker(type = "order-validate")
  public Map<String, Object> orderValidate(@Variable String orderRef,
      @Variable String productId, @Variable Integer quantity,
      @Variable Double unitPrice, @Variable Integer stockCount,
      @Variable Boolean failInventoryWorker) {
    if (Boolean.TRUE.equals(failInventoryWorker)) throw new IllegalStateException("Synthetic inventory worker failure");
    if (orderRef == null || productId == null || quantity == null || quantity <= 0 || unitPrice == null || unitPrice < 0)
      throw new IllegalArgumentException("Invalid order fields");
    return Map.of("stockAvailable", stockCount != null && stockCount >= quantity,
        "orderTotal", quantity * unitPrice);
  }

  @JobWorker(type = "order-fulfil")
  public Map<String, Object> fulfil(@Variable String orderRef) {
    return Map.of("fulfilmentStatus", "FULFILLED", "fulfilledOrderRef", orderRef);
  }

  @JobWorker(type = "patient-validate")
  public Map<String, Object> validatePatient(@Variable String patientRef,
      @Variable String appointmentPreference, @Variable String symptomsSummary,
      @Variable Boolean failRegistrationWorker) {
    if (Boolean.TRUE.equals(failRegistrationWorker)) throw new IllegalStateException("Synthetic registration worker failure");
    boolean ok = patientRef != null && !patientRef.isBlank()
        && appointmentPreference != null && !appointmentPreference.isBlank()
        && symptomsSummary != null && !symptomsSummary.isBlank();
    return Map.of("registrationValid", ok);
  }

  @JobWorker(type = "appointment-book")
  public Map<String, Object> bookAppointment(@Variable String patientRef,
      @Variable String selectedSlot) {
    return Map.of("bookingStatus", "BOOKED", "bookedPatientRef", patientRef,
        "bookedSlot", selectedSlot == null ? "first available slot" : selectedSlot);
  }
}
