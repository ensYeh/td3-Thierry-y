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

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final AdresseIP adresseIP = (AdresseIP) o;
    return Objects.equals(this.valeur, adresseIP.valeur);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.valeur);
  }

}