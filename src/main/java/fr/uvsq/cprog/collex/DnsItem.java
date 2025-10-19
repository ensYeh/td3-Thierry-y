package fr.uvsq.cprog.collex;

import java.util.Objects;

public final class DnsItem {
  
  private final NomMachine nom;
  private final AdresseIP ip;

  public DnsItem(final NomMachine nom, final AdresseIP ip) {
    this.nom = Objects.requireNonNull(nom, "Le NomMachine ne peut être nul");
    this.ip = Objects.requireNonNull(ip, "L'AdresseIP ne peut être nulle");
  }

  public NomMachine getNom() {
    return this.nom;
  }

  public AdresseIP getIp() {
    return this.ip;
  }

  public String formatForFile() {
    return this.nom.getValeur() + " " + this.ip.getValeur();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final DnsItem dnsItem = (DnsItem) o;
    return Objects.equals(this.nom, dnsItem.nom) 
        && Objects.equals(this.ip, dnsItem.ip);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.nom, this.ip);
  }
  
}
