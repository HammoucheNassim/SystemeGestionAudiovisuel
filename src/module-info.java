import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.HashMap;
class MaterielLocation {
   private String nom;
   private double prix;
   public MaterielLocation(String nom, double prix) {
       this.nom = nom;
       this.prix = prix;
   }
   public String getNom() {
       return nom;
   }
   public double getPrix() {
       return prix;
   }
}
class EspaceVendeur {
   private static final String EMAIL_VENDEUR = "vendeur@gmail.com";
   private static final String MOT_DE_PASSE = "123456";
   private static boolean estConnecte = false;
   public static void gererEspaceVendeur(Scanner scanner) {
       if (!estConnecte) {
           System.out.println("Authentification requise pour accéder à l'espace vendeur.");
           System.out.print("Entrez votre adresse e-mail : ");
           String email = scanner.nextLine();
           System.out.print("Entrez votre mot de passe : ");
           String motDePasse = scanner.nextLine();
           if (email.equals(EMAIL_VENDEUR) && motDePasse.equals(MOT_DE_PASSE)) {
               estConnecte = true;
               System.out.println("Authentification réussie.\n");
           } else {
               System.out.println("Authentification échouée. Vous n'êtes pas autorisé à accéder à cet espace.");
               return;
           }
       }
       boolean continuer = true;
       while (continuer) {
           System.out.println("Que souhaitez-vous faire en tant que vendeur ?");
           System.out.println("1. Afficher les réservations des clients");
           System.out.println("2. Supprimer une réservation");
           System.out.println("3. Revenir au menu principal (espace client)");
           System.out.println("4. Afficher le stock de matériel");
           String choixStr = scanner.nextLine();
           int choix;
           try {
               choix = Integer.parseInt(choixStr);
           } catch (NumberFormatException e) {
               System.out.println("Entrée invalide, veuillez saisir un nombre.");
               continue;
           }
           switch (choix) {
               case 1:
                   afficherReservationsClients();
                   demanderContinuer(scanner);
                   break;
               case 2:
                   supprimerReservation(scanner);
                   demanderContinuer(scanner);
                   break;
               case 3:
                   continuer = false;
                   break;
               case 4:
                   afficherStock();
                   demanderContinuer(scanner);
                   break;
               default:
                   System.out.println("Choix invalide. Veuillez choisir une option valide.");
                   break;
           }
       }
   }
   private static void afficherStock() {
       try {
           ProcessBuilder pb = new ProcessBuilder("ocaml", "C:\\Users\\nassi\\Documents\\IUL\\IUL 1\\POO\\workplace\\nom_du_fichier_ocaml.ml");
           pb.redirectErrorStream(true);
           Process p = pb.start();
           BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
           String line;
           while ((line = reader.readLine()) != null) {
               System.out.println(line);
           }
           p.waitFor();
       } catch (IOException | InterruptedException e) {
           System.err.println("Erreur lors de l'exécution du programme OCaml : " + e.getMessage());
       }
   }
   private static void demanderContinuer(Scanner scanner) {
       System.out.println("Souhaitez-vous continuer dans l'espace vendeur ? (oui/non)");
       String reponse = scanner.nextLine().trim().toLowerCase();
       if (!reponse.equals("oui")) {
           System.out.println("Retour au menu principal.");
           estConnecte = false;
       }
   }
  
