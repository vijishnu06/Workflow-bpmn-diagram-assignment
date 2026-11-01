# Decision rules and actual sample results

All tables use FIRST hit policy. Inputs and outputs are defined in the corresponding DMN files; rule order resolves overlapping matches.

| Decision | Inputs | Output |
|---|---|---|
| `loan_eligibility` | `validationOk`, `monthlyIncome`, `requestedAmount`, `creditResponse.body.score` | `loanDecision`: Approved / Rejected / Manual Review |
| `order_priority` | `orderTotal`, `customerCategory`, `deliveryPreference` | `orderPriority`: Express / High / Standard |
| `patient_triage` | `emergencyIndicator`, `severeSymptoms`, `priorityIndicator` | `triageCategory`: Urgent Assessment / Priority Review / Routine |

Actual decision examples from `evidence/run_results.csv`:

| Test | Relevant input and service result | DMN result |
|---|---|---|
| L1_APPROVE | Valid; income 5000; amount 10000; mock score 740 | Approved |
| L1_REJECT | Valid; income 1500; amount 10000; mock score 500 | Rejected |
| L1_REVIEW | Valid; income 3000; amount 12000; mock score 650 | Manual Review |
| L2_PAID | Total 200; regular customer; standard delivery | Standard |
| L2_REVIEW | Total 1200; regular customer; standard delivery | High |
| L3_ROUTINE | Emergency=false; severe=false; priority=false | Routine |
| L3_PRIORITY | Emergency=false; severe=false; priority=true | Priority Review |
| L3_URGENT | Emergency=true; severe=false; priority=false | Urgent Assessment |

The Express rule is defined in the order DMN but is not claimed as one of the 16 recorded process tests. Loan affordability rules compare the amount with twelve monthly incomes. The detailed thresholds and limitations are in the lab reports.

## Recorded local deployment

The corrected resources were successfully deployed to Camunda 8 Run 8.8.32 on 1 November 2026. The deployment response returned:

| Process | Version | Deployment key | Process definition key | Decision definition key |
|---|---|---|---|---|
| `loan_approval` | 2 | 2251799813770115 | 2251799813770116 | 2251799813770118 |
| `order_fulfilment` | 2 | 2251799813770123 | 2251799813770124 | 2251799813770126 |
| `appointment_triage` | 2 | 2251799813770119 | 2251799813770120 | 2251799813770122 |

Keys are specific to the tested local cluster. A new deployment generates new keys and may have a different version.
