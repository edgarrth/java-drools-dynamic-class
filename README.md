# Drools POC (Proof of Concept)

## Descripción del Proyecto

**Drools POC** es una aplicación de demostración que integra **Apache Drools** (motor de reglas empresariales) con **Spring Boot**. El proyecto permite procesamiento dinámico de entidades genéricas mediante reglas de negocio programadas en lenguaje DRL (Drools Rule Language).

La aplicación demuestra cómo cargar y ejecutar reglas de Drools desde archivos externos, transformar datos genéricos basados en reglas específicas y exponer una API REST para orquestar el procesamiento.

---

## Tecnologías Principales

### Backend Framework
- **Spring Boot 3.4.1** - Framework para aplicaciones Java modulares y escalables
- **Spring Web** - Para construcción de REST APIs
- **Java 17** - Lenguaje de programación

### Motor de Reglas
- **Apache Drools 9.44.0.Final** - Motor de reglas empresariales
  - `kie-api` - API del kernel de inteligencia empresarial
  - `drools-core` - Núcleo del motor Drools
  - `drools-compiler` - Compilador de reglas DRL
  - `drools-decisiontables` - Soporte para tablas de decisión
  - `kie-ci` - Integración continua para KIE

### Procesamiento de Datos
- **Jackson** - Librería para serialización/deserialización JSON
- **Jackson Databind** - Binding de datos JSON a objetos Java

### Desarrollo
- **Spring Boot DevTools** - Herramientas de desarrollo para recarga automática

### Testing
- **Spring Boot Starter Test** - Framework de testing con JUnit 5

---

## Clases Principales

### 1. **DroolsPocApplication** (`main/java/com/poc/droolspoc/DroolsPocApplication.java`)
- **Clase principal** de la aplicación Spring Boot
- Punto de entrada que inicia la aplicación
- Configura el escaneo de componentes para todos los paquetes del proyecto
- Anotaciones:
  - `@SpringBootApplication` - Habilita la configuración automática de Spring Boot
  - `@ComponentScan` - Escanea componentes en paquetes específicos

### 2. **DroolsService** (`service/DroolsService.java`)
- **Servicio principal** para aplicar reglas de Drools
- Responsabilidades:
  - Cargar archivos de reglas (.drl) basados en el tipo de entidad
  - Crear y compilar el contenedor KIE (Knowledge Container)
  - Ejecutar las reglas sobre las entidades genéricas
  - Retornar entidades transformadas
- Método clave: `applyRules(GenericEntity)` - Aplica reglas y transforma datos
- Directorio de reglas: `C:/drools-rules/` (configurable)

### 3. **DroolsController** (`controller/DroolsController.java`)
- **Controlador REST** que expone la API de transformación
- Clase anidada: `TransformController`
- Endpoint:
  - `POST /api/transform` - Recibe una entidad genérica y retorna la entidad transformada
  - Ejemplo: `POST http://localhost:8080/api/transform`
- Inyección de dependencias: Recibe `DroolsService` como Bean

### 4. **GenericEntity** (`domain/GenericEntity.java`)
- **Modelo de datos genérico** para entrada
- Atributos:
  - `type` (String) - Tipo de entidad (ej: "person", "vehicle")
  - `body` (Map<String, Object>) - Cuerpo dinámico con datos variados
- Define la estructura para cualquier tipo de documento
- Basado en principios de entidad genérica para flexibilidad

### 5. **TransformedEntity** (`domain/TransformedEntity.java`)
- **Modelo de datos** para salida transformada
- Atributos:
  - `result` (Map<String, Object>) - Resultado transformado por las reglas
- Contiene el resultado después de aplicar las reglas de negocio

### 6. **DroolsConfig** (`config/DroolsConfig.java`)
- **Clase de configuración** (actualmente deshabilitada)
- Potencial para:
  - Definir Beans de configuración de KIE
  - Crear contenedores KIE reutilizables
  - Centralizar la configuración de Drools

---

## Estructura del Proyecto

