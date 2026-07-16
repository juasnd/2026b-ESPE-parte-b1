package es.upm.grise.subscriptionService;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import es.upm.grise.subscriptionService.exceptions.ExistingUserException;
import es.upm.grise.subscriptionService.exceptions.NonExistingUserException;
import es.upm.grise.subscriptionService.exceptions.NullUserException;
import es.upm.grise.subscriptionService.exceptions.UserDoesNotHaveEmailException;

class SubscriptionServiceTest {

    @Test
    void testAgregarUsuarioCorrectamente() throws Exception {

        SubscriptionService servicio = new SubscriptionService(new EmailService());
        User usuario = new User(Delivery.EMAIL, "juan@espe.edu.ec");

        servicio.addSubscriber(usuario);

        assertDoesNotThrow(() -> servicio.removeSubscriber(usuario));
    }

    @Test
    void testAgregarUsuarioNull() {

        SubscriptionService servicio = new SubscriptionService(new EmailService());

        assertThrows(NullUserException.class, () -> {
            servicio.addSubscriber(null);
        });
    }

    @Test
    void testAgregarUsuarioDuplicado() throws Exception {

        SubscriptionService servicio = new SubscriptionService(new EmailService());
        User usuario = new User(Delivery.EMAIL, "juan@espe.edu.ec");

        servicio.addSubscriber(usuario);

        assertThrows(ExistingUserException.class, () -> {
            servicio.addSubscriber(usuario);
        });
    }

    @Test
    void testUsuarioSinCorreo() {

        SubscriptionService servicio = new SubscriptionService(new EmailService());
        User usuario = new User(Delivery.EMAIL, null);

        assertThrows(UserDoesNotHaveEmailException.class, () -> {
            servicio.addSubscriber(usuario);
        });
    }

    @Test
    void testEliminarUsuarioCorrectamente() throws Exception {

        SubscriptionService servicio = new SubscriptionService(new EmailService());
        User usuario = new User(Delivery.LOCAL, null);

        servicio.addSubscriber(usuario);
        servicio.removeSubscriber(usuario);

        assertThrows(NonExistingUserException.class, () -> {
            servicio.removeSubscriber(usuario);
        });
    }

    @Test
    void testEliminarUsuarioNull() {

        SubscriptionService servicio = new SubscriptionService(new EmailService());

        assertThrows(NullUserException.class, () -> {
            servicio.removeSubscriber(null);
        });
    }

    @Test
    void testEliminarUsuarioNoExistente() {

        SubscriptionService servicio = new SubscriptionService(new EmailService());
        User usuario = new User(Delivery.LOCAL, null);

        assertThrows(NonExistingUserException.class, () -> {
            servicio.removeSubscriber(usuario);
        });
    }

    @Test
    void testEnviarMensajesDescartados() throws Exception {

        SubscriptionService servicio = new SubscriptionService(new EmailService());

        User usuario1 = new User(Delivery.LOCAL, null);
        User usuario2 = new User(Delivery.DO_NOT_DELIVER,"jfgutierrez2@espe.edu.ec");

        servicio.addSubscriber(usuario1);
        servicio.addSubscriber(usuario2);

        Message mensaje = new Message(1, "Hola");

        int descartados = servicio.sendMessage(mensaje);

        assertEquals(1, descartados);
    }

}