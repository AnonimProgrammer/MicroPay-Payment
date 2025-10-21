# 💳 MicroPay - Payment Service

**MicroPay-Payment** is the **heart** of MicroPay’s transaction flow ecosystem — a distributed, event-driven **Wallet Application**.

It manages and coordinates all payment-related transactions through **Saga Orchestration** and asynchronous communication between multiple microservices.

---

## 🧩 System Context

**MicroPay** consists of six independent microservices:

| Service | Description |
|----------|-------------|
| Gateway | Central API gateway for routing and load balancing |
| Security | Handles authentication and authorization |
| **Payment** | Orchestrates the payment lifecycle |
| Wallet | Maintains wallet balance and reservations |
| Transaction | Records and tracks transaction states |
| Notification | Sends notifications to users |

> Four services — **Payment**, **Wallet**, **Transaction**, and **Notification** — communicate asynchronously via **RabbitMQ**.

---

## ⚙️ Core Responsibilities

The **Payment Service** acts as the **central coordinator** for all transaction workflows.  
It:
- Initiates, validates, and processes payment requests.  
- Implements **Saga orchestration** to ensure distributed consistency.  
- Coordinates asynchronous events across other microservices.  
- Handles failure recovery and compensation logic.

---

## 🔌 REST Endpoints

| Endpoint | Method | Description |
|-----------|---------|-------------|
| `/transfer` | `POST` | Wallet → Wallet transaction |
| `/top-up` | `POST` | External Provider → Wallet transaction |
| `/withdraw` | `POST` | Wallet → External Provider transaction |

---

## 🧠 Internal Design

To ensure scalability and extensibility, the Payment Service uses the **Strategy Pattern** for payment processing.

- **Three core processor services:**
  - `TopUpProcessorService`
  - `TransferProcessorService`
  - `WithdrawalProcessorService`

All implement:
```java
void processRequest(PaymentRequest request);
Two of them (TopUpProcessorService, WithdrawalProcessorService) use a strategy registry:
Map<PaymentKey, Processor> registry;
This allows automatic processor selection based on payment source and destination type.
New payment types can be added by simply registering a new processor — no changes to existing code.
🐇 Asynchronous Flow (Simplified)
Payment Service initiates the transaction cycle.
Coordinates processing and validation events.
Updates wallet and transaction states asynchronously.
Sends success/failure notifications at completion.
🚀 Deployment
All MicroPay services are:
Built with Gradle
Containerized using Docker
Deployed and tested in Google Cloud Platform (GCP)
Designed for Kubernetes orchestration
🧰 Tech Stack
Java 17
Spring Boot
RabbitMQ
PostgreSQL
Docker & Kubernetes
Gradle
JUnit & Mockito
⚡ Quick Start (Local)
# Clone repository
git clone https://github.com/yourusername/MicroPay-Payment.git
cd MicroPay-Payment

# Build
./gradlew build

# Run (with environment variables)
export RABBITMQ_HOST=localhost
export POSTGRES_URL=jdbc:postgresql://localhost:5432/paymentdb
export POSTGRES_USER=postgres
export POSTGRES_PASSWORD=postgres

./gradlew bootRun
📖 Documentation
Official system documentation is available in the main MicroPay Documentation Repository.
🪪 License
This project is licensed under the MIT License.
💡 Author
Omar Ismailov — Software Engineer | Backend & System Design Enthusiast
Building reliable systems with simplicity and architecture in mind.
