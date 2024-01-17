ALTER TABLE usuario
ADD COLUMN ativo BOOLEAN;

CREATE TABLE token_email (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    criado_as TIMESTAMP,
    expirado_as TIMESTAMP,
    confirmado_as TIMESTAMP,
    id_usuario INT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);