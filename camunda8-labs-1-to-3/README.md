# Camunda 8 practical labs

These three labs cover loan approval, order processing, and hospital appointments. Each process has one main route, with separate branches for review, rejection, or another outcome. This keeps the diagrams easy to follow during a demonstration.

| Lab | Files and explanation |
|---|---|
| 1. Loan approval | [Model, decision table, and report](lab1_loan/) |
| 2. Order processing | [Model, decision table, and report](lab2_order/) |
| 3. Hospital appointments | [Model, decision table, and report](lab3_appointment/) |

The BPMN files control the flow. DMN tables make the decisions, Java workers validate or complete work, and REST connectors call the mock services. All examples use made-up data and mock payment tokens.

## Run the project

The project was tested with Camunda 8 Run 8.8.32. You will also need Java 17 or later and Maven 3.9 or later.

1. Start Camunda 8 Run. On the machine used for testing, it is installed at `C:\camunda\c8run`; run `c8run.exe start` from that folder.
2. Open Operate at `http://localhost:8080/operate` and Tasklist at `http://localhost:8080/tasklist`. The local demo login is `demo` / `demo`.
3. From this project folder, run `mvn clean package`, then `java -jar target/camunda8-labs-1.0.0.jar`. You can also use `mvn spring-boot:run`. The local ZIP includes a built JAR. Keep the application running: it provides the workers and mock APIs on port 8085.
4. Deploy each lab's BPMN and DMN together from Camunda Modeler, or deploy the DMN first. Use authentication **None** for this local setup.
5. Start a process using the JSON example in its lab folder. For a normal run, set the failure flags to `false`.
6. Complete any review task in Tasklist, then open the instance in Operate to check its route and variables.

The [API examples](API_EXAMPLES.md) include deployment commands, requests, review variables, and retry steps. The connector runtime must be running along with Camunda. If it runs on another machine or in a container, replace the mock API's `localhost` address with one that the connector can reach.

## What was tested

The main test set contains 16 cases. These cover normal outcomes, rejection, manual review, and failed services or workers. Every case reached its expected end event. Five cases deliberately caused an incident; each completed after the cause was fixed and the job was retried.

Nine more instances were used for the screenshots. The three review tasks were assigned and completed in Tasklist, and the failed jobs were retried in Operate. These runs also completed.

- [Test inputs](test_inputs.json) contains the full input for each main case. Supply `humanTaskCompletionVariables` when completing the task, rather than when starting the process.
- [Main test results](evidence/run_results.csv) lists the 16 original runs.
- [Screenshot run results](evidence/screenshot_run_results.csv) lists the later demonstrations.
- [Screenshots and instance IDs](evidence/README.md) connects each image to the process it shows.
- [Decision examples](DMN_SAMPLE_RESULTS.md) explains the DMN inputs and sample results.
- [Submission checklist](SUBMISSION_CHECKLIST.md) shows where each required item is saved.

## A note about retries

For a worker failure, change its failure flag to `false` before retrying. A failed REST task needs one extra step: update the existing task's `body` object so that `simulateError` is `false`. The request body was copied when the task started, so changing only the process-level flag will not change that saved request.

The connector responses are read through `creditResponse.body`, `paymentResponse.body`, or `slotResponse.body`. The REST tasks use job type `io.camunda:http-json:1` and POST to the mock endpoints under `http://localhost:8085/mock/`.

## Evidence status: Complete

All three labs have the required screenshots: a completed process, an assigned Tasklist review, a failed job in Operate, and the same instance after recovery. The folder contains 16 actual screenshots, including extra views of the payment and scheduling errors. The test results and instance IDs are saved alongside them in the [evidence folder](evidence/README.md).

## Model version used

Use **version 2** in the tested local cluster. Version 1 was an earlier deployment with incorrect connector settings, and its old incidents are still visible in Operate. The submitted files contain the corrected settings.

## References

The requirements come from `Camunda8_Practical_Assignment_Labs_1_to_3.pdf`. Camunda setup and model configuration were checked against the [Camunda documentation](https://docs.camunda.io/).
