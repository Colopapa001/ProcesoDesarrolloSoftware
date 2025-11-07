package com.sportsmatching.infra;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Seeder de ejemplo: crea >=10 usuarios y >=10 partidos.
 * Llamar DataSeeder.seed() al iniciar la app (Main).
 */
public class DataSeeder {
    public static void seed() {
        // Locations cercanas (Buenos Aires y alrededores) - lat/lon aproximadas
        MockDomainDataStore.LocationDTO laPlata = new MockDomainDataStore.LocationDTO(-34.92145, -57.95453, "La Plata");
        MockDomainDataStore.LocationDTO palermo = new MockDomainDataStore.LocationDTO(-34.5733, -58.4350, "Palermo, Buenos Aires");
        MockDomainDataStore.LocationDTO recoleta = new MockDomainDataStore.LocationDTO(-34.5883, -58.3856, "Recoleta, Buenos Aires");
        MockDomainDataStore.LocationDTO centro = new MockDomainDataStore.LocationDTO(-34.6075, -58.4370, "Centro, Buenos Aires");
        MockDomainDataStore.LocationDTO floresta = new MockDomainDataStore.LocationDTO(-34.6480, -58.4935, "Floresta, Buenos Aires");
        MockDomainDataStore.LocationDTO sanIsidro = new MockDomainDataStore.LocationDTO(-34.4528, -58.5003, "San Isidro");
        MockDomainDataStore.LocationDTO avellaneda = new MockDomainDataStore.LocationDTO(-34.6643, -58.3662, "Avellaneda");
        MockDomainDataStore.LocationDTO mataderos = new MockDomainDataStore.LocationDTO(-34.6833, -58.4700, "Mataderos");
        MockDomainDataStore.LocationDTO belgrano = new MockDomainDataStore.LocationDTO(-34.5556, -58.4458, "Belgrano");
        MockDomainDataStore.LocationDTO palermoSoho = new MockDomainDataStore.LocationDTO(-34.5784, -58.4426, "Palermo Soho");

        // Crear usuarios (>=10)
        MockDomainDataStore.UsuarioDTO u1 = MockDomainDataStore.addUsuario("fhaedo3","fran.haedo3@gmail.com","12345678","Intermedio","Fútbol", palermo);
        MockDomainDataStore.UsuarioDTO u2 = MockDomainDataStore.addUsuario("andres","ahaedo@gmail.com","12345","Intermedio","Fútbol", centro);
        MockDomainDataStore.UsuarioDTO u3 = MockDomainDataStore.addUsuario("joaquin3","jhaedo2000@gmail.com","1234567","Principiante","Fútbol", laPlata);
        MockDomainDataStore.UsuarioDTO u4 = MockDomainDataStore.addUsuario("maria","maria@mail.com","pwd1","Intermedio","Básquet", recoleta);
        MockDomainDataStore.UsuarioDTO u5 = MockDomainDataStore.addUsuario("lucas","lucas@mail.com","pwd2","Avanzado","Tenis", sanIsidro);
        MockDomainDataStore.UsuarioDTO u6 = MockDomainDataStore.addUsuario("santiago","santi@mail.com","pwd3","Intermedio","Fútbol", palermoSoho);
        MockDomainDataStore.UsuarioDTO u7 = MockDomainDataStore.addUsuario("juan","juan@mail.com","pwd4","Intermedio","Fútbol", belgrano);
        MockDomainDataStore.UsuarioDTO u8 = MockDomainDataStore.addUsuario("ana","ana@mail.com","pwd5","Principiante","Básquet", floresta);
        MockDomainDataStore.UsuarioDTO u9 = MockDomainDataStore.addUsuario("diego","diego@mail.com","pwd6","Intermedio","Fútbol", avellaneda);
        MockDomainDataStore.UsuarioDTO u10 = MockDomainDataStore.addUsuario("sofia","sofia@mail.com","pwd7","Intermedio","Fútbol", palermo);
        MockDomainDataStore.UsuarioDTO u11 = MockDomainDataStore.addUsuario("facu","facu@mail.com","pwd8","Intermedio","Básquet", centro);
        MockDomainDataStore.UsuarioDTO u12 = MockDomainDataStore.addUsuario("carla","carla@mail.com","pwd9","Avanzado","Tenis", recoleta);

        // Crear partidos (>=10) - muchos "Intermedio" en zonas cercanas (Palermo, Centro, Recoleta)
        LocalDateTime base = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0);
        MockDomainDataStore.PartidoDTO p1 = MockDomainDataStore.addPartido("Fútbol", u1.username, 10, "CREADO", palermo, base.plusHours(1), 60, "Intermedio", "Avanzado");
        MockDomainDataStore.PartidoDTO p2 = MockDomainDataStore.addPartido("Fútbol", u6.username, 10, "CREADO", palermoSoho, base.plusDays(1), 90, "Intermedio", "Avanzado");
        MockDomainDataStore.PartidoDTO p3 = MockDomainDataStore.addPartido("Fútbol", u7.username, 10, "CREADO", belgrano, base.plusDays(2), 60, "Intermedio", "Avanzado");
        MockDomainDataStore.PartidoDTO p4 = MockDomainDataStore.addPartido("Fútbol", u9.username, 10, "CREADO", avellaneda, base.plusHours(4), 60, "Intermedio", "Intermedio");
        MockDomainDataStore.PartidoDTO p5 = MockDomainDataStore.addPartido("Básquet", u4.username, 6, "CREADO", recoleta, base.plusDays(1).plusHours(2), 90, "Principiante", "Intermedio");
        MockDomainDataStore.PartidoDTO p6 = MockDomainDataStore.addPartido("Tenis", u5.username, 4, "CREADO", sanIsidro, base.plusDays(3), 120, "Avanzado", "Avanzado");
        MockDomainDataStore.PartidoDTO p7 = MockDomainDataStore.addPartido("Fútbol", u2.username, 10, "CREADO", centro, base.plusHours(6), 60, "Intermedio", "Avanzado");
        MockDomainDataStore.PartidoDTO p8 = MockDomainDataStore.addPartido("Fútbol", u10.username, 8, "CREADO", palermo, base.plusDays(4), 75, "Intermedio", "Avanzado");
        MockDomainDataStore.PartidoDTO p9 = MockDomainDataStore.addPartido("Básquet", u11.username, 6, "CREADO", centro, base.plusDays(2).plusHours(1), 90, "Intermedio", "Avanzado");
        MockDomainDataStore.PartidoDTO p10 = MockDomainDataStore.addPartido("Fútbol", u1.username, 10, "CREADO", palermo, base.plusDays(5), 60, "Intermedio", "Avanzado");
        MockDomainDataStore.PartidoDTO p11 = MockDomainDataStore.addPartido("Fútbol", u3.username, 10, "CREADO", laPlata, base.plusDays(1).plusHours(3), 60, "Principiante", "Intermedio");
        MockDomainDataStore.PartidoDTO p12 = MockDomainDataStore.addPartido("Fútbol", u7.username, 10, "CREADO", palermo, base.plusDays(2).plusHours(2), 60, "Intermedio", "Avanzado");

