// Script de inicio para creación de colecciones e indices
conn = new Mongo();

// conexion a la base de datos admin
db = conn.getDB("admin");
// creacion de usuarios
db.createUser({user: "api", pwd: "4P1D4t4b4s3.", roles: ["readWrite", "dbAdmin"]})

// conexion a la base de datos amway
db = conn.getDB("amway_db");

// creacion de usuarios
db.dropDatabase();
db.createUser({user: "api", pwd: "4P1D4t4b4s3.", roles: ["readWrite", "dbAdmin"]})

db.createCollection('zonas');
db.zonas.ensureIndex({'codigo': 1, 'nombre': 1, 'nivel': 1, 'padre': 1}, {unique: true});

db.createCollection('empresarios');
db.empresarios.ensureIndex({'imc_number': 1});

db.createCollection('usuarios');
db.usuarios.createIndex({'imc_number': 1, 'rol': 1}, {unique: true});
db.usuarios.createIndex({'email': 1, 'rol': 1}, {unique: true});

db.createCollection('tipometa');
db.tipometa.createIndex({'nombre': 1}, {unique: true});

db.createCollection('usuariotipometa');
db.usuariotipometa.createIndex({'usuario': 1, 'tipometa': 1, 'periodo': -1}, {unique: true});

db.createCollection('meta');
db.usuariotipometa.createIndex({'usuariotipometa': 1}, {unique: true});

db.createCollection('reiniciopass');
db.reiniciopass.createIndex({'usuario': 1, 'estado': 1, 'fecha_reinicio': 1}, {unique: true});
db.reiniciopass.createIndex({'password': 1, 'estado': 1}, {unique: true});

load('./zones.js');
load('./tipoMeta.js');