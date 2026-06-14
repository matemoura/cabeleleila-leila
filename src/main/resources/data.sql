INSERT INTO servicos (nome, descricao, preco, duracao_minutos)
SELECT 'Corte Feminino', 'Corte completo com lavagem e escova', 80.00, 60
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE nome = 'Corte Feminino');

INSERT INTO servicos (nome, descricao, preco, duracao_minutos)
SELECT 'Coloração', 'Coloração completa', 150.00, 120
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE nome = 'Coloração');

INSERT INTO servicos (nome, descricao, preco, duracao_minutos)
SELECT 'Escova Progressiva', 'Alisamento progressivo', 200.00, 180
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE nome = 'Escova Progressiva');

INSERT INTO servicos (nome, descricao, preco, duracao_minutos)
SELECT 'Manicure', 'Unhas das mãos', 35.00, 45
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE nome = 'Manicure');

INSERT INTO servicos (nome, descricao, preco, duracao_minutos)
SELECT 'Pedicure', 'Unhas dos pés', 40.00, 45
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE nome = 'Pedicure');

INSERT INTO servicos (nome, descricao, preco, duracao_minutos)
SELECT 'Hidratação', 'Hidratação capilar profunda', 70.00, 60
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE nome = 'Hidratação');

-- CPFs válidos para teste
INSERT INTO clientes (nome, cpf, telefone, email)
SELECT 'Maria Silva', '52998224725', '(71) 98888-1111', 'maria@email.com'
WHERE NOT EXISTS (SELECT 1 FROM clientes WHERE cpf = '52998224725');

INSERT INTO clientes (nome, cpf, telefone, email)
SELECT 'Ana Souza', '11144477735', '(71) 98888-2222', 'ana@email.com'
WHERE NOT EXISTS (SELECT 1 FROM clientes WHERE cpf = '11144477735');

-- Itens de estoque iniciais
INSERT INTO itens_estoque (nome, descricao, categoria, unidade, quantidade, quantidade_minima, preco_unitario)
SELECT 'Tinta Louro Dourado 60g', 'Coloracao profissional', 'TINTURA', 'unidade', 2, 5, 25.00
WHERE NOT EXISTS (SELECT 1 FROM itens_estoque WHERE nome = 'Tinta Louro Dourado 60g');

INSERT INTO itens_estoque (nome, descricao, categoria, unidade, quantidade, quantidade_minima, preco_unitario)
SELECT 'Tinta Castanho Escuro 60g', 'Coloracao profissional', 'TINTURA', 'unidade', 8, 5, 25.00
WHERE NOT EXISTS (SELECT 1 FROM itens_estoque WHERE nome = 'Tinta Castanho Escuro 60g');

INSERT INTO itens_estoque (nome, descricao, categoria, unidade, quantidade, quantidade_minima, preco_unitario)
SELECT 'Shampoo Hidratante 1L', 'Uso profissional, uso coletivo', 'SHAMPOO', 'unidade', 1, 3, 45.00
WHERE NOT EXISTS (SELECT 1 FROM itens_estoque WHERE nome = 'Shampoo Hidratante 1L');

INSERT INTO itens_estoque (nome, descricao, categoria, unidade, quantidade, quantidade_minima, preco_unitario)
SELECT 'Condicionador Reconstrutor 1L', 'Uso profissional', 'SHAMPOO', 'unidade', 4, 3, 48.00
WHERE NOT EXISTS (SELECT 1 FROM itens_estoque WHERE nome = 'Condicionador Reconstrutor 1L');

INSERT INTO itens_estoque (nome, descricao, categoria, unidade, quantidade, quantidade_minima, preco_unitario)
SELECT 'Escova Progressiva 1L', 'Tratamento alisamento', 'TRATAMENTO', 'frasco', 0, 2, 89.00
WHERE NOT EXISTS (SELECT 1 FROM itens_estoque WHERE nome = 'Escova Progressiva 1L');

