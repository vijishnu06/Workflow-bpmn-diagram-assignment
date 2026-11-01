# Lab 1 - Loan approval

## Architecture and route

The `loan-validate` Spring worker checks required fields. Invalid details end as rejected. Valid details go to a REST credit assessment using synthetic data. The `loan_eligibility` DMN table returns `Approved`, `Rejected`, or `Manual Review`. A Tasklist user task resolves manual review using `reviewApproved`. The diagram has 13 elements, with one main flow and three outcome branches.

## Educational decision rules

Reject missing eligibility, income below 2000, or credit score below 550. Review income 2000-3999, score 550-699, or a requested amount above twelve monthly incomes. Approve income at least 4000, score at least 700, and amount at most twelve monthly incomes. The final catch-all is manual review. These thresholds are synthetic teaching rules, not financial advice.

## Variables

| Variable | Type | Purpose |
|---|---|---|
| applicantRef | string | Synthetic applicant identifier |
| requestedAmount | number | Loan amount |
| monthlyIncome | number | Synthetic monthly income |
| validationOk | boolean | Worker validation result |
| creditResponse.body.score | number | Mock REST credit score |
| loanDecision | string | DMN classification |
| reviewApproved | boolean | Tasklist reviewer choice |
| failWorker, simulateApiError | boolean | Incident tests |

## API example

`POST http://localhost:8085/mock/credit` with `{"applicantRef":"A-100","monthlyIncome":5000,"requestedAmount":10000,"simulateError":false}` returns `{"score":740,"assessment":"SYNTHETIC"}`.

## How to explain the diagram

Follow the main line from application to validation, credit REST call, and DMN decision. The first gateway rejects incomplete details. The decision gateway approves, rejects, or sends the case to a single human review task. That review has its own yes/no end events.

## Actual results and limitations

All five cases in `test_cases.csv` were run on local Camunda 8. The normal approval and DMN rejection completed directly. The manual case created a user task, then finished as **Approved after review** when completed through the local user-task API. Both the synthetic credit API error and worker failure created incidents; after correcting the input and retrying, both ended **Approved**. Instance IDs and dates are in `../evidence/run_results.csv`. The credit service is deterministic and educational; it does not check a real bureau.

The [actual completed-instance screenshot](../evidence/screenshots/lab1_loan_completed.png) shows the approved route, history, and variables. Tasklist and incident/recovery screenshots remain outstanding; see the [evidence index](../evidence/README.md).
