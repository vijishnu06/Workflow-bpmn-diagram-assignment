# Lab 2 - E-commerce fulfilment

## Architecture and route

The `order-validate` worker checks required fields, simulated stock, and total. No stock ends before payment. The `order_priority` DMN table outputs `Express`, `High`, or `Standard`. Orders worth 1000 or more require a Tasklist verification with `reviewApproved`. The REST payment task calls a mock API; a paid response proceeds to `order-fulfil`, while a declined response closes separately.

## Educational priority rules

Express delivery has first priority. Otherwise a total of at least 1000 or premium customer gets High priority. All other orders are Standard. The DMN table uses FIRST hit policy, so order matters. High-value manual review is independent of the output priority.

## Variables

| Variable | Type | Purpose |
|---|---|---|
| orderRef, productId | string | Synthetic order and product |
| customerRef | string | Optional synthetic customer reference retained as process context; included in the start example and not used by the priority rules |
| quantity, stockCount | integer | Demand and simulated inventory |
| unitPrice, orderTotal | number | Price and worker-calculated total |
| customerCategory, deliveryPreference | string | DMN priority inputs |
| stockAvailable | boolean | Gateway routing |
| orderPriority | string | DMN result |
| paymentToken | string | Mock token only |
| paymentResponse.body.status | string | `PAID` or `DECLINED` |
| reviewApproved | boolean | Tasklist reviewer choice |
| failInventoryWorker, simulatePaymentError | boolean | Incident tests |

## API example

`POST http://localhost:8085/mock/payment` with `{"orderRef":"O-100","orderTotal":200,"paymentToken":"tok_ok","simulateError":false}` returns `{"status":"PAID","reference":"MOCK-PAY-001"}`. `tok_decline` returns `DECLINED`.

## How to explain the diagram

Follow order validation and the stock gateway first. In-stock orders get a priority from DMN. Only high-value orders pause for manual verification. The payment REST result then chooses fulfilment or the declined end event.

## Actual results and limitations

All six cases in `test_cases.csv` were run on local Camunda 8. Paid, out-of-stock, declined, and manual-review routes reached their expected end events. Human verification was completed through the local user-task API. The payment API and inventory-worker failures each created an incident; after correction and retry, both completed as **Order fulfilled**. Instance IDs and dates are in `../evidence/run_results.csv`. The optional customerRef context field was added to the reusable start example after those runs; it does not affect routing. Inventory and payment are synthetic; stock is checked but not reserved across concurrent instances.

The [actual completed-instance screenshot](../evidence/screenshots/lab2_order_completed.png) shows fulfilment and its result variables. A fresh high-value review instance `2251799813784713` was assigned and completed in Tasklist with `reviewApproved=true`. The final payment incident capture shows HTTP 503 in instance `2251799813787894`. After correcting `body.simulateError` and the root error flag, Retry was selected in Operate and the same instance completed as Order fulfilled. [Tasklist](../evidence/screenshots/lab2_tasklist_review.jpg), [incident](../evidence/screenshots/lab2_payment_incident.jpg), [error details](../evidence/screenshots/lab2_payment_error_details.jpg), and [recovery](../evidence/screenshots/lab2_payment_recovered.jpg) captures are included.
