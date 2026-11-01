# Lab 1: Loan approval

## What the process does

This process checks a loan application and either approves it, rejects it, or asks for a manual review. The main route goes from applicant validation to a credit check and then to the DMN decision.

The `loan-validate` Java worker checks the applicant reference, income, and requested amount. Invalid details go straight to the rejected end event. Valid applications call the mock credit API. The `loan_eligibility` decision table then returns Approved, Rejected, or Manual Review.

If a review is needed, the process waits for the reviewer in Tasklist. Setting `reviewApproved` to true approves the application; setting it to false rejects it.

## Decision rules

These are simple rules chosen for the exercise. A failed validation, income below 2000, or a credit score below 550 leads to rejection. Income from 2000 to 3999, a score from 550 to 699, or a loan above twelve months of income leads to review. Approval needs income of at least 4000, a score of at least 700, and a loan within that income limit. Any remaining case goes to review. The table checks rules from top to bottom using FIRST hit policy.

## Variables

| Variable | Type | Purpose |
|---|---|---|
| applicantRef | string | Made-up applicant reference |
| requestedAmount | number | Loan amount |
| monthlyIncome | number | Income used in the test |
| validationOk | boolean | Worker validation result |
| creditResponse.body.score | number | Mock REST credit score |
| loanDecision | string | Result from the decision table |
| reviewApproved | boolean | Decision entered by the reviewer |
| failWorker, simulateApiError | boolean | Flags used to cause test failures |

## Example API call

`POST http://localhost:8085/mock/credit` with `{"applicantRef":"A-100","monthlyIncome":5000,"requestedAmount":10000,"simulateError":false}` returns `{"score":740,"assessment":"SYNTHETIC"}`.

## Testing and results

All five planned cases passed: approval, rejection, manual review, a credit API error, and a worker failure. The original review test was completed through the user-task API. A later review was assigned and completed in Tasklist with `reviewApproved=true`, ending as Approved after review.

For the failure demonstration, `failWorker=true` made the validation worker fail until its retries ran out. Operate showed the incident at Validate applicant. After the flag was changed to false and Retry was selected, the same instance continued through the credit check and ended as Approved.

The [test table](test_cases.csv) contains the inputs and outcomes. The [main results](../evidence/run_results.csv) have the original instance IDs. The later Tasklist review is instance `2251799813784665`, and the incident/recovery pair is `2251799813787883`.

Screenshots: [normal approval](../evidence/screenshots/lab1_loan_completed.png), [Tasklist review](../evidence/screenshots/lab1_tasklist_review.jpg), [worker incident](../evidence/screenshots/lab1_worker_incident.jpg), and [recovery](../evidence/screenshots/lab1_worker_recovered.jpg).

## Limitations

The credit API returns a fixed score based on the test income. It does not contact a credit bureau, and the thresholds are only for this classroom example.