```
drools-dynamic-class/
├── pom.xml                                  # Configuración Maven
├── mvnw, mvnw.cmd                          # Maven wrapper
├── src/
│   ├── main/
│   │   ├── java/com/poc/droolspoc/
│   │   │   ├── DroolsPocApplication.java    # App principal
│   │   │   ├── config/
│   │   │   │   └── DroolsConfig.java        # Config Drools
│   │   │   ├── controller/
│   │   │   │   └── DroolsController.java    # REST Controller
│   │   │   ├── service/
│   │   │   │   └── DroolsService.java       # Servicio de reglas
│   │   │   └── domain/
│   │   │       ├── GenericEntity.java       # Entidad de entrada
│   │   │       └── TransformedEntity.java   # Entidad de salida
│   │   └── resources/
│   │       ├── application.properties        # Propiedades de la app
│   │       ├── rules/
│   │       │   ├── person.drl               # Reglas para Person
│   │       │   └── vehicle.drl              # Reglas para Vehicle
│   │       └── json/
│   │           ├── person.json              # Datos de ejemplo
│   │           └── vehicle.json             # Datos de ejemplo
│   └── test/
│       └── java/com/poc/droolspoc/
│           └── DroolsPocApplicationTests.java # Tests
```

---

## Archivos de Reglas (DRL)

### person.drl
Define reglas para transformación de entidades de tipo "person":
```
rule "Transform Person"
when
    $input : GenericEntity(type == "person")
then
    Map<String, Object> body = $input.getBody();
    Map<String, Object> transformedResult = new java.util.HashMap<>();
    transformedResult.put("NombreCliente", body.get("nombre"));
    transformedResult.put("ApellidoCliente", body.get("apellido"));
    transformedResult.put("EdadCliente", body.get("edad"));
    transformedResult.put("DirecciónCasa", body.get("direccion"));
    transformedEntity.setResult(transformedResult);
end
```

### vehicle.drl
Define reglas para transformación de entidades de tipo "vehicle"

---

## Cómo Ejecutar

### Requisitos Previos
- Java 17 o superior
- Maven 3.8+
- Crear directorio de reglas: `C:/drools-rules/`

### Instalación y Inicio

1. **Clonar o descargar el proyecto**
   ```bash
   cd drools-dynamic-class
   ```

2. **Compilar el proyecto**
   ```bash
   ./mvnw clean install
   ```

3. **Ejecutar la aplicación**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Verificar que está corriendo**
   - La aplicación estará disponible en: `http://localhost:8080`

---

## Ejemplo de Uso - API REST

### Solicitud POST

**URL:** `POST http://localhost:8080/api/transform`

**Headers:**
```
Content-Type: application/json
```

**Body (ejemplo person):**
```json
{
  "type": "person",
  "body": {
    "nombre": "Juan",
    "apellido": "Pérez",
    "edad": 30,
    "direccion": "Calle Principal 123"
  }
}
```

**Respuesta Esperada:**
```json
{
  "type": "person",
  "transformedEntity": {
    "result": {
      "NombreCliente": "Juan",
      "ApellidoCliente": "Pérez",
      "EdadCliente": 30,
      "DirecciónCasa": "Calle Principal 123"
    }
  }
}
```

---

## Características Principales

✅ **Carga Dinámica de Reglas** - Las reglas se cargan desde archivos externos  
✅ **Transformación de Datos** - Transforma entidades genéricas según reglas específicas  
✅ **API REST** - Expone funcionalidad mediante endpoints REST  
✅ **Flexible** - Soporta cualquier tipo de entidad mediante estructura genérica  
✅ **Integrado con Spring Boot** - Aprovecha todo el ecosistema Spring  
✅ **Motor de Reglas Empresariales** - Utiliza el potente motor Drools  

---

## Flujo de Ejecución

```
1. Cliente envía POST a /api/transform
   ↓
2. DroolsController recibe GenericEntity
   ↓
3. DroolsService.applyRules(GenericEntity)
   ↓
4. Carga archivo .drl según tipo de entidad
   ↓
5. Compila y crea KieSession (sesión de Drools)
   ↓
6. Inserta datos en la sesión y dispara reglas
   ↓
7. Las reglas transforman los datos
   ↓
8. Retorna TransformedEntity con resultado
   ↓
9. Controller devuelve respuesta JSON al cliente
```

---

## Extensibilidad

Para agregar nuevos tipos de entidades:

1. **Crear archivo de reglas** en `C:/drools-rules/` con nombre `{tipo}.drl`
2. **Definir reglas** siguiendo la sintaxis DRL
3. **Enviar solicitud** POST con el nuevo tipo en GenericEntity
4. El servicio cargará automáticamente las reglas

---

## Notas Importantes

- Las reglas deben estar en el directorio configurado: `C:/drools-rules/`
- El sistema es case-sensitive para tipos de entidades
- Las reglas se compilan dinámicamente cada vez que se invocan
- TransformedEntity actúa como variable global en la ejecución de reglas

---

## Autor
POC - Demostración de integración Drools + Spring Boot

## Licencia
Proyecto de demostración - Uso libre

---

**Última actualización:** Mayo 2026

