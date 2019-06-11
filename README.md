# idprovider
Aquest es un dels servidors del meu TFG, es l'encarregat de crear i firmar els tokens (JWT)

## MongoDb
Per funcionar necesita una base de dades mongodb corrent en el port 27017 i haver creat un usuari i una col·lecció amb els seguents comandos.

La base de dades ha de tenir el nom id_provider_db. El usuari es te el nom root_provider amb contrassenya secret_provider i role root.

```bash
>use id_provider_db
>db
>db.createUser(
	{
	user: "root_provider",
	pwd: "secret_provider",
	roles: [{role: "root",db: "admin"}]
	})
```

## Keystore
Tambe utilitza un keystore.pkcs12 on guarda les claus RSA que utilitza per firma els tokens. 
Per crear aquest magatzem amb les claus publica/privada ho fem amb el comando keytool, aixo ho fem des de la carpeta src/resources/keystore que es on volem crear el magatzem.

```bash
>keytool -genkeypair -alias idprovider -storetype pkcs12 -keyalg RSA -keysize 2048 -keystore keystore.pkcs12 -validity 3650 -dname "CN=localhost, OU=idProvider, O=UPC, L=Barcelona, S=Catalunya, C=CA" -storepass secret
```

Tenim una altre comando per listar la informacio del keystore.pkcs12

```bash
>keytool -v -list -keystore keystore.pkcs12
```
