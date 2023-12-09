CREATE TABLE endereco (
    id INT PRIMARY KEY,
    logradouro VARCHAR(255),
    complemento VARCHAR(255),
    cidade VARCHAR(100),
    estado CHAR(2),
    id_usuario INT
);

CREATE TABLE nivel_acesso (
    id INT PRIMARY KEY,
    nome VARCHAR(255)
);

CREATE TABLE usuario (
    id INT PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255),
    senha VARCHAR(255),
    data_nascimento DATE,
    cpf CHAR(15),
    telefone VARCHAR(15),
    id_endereco INT,
    id_nivel_acesso INT
);

ALTER TABLE endereco
ADD CONSTRAINT fk_endereco_usuario
FOREIGN KEY (id_usuario) REFERENCES usuario(id);

ALTER TABLE usuario
ADD CONSTRAINT fk_usuario_endereco
FOREIGN KEY (id_endereco) REFERENCES endereco(id);

ALTER TABLE usuario
ADD CONSTRAINT fk_usuario_nivel_acesso
FOREIGN KEY (id_nivel_acesso) REFERENCES nivel_acesso(id);