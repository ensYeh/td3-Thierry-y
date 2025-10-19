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

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final NomMachine that = (NomMachine) o;
    return Objects.equals(this.valeur, that.valeur);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.valeur);
  }

  public String getDomaine() {
    int indexPremierPoint = this.valeur.indexOf('.');
    return this.valeur.substring(indexPremierPoint + 1);
  }

  public boolean belongsToDomaine(final String domaine) {
    return this.getDomaine().equals(domaine);
  }

}