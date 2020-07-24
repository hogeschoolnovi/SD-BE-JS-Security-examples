# Spring security voorbeeld
Een inleiding

## Inhoud
 * [Voorbereiding](#voorbereiding)
 * [Korte uitleg](#korte-uitleg)
    * [Gebruikersrollen](#gebruikersrollen)
    * [Rest endpoints](#rest-endpoints)
 * [Hoe te gebruiken](#hoe-te-gebruiken)
    * [Gebruiker aanmaken](#gebruiker-aanmaken)
    * [Inloggen](#inloggen)
    * [Rest endpoint benaderen met access-token](#rest-endpoint-benaderen-met-access-token)
 * [Beveiligingslek](#beveiligingslek) 
 * [Uitleg code](#uitleg-code)
    * [De payload-package](#de-payload-package-dto)
    * [Repository package](#repository-package)
    
## Voorbereiding
 * Pas de databaseinstellingen aan in `src/main/resources/application.properties`
    * Tip: Maak een nieuwe database aan voor deze code.
 * Start de applicatie: `mvnw spring-boot:run`
 * Voor de volgende SQL-query uit:
    ```postgresql
    INSERT INTO role(name) VALUES('ROLE_USER');
    INSERT INTO role(name) VALUES('ROLE_MODERATOR');
    INSERT INTO role(name) VALUES('ROLE_ADMIN');
    ```
De applicatie is nu klaar voor gebruik.

## Korte uitleg 
Dit project is een Spring security voorbeeld, waar een gebruiker geregistreerd kan worden, kan inloggen en vervolgens 
met de rest endpoint kan communiceren waar hij de authorisatie voor heeft.

### Gebruikersrollen
Dit voorbeeld maakt gebruik van drie user-rollen. `user`, `mod` & `admin`. Elke gebruiker kan 0 tot meerdere rollen 
hebben. Het is belangrijk om je te realiseren dat wanneer een gebruiker de `admin`-rol heeft dat deze dan niet
automatisch de `user` en `mod`-rollen heeft. 

###### Voorbeeld 
Je maakt een gebruiker aan met de admin-rol. Je logt in met deze gebruiker. Als je met deze gebruiker wilt communiceren
met een rest-endpoint dat alleen antwoord op gebruikers met de rol `user` dan geeft de applicatie de volgende error 
terug:
```
HTTP 401 Unauthorized
```

### Rest endpoints.
De back-end is op de volgende end-points te bereiken:
 1. `/api/auth/signup`
    * Hier kun je de volgende JSON sturen om een gebruiker aan te maken.
 2. `/api/auth/login`
    * Hier kun je login gegevens naar sturen. Je krijgt een Authorisatie-token terug.
 3. `/api/test/all`
    * Iedereen kan data uit deze end-point uitlezen.
 4. `/api/test/user`
    * Alleen (ingelogd) gebruikers met de user-rol kunnen data uit deze API uitlezen.
 5. `/api/test/mod`
     * Alleen (ingelogd) gebruikers met de mod-rol kunnen data uit deze API uitlezen.
 6. `/api/test/admin`
     * Alleen (ingelogd) gebruikers met de admin-rol kunnen data uit deze API uitlezen.
 
## Hoe te gebruiken.
Je kunt tegen de hierboven genoemde rest-points communiceren.

### Gebruiker aanmaken
Praat via Postman met de volgende link: `http://localhost:8080/api/auth/signup` en geef de volgende JSON in de body mee:

#### Gebruiker met userrol aanmaken
```json
{
    "username": "user",
    "email" : "user@user.com",
    "password" : "123456",
    "role": ["user"]
}
```
#### Gebruiker met mod- en userrol aanmaken
```json
{
    "username": "mod",
    "email" : "mod@mod.com",
    "password" : "123456",
    "role": ["mod", "user"]
}
```
#### Gebruiker met adminrol aanmaken
```json
{
    "username": "admin",
    "email" : "admin@admin.com",
    "password" : "123456",
    "role": ["admin"]
}
```

#### Gebruiker met alledrie de rollen (niet veilig)
```json
{
    "username": "superadmin",
    "email" : "superadmin@admin.com",
    "password" : "123456",
    "role": ["admin", "mod", "user"]
}
```

### Inloggen
Wanneer je inlogt geeft de backend-server een Json WebToken terug. Bewaar deze, want deze moet je meesturen.

Praat via Postman met de volgende link: `http://localhost:8080/api/auth/signin` en geef de volgende JSON in de body mee:
#### Inloggen user
```json
{
    "username":"user",
    "password":"123456"
}
```

#### Inloggen mod
```json
{
    "username":"mod",
    "password":"123456"
}
```

#### Inloggen admin
```json
{
    "username":"admin",
    "password":"123456"
}
```
#### Resultaat
De backend-server communiceert het volgende (soortgelijks) terug:
```json
{
    "id": 6,
    "username": "mod3",
    "email": "mod3@mod.com",
    "roles": [
        "ROLE_USER",
        "ROLE_MODERATOR"
    ],
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtb2QzIiwiaWF0IjoxNTk1NTg4MDk0LCJleHAiOjE1OTU2NzQ0OTR9.AgP4vCsgw5TMj_ePbPzMJXWWBNfFphJBHzAvTFyW9fzZ6UL-JO42pRq9puXAOlGh4hTijspAQAS-J8doHqADTA",
    "tokenType": "Bearer"
}
```

Wil je als ingelogde gebruiker nu tegen de beveiligde rest-points aanpraten dan moet je altijd `tokenType` en
`accesstoken` meesturen. Zie volgend kopje.

### Rest endpoint benaderen met access-token
Op het moment dat bovenstaande is gelukt, dan heb je van de server een Bearer + access  token ontvangen. Spring security
geeft deze uit en controleert op basis van die token wat de gebruiker wel of niet mag doen op de website. Dus willen we
praten met één van de drie beveiligde rest endpoints, dan moeten we token type + access token meegeven in postman. Dat
doen we zo:

![Plaatje postman met Authorization](img/auth_postman_example.png)

Onder het kopje headers voeg je als `Key` `Authorization` toe. Daarin zet je `<TOKEN TYPE> <SPATIE> <ACCESSTOKEN>`. 
Zonder de <>. Zie plaatje hierboven.

De volgende resultaten worden teruggegevn door de server, wanneer het succesvol is:

 1. `/api/test/all`
    * Iedereen kan data uit deze end-point uitlezen.
    * `Public Content.`
 2. `/api/test/user`
    * Alleen (ingelogd) gebruikers met de user-rol kunnen data uit deze API uitlezen.
    * `User Content.`
 3. `/api/test/mod`
     * Alleen (ingelogd) gebruikers met de mod-rol kunnen data uit deze API uitlezen.
     `Moderator Board.`
 4. `/api/test/admin`
     * Alleen (ingelogd) gebruikers met de admin-rol kunnen data uit deze API uitlezen.
     `Admin Board.`

## Beveiligingslek
In deze applicatie zit een beveiliginslek. Een stuk code dat nooit ijn productie zou mogen draaien. Kun je op basis van 
de hierboven uitgelegde rest endpoints bepalen wat er mis is (en wat je dus zelf niet moet doen in jouw eindopdracht)?

## Uitleg code
Dit hoofdstuk legt verschillende stukken code uit.

### De payload-package (DTO)
Dit voorbeeld maakt gebruik van DTO's. DTO's zijn Data Transfer Objects. Data Transfer Objects zijn objecten die
gebruikt worden om te communiceren tussen verschillende lagen. Deze zijn onderverdeeld in een request (te ontvangen 
objecten) en een response (antwoord-objecten) package.

#### Request package
Hier vind je `SignupRequest.java` en `LoginRequest.java`. De eerste klasse is het object dat binnenkomt om een gebruiker
te registreren. Het tweede object is het object dat binnenkomt om de login af te handelen. Tot nu toe hebben jullie 
geleerd om in de Controller-klasse objecten binnen te krijgen die 1 op 1 overeenkomen met je Entity-objecten. Dat is 
hier dus niet het geval. De controller klasse krijgt hier DTO-objecten binnen.

Je ziet in de `SignUpRequest.java` nieuwe annotaties terugkomen.
 * `@NotBlank`
 * `@Size`
 * `@Email`
 
Deze annotaties checken de geldigheid van de attributen van het object. Je hebt hiermee dus regels opstellen waaraan het 
DTO-object moet voldoen. Deze regels heb je opgesteld, maar die moet je ook nog afdwingen. Dat doen we in de controller.

Dit afdwingen gebeurt in de controller. Daar is ook een nieuwe annotatie verschenen: `@Valid`. Hiermee zeg je tegen
Spring: Deze rest endpoint krijgt de volgens DTO klasse binnen, deze DTO klasse heeft bepaalde regels. Controleer deze
en geef feedback wanneer deze regels niet gevolgd worden. Kijk zelf eens wat er gebeurd wanneer je bijvoorbeeld een
wachtwoord met vier tekens naar `http://localhost:8080/api/auth/signup` stuurt.

Dit werkt natuurlijk allemaal niet vanzelf. Om deze code te laten werken, hebben we een extra library nodig. Deze heet
`spring-boot-starter-validation`. Het onderstaande is dan ook aan de `pom.xml` toegevoegd.

```xml
<!-- Deze library gebruiken we om met Annotaties onze DTO's te controleren -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Meer lezen over de validatie kan hier: https://www.baeldung.com/spring-boot-bean-validation. Hier zie je dat je niet 
op DTO niveau hoeft te doen. Het kan ook op domeinklasse niveau. De foutmeldingen moeten nog wel netjes
teruggecommuniceerd worden. Dat gebeurt nu nog niet in de code. In de eerder genoemde link wordt een voorbeeld gegeven 
van hoe het kan.

De keuze is aan jou. Ga je DTO's gebruiken of niet. Je kunt op deze manier wel per HTTP Request zeer gedetailleerde 
regels opstellen die je vervolgens ook kunt controleren, maar daardoor krijg je wel veel code die op elkaar lijkt en
je moet extra code opstellen om het DTO-object om te zetten naar een Domeinklasse-object. Voor `SignupRequest.java`
gebeurt dat op dit moment (incorrect) in de `Authcontroller.java` - klasse. Zie code hieronder.

```java
// Create new user's account
User user = new User(signUpRequest.getUsername(),
    signUpRequest.getEmail(),
    encoder.encode(signUpRequest.getPassword()));

Set<String> strRoles = signUpRequest.getRole();
Set<Role> roles = new HashSet<>();

```

#### Response Package
In de response package staan, - en de naam verraadt het al een beetje- de antwoorden die de controller-laag terugstuurt.
In dit geval alle antwoorden die niet van het datatype `String` zijn. Dit voorbeeld bevat twee klassen. 
`MessageResponse.java` en `JwtResponse.java`. 

MessageResponse is een klasse die één attribuut bevat. Dit attribuut heeft het datatype String. Deze klasse wordt dan
ook gebruikt om of een error bericht, of een succesbericht terug te communiceren.

De `JwtResponse` klasse doet iets meer. Deze klasse bevat de attributen van de User-klasse die de ontwikkelaar nodig 
acht om terug te communiceren en de `accesstoken`. De access-token is een Json Web Token. Deze klasse wordt alleen terug
gecommuniceerd wanneer er een geslaagde inlogpoging is geweest. 

Het is daarna aan de frontend om de inlog-token te bewaren en deze mee te sturen bij elk request. Je kunt zelf voor 
de Json webtoken hier gedeeltelijk ontcijferen: https://jwt.io/. Kijk eens wat erin staat.

#### Eindopdracht
Wat je wilt gebruiken voor je eindopdracht is hier je eigen keuze. Ik zou wel de JwtResponse klasse houden en gebruiken.
Het gebruik van DTO's mag je zelf bepalen (en verantwoorden). Nog eens de gegevens links van dit hoofdstuk:
 * https://www.baeldung.com/spring-boot-bean-validation
 * https://jwt.io/
 
### Repository package
De repository-package ziet er uit zoals jullie gewend zijn. In dit geval zijn de klasse echter niet leeg. 
Dit komt, omdat we iets meer moeten dan alleen de standaard operaties. Om dit uit te voeren maken we gebruik van
keywords, zodat JPA precies weet welke Query uitgevoerd moet worden. Je kunt er hier meer over vinden: 
https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation

#### Voorbeeld uit de code
In `src/main/java/nl.novi.stuivenberg.springboot.example.repository.RoleRepository` staat de volgende code:

```java
    Boolean existsByEmail(String email);
```

We zeggen hier tegen JPA, geef ons een boolean-waarde terug op basis van de gegeven String email. Maar hoe weet JPA wat 
er verder gedaan moet worden? Dat komt door gebruik van key-woorden in de methode-naam. `existsBy` is zo'n key-woord. 
`Email` verwijst dan naar de kolomnaam uit de tabel (en de attribuut uit de entity-klasse). 
