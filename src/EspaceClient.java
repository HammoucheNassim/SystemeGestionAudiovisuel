import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.HashMap;
class EspaceClient {
	private final String email;
	   private Map<MaterielLocation, Integer> reservations;
	   private LocalDate dateReservation;
	   private String adresse;
	   public EspaceClient(String email) {
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
	       SystemeGestion.main(new String[0]);
	   }
	}



