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

Este projeto foi desenvolvido com foco em simular cenários reais de **sistemas distribuídos**, abordando desafios como **consistência eventual, processamento assíncrono e resiliência a falhas**.

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

modules/
   ├── customers
   ├── deliveryman
   ├── orders
   ├── products
   └── users

Cada módulo encapsula sua própria lógica de negócio.

---

## 🔹 Estrutura interna (ex: orders)

orders/
   ├── consumers
   ├── controller
   ├── dto
   ├── entity
   ├── mapper
   ├── repository
   └── usecases

---

## 🔹 Fluxo interno

HTTP → Controller → UseCase → Repository → Database
→ Event Publisher (RabbitMQ)
→ Event Consumer

---

## 🔹 Benefícios

- Baixo acoplamento
- Alta coesão
- Escalabilidade
- Preparado para microserviços
- Processamento assíncrono robusto

---

# 🔐 Segurança

A aplicação implementa:

- **JWT Authentication (OAuth2)**
- **Spring Security**
- Controle de acesso por roles:
  **ADMIN**
  **CUSTOMER**
- Proteção de endpoints
- Validação de requisições autenticadas

---

# 🧰 Tecnologias utilizadas

- Java 21  
- Spring Boot  
- Spring Security  
- JWT (OAuth2)  
- RabbitMQ (CloudAMQP em produção e instância local via Docker para desenvolvimento)
- PostgreSQL  
- Flyway    
- OpenFeign  
- Docker  
- AWS (EC2 + RDS)  
- Swagger / OpenAPI  

---

# 🗄️ Banco de dados

Entidades principais:

- Orders  
- OrderItems  
- Users  
- Customers  
- DeliveryMan  
- Products  

### 🔹 Relacionamentos

- Order → OrderItems  
- OrderItems → Products  
- Order → Customer  
- Order → DeliveryMan  

Uso de:

- JPA/Hibernate
- Cascade
- Fetch strategies
- Orphan Removal

---

# ❗ Regras de negócio e exceções

O sistema trata cenários como:

- `NoDeliveryManAvailableException`
- `DeliveryManNotAssignedException`
- `InvalidOrderStatusException`
- `OrderEmptyException`

---

# 📦 Padrão de respostas da API

A API utiliza um padrão unificado de respostas através do DTO `ApiResponse`, garantindo previsibilidade tanto em casos de sucesso quanto de erro.

### 🔹 Estrutura

- `status` → "success" ou "error"  
- `message` → mensagem descritiva da operação  
- `data` → payload da resposta  
- `timestamp` → momento da resposta  

### 🔹 Exemplo de sucesso

```json
{
  "status": "success",
  "message": "Order created successfully",
  "data": { ... },
  "timestamp": "2026-04-19T12:00:00"
}
```

### 🔹 Exemplo de erro

```json
{
  "status": "error",
  "message": "No delivery man available",
  "data": { ... },
  "timestamp": "2026-04-19T12:00:00"
}
```

Esse padrão facilita a integração com **frontends** e outros serviços.

---

# 🧪 Testes

- Testes unitários focados nas regras de negócio  
- Estrutura preparada para testes de integração  

---



# ⚙️ Como executar o projeto

## 1️⃣ Clonar repositório

```bash
git clone https://github.com/fcolucasvieira/smartdelivery.git
cd smartdelivery
```

## 2️⃣ Configurar variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
DB_URL=jdbc:postgresql://localhost:5433/smartdelivery
DB_USER=postgres
DB_PASSWORD=postgres

POSTGRES_DB=smartdelivery
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

PGADMIN_DEFAULT_EMAIL=admin@admin.com
PGADMIN_DEFAULT_PASSWORD=admin

RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USER=guest
RABBITMQ_PASSWORD=guest
```

### 🔹 Serviços disponíveis

Após subir o docker-compose, os seguintes serviços estarão disponíveis:

- PostgreSQL → localhost:5433  
- RabbitMQ → localhost:5672  
- RabbitMQ Management → http://localhost:15672  
  user: guest  
  password: guest  
- pgAdmin → http://localhost:5050  

## 3️⃣ Subir dependências

```bash
docker-compose up -d
```

## 4️⃣ Configurar aplicação 

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}

  rabbitmq:
    host: ${RABBITMQ_HOST}
    port: ${RABBITMQ_PORT}
    username: ${RABBITMQ_USER}
    password: ${RABBITMQ_PASSWORD}
```

## 5️⃣ Executar

```bash
mvn spring-boot:run
```

## 🔍 Notas sobre o ambiente

> ⚠️ O **docker-compose** é destinado apenas para ambiente local de desenvolvimento.  
> Em produção, a aplicação utiliza AWS (EC2 + RDS) e RabbitMQ via CloudAMQP.

---

## 📚 Documentação da API

A documentação interativa da API está disponível via **Swagger**:

👉 http://34.207.210.137:8080/swagger-ui/index.html

---

## ☁️ Deploy

Aplicação deployada utilizando:
- AWS EC2 (aplicação)
- AWS RDS (PostgreSQL)
- Docker

---

## 🚀 Diferenciais do projeto

- Arquitetura modular inspirada em **DDD**
- Sistema orientado a eventos (event-driven)
- Uso avançado de RabbitMQ (retry + DLQ + idempotência)
- Integração com APIs externas (OpenFeign)
- Deploy em cloud (AWS)
- Segurança completa com JWT
- Projeto baseado em fluxo real de negócio

---

## 👨‍💻 Autor

**Lucas Vieira**
Estudante de Engenharia de Computação – UFC Sobral

GitHub:
https://github.com/fcolucasvieira
