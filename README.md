# Registros App

Aplicación Android desarrollada en Kotlin siguiendo los principios de **Clean Architecture** y el patrón de diseño **MVVM** (Model-View-ViewModel). La app permite la gestión académica de Estudiantes y Asignaturas.

## Características

- **Gestión de Estudiantes:** Registro, edición, eliminación y listado.
- **Gestión de Asignaturas:** Control de códigos, nombres, aulas y créditos.
- **Validaciones Avanzadas:** - Limpieza de datos con `.trim()`.
  - Verificación de nombres únicos (case-insensitive).
  - Validación de formatos (Email, Edad, Créditos).
- **Interfaz Moderna:** Desarrollada íntegramente con Jetpack Compose y Material Design 3.
- **Persistencia Local:** Uso de Room Database con soporte para migraciones.

## Tecnologías Utilizadas

- **Kotlin:** Lenguaje principal.
- **Jetpack Compose:** UI declarativa.
- **Hilt:** Inyección de dependencias.
- **Room:** Abstracción de base de datos SQLite.
- **Coroutines & Flow:** Manejo de asincronía y flujo de datos en tiempo real.
- **Kotlin Serialization:** Navegación con seguridad de tipos.

## Arquitectura

El proyecto está dividido en capas para garantizar la escalabilidad y facilidad de pruebas:

1. **Data:** Implementación de DAOs, Repositorios, Entidades y Mappers.
2. **Domain:** Modelos de dominio, Interfaces de Repositorios y Casos de Uso.
3. **Presentation:** ViewModels, UI States, Screens y Componentes de Compose.
