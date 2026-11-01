# BPMN and Camunda assignments

This repository brings together the BPMN assignments completed for the course. The latest work is in [Camunda 8 Labs 1–3](camunda8-labs-1-to-3/): loan approval, order processing, and hospital appointments.

Each lab has its BPMN and DMN files, Java workers, test inputs, results, and a short report. The [screenshots](camunda8-labs-1-to-3/evidence/README.md) show the processes running in Camunda, including human reviews and recovery from failed jobs. All 16 main tests and nine additional demonstration runs completed.

## Where to find the work

| Folder or file | Contents |
|---|---|
| [camunda8-labs-1-to-3](camunda8-labs-1-to-3/) | The three practical labs, source code, reports, and test evidence |
| [ex3](ex3/) | Student project approval and logistics exception management |
| [scenario1_leave_approval.bpmn](scenario1_leave_approval.bpmn) | Employee leave approval |
| [scenario2_purchase_order.bpmn](scenario2_purchase_order.bpmn) | Online purchase order processing |
| [scenario3_it_service_request.bpmn](scenario3_it_service_request.bpmn) | IT service requests |

Open the `.bpmn` and `.dmn` files in Camunda Modeler. The practical labs have their own [setup instructions](camunda8-labs-1-to-3/README.md).

## Earlier BPMN exercises

**Leave approval:** The system checks the employee's leave balance before sending the request to a manager. Insufficient balance closes the request with a notification. If the manager approves, the balance is updated and an approval is sent; otherwise, a rejection is sent. The three outcomes have separate end events.

**Purchase order:** The process checks stock, then payment. An unavailable product or a failed payment ends the order with the appropriate notification. A successful payment leads to order confirmation, preparation, shipping, and a shipping confirmation.

**IT service request:** The help desk registers the problem and checks its severity. Low-severity cases go to a support technician, while high-severity cases go to a senior technician. After investigation, the problem is either fixed internally or sent to an external provider. Both routes return to a status update and a resolution notice before the request closes.
