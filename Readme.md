# Projeto
Api RESTful - Faculdade

## Pré-requisitos

- Java 17: Certifique-se de ter o Java 17 instalado no seu ambiente de desenvolvimento.
- Maven: O projeto utiliza o Maven como gerenciador de dependências. Certifique-se de tê-lo instalado.

## Como executar o projeto

1. Clone o repositório para o seu ambiente local:
```
git clone https://github.com/seu-usuario/seu-projeto.git
```

2. Acesse o diretório do projeto:
```
cd case
```

3. Compile o projeto com o Maven:
```
mvn clean install
```

4. Execute a aplicação Spring Boot:
```
mvn spring-boot:run
```

Após a execução, a API estará disponível em `http://localhost:8080`.

## Endpoints

A seguir estão os principais endpoints disponíveis na API:

### Alunos:

1. **GET /api/alunos**
   - Descrição: Endpoint para obter todos os alunos cadastrados.
   - Parâmetros de consulta: Nenhum.
   - Exemplo de resposta:
   ```json
   [
    {
    "_embedded": {
        "alunoDTOes": [
            {
                "id": 1,
                "nome": "Rodrigo",
                "matricula": 20230214,
                "cursoId": 1,
                "semestreAtual": 2,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/alunos/1"
                    }
                }
            },
            {
                "id": 2,
                "nome": "Roberto",
                "matricula": 20230211,
                "cursoId": 2,
                "semestreAtual": 3,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/alunos/2"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/alunos"
        }
    }
    }
   ]
   ```

2. **POST /api/alunos**
   - Descrição: Endpoint para criar um novo aluno.
   - Corpo da requisição:
   ```json
   {
    "nome": "Roberto",
    "matricula": 20230211,
    "cursoId": 2,
    "semestreAtual": 3
    }
   ```
   - Exemplo de resposta:
   ```json
   {
    "id": 2,
    "nome": "Roberto",
    "matricula": 20230211,
    "cursoId": 2,
    "semestreAtual": 3,
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/alunos/2"
        }
    }
    }
   ```

3. **GET /api/alunos/{id}**
   - Descrição: Endpoint para obter um aluno específico pelo ID.
   - Parâmetros de caminho: ID do aluno a ser obtido.
   - Exemplo de resposta:
   ```json
   {
    "id": 1,
    "nome": "Rodrigo",
    "matricula": 20230214,
    "cursoId": 1,
    "semestreAtual": 2,
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/alunos/1"
        }
    }
    }
   ```

4. **PUT /api/alunos/{id}**
   - Descrição: Endpoint para atualizar um aluno específico pelo ID.
   - Parâmetros de caminho: ID do aluno a ser atualizado.
   - Corpo da requisição:
   ```json
   {
    "nome": "Roberto",
    "matricula": 20230211,
    "cursoId": 2,
    "semestreAtual": 6
    }
   ```
   - Exemplo de resposta:
   ```json
   {
    "id": 2,
    "nome": "Roberto",
    "matricula": 20230211,
    "cursoId": 2,
    "semestreAtual": 6,
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/alunos/2"
        }
    }
    }
   ```

5. **DELETE /api/alunos/{id}**
   - Descrição: Endpoint para excluir um aluno específico pelo ID.
   - Parâmetros de caminho: ID do aluno a ser excluído.
   - Corpo da requisição:
   ```json
   
   ```

### Cursos:

1. **GET /api/cursos**
   - Descrição: Endpoint para obter todos os cursos cadastrados.
   - Parâmetros de consulta: Nenhum.
   - Exemplo de resposta:
   ```json
   [
    {
    "_embedded": {
        "cursoDTOes": [
            {
                "id": 1,
                "nome": "ADS",
                "codigo": 1,
                "departamento": "Tecnologia",
                "cargaHorariaTotal": 200,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/cursos/1"
                    }
                }
            },
            {
                "id": 2,
                "nome": "Redes",
                "codigo": 2,
                "departamento": "Tecnologia",
                "cargaHorariaTotal": 200,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/cursos/2"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/cursos"
        }
    }
    }
   ]
   ```

2. **POST /api/cursos**
   - Descrição: Endpoint para criar um novo curso.
   - Corpo da requisição:
   ```json
   {
    "nome": "Redes",
    "codigo": 2,
    "departamento": "Tecnologia",
    "cargaHorariaTotal": 200
    }
   ```
   - Exemplo de resposta:
   ```json
   {
    "id": 2,
    "nome": "Redes",
    "codigo": 2,
    "departamento": "Tecnologia",
    "cargaHorariaTotal": 200,
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/cursos/2"
        }
    }
    }
   ```

3. **GET /api/cursos/{id}**
   - Descrição: Endpoint para obter um curso específico pelo ID.
   - Parâmetros de caminho: ID do curso a ser obtido.
   - Exemplo de resposta:
   ```json
   {
    "id": 2,
    "nome": "Redes",
    "codigo": 2,
    "departamento": "Tecnologia",
    "cargaHorariaTotal": 200,
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/cursos/2"
        }
    }
    }
   ```

