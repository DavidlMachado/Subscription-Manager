# 📋 Subscription-Manager — Gestor Centralizado de Subscrições e Contratos

> Projeto de aprendizagem fullstack com Spring Boot + React para monitorizar subscrições recorrentes, controlar custos e evitar desperdício financeiro.

---

## 📌 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Configuração](#instalação-e-configuração)
- [Variáveis de Ambiente](#variáveis-de-ambiente)
- [Executar o Projeto](#executar-o-projeto)
- [Estrutura de Pastas](#estrutura-de-pastas)
- [API REST — Endpoints](#api-rest--endpoints)
- [Agendamento de Tarefas](#agendamento-de-tarefas)
- [Conversão de Moedas](#conversão-de-moedas)
- [Dashboard e Gráficos](#dashboard-e-gráficos)
- [Roadmap](#roadmap)
- [Aprendizagens do Projeto](#aprendizagens-do-projeto)
- [Licença](#licença)

---

## Sobre o Projeto

O **SubTracker** é uma aplicação fullstack pessoal (ou empresarial) para centralizar e monitorizar todos os pagamentos recorrentes — desde Netflix e ginásio até licenças de ferramentas cloud como AWS, GitHub Copilot ou Figma.

### O problema que resolve

É fácil esquecer subscrições ativas. Com o tempo, esses custos acumulam-se silenciosamente. Esta aplicação envia alertas antes das renovações, apresenta um dashboard visual dos gastos por categoria e normaliza todos os valores para uma moeda base, mesmo que algumas subscrições sejam em dólares ou libras.

---

## Funcionalidades

- ✅ **CRUD de subscrições** — adicionar, editar, pausar e remover contratos
- 🔔 **Alertas de renovação** — notificações automáticas X dias antes do débito
- 📊 **Dashboard de gastos** — gráficos por categoria, por mês e por moeda
- 💱 **Conversão de moedas** — integração com API externa de taxas de câmbio (EUR/USD/GBP)
- 🗓️ **Jobs agendados** — verificação diária automática de datas de renovação
- 🏷️ **Categorização** — Entretenimento, Produtividade, Cloud, Saúde, etc.
- 📱 **Interface responsiva** — funciona em desktop e mobile

---

## Tecnologias Utilizadas

### Backend

| Tecnologia | Versão | Propósito |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.x | Framework web |
| Spring Data JPA | — | Persistência de dados (ORM) |
| Spring Scheduler | — | Agendamento de jobs diários |
| Quartz Scheduler | 2.x | (alternativa) Jobs mais complexos |
| H2 Database | — | Base de dados em memória (desenvolvimento) |
| PostgreSQL | 15+ | Base de dados em produção |
| Maven | 3.x | Gestão de dependências |
| Lombok | — | Redução de boilerplate |
| MapStruct | — | Mapeamento DTO ↔ Entity |

### Frontend

| Tecnologia | Versão | Propósito |
|---|---|---|
| React | 18 | Framework UI |
| Vite | 5.x | Build tool e dev server |
| React Router | 6 | Navegação SPA |
| Axios | — | Chamadas HTTP à API |
| Recharts | — | Gráficos e visualizações |
| TailwindCSS | 3.x | Estilização |
| React Hook Form | — | Gestão de formulários |
| date-fns | — | Manipulação de datas |

### Ferramentas e DevOps

| Ferramenta | Propósito |
|---|---|
| Docker + Docker Compose | Ambiente de desenvolvimento containerizado |
| Git + GitHub | Controlo de versões |
| Postman / Insomnia | Testes de API |
| IntelliJ IDEA | IDE recomendada para o backend |
| VS Code | IDE recomendada para o frontend |

---

## Arquitetura

```
┌─────────────────────────────────────────────────────────┐
│                     FRONTEND (React)                    │
│   Dashboard │ Lista de Subscrições │ Formulários         │
│                   Recharts (gráficos)                   │
└──────────────────────┬──────────────────────────────────┘
                       │ HTTP / REST (JSON)
                       ▼
┌─────────────────────────────────────────────────────────┐
│                  BACKEND (Spring Boot)                  │
│                                                         │
│  Controllers → Services → Repositories → JPA Entities  │
│                                                         │
│  ┌──────────────────┐    ┌─────────────────────────┐   │
│  │  Scheduled Jobs  │    │  Exchange Rate Service  │   │
│  │  (@Scheduled /   │    │  (API externa de câmbio)│   │
│  │   Quartz)        │    └─────────────────────────┘   │
│  └──────────────────┘                                   │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│              PostgreSQL (ou H2 em dev)                  │
└─────────────────────────────────────────────────────────┘
```

### Padrão de Camadas (Backend)

```
controller/     → Recebe pedidos HTTP, valida DTOs, chama o service
service/        → Lógica de negócio (regras, cálculos, orquestração)
repository/     → Acesso à base de dados via Spring Data JPA
model/entity/   → Entidades JPA (mapeadas para tabelas)
model/dto/      → Data Transfer Objects (o que entra e sai pela API)
scheduler/      → Jobs agendados com @Scheduled ou Quartz
client/         → Clientes HTTP para APIs externas (taxas de câmbio)
config/         → Configurações (CORS, segurança, Quartz, etc.)
```

---

## Pré-requisitos

Certifica-te que tens instalado:

- [Java 21+](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/)
- [Node.js 20+](https://nodejs.org/) e npm
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) *(opcional, mas recomendado)*
- [Git](https://git-scm.com/)

---

## Instalação e Configuração

### 1. Clonar o repositório

```bash
git clone https://github.com/teu-username/subtracker.git
cd subtracker
```

### 2. Base de dados com Docker (recomendado)

```bash
docker compose up -d
```

Isto inicia um contentor PostgreSQL na porta `5432` e um pgAdmin na porta `5050`.

### 3. Configurar o Backend

```bash
cd backend
cp src/main/resources/application.example.properties src/main/resources/application.properties
# Edita o ficheiro com as tuas configurações (ver secção abaixo)
mvn spring-boot:run
```

### 4. Configurar o Frontend

```bash
cd frontend
npm install
cp .env.example .env
# Edita o .env com o URL da API
npm run dev
```

A aplicação estará disponível em:
- **Frontend:** http://localhost:5173
- **Backend API:** http://localhost:8080
- **H2 Console (dev):** http://localhost:8080/h2-console

---

## Variáveis de Ambiente

### Backend — `application.properties`

```properties
# Base de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/subtracker
spring.datasource.username=postgres
spring.datasource.password=postgres

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# API de taxas de câmbio (https://exchangeratesapi.io/ — plano gratuito)
exchange.api.key=A_TUA_CHAVE_AQUI
exchange.api.base-currency=EUR

# Alertas — quantos dias antes da renovação enviar notificação
alerts.days-before-renewal=7

# CORS
cors.allowed-origins=http://localhost:5173
```

### Frontend — `.env`

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

---

## Executar o Projeto

### Modo desenvolvimento (sem Docker)

```bash
# Terminal 1 — Backend
cd backend && mvn spring-boot:run

# Terminal 2 — Frontend
cd frontend && npm run dev
```

### Modo Docker Compose (tudo junto)

```bash
docker compose up --build
```

### Testes

```bash
# Backend
cd backend && mvn test

# Frontend
cd frontend && npm run test
```

---

## Estrutura de Pastas

```
subtracker/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/subtracker/
│   │   │   │   ├── controller/
│   │   │   │   │   ├── SubscriptionController.java
│   │   │   │   │   └── CategoryController.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── SubscriptionService.java
│   │   │   │   │   ├── AlertService.java
│   │   │   │   │   └── ExchangeRateService.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── SubscriptionRepository.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── entity/
│   │   │   │   │   │   ├── Subscription.java
│   │   │   │   │   │   └── Category.java
│   │   │   │   │   └── dto/
│   │   │   │   │       ├── SubscriptionDTO.java
│   │   │   │   │       └── SubscriptionRequestDTO.java
│   │   │   │   ├── scheduler/
│   │   │   │   │   └── RenewalCheckJob.java
│   │   │   │   ├── client/
│   │   │   │   │   └── ExchangeRateClient.java
│   │   │   │   └── config/
│   │   │   │       └── CorsConfig.java
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── application.example.properties
│   │   └── test/
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── Dashboard/
│   │   │   ├── SubscriptionList/
│   │   │   ├── SubscriptionForm/
│   │   │   └── Alerts/
│   │   ├── pages/
│   │   │   ├── Home.jsx
│   │   │   ├── Subscriptions.jsx
│   │   │   └── Analytics.jsx
│   │   ├── services/
│   │   │   └── api.js
│   │   ├── hooks/
│   │   └── App.jsx
│   ├── .env.example
│   └── package.json
│
├── docker-compose.yml
└── README.md
```

---

## API REST — Endpoints

Base URL: `http://localhost:8080/api`

### Subscrições

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/subscriptions` | Listar todas as subscrições |
| `GET` | `/subscriptions/{id}` | Obter uma subscrição por ID |
| `POST` | `/subscriptions` | Criar nova subscrição |
| `PUT` | `/subscriptions/{id}` | Atualizar subscrição |
| `DELETE` | `/subscriptions/{id}` | Remover subscrição |
| `GET` | `/subscriptions/upcoming` | Listar renovações próximas |
| `GET` | `/subscriptions/summary` | Totais por moeda e categoria |

### Categorias

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/categories` | Listar categorias |
| `POST` | `/categories` | Criar categoria |

### Taxas de Câmbio

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/exchange-rates` | Taxas atuais (cache de 1h) |

### Exemplo de payload — `POST /subscriptions`

```json
{
  "name": "Netflix",
  "description": "Plano Standard com anúncios",
  "amount": 7.99,
  "currency": "EUR",
  "billingCycle": "MONTHLY",
  "nextRenewalDate": "2024-08-15",
  "categoryId": 1,
  "active": true
}
```

---

## Agendamento de Tarefas

O coração do projeto no backend é o **job de verificação de renovações**, que corre automaticamente todos os dias.

### Com `@Scheduled` (simples)

```java
@Component
public class RenewalCheckJob {

    @Scheduled(cron = "0 0 9 * * *") // Todos os dias às 9h
    public void checkUpcomingRenewals() {
        // Busca subscrições com renovação nos próximos 7 dias
        // Cria alertas ou envia notificações
    }
}
```

Para ativar, adiciona no `application.properties`:
```properties
spring.task.scheduling.enabled=true
```

### Com Quartz (avançado)

O Quartz permite persistência de jobs, gestão de falhas e agendamentos mais complexos. Ver a pasta `scheduler/` para a implementação detalhada.

---

## Conversão de Moedas

Integração com a [ExchangeRates API](https://exchangeratesapi.io/) (plano gratuito — 250 chamadas/mês).

- As taxas são **armazenadas em cache** durante 1 hora para não esgotar a quota gratuita.
- Todos os valores são normalizados para EUR no dashboard, mantendo o valor original na base de dados.
- Se a API estiver indisponível, usa as últimas taxas conhecidas como fallback.

---

## Dashboard e Gráficos

O frontend usa **Recharts** para visualizar os dados:

| Gráfico | Tipo | Dados |
|---|---|---|
| Gastos por Categoria | `PieChart` | Distribuição percentual |
| Evolução Mensal | `AreaChart` | Gastos ao longo do ano |
| Custos por Moeda | `BarChart` | EUR vs USD vs GBP |
| Próximas Renovações | `Timeline` | Calendário dos próximos 30 dias |

---

## Roadmap

- [x] Estrutura base do projeto (Spring Boot + React)
- [ ] CRUD de subscrições
- [ ] Integração com base de dados (JPA + PostgreSQL)
- [ ] Job agendado com `@Scheduled`
- [ ] Integração com API de taxas de câmbio
- [ ] Dashboard com Recharts
- [ ] Sistema de alertas
- [ ] Autenticação (Spring Security + JWT)
- [ ] Notificações por email (Spring Mail)
- [ ] Migração para Quartz Scheduler
- [ ] Testes unitários (JUnit 5 + Mockito)
- [ ] Testes de integração
- [ ] Deploy (Railway / Render / Fly.io)
- [ ] PWA para notificações no telemóvel

---

## Aprendizagens do Projeto

Este projeto foi concebido como exercício de aprendizagem. Os principais conceitos explorados:

### Spring Boot
- Arquitetura em camadas (Controller → Service → Repository)
- Spring Data JPA e mapeamento de entidades
- DTOs e MapStruct para separar camadas
- `@Scheduled` e Quartz para jobs automáticos
- `RestTemplate` / `WebClient` para consumir APIs externas
- Tratamento de exceções com `@ControllerAdvice`
- Configuração de CORS

### React
- Gestão de estado com `useState` e `useReducer`
- Chamadas assíncronas com `useEffect` e Axios
- Roteamento com React Router v6
- Criação de custom hooks
- Integração de bibliotecas de gráficos (Recharts)
- Gestão de formulários com React Hook Form

---

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

---

<p align="center">
  Feito com ☕ e muitas horas de depuração.
</p>
