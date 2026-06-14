# Cabeleleila Leila — Sistema de Agendamento Online

Sistema web desenvolvido como solução para a avaliação técnica DSIN.

---

## Tecnologias Utilizadas

| Camada | Tecnologia |
|--------|-----------|
| Linguagem | Java 17 |
| Framework principal | Spring Boot 3.5 |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | MySQL 8.0 |
| Templates (View) | Thymeleaf 3.1 |
| Frontend | Bootstrap 5.3 |
| Testes | JUnit 5 + Mockito |
| Containerização | Docker + Docker Compose |
| Build | Maven 3.9 |

**Arquitetura:** MVC (Model-View-Controller) com Spring Boot.

---

## Como Rodar o Projeto

### Pré-requisitos

- [Docker](https://www.docker.com/) instalado e em execução
- [Docker Compose](https://docs.docker.com/compose/) (incluído no Docker Desktop)

### Passo a passo

### 1. Clone o repositório (ou extraia o zip)
```bash
git clone <url-do-repositorio>
cd salao-leila
```

### 2. Suba os containers (MySQL + aplicação)
```bash
docker-compose up --build
```

### Acessar o sistema

| URL | Descrição |
|-----|-----------|
| http://localhost:8081 | Página inicial (área do cliente) |
| http://localhost:8081/painel | Painel da Leila (área de funcionários) |
| http://localhost:8081/funcionarios/login | Login da Leila / funcionários |

### Credenciais de acesso padrão (carregadas automaticamente)

**Leila (Gerente):**
- CPF: `555.432.777-09`

**Funcionárias de exemplo:**
- Ana Costa (Cabeleireira) — CPF: `123.456.789-09`
- Carlos Mendes (Manicure) — CPF: `987.654.321-00`
- Mariana Souza (Colorista) — CPF: `444.555.666-77`
- Fernanda Lima (Esteticista) — CPF: `667.788.901-77`

**Clientes de exemplo:**
- Maria Silva — CPF: `529.982.247-25`
- João Pereira — CPF: `111.444.777-35`

### Parar o projeto

```bash
docker-compose down
```

> Para remover também os dados do banco: `docker-compose down -v`

---

## Funcionalidades Implementadas

### Fundamentais (obrigatórias)

- **Agendamento online pelo cliente** — escolha de um ou mais serviços, data e horário com calendário interativo
- **Edição de agendamento** — permitida até 2 dias antes; após esse prazo, orientação por telefone
- **Histórico de agendamentos** — listagem completa com status e valores por cliente
- **Detalhes do agendamento** — visualização individual com serviços, data, status e observações
- **Sugestão automática de horário** — quando o cliente já tem agendamento na mesma semana, o sistema sugere a mesma data/hora

### Diferenciais (opcionais)

- **Painel gerencial da Leila** — desempenho semanal com receita, agendamentos, serviços mais procurados e ranking de clientes
- **Agenda da semana** — visualização semanal dos agendamentos por funcionária com filtro de data
- **Gestão de agendamentos pela Leila** — confirmação, alteração de status (Confirmado / Concluído / Cancelado) e edição pelo lado do salão
- **Gestão de funcionárias** — cadastro, edição e ativação/desativação com vínculo de serviços
- **Controle de estoque** — cadastro de produtos por categoria com quantidade mínima e alerta de reposição no painel
- **Registro de consumo** — baixa de estoque quando um produto é descartado
- **Seleção de serviço direto da home** — ao clicar em "Agendar este serviço", o serviço já vem pré-selecionado no formulário de agendamento

---

## Testes Unitários

Foram implementados **25 testes unitários** com JUnit 5 + Mockito, cobrindo as três principais services:

| Arquivo | Testes | Cobertura |
|---------|--------|-----------|
| `AgendamentoServiceTest` | 10 | `podeEditar`, `listarPorCliente`, `buscarPorId`, `buscarHorariosOcupados` |
| `FuncionarioServiceTest` | 8 | `autenticar`, `cpfJaCadastrado`, `buscarPorId` |
| `ClienteServiceTest` | 7 | `cpfJaCadastrado`, `buscarPorId`, `buscarPorCpf`, `salvar` |

Para rodar os testes (requer Java 17 instalado localmente):

```bash
./mvnw test
```

---

## Estrutura do Projeto

```
salao-leila/
├── src/
│   ├── main/
│   │   ├── java/com/dsin/salaoleila/
│   │   │   ├── controller/     # Controllers MVC
│   │   │   ├── model/          # Entidades JPA
│   │   │   │   └── enums/      # Enums (StatusAgendamento, Cargo, Categoria)
│   │   │   ├── repository/     # Interfaces Spring Data JPA
│   │   │   └── service/        # Regras de negócio
│   │   └── resources/
│   │       ├── templates/      # Views Thymeleaf
│   │       │   └── fragments/  # Layout compartilhado (navbar, head)
│   │       ├── static/images/  # Imagens dos serviços e logo
│   │       ├── application.properties
│   │       └── data.sql        # Dados iniciais (idempotente)
│   └── test/                   # Testes unitários
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

## Observações

- O banco de dados é criado e populado automaticamente na primeira execução via `data.sql`. Os dados são idempotentes (o script pode rodar múltiplas vezes sem duplicar registros).
- A autenticação é baseada em sessão HTTP (`HttpSession`) — sem Spring Security, conforme o escopo do projeto.
- O sistema roda na porta **8081** (e não 8080) para evitar conflito com outras aplicações locais.
- Thymeleaf 3.1 possui restrições de segurança que exigem uso de `data-*` attributes em vez de `th:onclick` — isso foi tratado em todas as telas.
