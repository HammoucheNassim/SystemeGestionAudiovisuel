import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
public class SystemeGestion {
    public static List<EspaceClient> clients = new ArrayList<>();
    private static boolean continuerProgramme = true;
    public static final LocationDeMateriel[] materiels = {
        new LocationDeMateriel("Lumière LED", 50.0),
        new LocationDeMateriel("Lumière Stroboscopique", 70.0),
        new LocationDeMateriel("Lumière Ambiante", 40.0),
        new LocationDeMateriel("Enceinte Bluetooth", 100.0),
        new LocationDeMateriel("Enceinte Professionnelle", 200.0),
        new LocationDeMateriel("Estrade Simple", 80.0),
        new LocationDeMateriel("Estrade Double", 120.0),
        new LocationDeMateriel("Estrade VIP", 200.0),
        new LocationDeMateriel("Machine à fumée Standard", 90.0),
        new LocationDeMateriel("Machine à fumée Professionnelle", 150.0),
        new LocationDeMateriel("Machine à fumée Puissante", 200.0)
    };

    public static void afficherListeMateriel() {
        System.out.println("Liste des matériels disponibles avec leurs prix :");
        for (LocationDeMateriel materiel : materiels) {
            System.out.println("- " + materiel.getNom() + " : " + materiel.getPrix() + " €");
        }
    }

    public static boolean demanderDeconnexion(Scanner scanner) {
        System.out.print("Voulez-vous vous déconnecter ? (oui/non) : ");
        String reponse = scanner.nextLine();
        return reponse.trim().equalsIgnoreCase("oui");
    }

    public static void louerMateriel(EspaceClient client, Scanner scanner, String lieuLivraison, LocalDate dateLocation) {
        for (LocationDeMateriel materiel : materiels) {
            int quantite = choisirQuantite(scanner, materiel);
            if (quantite > 0) {
                client.louer(materiel, quantite, lieuLivraison, dateLocation);
            }
        }
        sauvegarderReservations();
    }

    public static int choisirQuantite(Scanner scanner, LocationDeMateriel materiel) {
        System.out.print("Combien de " + materiel.getNom() + " souhaitez-vous louer ? (Prix unitaire : " + materiel.getPrix() + " €) ");
        int quantite = scanner.nextInt();
        scanner.nextLine();  // Consomme la fin de ligne après le nombre
        return quantite;
    }

    public static EspaceClient authentifierClient(Scanner scanner) {
        System.out.print("Entrez votre adresse email : ");
        String email = scanner.nextLine();
        EspaceClient client = chercherClient(email);
        if (client == null) {
            client = new EspaceClient(email);
            clients.add(client);
        }
        return client;
    }

    public static EspaceClient chercherClient(String email) {
        for (EspaceClient client : clients) {
            if (client.getEmail().equals(email)) {
                return client;
            }
        }
        return null;
    }

    public static void sauvegarderReservations() {
        for (EspaceClient client : clients) {
            StringBuilder reservation = new StringBuilder();
            reservation.append(client.getEmail()).append("\n");
            reservation.append(client.getAdresse()).append("\n");
            LocalDate dateReservation = client.getDateReservation();
            reservation.append((dateReservation != null ? dateReservation.toString() : "null")).append("\n");
            for (Map.Entry<LocationDeMateriel, Integer> entry : client.getReservations().entrySet()) {
                reservation.append(entry.getKey().getNom()).append(",").append(entry.getValue()).append("\n");
            }
            reservation.append("\n");
        }
    }

    public static String demanderLieuLivraison(Scanner scanner) {
        System.out.print("Entrez le lieu de livraison : ");
        return scanner.nextLine();
    }

    public static LocalDate demanderDateLocation(Scanner scanner) {
        while (true) {
            System.out.print("Entrez la date de location (AAAA-MM-JJ) : ");
            String dateString = scanner.nextLine();
            try {
                LocalDate date = LocalDate.parse(dateString);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("La date doit être dans le futur.");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Format de date invalide. Veuillez réessayer.");
            }
        }
    }

    public static void chargerReservations() {
        // Cette méthode doit être implémentée en fonction de la manière dont vous stockez et chargez les données.
    }

    private static LocationDeMateriel trouverMateriel(String nom) {
        for (LocationDeMateriel materiel : materiels) {
            if (materiel.getNom().equals(nom)) {
                return materiel;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        chargerReservations(); // Charger les réservations existantes au démarrage
        while (continuerProgramme) {
            System.out.print("Êtes-vous un client ou un vendeur ? (client/vendeur/deconnexion) : ");
            String choix = scanner.nextLine().trim().toLowerCase();
            switch (choix) {
                case "client":
                    EspaceClient client = authentifierClient(scanner);
                    afficherListeMateriel();
                    String lieuLivraison = demanderLieuLivraison(scanner);
                    LocalDate dateLocation = demanderDateLocation(scanner);
                    louerMateriel(client, scanner, lieuLivraison, dateLocation);
                    sauvegarderReservations();
                    if (demanderDeconnexion(scanner)) {
                        System.out.println("Vous avez été déconnecté.");
                        break;
                    }
                    break;
                case "vendeur":
                    Vendeur.gererEspaceVendeur(scanner);
                    break;
                case "deconnexion":
                    System.out.println("Déconnexion effectuée.");
                    continuerProgramme = false;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez essayer à nouveau.");
                    break;
            }
        }
        scanner.close();
    }
}