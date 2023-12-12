CREATE TABLE endereco (
    id INT AUTO_INCREMENT PRIMARY KEY,
    logradouro VARCHAR(255),
    complemento VARCHAR(255),
    cidade VARCHAR(255),
    estado CHAR(2)
);

CREATE TABLE nivel_acesso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50)
);

CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255),
    senha VARCHAR(255),
    data_nascimento DATE,
    cpf VARCHAR(15),
    telefone VARCHAR(20),
    id_endereco INT,
    id_nivel_acesso INT,
    FOREIGN KEY (id_endereco) REFERENCES endereco(id) ON DELETE CASCADE,
    FOREIGN KEY (id_nivel_acesso) REFERENCES nivel_acesso(id)
);