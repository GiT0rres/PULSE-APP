# 🧡 PULSE

### Seu ritmo. Sua evolução. Seu desempenho.

> Aplicativo mobile desenvolvido em **Kotlin + Jetpack Compose**, com integração ao **Firebase Firestore**, criado para gerenciamento de alunos, acompanhamento de frequência e registro da evolução física e técnica.

---

## 📱 Sobre o projeto

O **PULSE** é um aplicativo mobile desenvolvido como projeto acadêmico com o objetivo de oferecer uma solução simples, moderna e intuitiva para o gerenciamento de alunos e acompanhamento de atividades relacionadas à academia.

A aplicação permite realizar operações de **CRUD diretamente pelo aplicativo**, mantendo os dados armazenados no **Firebase Firestore**.

O projeto foi desenvolvido seguindo uma identidade visual própria, com uma interface moderna, dinâmica e voltada para o contexto fitness.

---

## 🎯 Objetivo

O principal objetivo do PULSE é centralizar informações de alunos e seus respectivos acompanhamentos em um único aplicativo.

Por meio da aplicação, é possível:

* 👤 Cadastrar alunos;
* 🔎 Consultar informações;
* ✏️ Editar registros;
* 🗑️ Excluir registros;
* 📅 Registrar frequência;
* 📈 Acompanhar evolução física;
* 🏋️ Registrar evolução técnica;
* 📚 Gerenciar aulas;
* 👨‍🏫 Gerenciar professores;
* 🏃 Gerenciar modalidades;
* 📊 Consultar informações e registros.

---

# 🎨 Identidade visual

A identidade visual do PULSE foi desenvolvida especialmente para o contexto fitness.

A proposta utiliza uma combinação de **fundo escuro e elementos em laranja**, criando uma aparência moderna, energética e esportiva.

### Características da interface

* 🌑 Interface com tema escuro;
* 🧡 Laranja como cor de destaque;
* 🟠 Elementos com bordas arredondadas;
* 📱 Interface responsiva para dispositivos móveis;
* 🎯 Hierarquia visual clara;
* ⚡ Navegação simples e objetiva;
* 💪 Identidade visual inspirada em energia, movimento e evolução.

A identidade visual foi aplicada de forma consistente nas telas do aplicativo, mantendo uma experiência visual padronizada.

---

# 🛠️ Tecnologias utilizadas

## 📱 Desenvolvimento

| Tecnologia          | Utilização                     |
| ------------------- | ------------------------------ |
| **Kotlin**          | Linguagem principal            |
| **Jetpack Compose** | Construção da interface        |
| **Android Studio**  | Ambiente de desenvolvimento    |
| **Material 3**      | Componentes e estrutura visual |

## ☁️ Banco de dados

| Tecnologia             | Utilização                                |
| ---------------------- | ----------------------------------------- |
| **Firebase**           | Plataforma utilizada no projeto           |
| **Firebase Firestore** | Banco de dados NoSQL                      |
| **Cloud Firestore**    | Armazenamento dos registros do aplicativo |

---

# 🏗️ Arquitetura

O projeto foi estruturado utilizando uma organização baseada na separação de responsabilidades.

```text
PULSE
│
├── 📱 Android
│   ├── Kotlin
│   ├── Jetpack Compose
│   └── Material 3
│
├── 🎨 Interface
│   ├── Splash
│   ├── Login
│   ├── Dashboard
│   ├── Alunos
│   ├── Frequência
│   ├── Evolução
│   ├── Aulas
│   ├── Professores
│   ├── Modalidades
│   └── Perfil
│
└── ☁️ Firebase
    └── Firestore
        ├── Alunos
        ├── Frequências
        ├── Evoluções
        ├── Aulas
        ├── Professores
        └── Modalidades
```

---

# 🔥 Firebase Firestore

O **Firebase Firestore** é responsável pelo armazenamento dos dados utilizados pelo aplicativo.

As informações cadastradas através do aplicativo são persistidas diretamente no banco de dados.

### Fluxo da aplicação

