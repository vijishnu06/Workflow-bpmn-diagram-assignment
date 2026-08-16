# BPMN Process Modeling — Assignment Submission

This repository contains three BPMN 2.0 process models, built using the basic BPMN building blocks: **Start Events**, **Tasks**, **Exclusive Gateways**, and **End Events**. Each model is a standalone `.bpmn` file (valid BPMN 2.0 XML) that can be opened and edited in any standard BPMN tool — e.g. [Camunda Modeler]

## Repository Structure

```
├── README.md
├── scenario1_leave_approval.bpmn
├── scenario2_purchase_order.bpmn
└── scenario3_it_service_request.bpmn
```

---

## Scenario 1: Employee Leave Approval
**File:** `scenario1_leave_approval.bpmn`

The process begins with a **Start Event** when the employee submits a leave request. The HR system then performs a **Task** to check the employee's leave balance. This feeds into an **Exclusive Gateway** ("Sufficient Balance?"):

- **No** → the system sends an insufficient-balance notification and the process ends (**End Event: Insufficient Balance**).
- **Yes** → the request is forwarded to the manager for approval (**Task**), which leads to a second **Exclusive Gateway** ("Manager Decision?"):
  - **Approved** → the system updates the employee's leave balance and sends an approval notification, ending at **End Event: Approved**.
  - **Rejected** → the system sends a rejection notification, ending at **End Event: Rejected**.

This models three distinct process outcomes (approved, rejected, insufficient balance), each with its own End Event, as required.

---

## Scenario 2: Online Purchase Order Processing
**File:** `scenario2_purchase_order.bpmn`

The process starts when a customer places an order (**Start Event**). The system checks product availability (**Task**), feeding into an **Exclusive Gateway** ("Product Available?"):

- **No** → the customer is notified the product is out of stock, and the process ends (**End Event: Out of Stock**).
- **Yes** → the system processes payment (**Task**), leading to a second **Exclusive Gateway** ("Payment Successful?"):
  - **Fail** → the customer is notified of the payment failure, ending the process (**End Event: Payment Failed**).
  - **Success** → the order is confirmed and prepared for shipment, then shipped, then a shipping confirmation is sent to the customer — ending at **End Event: Order Shipped**.

This demonstrates multiple process paths driven by two sequential decision points (availability, then payment), each with an appropriate terminating End Event.

---

## Scenario 3: IT Service Request
**File:** `scenario3_it_service_request.bpmn`

The process begins when an employee reports an IT problem (**Start Event**). The employee submits an IT support request (**Task**), the help desk registers the request (**Task**), and then checks the severity of the problem (**Task**), which feeds an **Exclusive Gateway** ("Severity Level?"):

- **Low** → the request is assigned to a support technician.
- **High** → the request is assigned to a senior technician.

Both branches converge on a common **Task** ("Technician Investigates Problem"), followed by another **Exclusive Gateway** ("Can be Resolved?"):

- **Yes** → the technician fixes the problem internally.
- **No** → the technician escalates the problem to the external service provider.

Both alternative resolution paths converge again, after which the help desk updates the request status, sends a resolution notification to the employee, and the process ends at a single **End Event: Request Closed**.

This models two independent decision points (severity, then resolvability) with converging (merging) paths,a common real-world BPMN pattern in addition to simple branching.
