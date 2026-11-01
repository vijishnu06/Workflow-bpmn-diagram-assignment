# Lab 3 - Hospital appointment and triage

## Architecture and route

The `patient-validate` worker checks synthetic registration fields. The `patient_triage` DMN table returns `Routine`, `Priority Review`, or `Urgent Assessment`. Urgent requests immediately reach a Tasklist staff assessment and end as assessed. Other requests call the mock scheduling REST API. Priority cases or cases without slots reach Tasklist review. A selected slot is booked by a worker; otherwise the process ends with no slot.

## Educational triage rules

An emergency indicator or severe symptoms is Urgent Assessment. Otherwise a priority indicator is Priority Review; all other complete requests are Routine. These rules are invented for class demonstration and must not be used for real patient triage.

## Variables

| Variable | Type | Purpose |
|---|---|---|
| patientRef | string | Synthetic patient identifier |
| appointmentPreference, symptomsSummary | string | Request details |
| emergencyIndicator, severeSymptoms, priorityIndicator | boolean | DMN inputs |
| registrationValid | boolean | Worker validation result |
| triageCategory | string | DMN classification |
| slotResponse.body.slots | list | Mock API availability |
| slotSelected | boolean | Tasklist staff choice |
| selectedSlot | string | Selected appointment time |
| bookingStatus | string | Worker result |
| failRegistrationWorker, simulateSchedulingError | boolean | Incident tests |

## API example

`POST http://localhost:8085/mock/slots` with `{"patientRef":"P-100","appointmentPreference":"morning","simulateError":false}` returns a synthetic `slots` list. Preference `none` returns an empty list.

## How to explain the diagram

Follow registration and the DMN triage decision. Urgent requests go straight to staff assessment. Other requests call the slot REST service. A routine request with a slot books directly; priority or no-slot requests pause for staff review before booking or ending without a slot.

## Actual results and limitations

All five cases in `test_cases.csv` were run on local Camunda 8. Routine booking, priority review, urgent assessment, and no-slot review reached their expected end events. Human tasks were completed through the local user-task API. The scheduling API failure created an incident; after correction and retry it completed as **Appointment booked**. Instance IDs and dates are in `../evidence/run_results.csv`. Registration validation is performed by the worker; the separate REST connector performs the availability request. No real patient data or live hospital system is involved.

The [actual completed-instance screenshot](../evidence/screenshots/lab3_appointment_completed.png) shows the booked route, history, and booking variables. Tasklist and incident/recovery screenshots remain outstanding; see the [evidence index](../evidence/README.md).
