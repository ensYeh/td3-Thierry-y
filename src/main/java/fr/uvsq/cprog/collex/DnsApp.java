package fr.uvsq.cprog.collex;

import java.io.IOException;

public final class DnsApp {

  private final Dns dns;
  private final DnsTUI tui;

  public DnsApp() throws IOException {
    this.dns = new Dns();
    this.tui = new DnsTUI();
  }

  public void run() {
    boolean continuer = true;

    while (continuer) {
      Commande commande = this.tui.nextCommande();
      commande.execute(this.dns, this.tui);

      if (commande instanceof QuitCommand) {
        continuer = false;
      }
    }

    this.tui.close();
  }

  public static void main(final String[] args) {
    try {
      DnsApp app = new DnsApp();
      app.run();
    } catch (IOException e) {
      System.err.println("Erreur lors de l'initialisation : " + e.getMessage());
      System.exit(1);
    }
  }
}
