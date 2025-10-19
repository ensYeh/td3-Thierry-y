package fr.uvsq.cprog.collex;

import java.io.IOException;

public final class AddCommand implements Commande {

  private final AdresseIP ip;
  private final NomMachine nom;

  public AddCommand(final AdresseIP ip, final NomMachine nom) {
    this.ip = ip;
    this.nom = nom;
  }

  @Override
  public void execute(final Dns dns, final DnsTUI tui) {
    try {
      dns.addItem(this.ip, this.nom);
    } catch (IllegalArgumentException | IOException e) {
      tui.affiche(e.getMessage());
    }
  }
}