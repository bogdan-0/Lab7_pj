package ex2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MainApp {

    public static void scriere(Set<InstrumentMuzical> set) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator());
            File file = new File("src/main/resources/instrumente.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, set);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<InstrumentMuzical> citire() {
        try {
            File file = new File("src/main/resources/instrumente.json");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(file, new TypeReference<Set<InstrumentMuzical>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    public static void main(String[] args) {
        Set<InstrumentMuzical> instrumenteInitiale = new HashSet<>();

        instrumenteInitiale.add(new Chitara("Fender", 2500, TipChitara.ELECTRICA, 6));
        instrumenteInitiale.add(new Chitara("Ibanez", 2800, TipChitara.ELECTRICA, 7));
        instrumenteInitiale.add(new Chitara("Gibson", 4500, TipChitara.ACUSTICA, 12));

        instrumenteInitiale.add(new SetTobe("Yamaha", 2900, TipTobe.ACUSTICE, 7, 4));
        instrumenteInitiale.add(new SetTobe("Pearl", 2200, TipTobe.ACUSTICE, 5, 3));
        instrumenteInitiale.add(new SetTobe("Roland", 3500, TipTobe.ELECTRONICE, 6, 4));

        scriere(instrumenteInitiale);

        Set<InstrumentMuzical> instrumente = citire();
        System.out.println("--- 3) Colectia incarcata din instrumente.json ---");
        instrumente.forEach(System.out::println);

        System.out.println("\n--- 4) Implementarea utilizata pentru Set de catre ObjectMapper ---");
        System.out.println(instrumente.getClass().getName());

        System.out.println("\n--- 5) Verificare duplicate in Set ---");
        Chitara duplicat = new Chitara("Fender", 2500, TipChitara.ELECTRICA, 6);
        boolean adaugat = instrumente.add(duplicat);
        if (adaugat) {
            System.out.println("Instrumentul duplicat a fost adaugat (Set-ul permite duplicate).");
        } else {
            System.out.println("Instrumentul duplicat NU a fost adaugat (Set-ul nu permite duplicate).");
        }

        System.out.println("\n--- 6) Colectia dupa stergerea instrumentelor cu pret > 3000 RON ---");
        instrumente.removeIf(inst -> inst.getPret() > 3000);
        instrumente.forEach(System.out::println);

        System.out.println("\n--- 7) Datele chitarilor (filtrare cu instanceof) ---");
        instrumente.stream()
                .filter(inst -> inst instanceof Chitara)
                .forEach(System.out::println);

        System.out.println("\n--- 8) Datele tobelor (filtrare cu getClass()) ---");
        instrumente.stream()
                .filter(inst -> inst.getClass() == SetTobe.class)
                .forEach(System.out::println);

        System.out.println("\n--- 9) Chitara cu cele mai multe corzi ---");
        Optional<Chitara> chitaraMaxCorzi = instrumente.stream()
                .filter(inst -> inst instanceof Chitara)
                .map(inst -> (Chitara) inst)
                .max(Comparator.comparingInt(Chitara::getNr_corzi));
        chitaraMaxCorzi.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Nu exista chitari in colectie.")
        );
        //fara optional, formula lambda in loc de el si sa folosesc doar ifprezentorelse fara :,
        System.out.println("\n--- 10) Tobele acustice ordonate dupa numarul de tobe ---");
        instrumente.stream()
                .filter(inst -> inst instanceof SetTobe)
                .map(inst -> (SetTobe) inst)
                .filter(tobe -> tobe.getTip_tobe() == TipTobe.ACUSTICE)
                .sorted(Comparator.comparingInt(SetTobe::getNr_tobe))
                .forEach(System.out::println);
    }
}
