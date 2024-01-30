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
    
    private LocalDate dateReservation;
    
    private String adresse;
    
    private Map<LocationDeMateriel, Integer> reservations;

    public Map<LocationDeMateriel, Integer> getReservations() {
        return reservations;
    }
    public EspaceClient(String email) {
        this.email = email;
        this.reservations = new HashMap<>();
    }

    public String getEmail() {
        return email;
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

    public void louer(LocationDeMateriel materiel, int quantite, String adresse, LocalDate dateReservation) {
        reservations.put(materiel, quantite);
        this.adresse = adresse;
        this.dateReservation = dateReservation;
    }

    public void afficherReservations() {
        System.out.println("Réservations :");
        for (Map.Entry<LocationDeMateriel, Integer> entry : reservations.entrySet()) {
            LocationDeMateriel materiel = entry.getKey();
            int quantite = entry.getValue();
            System.out.println("- " + quantite + " x " + materiel.getNom() + " (" + materiel.getPrix() + "€ chacun)");
        }
        System.out.println("Adresse de livraison : " + adresse);
        System.out.println("Date de réservation : " + (dateReservation != null ? dateReservation.toString() : "Non spécifiée"));
    }

    public boolean supprimerReservation(String nomMateriel) {
        LocationDeMateriel materielASupprimer = null;
        for (Map.Entry<LocationDeMateriel, Integer> entry : reservations.entrySet()) {
            LocationDeMateriel materiel = entry.getKey();
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