   private static void afficherReservationsClients() {
       System.out.println("Vérification de la taille de la liste des clients : " + SystemeGestionAudiovisuel.clients.size());
       if (SystemeGestionAudiovisuel.clients.isEmpty()) {
           System.out.println("Aucune réservation trouvée.");
           return;
       }
       System.out.println("Liste des réservations des clients :");
       for (Client client : SystemeGestionAudiovisuel.clients) {
           System.out.println("Réservations pour le client avec l'adresse e-mail : " + client.getEmail());
           // Vérifier si le client a des réservations
           if (client.getReservations().isEmpty()) {
               System.out.println("Pas de réservations pour ce client.");
           } else {
               client.afficherReservations();
           }
       }
   }
   private static void supprimerReservation(Scanner scanner) {
       System.out.print("Entrez l'adresse e-mail du client pour voir ses réservations : ");
       String emailClient = scanner.nextLine();
       Client clientASupprimer = chercherClient(emailClient);
       if (clientASupprimer != null) {
           System.out.println("Réservations actuelles du client :");
           clientASupprimer.afficherReservations();
           System.out.println("Êtes-vous sûr de vouloir supprimer toutes les réservations de ce client ? (oui/non)");
           String confirmation = scanner.nextLine().trim().toLowerCase();
           if (confirmation.equals("oui")) {
               SystemeGestionAudiovisuel.clients.remove(clientASupprimer);
               System.out.println("Toutes les réservations du client ont été supprimées.");
           } else {
               System.out.println("Suppression annulée.");
           }
       } else {
           System.out.println("Client non trouvé.");
       }
   }
   private static Client chercherClient(String email) {
       for (Client client : SystemeGestionAudiovisuel.clients) {
           if (client.getEmail().equals(email)) {
               return client;
           }
       }
       return null;
   }
}
class Client {
   private final String email;
   private Map<MaterielLocation, Integer> reservations;
   private LocalDate dateReservation;
   private String adresse;
   public Client(String email) {
       this.email = email;
       this.reservations = new HashMap<>();
   }
   public String getEmail() {
       return email;
   }
   public Map<MaterielLocation, Integer> getReservations() {
       return reservations;
   }
   public LocalDate getDateReservation() {
       return dateReservation;
   }
   public String getAdresse() {
       return adresse;
   }
   public void setDateReservation(LocalDate dateReservation) {
       this.dateReservation = dateReservation;
   }
   public void setAdresse(String adresse) {
       this.adresse = adresse;
   }
   public void louer(MaterielLocation materiel, int quantite, String adresse, LocalDate dateReservation) {
       reservations.put(materiel, quantite);
       this.adresse = adresse;
       this.dateReservation = dateReservation;
   }
   public void afficherReservations() {
       System.out.println("Réservations :");
       for (Map.Entry<MaterielLocation, Integer> entry : reservations.entrySet()) {
           MaterielLocation materiel = entry.getKey();
           int quantite = entry.getValue();
           System.out.println("- " + quantite + " " + materiel.getNom());
       }
       System.out.println("Adresse : " + adresse);
       System.out.println("Date de réservation : " + dateReservation);
   }
   public boolean supprimerReservation(String nomMateriel) {
       MaterielLocation materielASupprimer = null;
       for (Map.Entry<MaterielLocation, Integer> entry : reservations.entrySet()) {
           MaterielLocation materiel = entry.getKey();
           if (materiel.getNom().equalsIgnoreCase(nomMateriel)) {
               materielASupprimer = materiel;
               break;
           }
       }
       if (materielASupprimer != null) {
           reservations.remove(materielASupprimer);
           return true;
       }
       return false;
   }
   public boolean demanderDeconnexion(Scanner scanner) {
       System.out.println("Voulez-vous vous déconnecter ? (oui/non)");
       String choixDeconnexion = scanner.nextLine();
       return choixDeconnexion.equalsIgnoreCase("oui");
   }
   public void deconnexion() {
       System.out.println("Vous avez été déconnecté de l'espace client.");
       SystemeGestionAudiovisuel.main(new String[0]);
   }
}
public class SystemeGestionAudiovisuel {
   public static List<Client> clients = new ArrayList<>();
   private static boolean continuerProgramme = true;
   private static final String FICHIER_RESERVATION = "reservations.txt";
   private static final MaterielLocation[] materiels = {
           new MaterielLocation("Lumière LED", 50.0),
           new MaterielLocation("Lumière Stroboscopique", 70.0),
           new MaterielLocation("Lumière Ambiante", 40.0),
           new MaterielLocation("Enceinte Bluetooth", 100.0),
           new MaterielLocation("Enceinte Professionnelle", 200.0),
           new MaterielLocation("Estrade Simple", 80.0),
           new MaterielLocation("Estrade Double", 120.0),
           new MaterielLocation("Estrade VIP", 200.0),
           new MaterielLocation("Machine à fumée Standard", 90.0),
           new MaterielLocation("Machine à fumée Professionnelle", 150.0),
           new MaterielLocation("Machine à fumée Puissante", 200.0)
   };
  