```text
USUÁRIO
   │
   ▼
📱 PULSE
   │
   ▼
Jetpack Compose
   │
   ▼
Kotlin
   │
   ▼
🔥 Firebase Firestore
   │
   ├── Criar
   ├── Consultar
   ├── Atualizar
   └── Excluir
```

---

# 🔄 Operações CRUD

Um dos principais requisitos do projeto é a implementação do **CRUD** utilizando o Firebase Firestore.

## ➕ Create — Criar

Permite cadastrar novos registros através do aplicativo.

Exemplo:

```text
Novo aluno
     ↓
Preenchimento do formulário
     ↓
Salvar
     ↓
Firebase Firestore
```

---

## 🔎 Read — Consultar

Os registros armazenados no Firestore podem ser consultados através das telas do aplicativo.

```text
Firebase Firestore
        ↓
Consulta dos dados
        ↓
Aplicativo
        ↓
Lista de registros
```

---

## ✏️ Update — Atualizar

Os registros existentes podem ser editados diretamente pelo aplicativo.

```text
Selecionar registro
        ↓
Editar informações
        ↓
Salvar alterações
        ↓
Firebase Firestore atualizado
```

---

## 🗑️ Delete — Excluir

Também é possível excluir registros através da aplicação.

```text
Selecionar registro
        ↓
Excluir
        ↓
Confirmação
        ↓
Firebase Firestore
```

---

# 📋 Funcionalidades

## 👤 Alunos

* Cadastro de alunos;
* Listagem de alunos;
* Visualização de informações;
* Edição de dados;
* Exclusão de registros.

## 📅 Frequência

* Registro de presença;
* Registro de ausência;
* Consulta de frequência;
* Histórico de registros.

## 📈 Evolução física

* Registro de informações físicas;
* Acompanhamento da evolução;
* Histórico dos registros.

## 🏋️ Evolução técnica

* Registro de desempenho;
* Acompanhamento da evolução;
* Histórico das informações.

## 📚 Aulas

* Cadastro;
* Consulta;
* Edição;
* Exclusão.

## 👨‍🏫 Professores

* Cadastro;
* Consulta;
* Edição;
* Exclusão.

## 🏃 Modalidades

* Cadastro;
* Consulta;
* Edição;
* Exclusão.

---

# 📱 Telas do aplicativo

### Splash Screen

Tela inicial responsável pela apresentação do aplicativo.

### Login

Tela de autenticação e acesso ao sistema.

### Dashboard

Painel principal com uma visão geral das informações.

### Alunos

Área destinada ao gerenciamento dos alunos e operações CRUD.

### Frequência

Área para registro e consulta da presença dos alunos.

### Evolução

Área destinada ao acompanhamento da evolução física e técnica.

### Aulas

Gerenciamento das aulas cadastradas.

### Professores

Gerenciamento dos professores.

### Modalidades

Gerenciamento das modalidades disponíveis.

### Perfil

Área destinada às informações do usuário.

---

# 🎥 Vídeo de apresentação

O vídeo apresenta o desenvolvimento e funcionamento do aplicativo, demonstrando as principais funcionalidades e a integração com o Firebase Firestore.

### ▶️ Assista à apresentação

> 🔗 **Link do vídeo:**
> **https://youtu.be/xLCG1RyRAPo**

### O vídeo demonstra:

* 📱 Apresentação do aplicativo;
* 🎨 Identidade visual;
* 🧭 Navegação entre as telas;
* 👤 Cadastro de informações;
* 🔎 Consulta dos registros;
* ✏️ Edição dos registros;
* 🗑️ Exclusão dos registros;
* 🔥 Firebase Firestore;
* ☁️ Dados sendo gravados no banco;
* 🔄 Funcionamento completo do CRUD.

---

# 🚀 Como executar o projeto

## Requisitos

Antes de executar o projeto, é necessário possuir:

* Android Studio;
* JDK compatível com o projeto;
* Android SDK;
* Emulador Android ou dispositivo físico;
* Conta Google para configuração do Firebase;
* Projeto configurado no Firebase.

