# Coffee Service Platform

Web application for a distributed coffee vending infrastructure.

The project is designed as a software architecture and distributed-systems project, with  attention to separation of concerns, modularity, communication between components and maintainability.

<br>

---
# Architecture

The application follows a microservices architecture, in which the backend is split in two independent services with different responsibilities. <br>
Following the separation of concerns principle, each component has a clearly defined responsibility. <br> Frontend handles presentation and user interactions,  backend services handle application logic and infrastructure communication. <br>
Within each backend service, controllers, business logic, entities and repositories are kept separate.

```text
                          ┌──────────────────────────┐
                          │        Frontend          │
                          │                          │
                          │  ┌────────────────────┐  │
                          │  │ Customer Interface │  │
                          │  ├────────────────────┤  │
                          │  │ Vending Machine UI │  │
                          │  ├────────────────────┤  │
                          │  │ Maintenance UI     │  │
                          │  ├────────────────────┤  │
                          │  │ Management UI      │  │
                          │  └────────────────────┘  │
                          └────────────┬─────────────┘
                                       │
                         HTTP / REST communication
                                       │
                    ┌──────────────────┴──────────────────┐
                    │                                     │
                    ▼                                     ▼
       ┌─────────────────────────┐         ┌───────────────────────────┐
       │     Main Service        │         │  Vending Monitor Service  │
       │    Java Spring Boot     │         │          Jakarta          │
       │                         │         │                           │
       │ Controllers             │         │ Controllers               │
       │ Business Logic          │         │ Business Logic            │
       │ Entities                │         │ Entities                  │
       │ Repositories            │         │ Repositories              │
       └────────────┬────────────┘         └──────────────┬────────────┘
                    │                                     │
                    ▼                                     ▼ 
       ┌─────────────────────────┐               ┌─────────────────────┐  
       │      Main Database      │◄──────────────│  Monitor Database   │
       │                         │ Periodic sync │                     │
       │ Customers               │               │ Machine state       │
       │ Maintenance Personnel   │               │ Machine position    │
       │ Vending Machines        │               └─────────────────────┘
       └─────────────────────────┘                                         
```


<br>

# Frontend

The frontend consists of four separate HTML/CSS/JavaScript interfaces. The interfaces are designed to simulate the user experience of interacting directly with a physical vending machine and the corporate administration.

### 1. Customer Interface

The customer-facing interface allows users to manage their available balance,
 deposit money, connect to a vending machine.


### 2. Vending Machine Interface

This interface represents the interactive display of an individual vending machine.
It provides available product selections, product availability, machine status,  interaction with the customer, select and purchase a product.


### 3. Maintenance Interface

The maintenance dashboard is intended for maintenance personnel. It provides visibility into the operational status of vending machines, including available stock, machine status, detected failures, machines requiring maintenance and operational information received from the monitoring service.


### 4. Management Interface

The management dashboard provides centralized company-level management capabilities.
It allows authorized personnel to register new vending machines, add maintenance personnel, visualize company information and manage the overall vending-machine fleet.

<br>

# Backend

The backend is divided into two independent microservices.<br>
Both services follow a Layered Architecture, keeping HTTP/API concerns, business logic, domain entities and persistence mechanisms separated.
## Microservice 1 — Main Service

**Technology:** Java + Spring Boot <br>
**Persistence:** MySQL

The Main Service is responsible for the application's central business domain and persistent company data. <br>
Its main responsibilities include:

* customer management;
* maintenance personnel management;
* company data management;
* vending-machine registration;
* coordination of persistent data;
* exposing APIs consumed by the frontend;
* synchronization with the vending-machine monitoring service.


## Microservice 2 — Vending Machine Monitoring Service

**Technology:** Java + Jakarta <br>
**Persistence:** MongoDB

The second microservice has a deliberately narrow responsibility: monitor the operational state of vending machines. <br>
Each vending machine periodically sends a heartbeat to the monitoring service,  allowing the system to determine whether a vending machine is still communicating with the infrastructure. A missing or delayed heartbeat can therefore be used to identify machines that may require investigation or maintenance.



## Inter-Service Synchronization

The two backend services periodically synchronize information.
The monitoring service:

1. receives heartbeat messages from vending machines;
2. updates the current machine state;
3. periodically communicates with the Main Service;
4. sends updated machine-state information;
5. receives changes originating from the central management system.



```text
                 ┌───────────────────────┐
                 │     Main Service      │
                 │                       │
                 │ Central business data │
                 └───────────────────────┘
                             ▲
                             │
                    Periodic synchronization
                             │
                             ▼
                 ┌───────────────────────┐
                 │ Monitoring Service    │
                 │                       │
                 │ Current machine state │
                 └───────────┬───────────┘
                             ▲
                             │
                          heartbeat
                             │
                    ┌────────┴────────┐
                    │                 │
               Machine A          Machine N
```





<br>

---

# Technology Stack

| Component             | Technology           |
| --------------------- | -------------------- |
| Frontend              | HTML5                |
| Styling               | CSS3                 |
| Client-side logic     | JavaScript           |
| Main Backend          | Java                 |
| Main Framework        | Spring Boot          |
| Monitoring Backend    | Java                 |
| Monitoring Framework  | Jakarta              |
| Architecture          | Microservices        |
| Internal architecture | Layered Architecture |
| Communication         | HTTP / REST          |
| Persistence           | MySQL / MongoDB     |

<br>

Although the application is built around a coffee vending platform, the primary objective of the project is to demonstrate how  software can be structured to address real-world operational requirements through clear boundaries, independent services, and coordinated system behavior.





