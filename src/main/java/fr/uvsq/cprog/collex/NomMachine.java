package fr.uvsq.cprog.collex;

import java.util.Objects; 

public final class NomMachine {

  private final String valeur;

  public NomMachine(final String valeur) {
    Objects.requireNonNull(valeur, "La valeur du nom de machine ne peut être nulle");
    if (valeur.isBlank() || !valeur.contains(".")) {
      throw new IllegalArgumentException(
          "Nom qualifié invalide : doit contenir un nom et un domaine séparés par '.'");
    }
    this.valeur = valeur;
  }

  public String getValeur() {
    return this.valeur;
  }

}