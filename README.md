# 🧬 **GlobalDS – API Mutantes**

**Autor:** Lucas Schiaffino
**Legajo:** 50711
**Materia:** Global – Desarrollo de Software
**Año:** 2025

---

# 📌 Descripción General

Este proyecto implementa la API REST del desafío **Mutantes**, cuyo objetivo es determinar si una secuencia de ADN corresponde a un **mutante** o a un **humano**.

El análisis se realiza detectando **al menos dos** secuencias de **4 letras consecutivas** (A, T, C, G) de forma:

* ➡️ Horizontal
* ⬇️ Vertical
* ↘️ Diagonal descendente
* ↗️ Diagonal ascendente

Además, la aplicación incorpora:

* ✔️ Base de datos **H2 en memoria**
* ✔️ Hash **SHA-256** para evitar análisis repetidos
* ✔️ Validaciones personalizadas (@Valid + validador propio)
* ✔️ Manejo global de excepciones
* ✔️ Endpoints principales + optativos implementados
* ✔️ **DELETE /mutant/{hash}**
* ✔️ **/stats con filtros de fecha**
* ✔️ **Rate Limiting (10 req/min/IP)**
* ✔️ **Swagger UI** integrado
* ✔️ **JaCoCo** con +80% de cobertura
* ✔️ **Despliegue en Render**
* ✔️ **Diagramas UML** (DS completo)

---

# 🗂️ **Arquitectura del Proyecto (estructura real)**

```
src/
└── main/
    ├── java/
    │   └── org/globalds/mutantes/
    │       ├── config/
    │       │   ├── RateLimitFilter.java
    │       │   └── SwaggerConfig.java
    │       ├── controller/
    │       │   ├── HealthController.java
    │       │   ├── HomeController.java
    │       │   └── MutantController.java
    │       ├── dto/
    │       │   ├── DnaRequest.java
    │       │   └── StatsResponse.java
    │       ├── entity/
    │       │   └── DnaRecord.java
    │       ├── exception/
    │       │   ├── DnaHashCalculationException.java
    │       │   └── GlobalExceptionHandler.java
    │       ├── repository/
    │       │   └── DnaRecordRepository.java
    │       ├── service/
    │       │   ├── MutantDetector.java
    │       │   ├── MutantService.java
    │       │   └── StatsService.java
    │       ├── validation/
    │       │   ├── ValidDnaSequence.java
    │       │   └── ValidDnaSequenceValidator.java
    │       └── MutantesApplication.java
    └── resources/
        └── application.properties

└── test/
    └── java/
        └── org/globalds/mutantes/
            ├── config/
            │   └── RateLimitFilterTest.java
            ├── controller/
            │   ├── MutantControllerTest.java
            │   └── MutantControllerDeleteTest.java
            ├── service/
            │   ├── MutantDetectorTest.java
            │   ├── MutantServiceTest.java
            │   ├── MutantServiceDeleteTest.java
            │   ├── StatsServiceTest.java
            │   └── StatsServiceWithFilterTest.java
            └── MutantesApplicationTests.java
```

---

# 📬 **Endpoints Implementados**

---

## ▶️ **POST /mutant**

Analiza el ADN.

**200 OK** → mutante
**403 Forbidden** → humano
**400 Bad Request** → error de validación

Request:

```json
{
  "dna": [
    "ATGCGA",
    "CAGTGC",
    "TTATGT",
    "AGAAGG",
    "CACCTA",
    "TCACTG"
  ]
}
```

---

## ❌ **DELETE /mutant/{hash}**

Elimina un ADN previamente analizado (optativo Nivel 2).

**204 No Content** → borrado
**404 Not Found** → no existe

---

## 📊 **GET /stats**

Estadísticas globales:

```json
{
  "count_mutant_dna": 3,
  "count_human_dna": 1,
  "ratio": 3.0
}
```

---

## 📅 **GET /stats?startDate&endDate**

Ejemplo:

```
/stats?startDate=2025-01-01&endDate=2025-01-07
```

Retorna **mutantes**, **humanos** y **ratio** dentro de un rango.

---

# 🧠 **Lógica del MutantDetector**

* Recorre la matriz NxN
* Evalúa 4 direcciones
* Usa un contador de secuencias encontradas
* Si encuentra **> 1**, retorna `true`

---

# 🛡️ Validaciones

Implementadas con:

* `@Valid`
* `@NotNull`
* `@Size`
* Anotación personalizada `@ValidDnaSequence`

Incluye chequeos:

* Matriz cuadrada NxN
* ADN no nulo / no vacío
* Solo caracteres válidos A/T/C/G
* Tamaño máximo **1000x1000** (optativo Nivel 3)

Errores manejados con:

✔ `GlobalExceptionHandler`
✔ Mensajes claros para el docente

---

# 🔐 Rate Limiting (Optativo Nivel 2)

Se implementó un filtro:

```
10 requests / minuto / IP
```

Excepciones (NO se limita):

* `/h2-console/**`
* `/swagger-ui/**`
* `/v3/api-docs/**`
* `/health`

Respuesta en exceso:

```
429 Too Many Requests
```

---

# 🗄️ H2 Database

URL:

```
http://localhost:8080/h2-console
```

Credenciales:

```
JDBC URL: jdbc:h2:mem:mutantsdb
User: sa
Password: 
```

*(espacio para captura de H2)*

---

# 📘 Swagger UI

Documentación disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

*(espacio para captura de Swagger)*

---

# 🔄 Despliegue en Render

### 🟦 Build local

```bash
./gradlew clean build
java -jar build/libs/mutantes-0.0.1-SNAPSHOT.jar
```

### 🟪 Configuración en Render

* Render asigna `PORT` automáticamente
* Comando:

```
java -jar mutantes-0.0.1-SNAPSHOT.jar
```

### 🟩 HomeController

Render siempre abre `/`.
Para evitar “Whitelabel Error Page”:

```java
@GetMapping("/")
public String redirectToSwagger() {
    return "redirect:/swagger-ui/index.html";
}
```

Esto permite que **cualquier persona entre directo a Swagger**, sin error.

*(espacio para captura de Render funcionando)*

---

# 🧪 Tests y Coverage (JaCoCo)

✔ Tests de controlador
✔ Tests de servicio
✔ Tests de detector
✔ Tests de repositorio (mockeado)
✔ Tests de filtros
✔ Tests de reglas de negocio
✔ >80% de cobertura total

*(Espacio para captura de JaCoCo)*

---

# 🧬 Diagrama de Secuencia Completo

*(espacio reservado para la captura del DS)*

---

# ✅ **Conclusión**

El proyecto cumple **todos los requisitos obligatorios** y **todos los optativos** del programa:

* Arquitectura en capas
* Validaciones robustas
* Persistencia correctamente implementada
* Documentación en Swagger
* Manejo de excepciones
* Código limpio y escalable
* Tests + JaCoCo
* Filtros, caching por hash, y rate limit
* Despliegue en la nube (Render)
* Diagramas UML profesionales