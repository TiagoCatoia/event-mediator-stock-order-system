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

# C1 – Contexto

![C1 Context](docs/architecture/images/c1-context.png)

Representa o sistema e seus atores externos.

**Atores principais:**

- **Usuário CRUD** – manipula pedidos.
- **Administrador** – gerencia o sistema.
- **Sistema Acadêmico Simplificado** – pode consultar dados do pedido (mock).

---

# C2 – Containers

![C2 Containers](docs/architecture/images/c2-containers.png)

Contêineres do sistema:

- **API** (Spring Boot / .NET)
- **Order Module**
- **Stock Module**
- **Tracking Module** (simplificado)
- **Event Mediator**
- **Database**

---

# C3 – Componentes

### Pedidos (Order)

![C3 Order](docs/architecture/images/c3-components-orders.png)

### Estoque (Stock)

![C3 Stock](docs/architecture/images/c3-components-stocks.png)

### Event Mediator

![C3 Mediator](docs/architecture/images/c3-components-mediator.png)

---

# Execução e Desenvolvimento

### 1. Clone o repositório

```sh
git clone https://github.com/usuario/event-mediator-stock-order-system.git

cd event-mediator-stock-order-system
```
