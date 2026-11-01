# Lab 2: Order processing and fulfilment

## What the process does

This process checks an order, decides its priority, takes a mock payment, and either fulfils it or ends with a reason it could not continue.

The `order-validate` worker checks the order fields, compares the quantity with the available stock, and calculates the total. An order without enough stock ends before payment. Otherwise, the `order_priority` DMN table sets its delivery priority.

Orders worth 1000 or more wait for Manual order verification in Tasklist. Approval lets the order continue to payment; rejection closes it. The mock payment API returns PAID or DECLINED. Paid orders reach the fulfilment worker, while declined payments have a separate end event.

## Priority rules

Express delivery is checked first and receives Express priority. For other deliveries, an order worth at least 1000 or a premium customer receives High priority. Everything else is Standard. The table uses FIRST hit policy.

The manual-review gateway checks the order total separately. For example, an Express order worth 1200 still needs verification before payment.

## Variables

| Variable | Type | Purpose |
|---|---|---|
| orderRef, productId | string | Synthetic order and product |
| customerRef | string | Optional customer reference kept with the order; it does not affect priority |
| quantity, stockCount | integer | Quantity ordered and stock available |
| unitPrice, orderTotal | number | Price and worker-calculated total |
| customerCategory, deliveryPreference | string | DMN priority inputs |
| stockAvailable | boolean | Whether the order can continue |
| orderPriority | string | DMN result |
| paymentToken | string | Mock token only |
| paymentResponse.body.status | string | `PAID` or `DECLINED` |
| reviewApproved | boolean | Decision entered by the reviewer |
| failInventoryWorker, simulatePaymentError | boolean | Flags used to cause test failures |

## Example API call

`POST http://localhost:8085/mock/payment` with `{"orderRef":"O-100","orderTotal":200,"paymentToken":"tok_ok","simulateError":false}` returns `{"status":"PAID","reference":"MOCK-PAY-001"}`. `tok_decline` returns `DECLINED`.

## Testing and results

All six planned cases passed. These covered a successful order, insufficient stock, declined payment, a high-value review, a payment service failure, and an inventory worker failure. Both failed jobs completed after their causes were corrected and they were retried.

The original review test used the user-task API. For the screenshots, a new order worth 1200 was assigned and approved in Tasklist using `reviewApproved=true`. It then completed payment and fulfilment.

The payment failure demonstration returned HTTP 503 from the mock service. The failed task's request body was corrected by setting `simulateError` to false, along with the process-level flag. Selecting Retry in Operate allowed the same instance to finish as Order fulfilled.

The [test table](test_cases.csv) and [main results](../evidence/run_results.csv) contain the original runs. The later review is instance `2251799813784713`, and the payment incident/recovery pair is `2251799813787894`.

Screenshots: [normal fulfilment](../evidence/screenshots/lab2_order_completed.png), [Tasklist review](../evidence/screenshots/lab2_tasklist_review.jpg), [payment incident](../evidence/screenshots/lab2_payment_incident.jpg), [HTTP 503 details](../evidence/screenshots/lab2_payment_error_details.jpg), and [recovery](../evidence/screenshots/lab2_payment_recovered.jpg).

## Limitations

Stock and payment are simulated. Stock is checked for each order but is not reserved across orders running at the same time. The optional `customerRef` field was added to the reusable start example after the original tests; it is kept as context and does not change the route.
