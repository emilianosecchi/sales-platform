# SalesPlatform
Este proyecto simula el funcionamiento de una plataforma de e-commerce usando una arquitectura de microservicios
orientada a eventos. El proyecto solo abarca el desarrollo backend, no está pensado para su uso en entornos 
productivos ni como solución final a un problema real.

# Tecnologías utilizadas
- Java
- Spring Boot (Cloud, Data JPA, Security)
- Build con Apache Maven Multi-Module
- PostgreSQL / Hibernate ORM
- Apache Kafka
- Redis
- Seguridad con JSON Web Token
- Testing con JUnit y Mockito
- Docker

# Arquitectura del sistema
El sistema se compone de **6 microservicios** desacoplados priorizando la escalabilidad. Cada servicio dispone de su propia base de datos relacional. La comunicación entre estos
microservicios se realiza mediante eventos que son almacenados en un cluster de **Kafka**. A continuación nombro
cada microservicio y el rol que cumplen en la arquitectura del sistema:

### discovery-server
Este servicio pone a disposición un server Eureka, que permite registrar las instancias de todos los microservicios
que componen el sistema y llevar la información del estado de cada uno en tiempo real.

### gateway-service
Este servicio es el encargado de recibir las requests externas y enviarlas al servicio que corresponda. Es el único
punto de acceso público al usuario. Además, por cuestiones de facilidad práctica, es el encargado de validar
los tokens JWT recibidos en las requests. Esta validación se centraliza en este servicio, luego en el tráfico 
interno, el token es reemplazado por headers X-User-Id y X-User-Roles utilizando los claims recibidos.

### notification-service
Este servicio simula el envío de notificaciones al usuario. Consume los eventos publicados en los siguientes 
topicos de Kafka:

#### "sp.user.created"
Topico que contiene los eventos que representan la creación de un usuario en el sistema.

#### "sp.user.email.updated"
Topico que contiene los eventos que representan la actualización del email de un usuario en el sistema.

#### "sp.order.cancelled"
Topico que contiene los eventos que representan la cancelación de una orden.

#### "sp.order.completed"
Topico que contiene los eventos que representan la compleción de una orden.

Cada uno de estos eventos son notificados al usuario que ejecutó la acción, sea de registro o de compra de una
orden. El punto de comunicación con el usuario es vía correo electrónico. Para disponer de los emails de los
usuarios sin depender exclusivamente del servicio **user-auth-service** decidí implementar una caché utilizando
**Redis**, utilizando los datos de los eventos emitidos en los topicos _sp.user.created_ y _sp.user.email.updated_.

### order-service
Este servicio es el encargado de gestionar las órdenes de compra. Toma el papel de orquestador en la transacción
distribuida: 
- cuando se recibe una nueva orden emite un evento al topico _sp.order.created_, destinado al servicio **product-inventory-service** para que verifique si hay stock y en caso afirmativo, que lo reserve.
- dado el resultado del paso anterior, si no hay stock de algún producto la orden se cancela y publica un evento en el topico _sp.order.cancelled_
- si se pudo reservar el stock, emite un evento al topico _sp.order.payment.requested_, solicitandole al servicio **payment-service** que procese el pago.
- si el pago se procesó exitosamente, la orden queda completada y se emite un evento al topico _"sp.order.completed"_. Si el pago no pudo ser procesado, se emite un evento al topico _"sp.order.cancelled"_, el cual será recibido por **product-inventory-service** y disparará que el stock reservado sea devuelto al inventario.

Al margen de esto, este servicio mantiene un snapshot de la tabla de productos que pertenece a **product-inventory-service**, esto permite que el costo total de una orden se pueda calcular sin depender del otro servicio. Para mantener actualizado este snapshot local se utilizan los eventos de los topicos: _"sp.productinventory.product.created"_ y _"sp.productinventory.product.price.updated"_.

### payment-service
Este servicio es el encargado de simular el procesamiento del pago de una orden. Escucha los eventos del topico _''sp.order.payment.requested''_, los procesa y publica el resultado del cobro en el topico _''sp.payment.result''_.

### product-inventory-service
Este servicio es el encargado de la gestión de productos y los inventarios en cada depósito. Escucha los eventos publicados en el topico _''sp.order.created''_ y se encarga de verificar que los productos solicitados en la orden tengan disponibilidad. Luego de procesar la nueva orden, publica un evento al topico _"sp.productinventory.reservation.result"_ informando si fue posible o no realizar la reserva de los productos solicitados.
Por otro lado, también está suscripto al topico _"sp.order.cancelled"_, al recibir un evento de una orden cancelada se devuelven los productos reservados a los depositos de los cuales fueron tomadas las reservas.

### user-auth-service
Este servicio es el encargado de la autenticación y gestión de los usuarios. Es el unico componente del sistema habilitado para emitir tokens de autenticación JWT.
Pone a disposición dos endpoints publicos para que un usuario se pueda registrar y loguear. Publica eventos en los topicos _"sp.user.created"_ y _''sp.user.email.updated''_.

# Restricciones y/o limitaciones del diseño actual
- El diseño actual del sistema centraliza la validación del JWT en el API Gateway suponiendo un despliegue en una misma red privada donde los microservicios no pueden ser accedidos por fuera de la misma. 
Si se necesitará un control de seguridad más exhaustivo, cada servicio debería validar el token por su cuenta.
- El flujo actual del sistema no contempla que una orden cuyo pago haya fallado pueda volver a intentar ser pagada. Si el procesamiento del pago falla, la orden quedará cancelada.
- Actualmente solo hay una API call sincrónica en el sistema que acopla un microservicio con otro: **notification-service** posee una caché de los emails de los usuarios (user:email:{#id}), en el caso de que se llegara a vencer el TTL o el dato se haya borrado, 
el servicio deberá ir a buscar el dato sincronicamente a la API de **user-auth-service** para garantizar el envío del correo electronico.
- Los envios de correos son simulados utilizando un logger.
- El procesamiento del pago también es simulado, para probar los distintos casos de exito o fallo, el procesamiento de un pago tiene una tasa de exito del 70%.