# Bitcoin Blockchain Web Application

A full-stack web application that turns a simplified Java Bitcoin blockchain implementation into an interactive blockchain dashboard.

The project keeps the original blockchain fundamentals intact: blocks, transactions, RSA signatures, UTXOs, transaction validation, a pending transaction pool, and block mining. On top of that core logic, it adds a Spring Boot REST API and a React interface for creating wallets, sending transactions, mining blocks, and exploring the chain visually.

![Dashboard](Bitcoin%20screenshots/dashboard.png)

## Highlights

- Full-stack blockchain demo built with Spring Boot and React.
- Original Java blockchain logic preserved under `backend/src/main/java/com/blockchain/core`.
- RSA-backed wallet generation.
- UTXO-based balance calculation.
- Signed transactions with validation before entering the transaction pool.
- Block mining from pending transactions.
- Blockchain viewer with block details and transaction inspection.
- WebSocket-ready backend for live block and transaction notifications.
- Clean dashboard UI for portfolio and demo use.

## Screenshots

### Dashboard

![Dashboard overview](Bitcoin%20screenshots/dashboard.png)

![Dashboard activity](Bitcoin%20screenshots/dashboard%202.png)

### Blockchain Explorer

![Blockchain page](Bitcoin%20screenshots/blockchain.png)

![Blockchain details](Bitcoin%20screenshots/blockchains.png)

### Wallets

![Wallet manager](Bitcoin%20screenshots/wallets.png)

![Wallet balances](Bitcoin%20screenshots/wallets%201.png)

### Transactions

![Transactions page](Bitcoin%20screenshots/Transactions.png)

### Mining

![Mining page](Bitcoin%20screenshots/Mining.png)

## Tech Stack

### Backend

- Java 17
- Spring Boot
- Spring Web
- Spring WebSocket
- Jakarta Validation
- Maven

### Frontend

- React
- Vite
- React Router
- Axios
- STOMP + SockJS
- Lucide React
- Framer Motion

## Core Concepts Implemented

- Blocks with previous hash references
- Coinbase transactions
- SHA-256 block and transaction hashing
- RSA public/private key wallets
- Digital signature verification
- UTXO pool management
- Pending transaction pool
- Transaction validation
- Mining new blocks onto the max-height chain
- In-memory blockchain state

## Project Structure

```text
Bitcoin-BlockChainNew/
  backend/
    pom.xml
    src/main/java/com/blockchain/
      BitcoinBlockchainApplication.java
      config/
      controller/
      core/
      dto/
      service/
      util/

  frontend/
    package.json
    vite.config.js
    src/
      api/
      components/
      context/
      hooks/
      utils/

  Bitcoin screenshots/
```

## Getting Started

### Prerequisites

- Java 17 or newer
- Maven
- Node.js and npm

### 1. Run the Backend

```bash
cd backend
mvn spring-boot:run
```

The backend API will run on:

```text
http://localhost:8080
```

### 2. Run the Frontend

Open a second terminal:

```bash
cd frontend
npm install
npm run dev
```

The frontend will run on:

```text
http://localhost:5173
```

## API Endpoints

### Blockchain

```text
GET  /api/blockchain/status
GET  /api/blockchain/blocks
GET  /api/blockchain/blocks/{hash}
POST /api/blockchain/mine
```

### Wallets

```text
GET  /api/wallets
POST /api/wallets
GET  /api/wallets/{id}
GET  /api/wallets/{id}/utxos
```

### Transactions

```text
GET  /api/transactions/pending
GET  /api/transactions/{hash}
POST /api/transactions
```

## Example Workflow

1. Start the Spring Boot backend.
2. Start the React frontend.
3. Open the dashboard.
4. Create one or more wallets.
5. Send BTC from the genesis wallet to another wallet.
6. Mine a block.
7. Confirm that balances, pending transactions, and chain height update.

## Verification

The project was verified with:

```bash
cd backend
mvn clean compile
```

```bash
cd frontend
npm run build
```

An API smoke test was also completed for:

- Getting blockchain status
- Creating a wallet
- Creating a signed transaction
- Mining a block
- Confirming pending transactions are cleared after mining

## Important Notes

This is an educational blockchain implementation, not a production cryptocurrency.

- Data is stored in memory only.
- Restarting the backend resets the blockchain.
- There is no peer-to-peer network layer.
- There is no proof-of-work difficulty target.
- The focus is on blockchain data structures, UTXOs, signatures, validation, and full-stack visualization.

## Background

The original version was a simplified Java blockchain assignment inspired by concepts from Bitcoin and Cryptocurrency Technologies. This version wraps the original logic in a modern web application to make the behavior easier to explore, demonstrate, and present.