        // Agregar algunos jugadores a partidos (simula inscripciones)
        MockDomainDataStore.addJugadorToPartido(p1.id, u6);
        MockDomainDataStore.addJugadorToPartido(p1.id, u7);
        MockDomainDataStore.addJugadorToPartido(p1.id, u9);
        MockDomainDataStore.addJugadorToPartido(p2.id, u1);
        MockDomainDataStore.addJugadorToPartido(p2.id, u10);
        MockDomainDataStore.addJugadorToPartido(p3.id, u2);
        MockDomainDataStore.addJugadorToPartido(p4.id, u11);
        MockDomainDataStore.addJugadorToPartido(p8.id, u5);

        // Estadísticas y comentarios de ejemplo
        MockDomainDataStore.setEstadisticas(p1.id,
                "Goles: 3-2 | Posesión: 60% | Faltas: 8",
                List.of("Muy buen partido!", "Organización impecable")
        );
        MockDomainDataStore.setEstadisticas(p2.id,
                "Puntos: 82-79 | Rebotes: 40-38",
                List.of("Intenso hasta el final", "Llevar agua extra")
        );
        MockDomainDataStore.setEstadisticas(p5.id,
                "Sets: 2-1 | Aces: 5",
                List.of("Cancha en buen estado", "Traer pelotas")
        );
        MockDomainDataStore.setEstadisticas(p10.id,
                "Goles: 1-0 | Tarjetas: 1",
                List.of("Ritmo tranquilo", "Buen arbitraje")
        );

        System.out.println("DataSeeder: cargados usuarios=" + MockDomainDataStore.allUsuarios().size()
                + " partidos=" + MockDomainDataStore.allPartidos().size());
    }
}