INSERT INTO itens_estoque (nome, descricao, categoria, unidade, quantidade, quantidade_minima, preco_unitario)
SELECT 'Mascara Capilar 500g', 'Hidratacao profunda', 'TRATAMENTO', 'unidade', 5, 2, 35.00
WHERE NOT EXISTS (SELECT 1 FROM itens_estoque WHERE nome = 'Mascara Capilar 500g');

INSERT INTO itens_estoque (nome, descricao, categoria, unidade, quantidade, quantidade_minima, preco_unitario)
SELECT 'Esmalte Rosa Antigo', 'Colorama colecao 2024', 'MANICURE', 'unidade', 12, 5, 5.00
WHERE NOT EXISTS (SELECT 1 FROM itens_estoque WHERE nome = 'Esmalte Rosa Antigo');

INSERT INTO itens_estoque (nome, descricao, categoria, unidade, quantidade, quantidade_minima, preco_unitario)
SELECT 'Lixa de Unha (pct 50un)', 'Lixa fina e grossa', 'MANICURE', 'pacote', 3, 2, 8.00
WHERE NOT EXISTS (SELECT 1 FROM itens_estoque WHERE nome = 'Lixa de Unha (pct 50un)');

INSERT INTO itens_estoque (nome, descricao, categoria, unidade, quantidade, quantidade_minima, preco_unitario)
SELECT 'Toalha Descartavel (pct 50un)', 'Para uso em atendimentos', 'DESCARTAVEL', 'pacote', 2, 5, 12.00
WHERE NOT EXISTS (SELECT 1 FROM itens_estoque WHERE nome = 'Toalha Descartavel (pct 50un)');

INSERT INTO itens_estoque (nome, descricao, categoria, unidade, quantidade, quantidade_minima, preco_unitario)
SELECT 'Alcool Gel 500ml', 'Higienizacao das maos', 'LIMPEZA', 'unidade', 1, 4, 18.00
WHERE NOT EXISTS (SELECT 1 FROM itens_estoque WHERE nome = 'Alcool Gel 500ml');
-- =============================================
-- Funcionarios (6 profissionais do salao)
-- =============================================
INSERT INTO funcionarios (nome, cpf, telefone, email, cargo, data_admissao, ativo)
SELECT 'Patricia Oliveira', '12345678909', '(71) 99111-0001', 'patricia@salaoleila.com', 'CABELEIREIRO', '2022-03-01', true
WHERE NOT EXISTS (SELECT 1 FROM funcionarios WHERE cpf = '12345678909');

INSERT INTO funcionarios (nome, cpf, telefone, email, cargo, data_admissao, ativo)
SELECT 'Renata Santos', '98765432100', '(71) 99111-0002', 'renata@salaoleila.com', 'CABELEIREIRO', '2021-06-15', true
WHERE NOT EXISTS (SELECT 1 FROM funcionarios WHERE cpf = '98765432100');

INSERT INTO funcionarios (nome, cpf, telefone, email, cargo, data_admissao, ativo)
SELECT 'Juliana Ferreira', '11223344517', '(71) 99111-0003', 'juliana@salaoleila.com', 'MANICURE', '2023-01-10', true
WHERE NOT EXISTS (SELECT 1 FROM funcionarios WHERE cpf = '11223344517');

INSERT INTO funcionarios (nome, cpf, telefone, email, cargo, data_admissao, ativo)
SELECT 'Bruna Costa', '22334455628', '(71) 99111-0004', 'bruna@salaoleila.com', 'MANICURE', '2023-08-20', true
WHERE NOT EXISTS (SELECT 1 FROM funcionarios WHERE cpf = '22334455628');

INSERT INTO funcionarios (nome, cpf, telefone, email, cargo, data_admissao, ativo)
SELECT 'Camila Rodrigues', '33445566739', '(71) 99111-0005', 'camila@salaoleila.com', 'PEDICURE', '2022-11-05', true
WHERE NOT EXISTS (SELECT 1 FROM funcionarios WHERE cpf = '33445566739');

INSERT INTO funcionarios (nome, cpf, telefone, email, cargo, data_admissao, ativo)
SELECT 'Leticia Alves', '44556677840', '(71) 99111-0006', 'leticia@salaoleila.com', 'AUXILIAR', '2024-02-12', true
WHERE NOT EXISTS (SELECT 1 FROM funcionarios WHERE cpf = '44556677840');

