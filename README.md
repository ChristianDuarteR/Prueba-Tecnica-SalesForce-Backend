# SkyLogistics Backend - Spring Boot + MySql

Este proyecto es un backend completo para una plataforma de gestion de entregas y clientes

- **Java 21**

- **Spring Boot 3**

- **MySql (Docker Container)**

- **Swagger UI (documentación interactiva)**

## 📁 Estructura del Proyecto

```bash
src/
└── main/
    ├── java/com/salesforce/skylogistics/
    │   ├── controller/          # Controladores REST
    │   ├── model/               # Entidades JPA (Cliente, Entrega)
    │   ├── repository/          # Repositorios JPA (ClienteRepository, EntregaRepository)
    │   ├── service/             # Interfaces de servicios
    │   ├── service/imp/         # Implementaciones de servicios
    │   └── SkyLogisticsApplication.java  # Clase principal de arranque
    │
    └── resources/
        ├── application.properties       # Configuración general del proyecto
        └── static/                      # Archivos estáticos si se requieren
```

## Funcionalidades principales

🛍️ CRUD de Clientes

📦 CRUD de Entregas

🧪 Swagger UI para probar la API visualmente

### Requisitos Previos
Para ejecutar este proyecto, necesitarás tener instalado:

- Java JDK 21.
- Un IDE de Java como IntelliJ IDEA, Eclipse.
- Maven para manejar las dependencias
- Docker para levantar una base de datos provisonal MySQL
- Un navegador web para interactuar con el servidor.

### Instalación 

1. Tener instalado Git en tu maquina local 
2. Elegir una carpeta en donde guardes tu proyecto
3. abrir la terminal de GIT --> mediante el clik derecho seleccionas Git bash here
4. Clona el repositorio en tu máquina local:
   ```bash
   git clone https://github.com/ChristianDuarteR/Prueba-Tecnica-SalesForce-Backend.git
   ```
5. Para que la aplicación funcione correctamente, debes tener una instancia de MySQL corriendo localmente puedes levantar una teniendo el Engine de Docker Corriendo y ejecutando:
     ```bash
    docker run --name skylogistics-mysql \
          -e MYSQL_ROOT_PASSWORD=1234 \
          -e MYSQL_DATABASE=skylogistics \
          -p 3306:3306 \
          -d mysql:8
    ```
7. Abre el proyecto con tu IDE favorito o navega hasta el directorio del proyecto 
8. Desde la terminal  para compilar el proyecto ejecuta:

   ```bash
   mvn clean install
   ```
7. compila el proyecto  

   ```bash
    mvn clean package
   ```
8. Corra el servidor en la clase SkyLogisticsApplication "main" o ejecute el siguiente comando desde consola luego de compilar
   
      ```bash
    java -jar target/skylogistics-0.0.1-SNAPSHOT.jar
   ```

9. Abre tu navegador y ve a:
   
      ```bash
    http://localhost:8080
   ```

## Endpoints disponibles

Clientes

![image](https://github.com/user-attachments/assets/028194cb-52d2-4e7c-9870-fcc462ad16f6)

Entregas

![image](https://github.com/user-attachments/assets/6ecbfbab-4932-4e31-84b9-007a8cae7a8f)


## Swagger UI

La documentación de la API está disponible en:

   ```bash
   http://localhost:8080/swagger-ui/index.html
   ```

## Built With
* [Maven](https://maven.apache.org/) - Dependency Management

## Autor

Desarrollado por [Christian Javier Duarte Rojas](https://github.com/ChristianDuarteR)

* **@ChristianDuarteR** - 

## Licencia

Este proyecto fue desarrollado con fines académicos/técnicos como parte de una prueba. El uso es libre y educativo.
