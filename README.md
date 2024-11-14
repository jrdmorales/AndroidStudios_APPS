# Verduritas SA - Gestión de Cultivos

## Descripción

La aplicación "Verduritas SA" está diseñada para permitir la gestión de cultivos orgánicos, donde los usuarios pueden registrar nuevos cultivos, calcular la fecha probable de cosecha y ver un listado de los cultivos activos. La aplicación también permite la autenticación de usuarios mediante Firebase, tanto con correo electrónico como con Google, y almacena los datos de los cultivos en Firestore.

## Características

- **Autenticación de usuario**: Registro e inicio de sesión mediante correo electrónico y Google.
- **Gestión de cultivos**: Los usuarios pueden agregar, editar y eliminar cultivos. Los cultivos incluyen información como el nombre del cultivo, fecha de siembra y tipo de planta.
- **Cálculo de la fecha de cosecha**: Al agregar un nuevo cultivo, la aplicación calcula automáticamente la fecha probable de cosecha en base al tipo de planta seleccionado.
- **Visualización en tiempo real**: Los cultivos registrados se actualizan en tiempo real en la pantalla principal.

## Requisitos

- **Lenguaje de desarrollo**: Java
- **Android Studio**: Última versión de Android Studio.
- **Firebase**: Firebase Authentication y Firestore.

## Instalación

1. **Configura Firebase**:
    - Crea un proyecto en Firebase.
    - Agrega el archivo `google-services.json` a tu proyecto en Android Studio.
    - Asegúrate de habilitar Firebase Authentication y Firestore en tu consola de Firebase.

2. **Dependencias**:
    - En el archivo `build.gradle` de tu módulo, agrega las siguientes dependencias:

    ```gradle
    implementation 'com.google.firebase:firebase-auth:21.0.0'
    implementation 'com.google.firebase:firebase-firestore:24.0.0'
    ```

    - No olvides aplicar el plugin de Google Services en el archivo `build.gradle` del proyecto:

    ```gradle
    apply plugin: 'com.google.gms.google-services'
    ```

3. **Configuración en Firebase**:
    - Registra la cuenta de correo `clases.inacap.mafer@gmail.com` con permisos de lectura en tu proyecto de Firebase.

4. **Ejecutar la aplicación**:
    - Conecta un dispositivo Android o usa un emulador.
    - Compila y ejecuta el proyecto desde Android Studio.

## Estructura del Proyecto

- **MainActivity**: Pantalla principal donde se muestra el listado de cultivos y permite la navegación a las pantallas de registro y ajustes.
- **LoginActivity**: Pantalla para iniciar sesión con correo electrónico y Google.
- **RegisterActivity**: Pantalla para el registro de nuevos usuarios.
- **CultivoActivity**: Pantalla donde los usuarios pueden agregar o editar un cultivo.
- **FirebaseService**: Servicio que maneja la interacción con Firebase para registrar usuarios, autenticar sesiones y gestionar cultivos.

## Funcionalidades

### 1. Registro de Usuario
- Permite el registro con correo electrónico y contraseña, y la opción de iniciar sesión con Google.
- Los datos del usuario (nombre, email, país, género) se almacenan en Firestore en la colección "usuarios".

### 2. Inicio de Sesión
- Los usuarios pueden iniciar sesión usando su correo electrónico o su cuenta de Google.
- Tras el inicio de sesión, el usuario es redirigido a la pantalla principal.

### 3. Gestión de Cultivos
- Los usuarios pueden agregar, editar y eliminar cultivos.
- Al agregar un cultivo, se debe seleccionar el tipo de planta (Tomates, Cebollas, Lechugas, Apio, Choclo) y la fecha de siembra.
- La fecha de cosecha se calcula automáticamente sumando días específicos a la fecha de siembra (según el tipo de cultivo).
- Los cultivos se almacenan en Firestore y se muestran en la pantalla principal.

### 4. Firebase Firestore
- Se almacenan los datos de los cultivos y los usuarios en Firestore.
- La vista del listado de cultivos se actualiza en tiempo real cuando se agregan, editan o eliminan cultivos.

## Paleta de Colores

| Muestra   | Código  | Usos                               |
|-----------|---------|------------------------------------|
| ![#9CEC32](https://via.placeholder.com/15/9CEC32/000000?text=+) | #9CEC32  | Color oscuro del gradiente para botones y color principal de vistas interiores. |
| ![#E8FACE](https://via.placeholder.com/15/E8FACE/000000?text=+) | #E8FACE  | Color claro del gradiente, fondo de botones eliminar. |
| ![#14AF61](https://via.placeholder.com/15/14AF61/000000?text=+) | #14AF61  | Color para botones de inicio. |
| ![#48760B](https://via.placeholder.com/15/48760B/000000?text=+) | #48760B  | Colores de bordes de botones. |
| ![#F8FDF2](https://via.placeholder.com/15/F8FDF2/000000?text=+) | #F8FDF2  | Color fondo de vistas interiores. |
| ![#3A590A](https://via.placeholder.com/15/3A590A/000000?text=+) | #3A590A  | Color letras de inicio. |
| ![#424242](https://via.placeholder.com/15/424242/000000?text=+) | #424242  | Color letras interiores y flechas de formulario. |
| ![#FFFFFF](https://via.placeholder.com/15/FFFFFF/000000?text=+) | #FFFFFF  | Color interior de textbox y ventana de botones editar. |

## Pruebas

### Registro de Usuario
- **Pasos**: Abrir la aplicación, presionar "No tienes cuenta, regístrate", llenar el formulario de registro con los datos solicitados y presionar "Registrarse".
- **Resultado esperado**: El usuario es redirigido al home con sesión activa.

### Logeo de Usuario
- **Pasos**: Abrir la aplicación, ingresar email y contraseña, presionar "Login".
- **Resultado esperado**: El usuario es redirigido al home con sesión activa.

### Logeo con Google
- **Pasos**: Abrir la aplicación, presionar "Continuar con Google", seleccionar cuenta.
- **Resultado esperado**: El usuario es redirigido al home con sesión activa.

### Agregar Cultivo
- **Pasos**: Iniciar sesión, presionar el botón de agregar cultivo, ingresar los datos del cultivo y presionar "Guardar".
- **Resultado esperado**: El cultivo se guarda en Firestore con la fecha de cosecha calculada correctamente.

## Archivos Adicionales

Para facilitar la distribución y prueba de la aplicación, se incluye un archivo comprimido (ZIP) que contiene los siguientes elementos:

- **Código fuente**: Todo el código fuente de la aplicación, que puedes abrir y modificar en Android Studio.
- **APK**: El archivo APK generado para instalar directamente la aplicación en dispositivos Android.

### Instrucciones para Descargar

1. **Descargar el archivo ZIP**: [Descargar ZIP del proyecto](AndroidStudios_APPS\Evaluacion 2\VerduritasSA.zip)
2. **Descomprimir el archivo** en tu máquina local.
3. **Abrir el código fuente**: Si deseas ver o modificar el código, abre el proyecto descomprimido en Android Studio.
4. **Instalar el APK**: Para instalar la aplicación en tu dispositivo Android:
    - Transfiere el archivo APK a tu dispositivo.
    - Asegúrate de habilitar la opción "Instalar aplicaciones de fuentes desconocidas" en los ajustes de seguridad de tu dispositivo.
    - Abre el archivo APK para instalar la aplicación.


**Nota**: Asegúrate de tener habilitada la opción de "Instalar aplicaciones de fuentes desconocidas" en los ajustes de seguridad de tu dispositivo para instalar el APK.




