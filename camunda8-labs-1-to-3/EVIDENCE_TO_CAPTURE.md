# How to reproduce the included screenshots

All required screenshot categories are now included in [the evidence index](evidence/README.md): normal completion, assigned Tasklist review, Operate incident investigation, and the same instance after recovery for each lab. Use the corrected version 2 processes to reproduce the captures:

| Lab | Operate completed-instance example | Tasklist example to rerun | Incident example to rerun |
|---|---|---|---|
| Loan | `2251799813770184` (Approved) | Start the `L1_REVIEW` inputs from `test_cases.csv` and photograph Manual loan review before completing it | Start `L1_API_ERROR` with `simulateApiError=true`; photograph the credit REST incident before correcting and retrying |
| Order | `2251799813770243` (Order fulfilled) | Start `L2_REVIEW` and photograph Manual order verification | Start `L2_SERVICE_ERROR` with `simulatePaymentError=true`; photograph the payment incident |
| Appointment | `2251799813770208` (Appointment booked) | Start `L3_PRIORITY` and photograph Staff scheduling review | Start `L3_API_ERROR` with `simulateSchedulingError=true`; photograph the slots incident |

Open Operate at `http://localhost:8080/operate` and Tasklist at `http://localhost:8080/tasklist`. The local demo login is `demo` / `demo`. Take a screenshot showing the process name, the highlighted route or task, and the instance key. These demonstration instances are completed, so start a fresh human-task or incident case to photograph it while waiting. The included loan incident pair uses `L1_WORKER_FAILURE` instead of the optional credit API failure in the table. Keep the Java worker and mock API running on port 8085. See `API_EXAMPLES.md` for correcting the copied REST request body before retrying.
