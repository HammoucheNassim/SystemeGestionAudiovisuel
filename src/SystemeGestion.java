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
public class SystemeGestion{
	public static List<Client> clients = new ArrayList<>();
	   private static boolean continuerProgramme = true;
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
	   public static List<String> reservationsEnregistrees = new ArrayList<>();

	    public static void sauvegarderReservations() {
	        reservationsEnregistrees.clear(); // Effacer les anciennes données
	        for (Client client : clients) {
	            StringBuilder reservation = new StringBuilder();
	            reservation.append(client.getEmail()).append("\n");
	            reservation.append(client.getAdresse()).append("\n");
	            LocalDate dateReservation = client.getDateReservation();
	            reservation.append((dateReservation != null ? dateReservation.toString() : "null")).append("\n");
	            for (Map.Entry<MaterielLocation, Integer> entry : client.getReservations().entrySet()) {
	                reservation.append(entry.getKey().getNom()).append(",").append(entry.getValue()).append("\n");
	            }
	            reservation.append("\n");
	            reservationsEnregistrees.add(reservation.toString());
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
	   public static List<String> reservationsEnregistrees1 = new ArrayList<>();

	    public static void chargerReservations() {
	        for (String reservationStr : reservationsEnregistrees) {
	            String[] lignes = reservationStr.split("\n");
	            Client client = null;
	            for (String ligne : lignes) {
	                if (client == null) {
	                    client = new Client(ligne); // email
	                    clients.add(client);
	                } else if (client.getAdresse() == null) {
	                    client.setAdresse(ligne);
	                } else if (client.getDateReservation() == null && !ligne.equals("null")) {
	                    client.setDateReservation(LocalDate.parse(ligne));
	                } else if (!ligne.isEmpty()) {
	                    String[] parts = ligne.split(",");
	                    MaterielLocation materiel = trouverMateriel(parts[0]);
	                    if (materiel != null) {
	                        int quantite = Integer.parseInt(parts[1]);
	                        client.louer(materiel, quantite, client.getAdresse(), client.getDateReservation());
	                    }
	                }
	            }
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
