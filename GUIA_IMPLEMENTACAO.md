# Guia de Implementação - Sistema de Inscrição

## 📋 Resumo do Projeto

O projeto é um **Sistema de Inscrição em Processos Seletivos** para uma universidade. A interface JavaFX está 100% pronta com todas as telas visuais. Este documento guia a implementação da **lógica de negócio** (Controllers) e **persistência em CSV**.

---

## 🎯 O Que Falta Implementar

### 1. **Camada de Modelos** ✅ PRONTO
- Arquivo: `model/Entidades.java`
- Contém: `Disciplina`, `Professor`, `Curso`, `Inscricao`, `Processo`
- Status: Classes de dados já definidas com getters

### 2. **Camada de Controladores** ❌ PENDENTE
Criar arquivos na pasta `controller/`:

#### **DisciplinaController.java**
```
Responsabilidades:
- Ler disciplinas.csv e popular ListaEncadeada (Filagenerica.jar)
- Métodos:
  * cadastrarDisciplina(codigo, nome, dia, horario, aulaDiaria, codigoCurso, status)
  * listarDisciplinas() → List<Disciplina>
  * listarDisciplinasEmFila() → Fila<Disciplina> (para carregarTabela)
  * buscarDisciplina(codigo) → Disciplina
  * atualizarDisciplina(codigo, ...)
  * excluirDisciplina(codigo) - Remove inscrições também!
- Arquivo: disciplinas.csv (formato: codigo,nome,dia,horario,aulaDiaria,codigoCurso,status)
- Regra: ListaEncadeada não deixa linhas vazias após remoção (reescreve arquivo)
```

#### **ProfessorController.java**
```
Responsabilidades:
- Ler professor.csv e popular ListaEncadeada
- Métodos:
  * cadastrarProfessor(cpf, nome, area, pontos)
  * listarProfessores() → List<Professor>
  * listarProfessoresEmFila() → Fila<Professor>
  * buscarProfessor(cpf) → Professor
  * atualizarProfessor(cpf, ...)
  * excluirProfessor(cpf)
- Arquivo: professor.csv (formato: cpf,nome,area,pontos)
- Validação: CPF como chave única
```

#### **CursoController.java**
```
Responsabilidades:
- Ler cursos.csv e popular ListaEncadeada
- Métodos:
  * cadastrarCurso(codigo, nome, area)
  * listarCursos() → List<Curso>
  * listarCursosEmFila() → Fila<Curso>
  * buscarCurso(codigo) → Curso
  * atualizarCurso(codigo, ...)
  * excluirCurso(codigo)
- Arquivo: cursos.csv (formato: codigo,nome,area)
- Validação: Código como chave única
```

#### **InscricaoController.java**
```
Responsabilidades:
- Ler inscricoes.csv e popular ListaEncadeada
- Métodos:
  * inscreverProfessor(cpf, codigoDisciplina, codigoProcesso)
  * listarInscricoes() → List<Inscricao>
  * listarInscricoesEmFila() → Fila<Inscricao>
  * removerInscricao(cpf, codigoDisciplina)
  * listarInscricoesDaDisciplina(codigoDisciplina) → List<Inscricao>
- Arquivo: inscricoes.csv (formato: cpfProfessor,codigoDisciplina,codigoProcesso)
- Validações:
  * Professor existe em professor.csv
  * Disciplina existe em disciplinas.csv
  * Processo está ATIVO em processos.csv
```

#### **ProcessoController.java**
```
Responsabilidades:
- Ler processos.csv
- Métodos:
  * criarProcesso(codigo, codigoDisciplina)
  * listarProcessosAtivos() → List<Processo>
  * buscarProcesso(codigo) → Processo
  * ativarProcesso(codigo)
  * finalizarProcesso(codigo)
- Arquivo: processos.csv (formato: codigo,codigoDisciplina,status)
- Status: "Ativo" ou "Inativo"
```

#### **ConsultaInscritosController.java**
```
Responsabilidades:
- Buscar inscritos por disciplina e ORDENAR por pontuação
- Métodos:
  * buscarInscritosOrdenados(codigoDisciplina) → List<Professor>
    1. Buscar inscricoes desta disciplina em inscricoes.csv
    2. Para cada CPF, buscar dados em professor.csv
    3. ORDENAR por pontuação DECRESCENTE usando Sort.jar
    4. NÃO use Collections.sort()! Use classe Sort do Sort.jar
  * listarDisciplinasComProcessosAtivos() → Fila<String>
    1. Buscar em processos.csv status="Ativo"
    2. Retornar Fila com códigos e nomes de disciplinas
```

