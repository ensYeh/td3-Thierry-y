package fr.uvsq.cprog.collex;

import java.util.Objects; 

public final class AdresseIP { 

  private final String valeur;

  public AdresseIP(final String valeur) {
    Objects.requireNonNull(valeur, "La valeur de l'IP ne peut être nulle");
    if (valeur.isBlank()) {
      throw new IllegalArgumentException("La valeur de l'IP ne peut être vide");
    }
    this.valeur = valeur;
  }

  public String getValeur() {
    return this.valeur;
  }

}