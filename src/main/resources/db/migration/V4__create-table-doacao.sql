CREATE TABLE doacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data_doacao DATE,
    id_animais INT,
    id_usuario INT,
    FOREIGN KEY (id_animais) REFERENCES animais(id),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);