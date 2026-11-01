# Lab 3: Hospital appointments and triage

## What the process does

This process checks a made-up appointment request, assigns a triage category, and either books a slot or sends the request to staff.

The `patient-validate` worker checks the patient reference, appointment preference, and symptoms summary. Incomplete requests end as Invalid request. Valid requests go to the `patient_triage` DMN table.

Urgent requests go straight to Immediate staff assessment in Tasklist. Other requests call the mock scheduling API. A Routine request with an available slot is booked directly. Priority requests and requests without slots go to Staff scheduling review. Staff can select a slot to book or finish the case as No slot available.

## Triage rules

An emergency indicator or severe symptoms gives Urgent Assessment. If neither is present but the priority indicator is true, the result is Priority Review. If all three indicators are false, the result is Routine. The rules are checked in that order using FIRST hit policy. They are classroom rules for synthetic requests, not clinical triage guidance.

## Variables

| Variable | Type | Purpose |
|---|---|---|
| patientRef | string | Made-up patient reference |
| appointmentPreference, symptomsSummary | string | Request details |
| emergencyIndicator, severeSymptoms, priorityIndicator | boolean | DMN inputs |
| registrationValid | boolean | Worker validation result |
| triageCategory | string | Result from the decision table |
| slotResponse.body.slots | list | Mock API availability |
| slotSelected | boolean | Whether staff selected a slot |
| selectedSlot | string | Selected appointment time |
| bookingStatus | string | Worker result |
| failRegistrationWorker, simulateSchedulingError | boolean | Flags used to cause test failures |

## Example API call

`POST http://localhost:8085/mock/slots` with `{"patientRef":"P-100","appointmentPreference":"morning","simulateError":false}` returns a synthetic `slots` list. Preference `none` returns an empty list.

## Testing and results

All five planned cases passed: routine booking, priority review, urgent assessment, no available slots, and a scheduling API failure. Registration was checked by the Java worker, and the separate REST connector requested the appointment slots.

The original human tasks were completed through the user-task API. For the screenshots, a fresh priority request was assigned in Tasklist. Staff completed it with `slotSelected=true` and the existing test slot, and the process ended as Appointment booked.

For the failure case, the scheduling API returned HTTP 503. After `simulateError` was set to false in the failed task's saved request body and the process flag was corrected, Retry was selected in Operate. The same request then received a slot and completed the booking.

The [test table](test_cases.csv) and [main results](../evidence/run_results.csv) list the original runs. The later staff review is instance `2251799813784785`, and the scheduling incident/recovery pair is `2251799813787933`.

Screenshots: [normal booking](../evidence/screenshots/lab3_appointment_completed.png), [Tasklist review](../evidence/screenshots/lab3_tasklist_review.jpg), [scheduling incident](../evidence/screenshots/lab3_scheduling_incident.jpg), [HTTP 503 details](../evidence/screenshots/lab3_scheduling_error_details.jpg), and [recovery](../evidence/screenshots/lab3_scheduling_recovered.jpg).

## Limitations

The service returns a fixed test slot or an empty list. It is not connected to a hospital, and the data does not describe real patients.
