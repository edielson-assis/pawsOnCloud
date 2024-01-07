ALTER TABLE animais
ADD COLUMN adotado BOOLEAN;

ALTER TABLE doacao
ADD COLUMN data_cadastramento DATE;

ALTER TABLE doacao
ADD COLUMN confirmar_doacao BOOLEAN;