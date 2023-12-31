
-- Tabla de Roles
INSERT INTO Roles (nombre) VALUES ('ROLE_MASTER');
INSERT INTO Roles (nombre) VALUES ('ROLE_ADMIN');
INSERT INTO Roles (nombre) VALUES ('ROLE_MANAGER');
INSERT INTO Roles (nombre) VALUES ('ROLE_RESPONSIBLE');
INSERT INTO Roles (nombre) VALUES ('ROLE_USER');

-- Tabla de PerfilesUsuarios
INSERT INTO Perfiles_Usuarios (nombre, primer_Apellido) VALUES('Enrique', 'Escalante');
INSERT INTO Perfiles_Usuarios (nombre, primer_Apellido) VALUES('Cristina', 'Liñan');
INSERT INTO Perfiles_Usuarios (nombre, primer_Apellido) VALUES('Invitado', 'invitado');

-- Tabla de Usuarios
INSERT INTO Usuarios (nombre_Usuario, correo_Electronico, password, perfil_Id, fecha_Creacion, fecha_Ultima_Modificacion, habilitado, cuenta_Bloqueada, intentos_Inicio_Sesion, cuenta_Verificada, nombre_Usuario_Creador, nombre_Usuario_Ultimo_Modificador) VALUES('enrique.escalante', 'enrique.escalante@hotmail.es', '$2a$10$.2VGBVkyf1zSE1fOwBIXXOMOZ6HfUn784vimes2mX6ILI89CzmTBu', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 0, 0, 1, 'enrique.escalante', 'enrique.escalante');
INSERT INTO Usuarios (nombre_Usuario, correo_Electronico, password, perfil_Id, fecha_Creacion, fecha_Ultima_Modificacion, habilitado, cuenta_Bloqueada, intentos_Inicio_Sesion, cuenta_Verificada, nombre_Usuario_Creador, nombre_Usuario_Ultimo_Modificador)  VALUES('cristina.liñan', 'tortu_marbella_93@hotmail.com', '$2a$10$.2VGBVkyf1zSE1fOwBIXXOMOZ6HfUn784vimes2mX6ILI89CzmTBu', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 0, 0, 1, 'enrique.escalante', 'enrique.escalante');
INSERT INTO Usuarios (nombre_Usuario, correo_Electronico, password, perfil_Id, fecha_Creacion, fecha_Ultima_Modificacion, habilitado, cuenta_Bloqueada, intentos_Inicio_Sesion, cuenta_Verificada, nombre_Usuario_Creador, nombre_Usuario_Ultimo_Modificador)  VALUES('invitado.invitado', 'invitado.invitado@hotmail.com', '$2a$10$.2VGBVkyf1zSE1fOwBIXXOMOZ6HfUn784vimes2mX6ILI89CzmTBu', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 0, 0, 1, 'enrique.escalante', 'enrique.escalante');

-- Tabla de UsuariosRoles
INSERT INTO Usuarios_Roles (nombre_Usuario, rol_Id) VALUES('enrique.escalante', 1);
INSERT INTO Usuarios_Roles (nombre_Usuario, rol_Id) VALUES('cristina.liñan', 2);
INSERT INTO Usuarios_Roles (nombre_Usuario, rol_Id) VALUES('invitado.invitado', 5);

INSERT INTO Puestos_Trabajo (nombre, fecha_Creacion, fecha_Ultima_Modificacion, nombre_Usuario_Creador, nombre_Usuario_Ultimo_Modificador) VALUES('Developer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'enrique.escalante', 'enrique.escalante');
INSERT INTO Puestos_Trabajo (nombre, fecha_Creacion, fecha_Ultima_Modificacion, nombre_Usuario_Creador, nombre_Usuario_Ultimo_Modificador)  VALUES('Logopeda', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'enrique.escalante', 'enrique.escalante');


UPDATE Perfiles_Usuarios SET puesto_Trabajo_Id = 1 WHERE id = 1;
UPDATE Perfiles_Usuarios SET puesto_Trabajo_Id = 2 WHERE id = 2;
UPDATE Perfiles_Usuarios SET puesto_Trabajo_Id = 1 WHERE id = 3;

INSERT INTO Servicios (nombre, fecha_Creacion, fecha_Ultima_Modificacion, nombre_Usuario_Creador, nombre_Usuario_Ultimo_Modificador) VALUES('Servicio de logopedia', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'enrique.escalante', 'enrique.escalante');
INSERT INTO Servicios (nombre, fecha_Creacion, fecha_Ultima_Modificacion, nombre_Usuario_Creador, nombre_Usuario_Ultimo_Modificador)  VALUES('Servicio de psicologia', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'enrique.escalante', 'enrique.escalante');


INSERT INTO Pacientes (nombre, colegio, primer_Apellido, fecha_Nacimiento, activo, lopd, consentimiento_Coordinacion_Escuelas, fecha_Creacion, fecha_Ultima_Modificacion, nombre_Usuario_Creador, nombre_Usuario_Ultimo_Modificador) VALUES('Juan', 'CEIP Juan Ramon Jimenez', 'García','2021-8-8', 1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'enrique.escalante', 'enrique.escalante');
INSERT INTO Pacientes (nombre, colegio, primer_Apellido, fecha_Nacimiento, activo, lopd, consentimiento_Coordinacion_Escuelas, fecha_Creacion, fecha_Ultima_Modificacion, nombre_Usuario_Creador, nombre_Usuario_Ultimo_Modificador) VALUES('Avril', 'CEIP Los olivos', 'Escalante','2022-7-7', 1, 1, 1,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'enrique.escalante', 'enrique.escalante');

INSERT INTO SESIONES (PACIENTE_ID,SERVICIO_ID,SUBJECT,DETAILS,SESSION_DATE,HORA,FECHA_CREACION,FECHA_ULTIMA_MODIFICACION, NOMBRE_USUARIO_CREADOR, NOMBRE_USUARIO_ULTIMO_MODIFICADOR,NOMBRE_USUARIO_PROPIETARIO) VALUES(1,1,'PRUEBASQL', 'ESTO ES UNA PRUEBA EN H2','2023-11-09 00:00:00','1970-01-01 12:00:00','2023-11-10 15:17:28.578','2023-11-10 15:17:28.578','enrique.escalante','enrique.escalante','enrique.escalante')