-- =============================================
-- Vinculo Funcionario <-> Servico
-- Patricia: Corte Feminino, Coloracao, Escova Progressiva, Hidratacao
-- =============================================
INSERT INTO funcionario_servicos (funcionario_id, servico_id)
SELECT f.id, s.id FROM funcionarios f CROSS JOIN servicos s
WHERE f.cpf = '12345678909' AND s.nome IN ('Corte Feminino', 'Coloracao', 'Escova Progressiva', 'Hidratacao')
AND NOT EXISTS (SELECT 1 FROM funcionario_servicos x WHERE x.funcionario_id = f.id AND x.servico_id = s.id);

-- Renata: Corte Feminino, Coloracao
INSERT INTO funcionario_servicos (funcionario_id, servico_id)
SELECT f.id, s.id FROM funcionarios f CROSS JOIN servicos s
WHERE f.cpf = '98765432100' AND s.nome IN ('Corte Feminino', 'Coloracao')
AND NOT EXISTS (SELECT 1 FROM funcionario_servicos x WHERE x.funcionario_id = f.id AND x.servico_id = s.id);

-- Juliana: Manicure, Pedicure
INSERT INTO funcionario_servicos (funcionario_id, servico_id)
SELECT f.id, s.id FROM funcionarios f CROSS JOIN servicos s
WHERE f.cpf = '11223344517' AND s.nome IN ('Manicure', 'Pedicure')
AND NOT EXISTS (SELECT 1 FROM funcionario_servicos x WHERE x.funcionario_id = f.id AND x.servico_id = s.id);

-- Bruna: Manicure
INSERT INTO funcionario_servicos (funcionario_id, servico_id)
SELECT f.id, s.id FROM funcionarios f CROSS JOIN servicos s
WHERE f.cpf = '22334455628' AND s.nome = 'Manicure'
AND NOT EXISTS (SELECT 1 FROM funcionario_servicos x WHERE x.funcionario_id = f.id AND x.servico_id = s.id);

-- Camila: Pedicure
INSERT INTO funcionario_servicos (funcionario_id, servico_id)
SELECT f.id, s.id FROM funcionarios f CROSS JOIN servicos s
WHERE f.cpf = '33445566739' AND s.nome = 'Pedicure'
AND NOT EXISTS (SELECT 1 FROM funcionario_servicos x WHERE x.funcionario_id = f.id AND x.servico_id = s.id);

-- Leticia: Hidratacao, Escova Progressiva
INSERT INTO funcionario_servicos (funcionario_id, servico_id)
SELECT f.id, s.id FROM funcionarios f CROSS JOIN servicos s
WHERE f.cpf = '44556677840' AND s.nome IN ('Hidratacao', 'Escova Progressiva')
AND NOT EXISTS (SELECT 1 FROM funcionario_servicos x WHERE x.funcionario_id = f.id AND x.servico_id = s.id);
-- =============================================
-- Servico: Sobrancelha
-- =============================================
UPDATE servicos SET nome = 'Sobrancelha' WHERE nome = 'Sombrancelha';
INSERT INTO servicos (nome, descricao, preco, duracao_minutos, imagem)
SELECT 'Sobrancelha', 'Design e manutencao de sobrancelhas', 45.00, 30, 'sombrancelha.png'
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE nome = 'Sobrancelha');

-- =============================================
-- Imagens dos servicos existentes
-- (UPDATE e idempotente, roda a cada start sem problema)
-- =============================================
UPDATE servicos SET imagem = 'coloracao.png'    WHERE nome = 'Coloracao'          AND (imagem IS NULL OR imagem = '');
UPDATE servicos SET imagem = 'escova.png'        WHERE nome = 'Escova Progressiva' AND (imagem IS NULL OR imagem = '');
UPDATE servicos SET imagem = 'manicure.png'      WHERE nome = 'Manicure'           AND (imagem IS NULL OR imagem = '');
UPDATE servicos SET imagem = 'pedicure.png'      WHERE nome = 'Pedicure'           AND (imagem IS NULL OR imagem = '');
UPDATE servicos SET imagem = 'sombrancelha.png'  WHERE nome = 'Sobrancelha'        AND (imagem IS NULL OR imagem = '');