4. **PUT /api/cursos/{id}**
   - Descrição: Endpoint para atualizar um curso específico pelo ID.
   - Parâmetros de caminho: ID do curso a ser atualizado.
   - Corpo da requisição:
   ```json
   {
    "nome": "Redes",
    "codigo": 2,
    "departamento": "Tecnologia",
    "cargaHorariaTotal": 350
    }
   ```
   - Exemplo de resposta:
   ```json
   {
    "nome": "Redes",
    "codigo": 2,
    "departamento": "Tecnologia",
    "cargaHorariaTotal": 350,
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/cursos/2"
        }
    }
    }
   ```

5. **DELETE /api/alunos/{id}**
   - Descrição: Endpoint para excluir um curso específico pelo ID.
   - Parâmetros de caminho: ID do curso a ser excluído.
   - Corpo da requisição:
   ```json
   
   ```

### Professores:

1. **GET /api/professores**
   - Descrição: Endpoint para obter todos os professores cadastrados.
   - Parâmetros de consulta: Nenhum.
   - Exemplo de resposta:
   ```json
   [
    {
    "_embedded": {
        "professorDTOes": [
            {   
                "id": 1,
                "nome": "Paulo",
                "departamento": "Tecnologia",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/professores/1"
                    }
                }
            },
            {
                "id": 2,
                "nome": "Marcelo",
                "departamento": "Tecnologia",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/professores/2"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/professores"
        }
    }
    }
   ]
   ```

2. **POST /api/professores**
   - Descrição: Endpoint para criar um novo professor.
   - Corpo da requisição:
   ```json
   {
    "nome": "Paulo",
    "departamento": "Tecnologia"
    }
   ```
   - Exemplo de resposta:
   ```json
   {
    "id": 1,
    "nome": "Paulo",
    "departamento": "Tecnologia",
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/professores/1"
        }
    }
    }
   ```

3. **GET /api/professores/{id}**
   - Descrição: Endpoint para obter um professor específico pelo ID.
   - Parâmetros de caminho: ID do professor a ser obtido.
   - Exemplo de resposta:
   ```json
   {
    "nome": "Paulo",
    "departamento": "Tecnologia",
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/professores/1"
        }
    }
    }
   ```

4. **PUT /api/professores/{id}**
   - Descrição: Endpoint para atualizar um professor específico pelo ID.
   - Parâmetros de caminho: ID do professor a ser atualizado.
   - Corpo da requisição:
   ```json
   {
    "nome": "Paulo",
    "departamento": "Tecnologia"
    }
   ```
   - Exemplo de resposta:
   ```json
   {
    "nome": "Paulo",
    "departamento": "Tecnologia",
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/professores/1"
        }
    }
    }
   ```

5. **DELETE /api/professores/{id}**
   - Descrição: Endpoint para excluir um professor específico pelo ID.
   - Parâmetros de caminho: ID do professor a ser excluído.
   - Corpo da requisição:
   ```json
   
   ```

### Semestres:

1. **GET /api/semestres**
   - Descrição: Endpoint para obter todos os semestres cadastrados.
   - Parâmetros de consulta: Nenhum.
   - Exemplo de resposta:
   ```json
   [
    {
    "_embedded": {
        "semestreDTOes": [
            {   
                "id": 1,
                "periodo": 8,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/semestres/1"
                    }
                }
            },
            {
                "id": 2,
                "periodo": 10,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/semestres/2"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/semestres"
        }
    }
    }
   ]
   ```

2. **POST /api/semestres**
   - Descrição: Endpoint para criar um novo semestre.
   - Corpo da requisição:
   ```json
   {
    "periodo": 8
    }
   ```
   - Exemplo de resposta:
   ```json
   {
    "id": 1,
    "periodo": 8,
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/semestres/1"
        }
    }
    }
   ```

3. **GET /api/semestres/{id}**
   - Descrição: Endpoint para obter um semestre específico pelo ID.
   - Parâmetros de caminho: ID do semestre a ser obtido.
   - Exemplo de resposta:
   ```json
   {
    "periodo": 8,
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/semestres/1"
        }
    }
    }
   ```

4. **PUT /api/semestres/{id}**
   - Descrição: Endpoint para atualizar um semestre específico pelo ID.
   - Parâmetros de caminho: ID do semestre a ser atualizado.
   - Corpo da requisição:
   ```json
   {
    "periodo": 6
    }
   ```
   - Exemplo de resposta:
   ```json
   {
    "periodo": 6,
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/semestres/1"
        }
    }
    }
   ```

5. **DELETE /api/semestres/{id}**
   - Descrição: Endpoint para excluir um semestre específico pelo ID.
   - Parâmetros de caminho: ID do semestre a ser excluído.
   - Corpo da requisição:
   ```json
   
   ```

## Configuração do Banco de Dados

Nesta API, estamos usando o banco de dados H2 em memória para fins de demonstração.
