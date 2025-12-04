# ADR: Uso do Circuit Breaker no OrderProcessor
```
Status: Aceito
```

## Contexto
O `OrderProcessor` é responsável por processar cancelamento de pedidos. Durante esse processamento, o método `orderService.cancelOrder()` pode falhar por diversos motivos:
- Falhas de rede ou latência alta
- Serviços downstream indisponíveis (API de estoque, banco de dados)
- Sobrecarga temporária do sistema

Sem um mecanismo de resiliência, essas falhas podem causar:
1. **Consumo excessivo de recursos**: Tentativas repetidas de processar mensagens fadadas ao fracasso consomem memória, threads e conexões do pool
2. **Cascata de falhas**: Se o serviço está com problemas, continuar enviando requisições pode piorar ainda mais sua condição
3. **Experiência degradada**: Mensagens ficam presas na fila aguardando processamento que não ocorrerá
4. **Falta de visibilidade**: Sem diferenciação entre falhas transitórias e persistentes, é difícil tomar ações apropriadas

## Decisão
Implementamos o padrão Circuit Breaker usando Resilience4j com dois métodos de fallback distintos:

```java
// Fallback quando circuit está OPEN (falhas persistentes)
public void fallbackCancelOrder(CancelOrderCommand command, CallNotPermittedException ex) {
    // Ação compensatória: devolver dinheiro ao cliente
}

// Fallback quando circuit está CLOSED (falhas transitórias)
public void fallbackCancelOrder(CancelOrderCommand command, Throwable throwable) {
    // Retry: reenvia mensagem para a fila
}
```

## Consequências
### Positivas
- **Fail Fast**: Quando o circuit está aberto, requisições falham imediatamente sem consumir recursos tentando operações fadadas ao fracasso
- **Recuperação Automática**: O estado half-open permite que o sistema teste se o serviço se recuperou, retornando automaticamente ao estado normal
- **Isolamento de Falhas**: Previne que falhas em `orderService.cancelOrder()` afetem outros componentes do sistema
- **Proteção de Recursos**: Evita sobrecarga do serviço downstream ao interromper requisições quando detecta padrão de falhas

### Negativas
- **Complexidade Adicional**: Introduz mais uma camada de abstração e estados a serem gerenciados
- **Configuração Sensível**: Thresholds mal configurados podem causar circuits abrindo prematuramente ou tarde demais