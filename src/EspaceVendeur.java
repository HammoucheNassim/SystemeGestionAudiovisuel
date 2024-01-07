import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.HashMap;
class Vendeur {
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
       System.out.println("Vérification de la taille de la liste des clients : " + SystemeGestion.clients.size());
       if (SystemeGestion.clients.isEmpty()) {
           System.out.println("Aucune réservation trouvée.");
           return;
       }
       System.out.println("Liste des réservations des clients :");
       for (Client client : SystemeGestion.clients) {
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
        	   SystemeGestion.clients.remove(clientASupprimer);
               System.out.println("Toutes les réservations du client ont été supprimées.");
           } else {
               System.out.println("Suppression annulée.");
           }
       } else {
           System.out.println("Client non trouvé.");
       }
   }
   private static Client chercherClient(String email) {
       for (Client client : SystemeGestion.clients) {
           if (client.getEmail().equals(email)) {
               return client;
           }
       }
       return null;
   }
}

