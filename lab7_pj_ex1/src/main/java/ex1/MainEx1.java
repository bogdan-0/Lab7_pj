package ex1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MainEx1 {

    public static void scriere(Map<Integer, Carte> map) {
        try {
            var mapper = new ObjectMapper();
            var file = new File("src/main/resources/carti.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Carte> citire() {
        try {
            var file = new File("src/main/resources/carti.json");
            var mapper = new ObjectMapper();
            return mapper.readValue(file, new TypeReference<HashMap<Integer, Carte>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public static void main(String[] args) {
        var mapCarti = citire();

        System.out.println("--- 1) Colectia Map initiala ---");
        for (var entry : mapCarti.entrySet()) {
            System.out.println("ID: " + entry.getKey() + " -> " + entry.getValue());
        }

        mapCarti.remove(2);

        mapCarti.putIfAbsent(7, new Carte("Nexus", "Yuval Noah Harari", 2024));

        scriere(mapCarti);

        System.out.println("\n--- 5) Cartile autorului Yuval Noah Harari (Set) ---");
        Set<Carte> cartiHarari = mapCarti.values().stream()
                .filter(carte -> "Yuval Noah Harari".equals(carte.autorul()))
                .collect(Collectors.toSet());
        cartiHarari.forEach(System.out::println);

        System.out.println("\n--- 6) Cartile din Set ordonate dupa titlu ---");
        cartiHarari.stream()
                .sorted((c1, c2) -> c1.titlul().compareTo(c2.titlul()))
                .forEach(System.out::println);

        System.out.println("\n--- 7) Cea mai veche carte din Set ---");
        Optional<Carte> ceaMaiVecheCarte = cartiHarari.stream()
                .min(Comparator.comparingInt(Carte::anul));
        ceaMaiVecheCarte.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Setul este gol.")
        );
    }
}