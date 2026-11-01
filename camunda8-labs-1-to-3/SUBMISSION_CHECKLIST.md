# PDF submission checklist

**Evidence status: Complete.** All three labs have their completed-process, Tasklist review, incident, and recovery screenshots. The evidence folder contains 16 actual screenshots and the recorded run results.

| Requirement from the assignment | Location / status |
|---|---|
| Separate BPMN and DMN for each lab | Three lab folders; deployed as version 2 |
| Simple process design images | `*_design.png` in each lab folder |
| Java/Spring Boot job workers and configuration | `src/main/java`, `src/main/resources/application.yml`, `pom.xml` |
| REST connectors and mock service | BPMN REST tasks; `MockServices.java`; `API_EXAMPLES.md` |
| Variable dictionaries and gateway explanation | Each lab's `REPORT.md` |
| Documented decision rules and sample results | DMN files, reports, `DMN_SAMPLE_RESULTS.md` |
| Multiple tests with expected and actual results | Each lab's `test_cases.csv`; `test_inputs.json`; 16 recorded completed runs |
| Deployment and execution evidence | Deployment keys in `DMN_SAMPLE_RESULTS.md`; definition versions and instance keys in `evidence/run_results.csv`; completed-process screenshots for all three labs |
| Intentional incidents and successful retry | Incident API records plus final results; recovery steps in `API_EXAMPLES.md` |
| Actual Tasklist screenshots | Assigned loan, order, and staff scheduling reviews in `evidence/screenshots` |
| Actual Operate incident and recovery screenshots | One incident and matching recovered instance per lab, plus REST error details; indexed in `evidence/README.md` |
| Concise implementation report for each lab | Each lab's `REPORT.md` |
| Synthetic data only | Synthetic references, mock payment tokens, educational triage flags; no real payment or patient information |

The screenshot evidence requirements are covered for all three labs. The original 16 tests and nine additional screenshot demonstration instances completed. Screenshots and API records are from the actual local implementation; no placeholder evidence is included.
