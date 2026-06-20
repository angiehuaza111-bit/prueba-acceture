# API de Franquicias

API reactiva para gestión de franquicias, sucursales y productos.

## Stack Tecnológico

- **Java 21** + **Spring Boot 3.2** + **Spring WebFlux** (programación reactiva)
- **R2DBC** + **PostgreSQL 16** (persistencia reactiva)
- **Redis 7** (caché reactiva)
- **Docker** + **Docker Compose** (contenedores)
- **Springdoc OpenAPI** (documentación Swagger)
- **Terraform** (IaC para AWS)

## Documentación Swagger

Una vez corriendo la aplicación:

| Recurso | URL |
|---------|-----|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/api-docs` |

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/franchises` | Crear franquicia |
| PUT | `/api/franchises/{id}/name` | Actualizar nombre de franquicia |
| POST | `/api/franchises/{franchiseId}/branches` | Agregar sucursal |
| PUT | `/api/branches/{id}/name` | Actualizar nombre de sucursal |
| POST | `/api/branches/{branchId}/products` | Agregar producto |
| PUT | `/api/products/{id}/name` | Actualizar nombre de producto |
| PATCH | `/api/products/{id}/stock` | Modificar stock de producto |
| GET | `/api/branches/{branchId}/products` | Listar productos de una sucursal |
| DELETE | `/api/products/{id}` | Eliminar producto |
| GET | `/api/franchises/{franchiseId}/top-products` | Producto con más stock por sucursal |

## Principios SOLID

- **S**: Cada clase tiene una responsabilidad única (controllers, services, repositories)
- **O**: Abierto a extensión vía interfaces (`FranchiseService`)
- **L**: Las implementaciones son sustituibles por sus interfaces
- **I**: Interfaces segregadas y específicas
- **D**: Inyección de dependencias vía constructor (Spring DI)

## Ejecución Local

### Requisitos

- Docker y Docker Compose

### Pasos

```bash
# Clonar repositorio
git clone <repo-url>
cd prueba-franquicias

# Construir y ejecutar con Docker Compose
docker-compose up --build
```

La aplicación estará disponible en `http://localhost:8080`.

### Ejemplos con curl

```bash
# Crear franquicia
curl -X POST http://localhost:8080/api/franchises \
  -H "Content-Type: application/json" \
  -d '{"name":"McBurger"}'

# Agregar sucursal
curl -X POST http://localhost:8080/api/franchises/1/branches \
  -H "Content-Type: application/json" \
  -d '{"name":"Sucursal Centro"}'

# Agregar producto
curl -X POST http://localhost:8080/api/branches/1/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Hamburguesa Clásica","stock":50}'

# Modificar stock
curl -X PATCH http://localhost:8080/api/products/1/stock \
  -H "Content-Type: application/json" \
  -d '{"stock":100}'

# Listar productos de una sucursal
curl http://localhost:8080/api/branches/1/products

# Eliminar producto
curl -X DELETE http://localhost:8080/api/products/1

# Top productos por sucursal
curl http://localhost:8080/api/franchises/1/top-products
```

## Despliegue en AWS con Terraform

```bash
cd terraform
terraform init
terraform plan -var="db_user=postgres" -var="db_password=secreto" \
  -var="app_image=tu-imagen:tag" \
  -var='subnet_ids=["subnet-xxx","subnet-yyy"]'
terraform apply
```

## Estructura del Proyecto

```
prueba-franquicias/
├── src/main/java/com/franquicias/
│   ├── config/        → Configuraciones (OpenAPI, Redis, Database)
│   ├── controller/    → Controladores REST con @RestController
│   ├── dto/           → Objetos de transferencia
│   ├── model/         → Entidades de dominio
│   ├── repository/    → Repositorios R2DBC + Redis
│   └── service/       → Lógica de negocio reactiva
├── src/main/resources/
│   ├── application.yml
│   └── schema.sql
├── terraform/         → Infraestructura como código (AWS)
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```
