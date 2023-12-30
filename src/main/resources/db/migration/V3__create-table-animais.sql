CREATE TABLE animais (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    porte VARCHAR(50) NOT NULL,
    idade INT NOT NULL,
    especie VARCHAR(100) NOT NULL,
    pelagem VARCHAR(100),
    peso DOUBLE NOT NULL,
    img_url VARCHAR(255) NOT NULL,
    status_adocao ENUM('DISPONIVEL', 'PROCESSO_ADOCAO', 'ADOTADO'),
    id_usuario INT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);