-- =============================================
-- Funcionaria: Fernanda Lima — Esteticista
-- CPF 667.788.901-77 (valido)
-- =============================================
INSERT INTO funcionarios (nome, cpf, telefone, email, cargo, data_admissao, ativo)
SELECT 'Fernanda Lima', '66778890177', '(71) 99111-0007', 'fernanda@salaoleila.com', 'ESTETICISTA', '2023-05-08', true
WHERE NOT EXISTS (SELECT 1 FROM funcionarios WHERE cpf = '66778890177');

-- Fernanda atende: Sobrancelha
INSERT INTO funcionario_servicos (funcionario_id, servico_id)
SELECT f.id, s.id FROM funcionarios f CROSS JOIN servicos s
WHERE f.cpf = '66778890177' AND s.nome = 'Sobrancelha'
AND NOT EXISTS (SELECT 1 FROM funcionario_servicos x WHERE x.funcionario_id = f.id AND x.servico_id = s.id);
UPDATE servicos SET imagem = 'corte_cabelo.png' WHERE nome = 'Corte Feminino'  AND (imagem IS NULL OR imagem = '');
UPDATE servicos SET imagem = 'hidratacao.png'   WHERE nome = 'Hidratacao'       AND (imagem IS NULL OR imagem = '');

-- =============================================
-- Leila Oliveira (GERENTE / dona do salao)
-- CPF: 555.432.777-09 (valido)
-- =============================================
INSERT INTO funcionarios (nome, cpf, telefone, email, cargo, data_admissao, ativo, login, senha)
SELECT 'Leila Oliveira', '55543277709', '(71) 99000-0001', 'leila@salaoleila.com', 'GERENTE', '2018-01-01', true, 'leila', '123'
WHERE NOT EXISTS (SELECT 1 FROM funcionarios WHERE cpf = '55543277709');

-- Login e senha dos funcionarios (idempotente)
UPDATE funcionarios SET login = 'patricia', senha = '123' WHERE cpf = '12345678909' AND (login IS NULL OR login = '');
UPDATE funcionarios SET login = 'renata',   senha = '123' WHERE cpf = '98765432100' AND (login IS NULL OR login = '');
UPDATE funcionarios SET login = 'juliana',  senha = '123' WHERE cpf = '11223344517' AND (login IS NULL OR login = '');
UPDATE funcionarios SET login = 'bruna',    senha = '123' WHERE cpf = '22334455628' AND (login IS NULL OR login = '');
UPDATE funcionarios SET login = 'camila',   senha = '123' WHERE cpf = '33445566739' AND (login IS NULL OR login = '');
UPDATE funcionarios SET login = 'leticia',  senha = '123' WHERE cpf = '44556677840' AND (login IS NULL OR login = '');
UPDATE funcionarios SET login = 'fernanda', senha = '123' WHERE cpf = '66778890177' AND (login IS NULL OR login = '');

-- =============================================
-- Agendamento de teste: Maria Silva - Corte Feminino - CONFIRMADO
-- =============================================
INSERT INTO agendamentos (cliente_id, data_hora, status)
SELECT c.id, '2026-06-17 10:00:00', 'CONFIRMADO'
FROM clientes c WHERE c.cpf = '52998224725'
AND NOT EXISTS (SELECT 1 FROM agendamentos a WHERE a.cliente_id = c.id);

INSERT INTO agendamento_servicos (agendamento_id, servico_id)
SELECT a.id, s.id
FROM agendamentos a
JOIN clientes c ON c.id = a.cliente_id AND c.cpf = '52998224725'
CROSS JOIN servicos s
WHERE s.nome = 'Corte Feminino'
AND NOT EXISTS (SELECT 1 FROM agendamento_servicos x WHERE x.agendamento_id = a.id AND x.servico_id = s.id);