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




class LocationDeMateriel {
	private String nom;
	   private double prix;
	   private int quantiteDisponible;
	   public LocationDeMateriel(String nom, double prix) {
	       this.nom = nom;
	       this.prix = prix;
	       this.quantiteDisponible = 30;
	   }
	   public String getNom() {
	       return nom;
	   }
	   public int getQuantiteDisponible() {
	        return quantiteDisponible;
	    }

	    public void setQuantiteDisponible(int quantite) {
	        this.quantiteDisponible = quantite;
	    }
	   public double getPrix() {
	       return prix;
	   }
	}