#### **ProcessosAbertosController.java**
```
Responsabilidades:
- Usar TABELA DE ESPALHAMENTO (Hash Table) customizada
- Métodos:
  * carregarProcessosAbertos() → List<Disciplina>
    1. Ler processos.csv com status="Aberto"
    2. Para cada processo, buscar disciplina em disciplinas.csv
    3. Inserir em HashTableProcessos: hash(codigoProcesso) → Disciplina
    4. Retornar todas as disciplinas com processos abertos
  * obterEstatisticasHashTable() → String (informações da tabela)
- Implementar classe HashTableProcessos:
  * Atributos: entrada[] (array), tamanho
  * Método hashFunction(String chave) - usar chave.hashCode() % tamanho
  * Método inserir(String chave, Disciplina valor)
  * Método listarTodos() → List<Disciplina>
  * Tratar colisões (encadeamento ou endereçamento aberto)
```

---

## 📂 Estrutura de Arquivos CSV

### **disciplinas.csv**
```csv
codigo,nome,dia,horario,aulaDiaria,codigoCurso,status
D001,Cálculo I,Segunda-feira,08:00,2,C001,Ativa
D002,Português,Terça-feira,10:00,4,C001,Ativa
```

### **professor.csv**
```csv
cpf,nome,area,pontos
123.456.789-00,João Silva,Matemática,85
987.654.321-00,Maria Santos,Português,92
```

### **cursos.csv**
```csv
codigo,nome,area
C001,Engenharia,Matemática
C002,Letras,Português
```

### **inscricoes.csv**
```csv
cpfProfessor,codigoDisciplina,codigoProcesso
123.456.789-00,D001,P001
987.654.321-00,D002,P001
```

### **processos.csv**
```csv
codigo,codigoDisciplina,status
P001,D001,Ativo
P002,D002,Ativo
P003,D003,Inativo
```

---

## 🔧 Estruturas de Dados Obrigatórias

### 1. **ListaEncadeada** (Filagenerica.jar)
- Usada em: `DisciplinaController`, `ProfessorController`, `CursoController`, `InscricaoController`
- Finalidade: Armazenar dados em memória
- Requisito: **Não deixar linhas vazias após remoção** (reescrever arquivo)

### 2. **Fila** (ListaGenerica.jar)
- Usada em: `carregarTabela()` de todas as telas
- Finalidade: Popular dados na TableView
- Padrão: `while (!fila.estaVazia()) { tabela.add(fila.desenfileirar()); }`

### 3. **Ordenação** (Sort.jar)
- Usada em: `ConsultaInscritosController.buscarInscritosOrdenados()`
- Finalidade: Ordenar professores por pontuação DECRESCENTE
- Requisito: **Use classe Sort do Sort.jar, NÃO Collections.sort()**

### 4. **Tabela de Espalhamento** (Implementação própria)
- Usada em: `ProcessosAbertosController`
- Classe: `HashTableProcessos` (criar em `controller/util/`)
- Função hash: `hash(codigo) = codigo.hashCode() % tamanho`
- Requisito: **Implementação própria, não use HashMap/Hashtable do Java**

---

## 📺 Telas e Seus TODOs

### **TelaPrincipal** ✅
- Menu principal com 7 botões
- Novos botões adicionados:
  1. "Inscrições em Processos" → TelaCRUDInscricoes
  2. "Consulta de Inscritos" → TelaConsultaInscritos
  3. "Processos Abertos" → TelaProcessosAbertos

### **TelaDisciplina** - TODO Comments
- [x] Controller instance comments melhorados
- [x] Cadastro comments com fluxo CSV
- [x] Excluir comments com limpeza de inscrições
- [x] carregarTabela() com Fila

### **TelaProfessor** - TODO Comments
- [x] Controller instance comments
- [x] Botões comments com validação CPF
- [x] carregarTabela() com Fila

### **TelaCurso** - TODO Comments
- [x] Controller instance comments
- [x] Botões comments com validação código
- [x] carregarTabela() com Fila

### **TelaCRUDInscricoes** ✅ CRIADA
- [x] Campos: CPF Professor, Código Disciplina, Código Processo
- [x] Tabela: CPF, Disciplina, Processo
- [x] Botões: Inscrever, Limpar, Atualizar, Remover
- TODO: Implementar actions