   public static void afficherListeMateriel() {
       System.out.println("Liste des matériels disponibles avec leurs prix :");
       for (MaterielLocation materiel : materiels) {
           System.out.println("- " + materiel.getNom() + " : " + materiel.getPrix() + " €");
       }
   }
   public static boolean demanderDeconnexion(Scanner scanner) {
       System.out.print("Voulez-vous vous déconnecter ? (oui/non) : ");
       String reponse = scanner.nextLine();
       return reponse.trim().equalsIgnoreCase("oui");
   }
   public static void louerMateriel(Client client, Scanner scanner, String lieuLivraison, LocalDate dateLocation) {
       for (MaterielLocation materiel : materiels) {
           int quantite = choisirQuantite(scanner, materiel);
           if (quantite > 0) {
               client.louer(materiel, quantite, lieuLivraison, dateLocation);
           }
       }
       sauvegarderReservations();
   }
   public static int choisirQuantite(Scanner scanner, MaterielLocation materiel) {
       System.out.print("Combien de " + materiel.getNom() + " souhaitez-vous louer ? (Prix unitaire : " + materiel.getPrix() + " €) ");
       return scanner.nextInt();
   }
   public static Client authentifierClient(Scanner scanner) {
       System.out.println("Entrez votre adresse email : ");
       String email = scanner.nextLine();
       Client client = chercherClient(email);
       if (client == null) {
           client = new Client(email);
           clients.add(client);
       }
       return client;
   }
   public static Client chercherClient(String email) {
       for (Client client : clients) {
           if (client.getEmail().equals(email)) {
               return client;
           }
       }
       return null;
   }
   public static void sauvegarderReservations() {
       try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER_RESERVATION))) {
           for (Client client : clients) {
               writer.write(client.getEmail() + "\n");
               writer.write(client.getAdresse() + "\n");
               LocalDate dateReservation = client.getDateReservation();
               writer.write((dateReservation != null ? dateReservation.toString() : "null") + "\n");
               for (Map.Entry<MaterielLocation, Integer> entry : client.getReservations().entrySet()) {
                   writer.write(entry.getKey().getNom() + "," + entry.getValue() + "\n");
               }
               writer.write("\n");
           }
       } catch (IOException e) {
           System.err.println("Erreur lors de la sauvegarde des réservations : " + e.getMessage());
       }
   }
   public static String demanderLieuLivraison(Scanner scanner) {
       System.out.println("Entrez le lieu de livraison : ");
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
       try (BufferedReader reader = new BufferedReader(new FileReader(FICHIER_RESERVATION))) {
           String line;
           Client client = null;
           while ((line = reader.readLine()) != null) {
               if (client == null) {
                   client = new Client(line);  // email
                   clients.add(client);
               } else if (client.getAdresse() == null) {
                   client.setAdresse(line);
               } else if (client.getDateReservation() == null && !line.equals("null")) {
                   client.setDateReservation(LocalDate.parse(line));
               } else if (line.isEmpty()) {
                   client = null;
               } else {
                   String[] parts = line.split(",");
                   MaterielLocation materiel = trouverMateriel(parts[0]);
                   if (materiel != null) {
                       int quantite = Integer.parseInt(parts[1]);
                       client.louer(materiel, quantite, client.getAdresse(), client.getDateReservation());
                   }
               }
           }
       } catch (IOException e) {
           System.err.println("Erreur lors du chargement des réservations : " + e.getMessage());
       }
   }
   private static MaterielLocation trouverMateriel(String nom) {
       for (MaterielLocation materiel : materiels) {
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
           System.out.println("Êtes-vous un client ou un vendeur ? (client/vendeur/deconnexion)");
           String choix = scanner.nextLine().trim().toLowerCase();
           switch (choix) {
               case "client":
                   Client client = authentifierClient(scanner);
                   afficherListeMateriel();
                   String lieuLivraison = demanderLieuLivraison(scanner);
                   LocalDate dateLocation = demanderDateLocation(scanner);
                   louerMateriel(client, scanner, lieuLivraison, dateLocation);
                   sauvegarderReservations();
                   if (demanderDeconnexion(scanner)) {
                       System.out.println("Vous avez été déconnecté.");
                       break; // Sortir de la boucle si l'utilisateur souhaite se déconnecter
                   }
                   break;
               case "vendeur":
                   EspaceVendeur.gererEspaceVendeur(scanner);
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
