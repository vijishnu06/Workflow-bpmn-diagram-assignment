# API and execution examples

Use synthetic data only. Start Camunda 8 Run, then run the Spring application on port 8085. Commands below use `curl`; in Windows PowerShell, call `curl.exe` to avoid the legacy alias. Keep both services running.

## Deploy a BPMN and its DMN

From this project folder:

```sh
curl -X POST http://localhost:8080/v2/deployments -F "resources=@lab1_loan/loan_eligibility.dmn" -F "resources=@lab1_loan/loan_approval.bpmn"
curl -X POST http://localhost:8080/v2/deployments -F "resources=@lab2_order/order_priority.dmn" -F "resources=@lab2_order/order_fulfilment.bpmn"
curl -X POST http://localhost:8080/v2/deployments -F "resources=@lab3_appointment/patient_triage.dmn" -F "resources=@lab3_appointment/appointment_triage.bpmn"
```

Alternatively deploy each pair from Camunda Modeler with authentication set to None for the local demo cluster.

## Mock REST requests and responses

All three BPMN connector tasks use `io.camunda:http-json:1`, two retries, POST, input mappings for `url`, `method`, and `body`, and a `resultVariable` header.

| Endpoint | JSON request body | JSON success body |
|---|---|---|
| `http://localhost:8085/mock/credit` | `{"applicantRef":"A-100","monthlyIncome":5000,"requestedAmount":10000,"simulateError":false}` | `{"score":740,"assessment":"SYNTHETIC"}` |
| `http://localhost:8085/mock/payment` | `{"orderRef":"O-100","orderTotal":200,"paymentToken":"tok_ok","simulateError":false}` | `{"status":"PAID","reference":"MOCK-PAY-001"}` |
| `http://localhost:8085/mock/slots` | `{"patientRef":"P-100","appointmentPreference":"morning","simulateError":false}` | `{"slots":["2026-11-05T10:00:00+05:30"]}` |

JSON object property order can vary. Credit scores are 740 for income >=4000, 650 for income >=2000, and 500 otherwise. `tok_decline` returns `DECLINED`. Appointment preference `none` returns an empty slots list. `simulateError=true` produces HTTP 503. Connector results contain the response body at `creditResponse.body`, `paymentResponse.body`, or `slotResponse.body`.

## Start a process

POST `http://localhost:8080/v2/process-instances` with this structure:

```json
{
  "processDefinitionId": "loan_approval",
  "variables": {
    "applicantRef": "A-100",
    "monthlyIncome": 5000,
    "requestedAmount": 10000,
    "failWorker": false,
    "simulateApiError": false
  }
}
```

For all test cases, use the `processDefinitionId` and complete `variables` object in [test_inputs.json](test_inputs.json). The response contains the new `processInstanceKey`. Review variables should be supplied when completing the human task.

## Review tasks

Open `http://localhost:8080/tasklist` using the local demo account. Select and assign the waiting task, review its variables, and complete with:

| Task | Completion variables |
|---|---|
| Manual loan review | `{"reviewApproved":true}` to approve; false to reject |
| Manual order verification | `{"reviewApproved":true}` to continue payment; false to reject |
| Staff scheduling review | `{"slotSelected":true,"selectedSlot":"2026-11-05T10:00:00+05:30"}` to book; `{"slotSelected":false}` for no slot |
| Immediate staff assessment | `{}`; completion records that simulated assessment occurred |

The equivalent local API call is POST `/v2/user-tasks/USER_TASK_KEY/completion` with `{"variables":{...}}`. Find tasks with POST `/v2/user-tasks/search` and `{"filter":{"processInstanceKey":"INSTANCE_KEY"}}`. The initial 16-test suite used this API. The three later screenshot review cases were assigned and completed through the Tasklist UI, with `reviewApproved=true` or `slotSelected=true` entered in the task.

## Monitor and recover

1. Start a failure case from `test_inputs.json`; wait for retries to reach zero.
2. Open that instance in Operate. Inspect the failed activity, error, variables, and job retries. Capture the actual incident screen.
3. For a worker fault, change `failWorker` or `failInventoryWorker` to false at process scope. For a REST fault, select the failed activity scope and edit the existing `body` object so `simulateError` is false. Preserve its other fields. Input mappings ran when the task started, so changing only the root `simulate...Error` flag does not change the copied request body.
4. Retry in Operate. Verify the completed end event and capture the recovery screen.

The equivalent API sequence used in the recorded tests was:

```text
POST  /v2/incidents/search
      {"filter":{"processInstanceKey":"INSTANCE_KEY"}}
PUT   /v2/element-instances/SCOPE_KEY/variables
      {"variables":{"failWorker":false}}
PATCH /v2/jobs/JOB_KEY
      {"changeset":{"retries":1}}
POST  /v2/incidents/INCIDENT_KEY/resolution
      {}
```

For REST faults, use the failed activity's element instance as `SCOPE_KEY` and update the full `body` object instead. Use keys returned by the current cluster; the uppercase values in these instructions are parameters, not evidence.

Final states and variable values can be queried with POST `/v2/process-instances/search`, `/v2/element-instances/search`, and `/v2/variables/search`, each filtered by `processInstanceKey`. The actual saved responses and results are indexed under [evidence](evidence/README.md).