### **TelaConsultaInscritos** ✅ CRIADA
- [x] ComboBox para selecionar disciplina
- [x] Tabela: CPF, Nome, Área, Pontuação (ORDENADA)
- [x] Botão Buscar
- TODO: Implementar busca e ordenação com Sort.jar

### **TelaProcessosAbertos** ✅ CRIADA
- [x] Tabela com disciplinas que têm processos abertos
- [x] Botão Recarregar
- [x] Info sobre Hash Table
- TODO: Implementar Hash Table customizada

---

## 🚀 Ordem de Implementação Recomendada

1. **Criar estrutura de pastas**
   ```
   app/src/main/java/
   ├── controller/
   │   ├── DisciplinaController.java
   │   ├── ProfessorController.java
   │   ├── CursoController.java
   │   ├── InscricaoController.java
   │   ├── ProcessoController.java
   │   ├── ConsultaInscritosController.java
   │   ├── ProcessosAbertosController.java
   │   └── util/
   │       └── HashTableProcessos.java
   ├── util/
   │   └── CSVHandler.java (classe utilitária para ler/escrever CSV)
   ```

2. **Implementar controllers na ordem:**
   1. `ProcessoController` (menos dependências)
   2. `CursoController`
   3. `DisciplinaController`
   4. `ProfessorController`
   5. `InscricaoController` (depende dos acima)
   6. `ConsultaInscritosController` (depende de Inscrição + Professor)
   7. `ProcessosAbertosController` (implementar HashTableProcessos primeiro)

3. **Implementar Views na ordem:**
   1. `TelaCurso` → `TelaDisciplina` → `TelaProfessor`
   2. `TelaCRUDInscricoes`
   3. `TelaConsultaInscritos`
   4. `TelaProcessosAbertos`

---

## 📝 Padrão de Implementação

### Exemplo: DisciplinaController.java
```java
public class DisciplinaController {
    private ListaEncadeada<Disciplina> lista = new ListaEncadeada<>();
    private final String CSV_FILE = "app/src/main/arquivos/disciplinas.csv";

    public DisciplinaController() {
        carregarDoCSV();
    }

    private void carregarDoCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                Disciplina d = new Disciplina(dados[0], dados[1], ...);
                lista.inserir(d);
            }
        } catch (IOException e) { ... }
    }

    public void cadastrarDisciplina(String codigo, String nome, ...) {
        if (lista.buscar(d -> d.getCodigo().equals(codigo)) != null) {
            throw new Exception("Código já existe");
        }
        Disciplina d = new Disciplina(codigo, nome, ...);
        lista.inserir(d);
        salvarNoCSV();
    }

    private void salvarNoCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE))) {
            // Percorrer lista e escrever CSV
            // Não deixar linhas vazias!
        } catch (IOException e) { ... }
    }

    public Fila<Disciplina> listarDisciplinasEmFila() {
        Fila<Disciplina> fila = new Fila<>();
        // TODO: Popular fila com elementos da lista
        return fila;
    }
}
```

---

## ✅ Checklist de Implementação

- [ ] Criar classe `CSVHandler` para utilitários de arquivo
- [ ] Implementar `DisciplinaController` e testar leitura/escrita CSV
- [ ] Implementar `ProfessorController`
- [ ] Implementar `CursoController`
- [ ] Implementar `InscricaoController`
- [ ] Implementar `ProcessoController`
- [ ] Implementar `ConsultaInscritosController` com Sort
- [ ] Implementar `HashTableProcessos`
- [ ] Implementar `ProcessosAbertosController`
- [ ] Testar botões de cada tela
- [ ] Validar fluxo de CRUD completo
- [ ] Verificar remocao de inscrições ao deletar disciplina
- [ ] Testar ordenação por pontuação
- [ ] Testar Hash Table com processos abertos
- [ ] Criar arquivos CSV de exemplo
- [ ] Documentar função hash customizada
- [ ] Testar com dados reais

---

## 💡 Dicas Importantes

1. **ListaEncadeada**: Use sempre para persistência - nunca ArrayList!
2. **Fila**: Para popular TableView, use Fila, não List
3. **Sort.jar**: Implementar algoritmo de ordenação (não usar Collections)
4. **Hash Table**: Implementar função hash própria, tratar colisões
5. **CSV**: Não deixar linhas vazias, sempre reescrever completamente
6. **Validações**: Verificar existência de referências (professor, disciplina, etc)
7. **Regras**: Remover disciplina = remover inscrições automaticamente

---

**Status Atual**: ✅ Interface 100% pronta | ❌ Lógica pendente | ⏳ Pronto para começar a implementação!
