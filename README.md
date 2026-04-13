# ♟️ Juego de Damas

Juego de Damas clasico desarrollado en **Java** con interfaz gráfica en **JavaFX**, siguiendo el patrón de arquitectura **MVC (Modelo - Vista - Controlador)**.

---

## Vista (Interfaz gráfica)

> Menu principal con animaciones, tablero interactivo 8x8, panel de puntuaciones y pantalla de victoria.

---

## 🚀 Características

- Tablero interactivo de 8x8 con piezas blancas y negras
- Movimientos validos resaltados visualmente al seleccionar una pieza
- Sistema de captura obligatoria y multicaptura
- Coronación de piezas (conversion a Dama)
- Puntaje en tiempo real para ambos jugadores
- Panel de victoria al finalizar la partida
- Historial de victorias que se guarda en archivo y carga entre partidas
- Animaciones en botones del menu
- Pantalla de instrucciones

---

## 🛠️ Tecnologías

| Tecnologia | Uso |

| Java       | Logica del juego |
| JavaFX     | Interfaz grafica |
| FXML       | Definicion de vistas |
| VS Code    | Entorno de desarrollo |

---

## 📁 Estructura del proyecto

```
Damas/
├── src/
│   ├── Main.java                          # Punto de entrada de la aplicación
│   ├── model/
│   │   ├── Damas.java                     # Lógica principal del juego
│   │   └── Piezas.java                    # Representación de cada pieza
│   ├── controller/
│   │   ├── TableroController.java         # Controlador del tablero y partida
│   │   ├── MenuController.java            # Controlador del menú principal
│   │   └── InstruccionesController.java   # Controlador de instrucciones
│   ├── view/
│   │   ├── tablero.fxml                   # Vista del tablero de juego
│   │   ├── menu.fxml                      # Vista del menú principal
│   │   └── instrucciones.fxml             # Vista de instrucciones
│   └── img/
│       ├── MENU.png
│       ├── botonInicioJuego.png
│       └── verInstruccioness.png
├── bin/                                   # Archivos compilados (.class)
├── lib/                                   # Dependencias
└── .vscode/
    ├── settings.json
    └── launch.json
```

---

## ⚙️ Requisitos previos

- **Java JDK 11** o superior
- **JavaFX SDK** (compatible con tu versión de JDK)
- **Visual Studio Code** con las extensiones:
- Extension Pack for Java

---

## 🔧 Instalación y ejecución

### 1. Clona el repositorio

```bash
git clone https://github.com/LewisRod/LewisRodriguez_DAMAS.git
```

### 2. Configura JavaFX

Descarga el [JavaFX SDK](https://openjfx.io/) y asegurate de que el archivo `.vscode/launch.json` apunte correctamente a la carpeta `lib` de tu JavaFX SDK.

### 3. Ejecuta el proyecto

Abre la carpeta `Damas/` en VS Code ejecuta desde la terminal:


---

## 🎮 Cómo jugar

1. En el menu principal, haz clic en **Iniciar Juego**
2. Las **piezas blancas** siempre comienzan primero
3. Haz clic en una pieza para seleccionarla — los movimientos válidos se resaltarán
4. Haz clic en una casilla resaltada para mover
5. Si hay una captura disponible, **es obligatorio realizarla**
6. Una pieza que llega al extremo opuesto del tablero se convierte en **Dama** y puede moverse en todas las direcciones
7. Gana el jugador que capture todas las piezas del oponente

---

## 🏗️ Arquitectura MVC

| Capa            | Clase              | Responsabilidad |
|---|---|--       |
| **Modelo**      | `Damas.java`       | Logica del tablero, movimientos, capturas, turnos y puntaje |
| **Modelo**      | `Piezas.java`      | Representa una pieza con su color y estado (normal/dama) |
| **Vista**       | `*.fxml`           | Definición de la interfaz gráfica en FXML |
| **Controlador** | `TableroController.java` | Maneja eventos del tablero y actualiza la vista |
| **Controlador** | `MenuController.java` | Navegación entre pantallas y animaciones del menú |

---

## 📄 Licencia

Este proyecto es de uso educativo,Asigando a Lewis Rodriguez.

---

## 👤 Autor

Desarrollado como proyecto de programacion 3 por Lewis Rodriguez #1000-4335
