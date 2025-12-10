insert into district(name, abbreviation) values ('São Paulo', 'SP');
insert into district(name, abbreviation) values ('Paraná', 'PR');
insert into district(name, abbreviation) values ('Santa Catarina', 'SC');
insert into district(name, abbreviation) values ('Rio Grande do Sul', 'RS');
insert into district(name, abbreviation) values ('Mato Grosso do Sul', 'MS');
insert into district(name, abbreviation) values ('Rondônia', 'RO');
insert into district(name, abbreviation) values ('Acre', 'AC');
insert into district(name, abbreviation) values ('Amazonas', 'AM');
insert into district(name, abbreviation) values ('Roraima', 'RR');
insert into district(name, abbreviation) values ('Pará', 'PA');
insert into district(name, abbreviation) values ('Amapá', 'AP');
insert into district(name, abbreviation) values ('Tocantins', 'TO');
insert into district(name, abbreviation) values ('Maranhão', 'MA');
insert into district(name, abbreviation) values ('Rio Grande do Norte', 'RN');
insert into district(name, abbreviation) values ('Paraíba', 'PB');
insert into district(name, abbreviation) values ( 'Pernambuco', 'PE');
insert into district(name, abbreviation) values ( 'Alagoas', 'AL');
insert into district(name, abbreviation) values ( 'Sergipe', 'SE');
insert into district(name, abbreviation) values ( 'Bahia', 'BA');
insert into district(name, abbreviation) values ( 'Minas Gerais', 'MG');
insert into district(name, abbreviation) values ( 'Rio de Janeiro', 'RJ');
insert into district(name, abbreviation) values ( 'Mato Grosso', 'MT');
insert into district(name, abbreviation) values ( 'Goiás', 'GO');
insert into district(name, abbreviation) values ( 'Distrito Federal', 'DF');
insert into district(name, abbreviation) values ( 'Piauí', 'PI');
insert into district(name, abbreviation) values ( 'Ceará', 'CE');
insert into district(name, abbreviation) values ( 'Espírito Santo', 'ES');

-- District Capitals
insert into city (district_id, name) values (1,'São Paulo'); -- São Paulo
insert into city (district_id, name) values (2, 'Curitiba'); -- Paraná
insert into city (district_id, name) values (3,'Florianópolis'); -- Santa Catarina
insert into city (district_id, name) values (4,'Porto Alegre'); -- Rio Grande do Sul
insert into city (district_id, name) values (5,'Campo Grande'); -- Mato Grosso do Sul
insert into city (district_id, name) values (6, 'Porto Velho'); -- Rondônia
insert into city (district_id, name) values (7, 'Rio Branco'); -- Acre
insert into city (district_id, name) values (8, 'Manaus'); -- Amazonas
insert into city (district_id, name) values (9, 'Boa Vista'); -- Roraima
insert into city (district_id, name) values (10, 'Belém'); -- Pará
insert into city (district_id, name) values (11, 'Macapá'); -- Amapá
insert into city (district_id, name) values (12, 'Palmas'); -- Tocantins
insert into city (district_id, name) values (13,'São Luís'); -- Maranhão
insert into city (district_id, name) values (14,'Natal'); -- Rio Grande do Norte
insert into city (district_id, name) values (15,'João Pessoa'); -- Paraíba
insert into city (district_id, name) values (16,'Recife'); -- Pernambuco
insert into city (district_id, name) values (17, 'Maceió'); -- Alagoas
insert into city (district_id, name) values (18, 'Aracaju'); -- Sergipe
insert into city (district_id, name) values (19, 'Salvador'); -- Bahia
insert into city (district_id, name) values (20,'Belo Horizonte'); -- Minas Gerais
insert into city (district_id, name) values (21,'Rio de Janeiro'); -- Rio de Janeiro
insert into city (district_id, name) values (22, 'Cuiabá'); -- Mato Grosso
insert into city (district_id, name) values (23,'Goiânia'); -- Goiás
insert into city (district_id, name) values (24,'Brasília'); -- Distrito Federal
insert into city (district_id, name) values (25, 'Teresina'); -- Piauí
insert into city (district_id, name) values (26, 'Fortaleza'); -- Ceará
insert into city (district_id, name) values (27,'Vitória'); -- Espírito Santo

insert into customer(name, social_security_number, city_id) values ('Manoel Campos', '33184755053', 4);
insert into customer(name, social_security_number, city_id) values ('João Pedro', '28327907042', 1);
insert into customer(name, social_security_number, city_id) values ('Ana Paula Maria', '02894896018', 2);
insert into customer(name, social_security_number, city_id) values ('Maria Francisca', '63512889085', 6);
insert into customer(name, social_security_number, city_id) values ('Pedro Miguel', '94407622091', 6);
insert into customer(name, social_security_number, city_id) values ('Pedro Artur', '33660586099', 6);
insert into customer(name, social_security_number, city_id) values ('Paula Gomes', '79538783050', 3);
insert into customer(name, social_security_number, city_id) values ('Helena Silva', '32253097020', 4);
insert into customer(name, social_security_number, city_id) values ('Marta Silva', '26341362005', 1);

insert into product(description, price, amount) values ('Notebook', 5.00, 2);
insert into product(description, price, amount) values ('TV', 1500.00, 10);
insert into product(description, price, amount) values ('iPhone', 4000.00, 100);
insert into product(description, price, amount) values ('Teclado', 200.00, 50);

-- Out of stock Product
insert into product(description, price, amount) values ('Mouse', 150.00, 0);

insert into purchase (customer_id, date_time) values (1, '2025-02-23');
    insert into purchase_item (purchase_id, product_id, quant) values (1, 1, 2);
    insert into purchase_item (purchase_id, product_id, quant) values (1, 3, 4);
    insert into purchase_item (purchase_id, product_id, quant) values (1, 4, 1);

insert into purchase (customer_id, date_time) values (1, '2024-10-30');
    insert into purchase_item (purchase_id, product_id, quant) values (2, 2, 2);
    insert into purchase_item (purchase_id, product_id, quant) values (2, 5, 1);

insert into purchase (customer_id, date_time) values (3, '2025-01-12');
    insert into purchase_item (purchase_id, product_id, quant) values (3, 4, 1);

-- Sell for a product out of stock, to simulate that a purchase was made before and now there is no stock anymore
insert into purchase (customer_id, date_time) values (2, '2025-02-24');
    insert into purchase_item (purchase_id, product_id, quant) values (4, 5, 4);
