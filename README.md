# Sistema de Inscrição - Documentação Rápida

## 📦 O Que Está Pronto?

### ✅ Interface Completa (JavaFX)
- **7 telas visuais** totalmente implementadas
- **Menu principal** com navegação entre telas
- **Formulários e tabelas** para CRUD de:
  - Disciplinas (7 campos)
  - Professores (4 campos)
  - Cursos (3 campos)
  - Inscrições (3 campos)
  - Consulta de Inscritos (tabela de 4 colunas)
  - Processos Abertos (tabela de 7 colunas)

### ✅ Modelo de Dados
- Classes em `model/Entidades.java`:
  - `Disciplina`
  - `Professor`
  - `Curso`
  - `Inscricao` ← NEW
  - `Processo` ← NEW

### ✅ Estrutura de Projeto
- Gradle configurado (Java 21)
- Bibliotecas locais integradas:
  - `Filagenerica.jar` (ListaEncadeada)
  - `ListaGenerica.jar` (Fila)
  - `Sort.jar` (Algoritmos de ordenação)

### ✅ Comentários de Implementação
- Cada tela tem TODO comments detalhados
- Indicações sobre estruturas de dados a usar
- Pseudocódigo para cada ação

---

## ❌ O Que Falta?

### Controllers (Lógica de Negócio)
Criar em `app/src/main/java/controller/`:
1. `DisciplinaController.java`
2. `ProfessorController.java`
3. `CursoController.java`
4. `InscricaoController.java`
5. `ProcessoController.java`
6. `ConsultaInscritosController.java`
7. `ProcessosAbertosController.java`

### Persistência
- Implementar leitura/escrita de arquivos CSV
- Usar ListaEncadeada para evitar linhas vazias
- Criar classe utilitária `CSVHandler`

### Hash Table Customizada
- Implementar `HashTableProcessos` em `controller/util/`
- Função hash própria
- Tratar colisões

---

## 🎯 Próximos Passos

1. **Ler** `GUIA_IMPLEMENTACAO.md` para instruções detalhadas
2. **Criar pasta** `controller/` 
3. **Implementar** controllers na ordem recomendada
4. **Testar** botões com dados reais
5. **Validar** fluxos completos

---

## 📂 Estrutura Atual

```
ProjetoED/
├── app/
│   └── src/main/java/
│       ├── model/
│       │   └── Entidades.java ✅
│       └── view/ ✅
│           ├── App.java
│           ├── TelaPrincipal.java
│           ├── TelaDisciplina.java
│           ├── TelaProfessor.java
│           ├── TelaCurso.java
│           ├── TelaCRUDInscricoes.java ← NEW
│           ├── TelaConsultaInscritos.java ← NEW
│           └── TelaProcessosAbertos.java ← NEW
└── GUIA_IMPLEMENTACAO.md ← Leia isso!
```

---

## 🚀 Como Compilar e Rodar?

```bash
# Compilar
.\gradlew build

# Rodar
.\gradlew run
```

O projeto compila sem erros. Interface abre normalmente, mas botões não funcionam até implementar controllers.

---

## 💬 Dúvidas Frequentes

**P: Posso usar HashMap/Hashtable do Java?**  
R: Não para Hash Table. Para ProcessosAbertos, deve ser implementação própria.

**P: Posso usar Collections.sort()?**  
R: Não. Use a classe Sort de Sort.jar.

**P: Posso usar ArrayList?**  
R: Não. Use ListaEncadeada para persistência.

**P: Os arquivos CSV já existem?**  
R: Não. Criar em `app/src/main/arquivos/` com dados de exemplo.

---

## 📋 Resumo da Tarefa

| O Que | Status | Local |
|------|--------|-------|
| Interface | ✅ Pronto | `view/*.java` |
| Modelos | ✅ Pronto | `model/Entidades.java` |
| Controllers | ❌ Pendente | `controller/*.java` |
| CSV Handler | ❌ Pendente | `controller/util/` |
| Hash Table | ❌ Pendente | `controller/util/` |
| Testes | ❌ Pendente | Testes manuais |

---

**Tudo está pronto para começar! Leia o GUIA_IMPLEMENTACAO.md para detalhes.**
