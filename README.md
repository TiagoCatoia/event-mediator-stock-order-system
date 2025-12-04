# event-mediator-stock-order-system

Sistema simples baseado em **eventos + mediador**, composto pelos módulos:

- **Pedidos (Order)**
- **Estoque (Stock)**
- **Rastreamento (Tracking)**
- **Event Mediator** (publicação e manipulação de eventos internos)

---

# Arquitetura

A documentação segue o modelo **C4**:

- **C1 – Contexto**
- **C2 – Containers**
- **C3 – Componentes**
- **ADRs – Architectural Decision Records**

---

# ADR 001 — Arquitetura Baseada em Eventos

Local:  
`docs/architecture/adr/ADR-001-architecture-style.md`

---

# Execução e Desenvolvimento

### 1. Clone o repositório

```sh
git clone https://github.com/TiagoCatoia/event-mediator-stock-order-system.git

cd event-mediator-stock-order-system
```

### 2. Inicie o docker

```sh
docker compose up
```

### 3. Inicie a aplicação

