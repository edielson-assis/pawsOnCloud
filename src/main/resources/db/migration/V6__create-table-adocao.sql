CREATE TABLE adocao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data_adocao DATE,
    id_animais INT,
    id_usuario INT,
    FOREIGN KEY (id_animais) REFERENCES animais(id),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id),
    confirmar_adocao BOOLEAN
);