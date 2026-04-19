# 🚚 SmartDelivery - Event-Driven Delivery System

![Java](https://img.shields.io/badge/java-21-red?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/spring_boot-brightgreen?style=for-the-badge&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/postgresql-database-blue?style=for-the-badge&logo=postgresql)
![RabbitMQ](https://img.shields.io/badge/rabbitmq-event--driven-orange?style=for-the-badge&logo=rabbitmq)
![Docker](https://img.shields.io/badge/docker-containerization-blue?style=for-the-badge&logo=docker)
![AWS](https://img.shields.io/badge/aws-cloud-black?style=for-the-badge&logo=amazonaws)
![JWT](https://img.shields.io/badge/JWT-security-black?style=for-the-badge&logo=jsonwebtokens)

---

## 📌 Sobre o projeto

O **SmartDelivery** é uma API backend desenvolvida com **Spring Boot**, que simula um sistema moderno de **gestão de pedidos e entregas**, utilizando uma abordagem **event-driven** com mensageria.

A aplicação foi projetada para representar um fluxo real de negócio, onde a criação de pedidos é processada de forma **assíncrona**, garantindo maior **escalabilidade, resiliência e desacoplamento**.

---

## 🎯 Objetivo

Demonstrar na prática conceitos avançados de backend:

- Arquitetura modular inspirada em DDD
- Processamento assíncrono com RabbitMQ
- Segurança com JWT e Spring Security
- Integração com serviços externos
- Deploy em ambiente cloud (AWS)
- Boas práticas de código e organização

---

# 🧠 Fluxo principal do sistema

O sistema é baseado em um fluxo orientado a eventos:

**CreateOrder → AssignDeliveryMan → CompleteOrder**

### 🔹 1. Criação do pedido

- Cliente autenticado cria um pedido com itens
- Pedido é persistido
- Evento é publicado no RabbitMQ

---

### 🔹 2. Designação de entregador (ASSÍNCRONO)

Consumer executa:

- Busca o primeiro entregador disponível
- Atualiza:
  - Pedido → `EM_ROTA`
  - DeliveryMan → `isAvailable = false`

#### ⚠️ Tratamento de falhas:

- Sem entregador disponível:
  - Evento vai para fila de **retry (3 tentativas com TTL)**
- Após falhas:
  - Evento direcionado para **DLQ (Dead Letter Queue)**

---

### 🔹 3. Finalização do pedido

- Pedido → `ENTREGUE`
- Entregador é liberado (`isAvailable = true`)

---

# 📡 Arquitetura orientada a eventos

O projeto utiliza **RabbitMQ (CloudAMQP)** com:

- Exchanges
- Routing Keys
- Filas dedicadas
- Retry com TTL
- Dead Letter Queue (DLQ)
- Idempotência no consumo de mensagens

---

# 🏗️ Arquitetura do projeto

O sistema segue uma abordagem de:

- **Monólito Modular**
- **Clean Architecture**
- **Domain-Driven Design (DDD)**

---

## 🔹 Organização por domínios
modules
