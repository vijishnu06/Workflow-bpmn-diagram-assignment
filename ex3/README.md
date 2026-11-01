# Student project and logistics assignments

This folder has the two BPMN models for student project approval and logistics exception handling. Both were prepared for Camunda 8 and can be opened in Camunda Modeler.

## Files

```text
ex3/
|-- README.md
|-- Assignment_1_Student_Project_Approval.bpmn
`-- Assignment_2_Logistics_and_Shipment_Exception.bpmn
```

Both files are marked executable and include their diagram layout and Camunda 8 job types. Open either file in Modeler to follow the routes or check the task settings.

## Assignment 1 - Student Project Approval & Allocation

The process starts when a team submits a proposal. It checks the form, team rules, and topic similarity before sending the proposal to the committee. An approved proposal is matched with a guide. Once the guide accepts, the allocation is recorded and the team is notified.

| ID | What can go wrong and how the process handles it |
|---|---|
| F1 | Missing or invalid fields return a checklist for correction. The attempt counter limits resubmission to two tries before coordinator review. |
| F2 | Invalid team composition is recorded, the team is notified, and the case closes with an invalid-team outcome. |
| F3 | A high similarity score goes to coordinator review. The coordinator can clear it, request a topic change and repeat the check, or close it as a duplicate. |
| F4 | A 14-day timer on the submission task routes a late request to the coordinator. Approval reopens submission; denial closes the case. |
| F5 | Committee revision requests loop through student revision. Each revision has a 7-day timer, and no more than two cycles are allowed before a final approve-or-reject vote. |
| F6 | Rejection reasons are sent to the team. One new-topic submission is allowed; a later rejection closes the case. |
| F7 | A non-interrupting 2-day review timer sends a reminder. A further 1-day wait routes the delay to the HoD. |
| F8 | If automated matching finds no guide, the coordinator can assign manually, add a co-guide, or wait 7 days and retry matching. |
| F9 | The guide can accept or decline by message. A 3-day timer handles silence. Declined or silent guides are excluded; after three attempts the coordinator resolves the allocation manually. |
| F10 | Notification and record tasks retry up to three times. A BPMN error routes a failed notification or write to a coordinator for manual completion. |
| F11 | While awaiting the guide's response, a correlated withdrawal message triggers compensation to release the reserved guide slot, records the withdrawal, and closes the case. |

### How the model is arranged

- **User tasks** represent work done by students, coordinators, and the committee. The Faculty Guide responds through correlated messages so acceptance, decline, timeout, and withdrawal can race at an event-based gateway.
- **Service tasks** represent system work and publish notifications. Their `zeebe:taskDefinition` job types identify the workers that must perform the work.
- **Business rule tasks** run team/topic/guide rules through decision workers and return variables used by gateways.
- **Exclusive gateways** route mutually exclusive validation, committee, similarity, and guide outcomes. Explicit conditions and default flows are defined for each split.
- **Timers** represent submission, revision, review, waitlist, and guide response deadlines. A non-interrupting review timer preserves the committee task while reminders/escalation proceed.
- **Compensation** releases a guide reservation if the team withdraws after allocation has started.

## Assignment 2 - Logistics & Shipment Exception Management

The normal route starts with booking and parcel validation, followed by payment and pickup scheduling. After collection and scanning, parcel movement and a tracking update run in parallel. The process then handles delivery, proof of delivery, invoicing, and closure.

| ID | What can go wrong and how the process handles it |
|---|---|
| E1 | An absent shipper or parcel increments the pickup counter. Pickup is rescheduled up to two times; exhaustion cancels the booking and applies the cancellation fee. |
| E2 | Booking address/details can be corrected up to two times. If an address problem appears at the door, the agent contacts the recipient; an approved change is priced and delivery is retried, otherwise the parcel is returned. |
| E3 | Hub or doorstep damage goes through insurance and claim assessment. The remedy can be refund, replacement/reshipment, or return. Uninsured damage is recorded and reported; unsafe parcels are returned. |
| E4 | A 6-hour movement timer checks the last-scan age. At 48 hours without a scan, tracing waits for a found message or a 5-day timer. An unlocated parcel is declared lost and compensated. |
| E5 | A carrier delay message or 6-hour no-update timer triggers customer notification, rerouting or alternate capacity, and ETA updates before movement monitoring resumes. |
| E6 | A customs hold requests documents and waits for a correlated response. A 5-day boundary timer returns the parcel to origin if documents do not arrive. |
| E7 | Delivery attempts are counted and limited to three. The parcel is then held at the depot for 7 days; a redelivery request resumes delivery, while timeout returns it to sender. |
| E8 | Refusal or failed cash-on-delivery adjusts charges, returns the parcel, and closes the case with the refusal/COD outcome. |
| E9 | A scan error boundary event sends the hub supervisor to record the scan manually and reconcile tracking records. |
| E10 | While the transit event gateway is active after dispatch, a cancellation message stops movement, returns the parcel, and triggers fee compensation less the dispatch charge. |

### How the model is arranged

- **User tasks** cover shipper corrections, pickup, proof of delivery, recipient confirmation, and claims decisions.
- **Business rule tasks** validate bookings and determine insurance eligibility. **Service tasks** schedule and route work, update scans, handle claims, and close invoices. **Send tasks** publish notifications/tracking updates; the **receive task** waits for customs documents.
- The **parallel gateway** starts physical movement and tracking/shipper notification together, then joins them before last-mile handling.
- **Exclusive gateways** select a single outcome such as damaged/intact, insured/uninsured, available/unavailable, or delivery result.
- **Event-based gateways** wait for the first movement, delay, cancellation, parcel-found, or redelivery event that occurs. Timer catches model the relevant service deadlines.
- **Compensation** reverses the shipment fee, retaining the dispatch charge, when a dispatched shipment is cancelled.

## Before running these models

Deploying these files creates the process definitions. To run them from start to finish, workers are needed for the service, business rule, and send tasks. This older exercise folder contains the diagrams and the variables they expect, but no worker application or forms. Register workers for the job types in the XML and set up the user-task forms and assignments before running a full case.

Start Assignment 1 with a unique `proposalId` and variables used by its gateways, such as `formValid`, `teamRulesValid`, `proposalAttempts`, `similarityScore`, `similarityThreshold`, `similarityDisposition`, `committeeDecision`, `revisionCycles`, `finalCommitteeDecision`, `newTopicUsed`, `guideAvailable`, `guideAllocationPlan`, `guideAttempts`, and `manualGuideAvailable`. Workers update the counters and decision variables before the process reaches the corresponding gateways.

Start Assignment 2 with a unique `shipmentId`, `shipmentOutcome="continue"`, and shipment inputs. Workers supply routing variables including `bookingValid`, `bookingIssue`, `bookingCorrections`, `pickupOutcome`, `pickupAttempts`, `damageFound`, `damageStage` (`hub` or `delivery`), `insured`, `claimRemedy`, `safeToContinue`, `customsRequired`, `hoursSinceLastScan`, `moreHubLegs`, `deliveryOutcome`, `deliveryAttempts`, and `addressChangeApproved`.

Messages are correlated using `proposalId` or `shipmentId` as shown in the BPMN message subscriptions. Message producers must use the exact message names in the XML. The logistics parallel gateway waits for both the movement path and the independent pickup tracking/notification path to finish before last-mile processing begins.

Candidate groups are not fixed in the files. Set them to the groups used in your own Camunda environment.
