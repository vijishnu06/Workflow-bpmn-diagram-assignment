# Camunda 8 practical assignment - Labs 1 to 3

Three separate, deliberately compact Camunda 8 processes are in `lab1_loan`, `lab2_order`, and `lab3_appointment`. Each folder contains one executable BPMN file, one DMN decision table, a design PNG, test cases, and a short report. The shared Spring Boot project implements the job workers and three synthetic REST endpoints.

## Tested result

The three BPMN/DMN pairs were deployed to local Camunda 8 Run 8.8.32 on 1 November 2026. The Java project built successfully. Sixteen synthetic instances were executed: all reached the expected end event. Five intentional worker/REST failures produced incidents, then completed after correcting the input and retrying. The measured results and instance IDs are in `evidence/run_results.csv`; four incident responses captured before retry are in `evidence/incident_*.json`.

## Start locally

1. Start C8 Run from `C:\camunda\c8run` with `c8run.exe start`. Wait for `http://localhost:8080/operate` and `http://localhost:8080/tasklist` to open. Default local login is `demo` / `demo`.
2. Use Java 17 or later and Maven 3.9+. Run `mvn clean package`, then `java -jar target/camunda8-labs-1.0.0.jar` in this directory, or use `mvn spring-boot:run`. The downloadable local ZIP also contains the built JAR. The mock API listens on port 8085. Keep it running throughout testing.
3. Deploy each `.dmn` and `.bpmn` file in Camunda Modeler to the local cluster. Set authentication to **None**. Deploy the matching DMN before its BPMN, or deploy both together.
4. Start instances with the synthetic JSON examples in each lab folder. Set every `simulate...Error` and `fail...Worker` flag explicitly to `false` for ordinary cases.
5. Complete human review tasks in Tasklist, setting the indicated Boolean variable. Inspect the finished instance, variables, and activities in Operate.
6. For incident evidence, start an instance with an error flag `true`, wait for retries to exhaust, capture the incident in Operate, correct the flag or mock-service cause, then retry the job. For a REST task, edit the existing task-local `body` object and set its `simulateError` member to `false`; the input mapping has already copied this value, so changing only the root flag does not update the failed request. For a validation worker, change its root failure flag to `false`. See [API examples](API_EXAMPLES.md).

## Design principles

- One main horizontal flow per process; only meaningful rejection and review branches.
- DMN owns business classification. Workers validate or fulfil. REST connectors call local mock services.
- Failed REST calls or worker exceptions consume job retries and create incidents for Operate investigation; no fabricated success route is drawn.
- All data is synthetic. Payment tokens are mock strings such as `tok_ok` and `tok_decline`.

## Connector notes

The three REST tasks are configured with `io.camunda:http-json:1`, POST to `http://localhost:8085/mock/...`, a FEEL request body, and a result variable. C8 Run's connector runtime must be active. If the connector runtime is containerized on another host, replace `localhost` with an address reachable from that runtime before deployment. The result is read from `creditResponse.body`, `paymentResponse.body`, or `slotResponse.body`.

## Evidence status

Three actual completed-instance screenshots supplied from the running implementation are in [evidence/screenshots](evidence/screenshots/). The other design PNGs are renderings of the authored models. The CSV and JSON files under `evidence` contain real local Camunda API results. **Tasklist, incident-investigation, and recovery screenshots are still outstanding** because browser access could not complete its automatic approval check. See the [evidence index](evidence/README.md) and [submission checklist](SUBMISSION_CHECKLIST.md). No placeholder screenshots are included.

The original version 1 deployments remain in the local cluster with three obsolete connector-configuration incidents. The submitted BPMN/DMN files are the corrected version 2 deployments, and their 16 recorded test cases all completed. Use version 2 for this assignment.

## Files to review

- Each lab folder: BPMN, DMN, design image, report, test table, and a start JSON example.
- [test_inputs.json](test_inputs.json): full synthetic inputs for all 16 cases. `humanTaskCompletionVariables` belongs in the human task completion, rather than the initial request.
- [API_EXAMPLES.md](API_EXAMPLES.md): deployment, start, mock API, review, monitoring, and incident recovery examples.
- [DMN_SAMPLE_RESULTS.md](DMN_SAMPLE_RESULTS.md): rules and actual decision examples.
- [evidence/run_results.csv](evidence/run_results.csv): recorded instance keys, decisions, states, and end events.

## Sources

- Assignment brief: `Camunda8_Practical_Assignment_Labs_1_to_3.pdf` (provided by the student).
- Camunda 8 service tasks, user tasks, business rule tasks, REST connector, and Spring Boot starter: `https://docs.camunda.io/`.
