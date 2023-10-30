-- Tabla de Roles
INSERT INTO [Roles]([nombre]) VALUES('ROLE_MASTER');
INSERT INTO [Roles]([nombre]) VALUES('ROLE_ADMIN');
INSERT INTO [Roles]([nombre]) VALUES('ROLE_MANAGER');
INSERT INTO [Roles]([nombre]) VALUES('ROLE_RESPONSIBLE');
INSERT INTO [Roles]([nombre]) VALUES('ROLE_USER');

-- Tabla de PerfilesUsuarios
INSERT INTO [dbo].[PerfilesUsuarios]([nombre], [primerApellido]) VALUES('Enrique', 'Escalante');
INSERT INTO [dbo].[PerfilesUsuarios]([nombre], [primerApellido]) VALUES('Cristina', 'Liñan');

-- Tabla de Usuarios
INSERT INTO [dbo].[Usuarios]([nombreUsuario], [correoElectronico], [password], [perfilId], [fechaCreacion], [fechaUltimaModificacion], [habilitado], [cuentaBloqueada], [intentosInicioSesion], [cuentaVerificada], [nombreUsuarioCreador], [nombreUsuarioUltimoModificador]) VALUES('enrique.escalante', 'enrique.escalante@hotmail.es', '$2a$10$.2VGBVkyf1zSE1fOwBIXXOMOZ6HfUn784vimes2mX6ILI89CzmTBu', 1, GETDATE(), GETDATE(), 1, 0, 0, 1, 'enrique.escalante', 'enrique.escalante');
INSERT INTO [dbo].[Usuarios]([nombreUsuario], [correoElectronico], [password], [perfilId], [fechaCreacion], [fechaUltimaModificacion], [habilitado], [cuentaBloqueada], [intentosInicioSesion], [cuentaVerificada], [nombreUsuarioCreador], [nombreUsuarioUltimoModificador]) VALUES('cristina.liñan', 'tortu_marbella_93@hotmail.com', '$2a$10$.2VGBVkyf1zSE1fOwBIXXOMOZ6HfUn784vimes2mX6ILI89CzmTBu', 2, GETDATE(), GETDATE(), 1, 0, 0, 1, 'enrique.escalante', 'enrique.escalante');

-- Tabla de UsuariosRoles
INSERT INTO [dbo].[UsuariosRoles]([nombreUsuario], [rolId]) VALUES('enrique.escalante', 1);
INSERT INTO [dbo].[UsuariosRoles]([nombreUsuario], [rolId]) VALUES('cristina.liñan', 2);


INSERT INTO [dbo].[PuestosTrabajo]([nombre], [fechaCreacion], [fechaUltimaModificacion], [nombreUsuarioCreador], [nombreUsuarioUltimoModificador]) VALUES('Developer', GETDATE(), GETDATE(), 'enrique.escalante', 'enrique.escalante');
INSERT INTO [dbo].[PuestosTrabajo]([nombre], [fechaCreacion], [fechaUltimaModificacion], [nombreUsuarioCreador], [nombreUsuarioUltimoModificador]) VALUES('Logopeda', GETDATE(), GETDATE(), 'enrique.escalante', 'enrique.escalante');


UPDATE PerfilesUsuarios SET puestoTrabajoId = 1 WHERE id = 1;
UPDATE PerfilesUsuarios SET puestoTrabajoId = 2 WHERE id = 2;

INSERT INTO [dbo].[Servicios]([nombre], [fechaCreacion], [fechaUltimaModificacion], [nombreUsuarioCreador], [nombreUsuarioUltimoModificador]) VALUES('Servicio de logopedia', GETDATE(), GETDATE(), 'enrique.escalante', 'enrique.escalante');
INSERT INTO [dbo].[Servicios]([nombre], [fechaCreacion], [fechaUltimaModificacion], [nombreUsuarioCreador], [nombreUsuarioUltimoModificador]) VALUES('Servicio de psicologia', GETDATE(), GETDATE(), 'enrique.escalante', 'enrique.escalante');


INSERT INTO [dbo].[Pacientes]([nombre], [primerApellido], [fechaNacimiento], [activo], [lopd], [consentimientoCoordinacionEscuelas], [fechaCreacion], [fechaUltimaModificacion], [nombreUsuarioCreador], [nombreUsuarioUltimoModificador]) VALUES('Juan', 'García','2021-8-8', 1, 1, 1, GETDATE(), GETDATE(), 'enrique.escalante', 'enrique.escalante');
INSERT INTO [dbo].[Pacientes]([nombre], [primerApellido], [fechaNacimiento], [activo], [lopd], [consentimientoCoordinacionEscuelas], [fechaCreacion], [fechaUltimaModificacion], [nombreUsuarioCreador], [nombreUsuarioUltimoModificador]) VALUES('Avril', 'Escalante','2022-7-7', 1, 1, 1, GETDATE(), GETDATE(), 'enrique.escalante', 'enrique.escalante');