---

## 📥 Clonar o projeto

```bash
git clone https://github.com/GiT0rres/Pulse-App.git
```

Entre na pasta:

```bash
cd Pulse-App
```

Abra o projeto no **Android Studio**.

---

# 🔥 Configuração do Firebase

Para executar a aplicação corretamente, o projeto precisa estar conectado a um projeto Firebase.

No Firebase Console:

1. Criar ou selecionar um projeto;
2. Adicionar o aplicativo Android;
3. Informar o package name utilizado pelo projeto;
4. Baixar o arquivo `google-services.json`;
5. Colocar o arquivo na pasta:

```text
app/
└── google-services.json
```

6. Sincronizar o projeto no Android Studio;
7. Executar o aplicativo.

> ⚠️ O arquivo `google-services.json` contém configurações específicas do projeto Firebase. Por segurança, siga as boas práticas de gerenciamento de credenciais antes de disponibilizar o projeto publicamente.

---

# ▶️ Executando o aplicativo

No Android Studio:

```text
1. Abrir o projeto
        ↓
2. Sincronizar o Gradle
        ↓
3. Configurar o Firebase
        ↓
4. Selecionar um dispositivo
        ↓
5. Run ▶
        ↓
6. Aplicativo PULSE
```

---

# 📂 Estrutura do projeto

```text
app/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── ...
│       │
│       ├── res/
│       │   ├── drawable/
│       │   ├── mipmap/
│       │   └── values/
│       │
│       └── AndroidManifest.xml
│
├── google-services.json
│
├── build.gradle.kts
└── proguard-rules.pro
```

---

# 🧪 Demonstração do CRUD

| Operação   | Aplicativo | Firebase Firestore       |
| ---------- | ---------- | ------------------------ |
| ➕ Create   | Cadastro   | Novo documento           |
| 🔎 Read    | Consulta   | Leitura do documento     |
| ✏️ Update  | Edição     | Atualização do documento |
| 🗑️ Delete | Exclusão   | Remoção do documento     |

---

# 🎓 Requisitos da atividade

O projeto atende aos requisitos propostos para a atividade:

### ✅ Tema individual

O aplicativo foi desenvolvido seguindo o tema definido para o projeto.

### ✅ Identidade visual

A aplicação possui identidade visual própria relacionada ao tema fitness.

### ✅ Jetpack Compose

A interface do aplicativo foi desenvolvida utilizando **Jetpack Compose**.

### ✅ Kotlin

O aplicativo foi desenvolvido utilizando **Kotlin** como linguagem principal.

### ✅ Firebase Firestore

Os dados são armazenados no **Firebase Firestore**.

### ✅ CRUD

O aplicativo permite realizar operações de:

* Create;
* Read;
* Update;
* Delete.

### ✅ Demonstração em vídeo

O vídeo de apresentação demonstra o aplicativo funcionando e a utilização do Firebase Firestore.

---

# 📊 Tecnologias e conceitos aplicados

```text
Kotlin
│
├── Programação Android
│
Jetpack Compose
│
├── Interface declarativa
├── Componentes reutilizáveis
└── Material Design
│
Firebase
│
└── Cloud Firestore
    ├── Create
    ├── Read
    ├── Update
    └── Delete
```

---

# 👩‍💻 Autoria

**Giovanna Torres**

Projeto acadêmico desenvolvido para demonstração de conhecimentos em:

* Desenvolvimento Android;
* Kotlin;
* Jetpack Compose;
* Firebase;
* Cloud Firestore;
* Banco de dados;
* CRUD;
* Design de interfaces.

---

# 📌 Entrega

### 💻 GitHub

🔗 **https://github.com/GiT0rres/Pulse-App**

### 🎥 Vídeo de apresentação

🔗 **[ADICIONE AQUI O LINK DO VÍDEO]**

---

# 🧡 PULSE

> **Seu ritmo. Sua evolução. Seu desempenho.**

Aplicativo desenvolvido com **Kotlin + Jetpack Compose + Firebase Firestore**.
