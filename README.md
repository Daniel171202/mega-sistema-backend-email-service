# mega-sistema-backend-email-service
 
# 📬 Servicio de Envío de Correos (Microservicio con Spring Boot)

Este repositorio contiene un **microservicio de envío de correos electrónicos** desarrollado con **Java y Spring Boot**. Forma parte de un sistema distribuido donde cada componente tiene una responsabilidad específica. Este servicio en particular se encarga del envío masivo de correos utilizando múltiples hilos para mejorar la eficiencia y tolerancia a fallos.

---

## 🚀 ¿Qué es un Microservicio?

Un **microservicio** es una pequeña aplicación autónoma que realiza una única función dentro de un sistema más grande. En este caso, este microservicio gestiona únicamente el **envío de correos electrónicos**. Su diseño permite que sea escalable, mantenible y que pueda ser desplegado de forma independiente.

---

## 🧩 Integración con el API Gateway

Este microservicio **no recibe las peticiones directamente del frontend**, sino a través de un **API Gateway** (`main.go` en este caso), el cual actúa como punto de entrada único. El API Gateway enruta la solicitud al microservicio adecuado según el endpoint.

---

## ✉️ Funcionalidad Principal

Este servicio permite enviar correos electrónicos de forma paralela (multi-threading), mejorando la velocidad y eficiencia del envío masivo. En caso de que algún correo falle al enviarse, se intenta nuevamente **después de 3 minutos** automáticamente.

---

## 🔧 Endpoints Disponibles

### 1. `POST /api/v1/auth/send-email-invitation`

Envía un correo de invitación con **un mensaje HTML predeterminado**.

### 2. `POST /api/v1/auth/send-email-html`

Envía un correo de con **un mensaje HTML personalizado** que manda el client desde la petición.

#### 📥 Ejemplo de solicitud

```http
POST http://localhost:8017/api/v1/auth/send-email-invitation
Content-Type: application/json

{
  "recipients": [
    "daniel.aldazosa@gmail.com", 
   "daniel.alsa@gmail.com",
    "jhessikazarate@gmail.com",
    "tania.perez.d@ucb.edu.bo"
  ],
  "subject": "NOTASSSS",
  "body": ""
}


POST http://localhost:8017/api/v1/auth/send-email-html
Content-Type: application/json

{
  "recipients": [
    "daniel.aldazosa@gmail.com", 
   "daniel.alsa@gmail.com",
    "jhessikazarate@gmail.com",
    "tania.perez.d@ucb.edu.bo"
  ],
  "subject": "NOTASSSS",
   "body": "<!DOCTYPE html>\n<html lang=\"es\">\n  <head>\n    <meta charset=\"UTF-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n    <style>\n      body {\n        font-family: Arial, sans-serif;\n        background-color: #f3f4f6;\n        margin: 0;\n        padding: 0;\n      }\n      .container {\n        max-width: 600px;\n        margin: 0 auto;\n        background-color: #ffffff;\n        border: 1px solid #e2e8f0;\n        border-radius: 8px;\n        overflow: hidden;\n      }\n      .header {\n        background-color: #000f4a;\n        padding: 20px;\n        text-align: center;\n      }\n      .header img {\n        width: 100%;\n        max-width: 560px;\n        height: auto;\n        border-radius: 8px;\n      }\n      .welcome {\n        font-size: 1.5rem;\n        color: #00197c;\n        text-align: center;\n        margin-top: 20px;\n      }\n      .message {\n        padding: 20px;\n        font-size: 1rem;\n        color: #333;\n        text-align: justify;\n      }\n      .footer {\n        padding: 20px;\n        background-color: #f3f4f6;\n        text-align: center;\n        font-size: 0.9rem;\n        color: #666;\n      }\n      .footer a {\n        color: #0026c3;\n        text-decoration: none;\n      }\n    </style>\n  </head>\n  <body>\n    <div class=\"container\">\n      <div class=\"header\">\n        <img src=\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDOIyeUw-xVtSHzho0un9hE3r-gNXb_pF9WQ&s\" alt=\"Imagen de cabecera\" />\n      </div>\n      <div class=\"welcome\">¡Bienvenida Jhessika!</div>\n      <div class=\"message\">\n        Este es un mensaje de prueba para verificar que el diseño del correo se visualiza correctamente. Aquí puedes colocar cualquier contenido importante, como noticias, recordatorios o información relevante para los destinatarios.\n      </div>\n      <div class=\"footer\">\n        © 2025 Universidad Católica Boliviana | <a href=\"https://ucb.edu.bo\">Visita nuestro sitio web</a>\n      </div>\n    </div>\n  </body>\n</html>"
}

