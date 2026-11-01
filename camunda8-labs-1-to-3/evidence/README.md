# Screenshots and test results

These screenshots were taken from the local Camunda 8 Run 8.8.32 setup on 1 November 2026. They show successful runs, waiting review tasks, failed jobs, and the same jobs after recovery. The first three completed-process images were captured manually. The remaining images were captured during new demonstration runs. Diagram exports are saved separately in each lab folder.

## Screenshots for each lab

| Lab | Normal completion | Assigned human review | Incident investigation | Same instance after recovery |
|---|---|---|---|---|
| Loan | [Approved](screenshots/lab1_loan_completed.png) | [Manual loan review](screenshots/lab1_tasklist_review.jpg) | [Worker failure](screenshots/lab1_worker_incident.jpg) | [Approved after retry](screenshots/lab1_worker_recovered.jpg) |
| Order | [Order fulfilled](screenshots/lab2_order_completed.png) | [Manual order verification](screenshots/lab2_tasklist_review.jpg) | [Payment failure](screenshots/lab2_payment_incident.jpg) | [Fulfilled after retry](screenshots/lab2_payment_recovered.jpg) |
| Appointment | [Appointment booked](screenshots/lab3_appointment_completed.png) | [Staff scheduling review](screenshots/lab3_tasklist_review.jpg) | [Scheduling failure](screenshots/lab3_scheduling_incident.jpg) | [Booked after retry](screenshots/lab3_scheduling_recovered.jpg) |

For a closer look at the REST failures: [payment failed route](screenshots/lab2_payment_failed_path.jpg), [payment HTTP 503 details](screenshots/lab2_payment_error_details.jpg), [scheduling failed route](screenshots/lab3_scheduling_failed_path.jpg), and [scheduling HTTP 503 details](screenshots/lab3_scheduling_error_details.jpg).

## Matching the screenshots to the runs

| Lab | Original normal instance | Tasklist review process instance | Task key | Final incident and recovery process instance |
|---|---|---|---|---|
| Loan | 2251799813770184 | 2251799813784665 | 2251799813784700 | 2251799813787883 |
| Order | 2251799813770243 | 2251799813784713 | 2251799813784764 | 2251799813787894 |
| Appointment | 2251799813770208 | 2251799813784785 | 2251799813784842 | 2251799813787933 |

The three review tasks were assigned to the demo user and completed in Tasklist. Loan and order reviews used `reviewApproved=true`. The appointment review used `slotSelected=true` with the test slot already stored in the request.

The loan worker failed when `failWorker` was true. The payment and scheduling services returned HTTP 503 when their requests had `simulateError=true`. After the screenshots were taken, these values were corrected through the local API and Retry was clicked in Operate. For the REST tasks, both the process flag and the saved `body.simulateError` value were changed. Each recovery image shows the same instance ID as its failure image, now completed without an active incident.

## Saved records

- `run_results.csv` and `.json`: original 16 synthetic tests, all completed as expected.
- `screenshot_run_results.csv` and `.json`: nine additional demonstration runs, all completed. The first failure images were too narrow, so those cases were run again to capture the complete details. The last three failure instances are the ones shown in the final images.
- `screenshot_run_keys.json`: six initial demonstration instance keys.
- `final_capture_failure_keys.json`: three final full-width incident/recovery instance keys.
- `incident_*.json`: four active incident responses preserved from the original test suite.
- `screenshot_incident_*.json`: active incident responses preserved during screenshot demonstrations. Each final failure instance has its own saved incident record.

The original 16 tests used the user-task API to complete reviews. The later three review demonstrations used Tasklist itself. Every screenshot here comes from the running application.

## Preview

![Assigned loan review in Tasklist](screenshots/lab1_tasklist_review.jpg)

![Loan worker incident](screenshots/lab1_worker_incident.jpg)

![Same loan instance recovered](screenshots/lab1_worker_recovered.jpg)

![Payment service error details](screenshots/lab2_payment_error_details.jpg)

![Scheduling service recovered](screenshots/lab3_scheduling_recovered.jpg)
