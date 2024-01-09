insert into notion_folder (id, name)
values (1, '개발'),
       (2, '운동');

insert into graph (id, notion_folder_id)
values (1, 1),
       (2, 1);

insert into notion (id, graph_id, name)
values (1, 1, '스프링'),
       (2, 1, 'JPA'),
       (3, 1, 'MVC'),
       (4, 2, '네트워크'),
       (5, 2, '운영체제');

insert into edge (id, source_notion_id, target_notion_id)
values (1, 1, 3),
       (2, 3, 1);
