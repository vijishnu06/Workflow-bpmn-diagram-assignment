# PDF submission checklist

| Requirement from the assignment | Location / status |
|---|---|
| Separate BPMN and DMN for each lab | Three lab folders; deployed as version 2 |
| Simple process design images | `*_design.png` in each lab folder |
| Java/Spring Boot job workers and configuration | `src/main/java`, `src/main/resources/application.yml`, `pom.xml` |
| REST connectors and mock service | BPMN REST tasks; `MockServices.java`; `API_EXAMPLES.md` |
| Variable dictionaries and gateway explanation | Each lab's `REPORT.md` |
| Documented decision rules and sample results | DMN files, reports, `DMN_SAMPLE_RESULTS.md` |
| Multiple tests with expected and actual results | Each lab's `test_cases.csv`; `test_inputs.json`; 16 recorded completed runs |
| Deployment and execution evidence | Deployment keys in `DMN_SAMPLE_RESULTS.md`; actual definition versions and instance keys in `evidence/run_results.csv`; three Operate screenshots |
| Intentional incidents and successful retry | Incident API records plus final results; recovery steps in `API_EXAMPLES.md` |
| Actual Tasklist screenshots | **Outstanding** for all three labs |
| Actual Operate incident and recovery screenshots | **Outstanding** for all three labs |
| Concise implementation report for each lab | Each lab's `REPORT.md` |
| Synthetic data only | Synthetic references, mock payment tokens, educational triage flags; no real payment or patient information |

This submission remains incomplete until the outstanding screenshots are added. The recorded API results are actual tests, and the three existing screenshots are genuine running-instance evidence.
